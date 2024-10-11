package no.hiof.set.g6.db.net.naive;


import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;


/**
 *
 */


public class NettyClient {
    
    private Channel channel;
    private EventLoopGroup workGroup;
    
    
    public NettyClient() { }
    
    
    public void start(String address, int port) throws Exception {
        if (workGroup != null) throw new Exception("Netty Client Already Started without exception");
        address = address == null ? "" : address;
        try {
            workGroup = new NioEventLoopGroup();
            Bootstrap bootstrap = new Bootstrap();
            bootstrap.group(workGroup);
            bootstrap.channel(NioSocketChannel.class);
            bootstrap.option(ChannelOption.SO_KEEPALIVE, true);
            bootstrap.remoteAddress(address,port);
            bootstrap.handler(new NettyClientInitializer());
            ChannelFuture future = bootstrap.connect().sync();
            if (future.isSuccess()) {
                channel = future.channel();
            } else {
                throw new Exception("NettyClient unable to connect to Host");
            }
        } catch (Exception e) {
            shutdown();
            throw e;
        }
    }
    
    public void shutdown() {
        if (workGroup != null) {
            workGroup.shutdownGracefully();
            workGroup = null;
        }
    }
}
