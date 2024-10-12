package no.hiof.set.g6.db.net;


import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import org.json.simple.JSONObject;

import java.io.IOException;
import java.net.InetSocketAddress;

/**
 *
 * @author Frederik Dahl
 * 12/10/2024
 */


public final class ClientInstance extends NetworkInterface<JSONObject> {
    
    // The channel of communication with Server
    // The channel is open throughout the ClientInstance's lifecycle
    // When the channel is closed, it cannot be opened again. (Must create a new ClientInstance)
    private Channel channel;
    private final EventLoopGroup workGroup; // Worker Threads (1)
    
    
    public ClientInstance(String host, int port) {
        workGroup = new NioEventLoopGroup();
        try {
            InetSocketAddress address = new InetSocketAddress(host,port);
            log(LogEntry.info("Creating ClientInstance"));
            Bootstrap bootstrap = new Bootstrap();
            bootstrap.group(workGroup);
            bootstrap.channel(NioSocketChannel.class);
            bootstrap.option(ChannelOption.SO_KEEPALIVE, true);
            bootstrap.remoteAddress(address);
            bootstrap.handler(this);
            log(LogEntry.info("Attempting to connect to server @: " + address));
            ChannelFuture future = bootstrap.connect().sync(); // blocking
            if (future.isSuccess()) {
                channel = future.channel();
                log(LogEntry.info("Connected to Server!"));
            } else throw new Exception("Unable to connect with Server");
        } catch (Exception e1) {
            log(LogEntry.error(e1.getMessage()));
            try { shutDown();
            } catch (Exception e2) {
                log(LogEntry.error(e2.getMessage()));
            }
        }
    }
    
    @Override
    public boolean createdSuccessfully() {
        return channel != null;
    }
    
    @Override
    public void shutDown() throws Exception {
        log(LogEntry.info("Shutting down Client Workgroup.."));
        workGroup.shutdownGracefully().sync(); // blocking
    }
    
    @Override
    public void sendMessage(JSONObject request) throws Exception {
        if (request == null) throw new IllegalStateException("Request is Null"); // Runtime Exception
        if (!createdSuccessfully()) throw new IllegalStateException("Invalid ClientInstance");
        if (channel.isActive()) {
            channel.writeAndFlush(request).addListener(future -> {
                // Triggered from a Worker Thread
                synchronized (this) {
                    if (future.isSuccess()) {
                        log(LogEntry.debug("Client Sent Request To Server"));
                        num_messages_sent++;
                    } else {
                        log(LogEntry.warn("Client failed to send Request To Server"));
                        num_failed_outgoing++;
                    }
                }
            });
        } else {
            synchronized (this) { num_failed_outgoing++; }
            log(LogEntry.warn("Client failed to send Request To Server"));
            throw new IOException("Client not connected to Server");
        }
    }
    
   
    @Override
    protected void onMessageReceived(JSONObject message) throws Exception {
        if (message == null) throw new Exception("Message is null"); // handled by the pipeline
        attemptAddMessage(message);
    }
    
    @Override
    protected ChannelHandler createPipeLineTail() throws Exception {
        return new ClientHandler(this);
    }
    
   
}
