package no.hiof.set.g6.db.net;


import io.netty.channel.*;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.codec.LengthFieldPrepender;
import io.netty.handler.codec.MessageToMessageDecoder;
import io.netty.handler.codec.MessageToMessageEncoder;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import no.hiof.set.g6.dt.G6JSON;
import org.json.simple.JSONObject;

import java.util.LinkedList;
import java.util.List;

/**
 * @author Frederik Dahl
 * 12/10/2024
 */


abstract class NetworkInterface<T> extends ChannelInitializer<SocketChannel> {
    
    private static final int DEFAULT_INCOMING_QUEUE_CAPACITY = 32;
    private static final int DEFAULT_LOG_ENTRIES_CAPACITY = 64;
    
    // Maximum stored incoming messages before discarding
    protected int incoming_queue_cap = DEFAULT_INCOMING_QUEUE_CAPACITY;
    
    // Incoming messages
    // The Client / Server Application is responsible for "picking up" incoming messages
    protected LinkedList<T> incoming = new LinkedList<>();
    
    // Various counters
    protected int num_failed_outgoing;
    protected int num_discarded_incoming;
    protected int num_messages_sent;
    protected int num_messages_received;
    protected int num_collected_messages;
    
    // On Logging:
    // Logs are queued up to a maximum number of entries.
    // Beyond the maximum capacity, log messages are printed out.
    // Logs with a severity level < log_filter_severity are ignored (Not printed out) but still stored
    // Application can collect log entries from a ClientInstance (custom loggers)
    // Logging related fields are synchronized on the log_lock Object
    private final LinkedList<LogEntry> log_entries = new LinkedList<>();
    private final Object log_lock = new Object();
    private int log_entries_capacity = DEFAULT_LOG_ENTRIES_CAPACITY;
    private int log_filter_severity = 0;
    
    /**
     * Call this after creating a new NetworkInterface.
     * If it returns false, there is no need for calling shutDown.
     * Logs are available. (collectLogs or flushLogsToConsole)
     * @return true if Instance was created successfully
     */
    public abstract boolean createdSuccessfully();
    
    /**
     * "Close Connection" before exiting the main program running this NetworkInterface
     * @throws Exception if workgroup/s could not be shut down for any reason
     */
    public abstract void shutDown() throws Exception;
    
    public abstract void sendMessage(T message) throws Exception;
    
    /**
     * This is called from the Handler when a message is received on the channel
     * @param message a message from "the other end"
     * @throws Exception for any reason. It will be handled by the pipeline.
     */
    protected abstract void onMessageReceived(T message) throws Exception;
    
    protected abstract ChannelHandler createPipeLineTail() throws Exception;
    
    public void setLogEntriesCapacity(int cap) {
        cap = Math.max(Math.min(256,cap),0);
        if (cap != log_entries_capacity) {
            log_entries_capacity = cap;
            synchronized (log_lock) {
                while (log_entries.size() >= log_entries_capacity) {
                    LogEntry removed = log_entries.removeLast();
                    if (log_filter_severity <= removed.severity()) {
                        removed.SysOut();
                    }
                }
            }
        }
    }
    
    public void flushLogsToConsole() {
        flushLogsToConsole(LogEntry.Type.DEBUG);
    }
    
    public void flushLogsToConsole(LogEntry.Type severity) {
        synchronized (log_lock) {
            while (!log_entries.isEmpty()) {
                LogEntry removed = log_entries.removeLast();
                if (severity.ordinal() <= removed.severity()) {
                    removed.SysOut();
                }
            }
        }
    }
    
    public void collectLogs(List<LogEntry> dst) {
        if (dst == null) throw new IllegalStateException("List argument is null");
        synchronized (log_lock) {
            while (!log_entries.isEmpty()) {
                dst.add(log_entries.removeLast());
            }
        }
    }
    
    public void setLogFilter(LogEntry.Type type) { log_filter_severity = type.ordinal(); }
    
    public int incomingMessagesCapacity() { return incoming_queue_cap; }
    
