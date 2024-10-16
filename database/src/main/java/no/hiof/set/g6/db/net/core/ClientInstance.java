package no.hiof.set.g6.db.net.core;


import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import no.hiof.set.g6.db.net.util.LogEntry;

import java.net.InetSocketAddress;

/**
 * @author Frederik Dahl
 * 13/10/2024
 */


public final class ClientInstance extends AppInterface {
    
    
    private Channel channel;
    private EventLoopGroup workGroup;
    public ClientInstance() { super(32); }
    
    
    public ChannelFuture connectToHost(String host, int port) throws Exception {
        if (connectedSuccessfully())
        {
            eventLog.write(LogEntry.warn("client instance already running"));
            shutDownAndWait();
        }
        eventLog.write(LogEntry.info("bootstrapping client instance"));
        workGroup = new NioEventLoopGroup();
        InetSocketAddress address = new InetSocketAddress(host,port);
        Bootstrap bootstrap = new Bootstrap();
        bootstrap.group(workGroup);
        bootstrap.channel(NioSocketChannel.class);
        bootstrap.option(ChannelOption.SO_KEEPALIVE, true);
        bootstrap.remoteAddress(address);
        bootstrap.handler(this);
        eventLog.write(LogEntry.info("attempting to connect to server..."));
        ChannelFuture connect = bootstrap.connect().sync();
        if (connect.isSuccess()) {
            eventLog.write(LogEntry.info("connected to Server: " + channel));
            channel = connect.sync().channel();
        } return connect;
    }
    
    @Override
    public boolean sendPacket(JsonPacket packet) {
        
        if (packet == null) {
            throw new IllegalStateException("null argument packet");
        } eventLog.write(LogEntry.debug("client attempting to send packet"));
        boolean failed = false;
        
        if (!isConnected()) {
            eventLog.write(LogEntry.warn("client not connected to host"));
            failed = true;
        }
        else if (packet.get() == null) {
            eventLog.write(LogEntry.warn("attempted to send packet without contents"));
            failed = true;
        }
        else if (packet.assignedToChannel() && !packet.get().equals(channel)) {
            eventLog.write(LogEntry.warn("packet assigned to unknown channel"));
            failed = true;
        }
        
        if (failed) {
            incrementFailedOutGoing();
            return false;
        }
        
        channel.writeAndFlush(packet.get()).addListener(future -> {
            if (future.isSuccess()) {
                eventLog.write(LogEntry.debug("packet sent over channel: " + channel));
                incrementPacketsSent();
            } else { eventLog.write(LogEntry.warn("failed to send packet to: " + channel));
                incrementFailedOutGoing();
            }
        });
        return true;
    }
    
    public boolean connectedSuccessfully() {
        return channel != null;
    }
    
    @Override
    public boolean isConnected() {
        return connectedSuccessfully() && channel.isActive();
    }
    
    @Override
    public void shutDownAndWait() throws Exception {
        eventLog.write(LogEntry.info("shutting down client.."));
        workGroup.shutdownGracefully().sync(); // blocking
        eventLog.write(LogEntry.info("client is shut down"));
        channel = null;
    }
    
    @Override
    public void shutDown() {
        eventLog.write(LogEntry.info("shutting down client.."));
        workGroup.shutdownGracefully().addListener(future -> {
            eventLog.write(LogEntry.info("client is shut down"));
            channel = null;
        });
    }
    
    @Override
    protected void onPacketReceived(JsonPacket packet) throws Exception {
        Channel c = packet.channel();
        if (c != null && c.equals(channel)) {
            synchronized (this) {
                num_received++;
                incoming.addFirst(packet);
                eventLog.write(LogEntry.debug("packet stored in incoming queue"));
                while (incoming.size() > incoming_packets_capacity) {
                    eventLog.write(LogEntry.warn("packet storage limit reached"));
                    eventLog.write(LogEntry.info("discarding older packets from: " + channel));
                    num_discarded_incoming++;
                }
            }
        } else throw new Exception("received packet from unknown source: " + c);
    }
}
