package no.hiof.set.g6.db.net.core;


import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import no.hiof.set.g6.db.net.util.LogEntry;

import java.util.*;

/**
 * @author Frederik Dahl
 * 14/10/2024
 */


public final class ServerInstance extends AppInterface {
    
    private final Set<Channel> channels;
    private final EventLoopGroup masterGroup;
    private final EventLoopGroup workerGroup;
    private final Channel socketChannel;
    
    
    public ServerInstance(int port) throws Exception {
        super(128);
        channels = new HashSet<>(128);
        masterGroup = new NioEventLoopGroup();
        workerGroup = new NioEventLoopGroup();
        try {
            eventLog.write(LogEntry.info("creating new ServerInstance"));
            ServerBootstrap serverBootstrap = new ServerBootstrap();
            serverBootstrap.group(masterGroup,workerGroup);
            serverBootstrap.channel(NioServerSocketChannel.class);
            serverBootstrap.option(ChannelOption.SO_BACKLOG,32);
            serverBootstrap.childOption(ChannelOption.SO_KEEPALIVE,true);
            serverBootstrap.childHandler(this);
            eventLog.write(LogEntry.info("attempting to bind server to port: " + port));
            ChannelFuture future = serverBootstrap.bind(port).sync();
            if (future.isSuccess()) {
                socketChannel = future.channel();
                eventLog.write(LogEntry.info("server running!"));
            } else throw new Exception("unable to bind");
        } catch (Exception e) {
            shutDownAndWait();
            throw e;
        }
    }
    
    
    @Override
    public boolean sendPacket(JsonPacket packet) {
        
        if (packet == null) {
            throw new IllegalStateException("null argument packet");
        } eventLog.write(LogEntry.debug("server attempting to send packet"));
        boolean failed = false;
        
        if (!isConnected()) {
            eventLog.write(LogEntry.warn("server socket channel is disconnected"));
            failed = true;
        }
        else if (packet.get() == null) {
            eventLog.write(LogEntry.warn("attempted to send packet without contents"));
            failed = true;
        }
        else if (!packet.assignedToChannel()) {
            eventLog.write(LogEntry.warn("packet not assigned to a client"));
            failed = true;
        }
        else
        {
            boolean recognized;
            synchronized (channels) {
                recognized = channels.contains(packet.channel());
            } if (!recognized) {
                eventLog.write(LogEntry.warn("packet assigned to unknown channel"));
                failed = true;
            }
        }
        
        if (failed) {
            incrementFailedOutGoing();
            return false;
        }
        
        Channel channel = packet.channel();
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
    
    @Override
    public boolean isConnected() {
        return socketChannel.isActive();
    }
    
    @Override
    public void shutDownAndWait() throws Exception {
        eventLog.write(LogEntry.info("shutting down server.."));
        masterGroup.shutdownGracefully().sync();
        workerGroup.shutdownGracefully().sync();
        eventLog.write(LogEntry.info("server is shut down"));
    }
    
    @Override
    public void shutDown() {
        eventLog.write(LogEntry.info("shutting down server.."));
        masterGroup.shutdownGracefully().addListener(future -> {
            if (future.isSuccess()) eventLog.write(LogEntry.info("server master group shut down"));
            else eventLog.write(LogEntry.warn("server master group failed to shut down"));
        }); workerGroup.shutdownGracefully().addListener(future -> {
            if (future.isSuccess()) eventLog.write(LogEntry.info("server worker group shut down"));
            else eventLog.write(LogEntry.warn("server worker group failed to shut down"));
        });
    }
    
    public int numRegisteredChannels() {
        int size;
        synchronized (channels) {
            size = channels.size();
        } return size;
    }
    
    @Override
    protected void onChannelActive(Channel channel) throws Exception {
        if (channel != null) {
            synchronized (channels) {
                if (channels.add(channel)) {
                    eventLog.write(LogEntry.debug("opened connection to: " + channel));
                }
            }
        }
    }
    
    @Override
    protected void onChannelInactive(Channel channel) throws Exception {
        if (channel != null) {
            synchronized (channels) {
                if (channels.remove(channel)) {
                    eventLog.write(LogEntry.debug("lost connection to: " + channel));
                    eventLog.write(LogEntry.debug("discarding earlier requests"));
                    discardStoredRequests(channel);
                    if (channel.isActive()) { // should not be
                        channel.close();
                    }
                }
            }
        }
    }
    
    @Override
    protected void onPacketReceived(JsonPacket packet) throws Exception {
        Channel c = packet.channel();
        boolean recognized_channel;
        synchronized (channels) {
            recognized_channel = channels.contains(c);
        } if (recognized_channel) {
            synchronized (this) {
                num_received++;
                incoming.addFirst(packet);
                eventLog.write(LogEntry.debug("packet stored in incoming queue"));
                while (incoming.size() > incoming_packets_capacity) {
                    eventLog.write(LogEntry.warn("packet storage limit reached"));
                    eventLog.write(LogEntry.info("discarding packet from: " + c));
                    num_discarded_incoming++;
                }
            }
        }
    }
    
    
    private synchronized void discardStoredRequests(Channel channel) {
        final LinkedList<JsonPacket> l = incoming;
        for(int i = l.size() - 1; i >= 0; --i) {
            if(l.get(i).get().equals(channel)) {
                num_discarded_incoming++;
            }
        }
    }
}
