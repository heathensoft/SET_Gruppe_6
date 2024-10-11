package no.hiof.set.g6.db.net.ny;


import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import no.hiof.set.g6.db.net.ny.handlers.ServerRequestHandler;
import no.hiof.set.g6.db.net.ny.pipeline.ServerChannelInitializer;
import org.json.simple.JSONObject;

/**
 *
 */


public abstract class ServerInstance  {
    
    private final EventLoopGroup masterGroup;
    private final EventLoopGroup workerGroup;
    
    
    public ServerInstance(ServerRequestHandler handler, int port) throws Exception {
        masterGroup = new NioEventLoopGroup();
        workerGroup = new NioEventLoopGroup();
        try {
            ServerBootstrap serverBootstrap = new ServerBootstrap();
            serverBootstrap.group(masterGroup,workerGroup);
            serverBootstrap.channel(NioServerSocketChannel.class);
            serverBootstrap.option(ChannelOption.SO_BACKLOG,32);
            serverBootstrap.childOption(ChannelOption.SO_KEEPALIVE,true);
            serverBootstrap.childHandler(new ServerChannelInitializer(handler));
            serverBootstrap.bind(port).sync();
        } catch (Exception e) {
            shutDown();
            throw e;
        }
    }
    
    /** Shut down manually*/
    public void shutDown() {
        masterGroup.shutdownGracefully();
        workerGroup.shutdownGracefully();
    }
    
}
