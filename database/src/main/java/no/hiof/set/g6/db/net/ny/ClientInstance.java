package no.hiof.set.g6.db.net.ny;


import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.util.concurrent.Future;
import no.hiof.set.g6.db.net.net.ClientHandlerOld;
import no.hiof.set.g6.db.net.ny.pipeline.ClientChannelInitializer;
import org.json.simple.JSONObject;

/**
 *
 */


public abstract class ClientInstance  {
    
    private final EventLoopGroup workGroup;
    private final ClientHandlerOld handler;
    
    
    public ClientInstance(String host, int port) throws Exception {
        host = host == null ? "" : host;
        handler = new ClientHandlerOld();
        workGroup = new NioEventLoopGroup();
        try { ClientChannelInitializer initializer;
            initializer = new ClientChannelInitializer(handler);
            Bootstrap bootstrap = new Bootstrap();
            bootstrap.group(workGroup);
            bootstrap.channel(NioSocketChannel.class);
            bootstrap.option(ChannelOption.SO_KEEPALIVE, true);
            bootstrap.remoteAddress(host,port);
            bootstrap.handler(initializer);
            ChannelFuture future = bootstrap.connect().sync();
        } catch (Exception e) {
            shutDown();
            throw e;
        }
    }
    
    
    /** Send request to server, receive response some time in the future */
    public Future<JSONObject> send(JSONObject request) {
        return handler.sendRequest(request);
    }
    
    /** Shut down manually*/
    public void shutDown() {
        workGroup.shutdownGracefully();
    }
    
    
    
    
    
}
