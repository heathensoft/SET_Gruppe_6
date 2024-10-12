package no.hiof.set.g6.db.net;


import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import org.json.simple.JSONObject;

import java.io.IOException;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Set;

/**
 * @author Frederik Dahl
 * 12/10/2024
 */


public final class ServerInstance extends NetworkInterface<ServerPacket> {
    
    private final Set<Channel> channels;
    private final EventLoopGroup masterGroup;
    private final EventLoopGroup workerGroup;
    private Channel socketChannel;
    
    public ServerInstance(int port) {
        channels = new HashSet<>(128);
        masterGroup = new NioEventLoopGroup();
        workerGroup = new NioEventLoopGroup();
        incoming_queue_cap = 64;
        try {
            log(LogEntry.info("Creating ServerInstance"));
            ServerBootstrap serverBootstrap = new ServerBootstrap();
            serverBootstrap.group(masterGroup,workerGroup);
            serverBootstrap.channel(NioServerSocketChannel.class);
            serverBootstrap.option(ChannelOption.SO_BACKLOG,32);
            serverBootstrap.childOption(ChannelOption.SO_KEEPALIVE,true);
            serverBootstrap.childHandler(this);
            log(LogEntry.info("Attempting to bind server to port: " + port));
            ChannelFuture future = serverBootstrap.bind(port).sync();
            if (future.isSuccess()) {
                socketChannel = future.channel();
                log(LogEntry.info("Server Running!"));
            } else throw new Exception("Unable to bind");
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
        return socketChannel != null;
    }
    
    @Override
    public void shutDown() throws Exception {
        log(LogEntry.info("Shutting down Server Workgroups.."));
        masterGroup.shutdownGracefully().sync();
        workerGroup.shutdownGracefully().sync();
    }
    
    @Override
    public void sendMessage(ServerPacket response) throws Exception {
        if (response == null) throw new IllegalStateException("Request is Null"); // Runtime Exception
        JSONObject jsonObject = response.get();
        Channel channel = response.channel();
        if (anyObjectNull(jsonObject,channel)) {
            throw new IllegalStateException("ServerPacket contains null elements");
        } boolean channel_registered;
        synchronized (channels) {
            channel_registered = channels.contains(channel);
        } if (channel_registered) {
            if (channel.isActive()) {
                channel.writeAndFlush(jsonObject).addListener(future -> {
                    // Triggered from a Worker Thread
                    synchronized (this) {
                        if (future.isSuccess()) {
                            log(LogEntry.debug("Server sent Response To Client"));
                            num_messages_sent++;
                        } else {
                            log(LogEntry.warn("Server failed to send Response To Client"));
                            num_failed_outgoing++;
                        }
                    }
                });
            } else {
                removeChannel(channel);
                synchronized (this) { num_failed_outgoing++; }
                log(LogEntry.warn("Server failed to send Response To Client"));
                throw new IOException("Client not connected to Server");
            }
        } else {
            synchronized (this) { num_failed_outgoing++; }
            log(LogEntry.warn("Server failed to send Response To Client"));
            throw new IOException("Client not connected to Server");
        }
    }
    
    @Override
    protected void onMessageReceived(ServerPacket request) throws Exception {
        synchronized (channels) {
            if (channels.add(request.channel()))
                log(LogEntry.debug("new channel registered by server"));
        } attemptAddMessage(request);
    }
    
    @Override
    protected ChannelHandler createPipeLineTail() throws Exception {
        return new ServerHandler(this);
    }
    
    /**
     * Will remove reference to client channel from server instance.
     * Will discard stored pending requests from the channel.
     * Any requests already picked up by the Server application will not be sent.
     * (Will fail to send back response,(Throws Exception) )
     * @param channel the channel to remove
     */
    void removeChannel(Channel channel) {
        log(LogEntry.debug("removeChannel: " + channel.id()));
        boolean removed;
        synchronized (channels) {
            removed = channels.remove(channel);
        } if (removed) {
            discardStoredRequests(channel);
            attemptClose(channel);
        }
    }
    
    /**
     * Dispose lingering requests
     * @param channel the channel to purge
     */
    private synchronized void discardStoredRequests(Channel channel) {
        final LinkedList<ServerPacket> l = incoming;
        final Channel c = channel;
        int discarded = num_discarded_incoming;
        for(int i = l.size() - 1; i >= 0; --i) {
            if(l.get(i).get().equals(c)) {
                num_discarded_incoming++;
                l.remove(i);
            }
        } discarded = num_failed_outgoing - discarded;
        log(LogEntry.debug("Discarded " + discarded + " incoming requests"));
    }
    
    /**
     * Will close the channel if not closed already
     * @param channel the channel to close
     */
    private void attemptClose(Channel channel) {
        log(LogEntry.debug("Called close(channel)"));
        if (channel.isActive()) {
            channel.close().addListener(future -> {
                if (future.isSuccess()) log(LogEntry.info("channel closed: " + channel.id()));
                else log(LogEntry.warn("unable to close channel for some reason"));
            });
        }
    }
    
    public int numRegisteredChannels() {
        int size;
        synchronized (channels) {
            size = channels.size();
        } return size;
    }
    
    private boolean anyObjectNull(Object ...objects) {
        if (objects == null) throw new IllegalStateException("");
        for (Object o : objects) if (o == null) return true;
        return false;
    }
    
}
