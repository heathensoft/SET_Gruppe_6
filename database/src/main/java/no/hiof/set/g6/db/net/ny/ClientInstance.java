package no.hiof.set.g6.db.net.ny;


import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import org.json.simple.JSONObject;

import java.io.IOException;
import java.net.InetSocketAddress;

/**
 * @author Frederik Dahl
 * 13/10/2024
 */


public final class ClientInstance extends AppInterface {
    
    public static final int MAX_STORED_PACKETS = 32;
    
    private Channel channel;
    private final EventLoopGroup workGroup;
    
    
    public ClientInstance(String host, int port) {
        super(MAX_STORED_PACKETS);
        workGroup = new NioEventLoopGroup();
        try { InetSocketAddress address = new InetSocketAddress(host,port);
            eventLog.write(LogEntry.info("Bootstrapping new ClientInstance"));
            Bootstrap bootstrap = new Bootstrap();
            bootstrap.group(workGroup);
            bootstrap.channel(NioSocketChannel.class);
            bootstrap.option(ChannelOption.SO_KEEPALIVE, true);
            bootstrap.remoteAddress(address);
            bootstrap.handler(this);
            eventLog.write(LogEntry.info("Attempting to connect to server"));
            ChannelFuture future = bootstrap.connect().sync(); // blocking
            if (future.isSuccess()) {
                channel = future.channel();
                eventLog.write(LogEntry.info("Connected to Server: " + channel));
            } else throw new Exception("Unable to connect with Server: " + channel);
        } catch (Exception e1) {
            eventLog.write(LogEntry.error(e1.getMessage()));
            shutDown();
        }
    }
    
    
    @Override
    public void send(G6Packet packet) throws Exception {
        if (!createdSuccessfully()) throw new IllegalStateException("ClientInstance not created successfully");
        if (channel.isActive()) {
            if (packet.assignedToChannel()) {
                if (channel.equals(packet.channel())) {
                    JSONObject payload = packet.get();
                    if (payload != null) {
                        channel.writeAndFlush(payload).addListener(future -> {
                            if (future.isSuccess()) { num_sent++;
                                eventLog.write(LogEntry.debug("Sent packet to: " + channel));
                            } else { eventLog.write(LogEntry.warn("Failed to send packet to: " + channel));
                                num_failed_outgoing++;
                            }
                        });
                    } else synchronized (this) { num_failed_outgoing++;
                        throw new IOException("Failed to send packet, packet is empty");
                    }
                } else synchronized (this) { num_failed_outgoing++;
                    throw new IOException("Failed to send packet, unrecognized channel");
                }
            } else { JSONObject payload = packet.get();
                if (payload != null) {
                    channel.writeAndFlush(payload).addListener(future -> {
                        if (future.isSuccess()) { num_sent++;
                            eventLog.write(LogEntry.debug("Sent packet to: " + channel));
                        } else { eventLog.write(LogEntry.warn("Failed to send packet to: " + channel));
                            num_failed_outgoing++;
                        }
                    });
                } else synchronized (this) { num_failed_outgoing++;
                    throw new IOException("Failed to send packet, packet is empty");
                }
            }
        } else synchronized (this) { num_failed_outgoing++;
            throw new IOException("Failed to send packet, channel is not active");
        }
    }
    
    @Override
    public void sendToAll(G6Packet packet) throws Exception {
        send(packet);
    }
    
    @Override
    public boolean createdSuccessfully() {
        return channel != null;
    }
    
    @Override
    public void shutDownAndWait() throws Exception {
        eventLog.write(LogEntry.info("Shutting down ClientInstance Workgroup.."));
        workGroup.shutdownGracefully().sync(); // blocking
        eventLog.write(LogEntry.info("ClientInstance is shut down"));
    }
    
    @Override
    public void shutDown() {
        eventLog.write(LogEntry.info("Shutting down ClientInstance Workgroup.."));
        workGroup.shutdownGracefully().addListener(future -> {
            eventLog.write(LogEntry.info("ClientInstance is shut down"));
        });
    }
    
    @Override
    protected void onPacketReceived(G6Packet packet) throws Exception {
        Channel c = packet.channel();
        if (c != null && c.equals(channel)) {
            synchronized (this) {
                num_received++;
                incoming.addFirst(packet);
                eventLog.write(LogEntry.debug("Packet stored in incoming"));
                while (incoming.size() > incoming_packets_capacity) {
                    eventLog.write(LogEntry.warn("Packet storage limit reached"));
                    eventLog.write(LogEntry.info("Discarding older packets from: " + channel));
                    incoming.removeLast();
                    num_discarded_incoming++;
                }
            }
        } else throw new Exception("Received packet from unknown source: " + c);
    }
}
