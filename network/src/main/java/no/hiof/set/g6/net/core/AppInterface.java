package no.hiof.set.g6.net.core;


import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.codec.LengthFieldPrepender;
import io.netty.handler.codec.MessageToMessageDecoder;
import io.netty.handler.codec.MessageToMessageEncoder;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import no.hiof.set.g6.dt.old.JsonUtils;
import org.json.simple.JSONObject;

import java.util.LinkedList;
import java.util.List;

/**
 *
 * AppInterface acts as the interface between application and the network layer.
 * ClientInstance and ServerInstance shares a decent amount of similar functionality inherited from AppInterface.
 *
 *
 *
 * @author Frederik Dahl
 * 13/10/2024
 */


public abstract class AppInterface extends ChannelInitializer<SocketChannel> {
    
    protected final LinkedList<JsonPacket> incoming = new LinkedList<>();
    protected final EventLog eventLog = new EventLog();
    
    protected int num_sent;
    protected int num_received;
    protected int num_collected;
    protected int num_failed_outgoing;
    protected int num_discarded_incoming;
    protected final int incoming_packets_capacity;
    
    
    protected AppInterface(int incoming_packet_capacity) {
        this.incoming_packets_capacity = incoming_packet_capacity;
    }
    
    public synchronized void collectIncomingPackets(List<JsonPacket> dst) {
        while (!incoming.isEmpty()) {
            JsonPacket packet = incoming.removeLast();
            dst.add(packet);
            num_collected++;
        }
    }
    
    public EventLog eventLog() { return eventLog; }
    
    public int incomingPacketsCapacity() { return incoming_packets_capacity; }
    public synchronized int numFailedOutgoing() { return num_failed_outgoing; }
    public synchronized int numDiscardedIncoming() { return num_discarded_incoming; }
    public synchronized int numPacketsReceived() { return num_received; }
    public synchronized int numPacketsSent() { return num_sent; }
    public synchronized int numCollectedPackets() { return num_collected; }
    protected synchronized void incrementFailedOutGoing() { num_failed_outgoing++; }
    protected synchronized void incrementDiscardedIncoming() { num_discarded_incoming++; }
    protected synchronized void incrementPacketsReceived() { num_received++; }
    protected synchronized void incrementPacketsSent() { num_sent++; }
    protected synchronized void incrementCollectedPackets(int count) { num_collected += count; }
    
    
    /**
     * Attempt to send packet.
     * @param packet packet
     * @return returns true if the packet is valid
     */
    public abstract boolean sendPacket(JsonPacket packet);
    
    public abstract boolean isConnected();
    
    /**Shut down and block until complete*/
    public abstract void shutDownAndWait() throws Exception;
    
    /**Shut down in the background*/
    public abstract void shutDown();
    
    protected abstract void onPacketReceived(JsonPacket packet) throws Exception;
    
    protected void onPipelineException(Channel channel) { /*...*/}
    
    protected void onChannelRegistered(Channel channel) throws Exception { /*...*/ };
    
    protected void onChannelUnregistered(Channel channel) throws Exception { /*...*/ };
    
    protected void onChannelActive(Channel channel) throws Exception { /*...*/ };
    
    protected void onChannelInactive(Channel channel) throws Exception { /*...*/ };
    
    protected void onChannelReadComplete(Channel channel) throws Exception { /*...*/ };
    
    @Override
    protected void initChannel(SocketChannel channel) throws Exception {
        // For context: https://netty.io/4.0/api/io/netty/channel/ChannelPipeline.html
        ChannelPipeline pipeline = channel.pipeline();
        pipeline.addLast(new LengthFieldBasedFrameDecoder(
        256 * 1024, 0, Integer.BYTES, 0, Integer.BYTES));
        pipeline.addLast(new LengthFieldPrepender(Integer.BYTES));
        pipeline.addLast(new StringDecoder());
        pipeline.addLast(new StringEncoder());
        pipeline.addLast(new StringToObjectConverter());
        pipeline.addLast(new ObjectToStringConverter());
        pipeline.addLast(new PacketHandler(this));
    }
    
    private static final class ObjectToStringConverter extends MessageToMessageEncoder<JSONObject> {
        protected void encode(ChannelHandlerContext channelHandlerContext, JSONObject object, List<Object> list) throws Exception {
            String string = object.toJSONString();
            list.add(string);
        }
    }
    
    private static final class StringToObjectConverter extends MessageToMessageDecoder<String> {
        protected void decode(ChannelHandlerContext channelHandlerContext, String string, List<Object> list) throws Exception {
            JSONObject object = JsonUtils.parse(string);
            list.add(object);
        }
    }
}