    public synchronized int numFailedOutgoingMessages() { return num_failed_outgoing; }
    
    public synchronized int numDiscardedIncomingMessages() { return num_discarded_incoming; }
    
    public synchronized int numMessagesReceived() { return num_messages_received; }
    
    public synchronized int numMessagesSent() { return num_messages_sent; }
    
    public synchronized int numCollectedMessages() { return num_collected_messages; }
    
    public synchronized void collectIncoming(List<T> dst) {
        if (dst == null) throw new IllegalStateException("List argument is null");
        while (!incoming.isEmpty()) {
            dst.add(incoming.removeLast());
            num_collected_messages++;
        }
    }
    
    protected void log(LogEntry entry) {
        if (entry == null) throw new IllegalStateException("Log Entry is Null");
        if (log_entries_capacity <= 0) {
            if (log_filter_severity <= entry.severity()) entry.SysOut();
        } else synchronized (log_lock) {
            while (log_entries.size() >= log_entries_capacity) {
                LogEntry removed = log_entries.removeLast();
                if (log_filter_severity <= removed.severity()) removed.SysOut();
            } log_entries.addFirst(entry);
        }
    }
    
    protected synchronized void attemptAddMessage(T message) {
        while (incoming.size() >= incoming_queue_cap) {
            log(LogEntry.info("Incoming queue is at capacity. Discarding.."));
            incoming.removeLast();
            num_discarded_incoming++;
        } // Enqueue message
        incoming.addFirst(message);
        num_messages_received++;
    }
    
    @Override
    protected final void initChannel(SocketChannel channel) throws Exception {
        
         /*
        https://netty.io/4.0/api/io/netty/channel/ChannelPipeline.html
        
        
        IN  - lengthDecoder (Decode incoming bytes to single ByteBuff)
        OUT - lengthEncoder (Encode outgoing ByteBuff with length Header)
        IN  - StringDecoder (Decodes incoming ByteBuff to String)
        OUT - StringEncoder (Encodes Outgoing String to ByteBuff)
        IN  - StringToObjectConverter (Decodes String to JsonObject)
        OUT - ObjectToStringConverter (Encodes JsonObject to String)
        IN / OUT - Pipeline Tail
         */
        
        // En "frame" er hvordan Netty deler opp innkommende / utgående TCP pakker
        // 256 KB er arbitrært, men holder til vårt formål
        final int MAX_FRAME_LENGTH = 256 * 1024;
        
        // Headeren er en 4 byte integer som beskriver payload størrelsen
        final int LENGTH_HEADER_SIZE_BYTES = Integer.BYTES;
        
        ChannelPipeline pipeline = channel.pipeline();
        pipeline.addLast(new LengthFieldBasedFrameDecoder(
                MAX_FRAME_LENGTH,
                0,
                LENGTH_HEADER_SIZE_BYTES,
                0,
                LENGTH_HEADER_SIZE_BYTES))
        ;
        pipeline.addLast(new LengthFieldPrepender(LENGTH_HEADER_SIZE_BYTES));
        pipeline.addLast(new StringDecoder());
        pipeline.addLast(new StringEncoder());
        pipeline.addLast(new StringToJsonObjectConverter());
        pipeline.addLast(new JsonObjectToStringConverter());
        
        ChannelHandler tail = createPipeLineTail();
        if (tail != null) {
            pipeline.addLast(tail);
        }
    }
    
    
    private static final class JsonObjectToStringConverter extends MessageToMessageEncoder<JSONObject> {
        protected void encode(ChannelHandlerContext channelHandlerContext, JSONObject object, List<Object> list) throws Exception {
            String string = object.toJSONString();
            list.add(string);
        }
    }
    
    private static final class StringToJsonObjectConverter extends MessageToMessageDecoder<String> {
        protected void decode(ChannelHandlerContext channelHandlerContext, String string, List<Object> list) throws Exception {
            JSONObject object = G6JSON.parse(string);
            list.add(object);
        }
    }
    
}
