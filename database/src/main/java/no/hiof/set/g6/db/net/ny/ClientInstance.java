package no.hiof.set.g6.db.net.ny;


import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.Promise;
import no.hiof.set.g6.db.net.ny.handlers.ClientHandler;
import no.hiof.set.g6.db.net.ny.pipeline.ClientChannelInitializer;
import org.json.simple.JSONObject;

import java.io.IOException;
import java.nio.BufferOverflowException;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

/**
 *
 */


public abstract class ClientInstance  {
    
    private final EventLoopGroup workGroup;
    private final ClientHandler handler;
    
    
    public ClientInstance(String host, int port) throws Exception {
        host = host == null ? "" : host;
        handler = new ClientHandler();
        workGroup = new NioEventLoopGroup();
        try { ClientChannelInitializer initializer;
            initializer = new ClientChannelInitializer(handler);
            Bootstrap bootstrap = new Bootstrap();
            bootstrap.group(workGroup);
            bootstrap.channel(NioSocketChannel.class);
            bootstrap.option(ChannelOption.SO_KEEPALIVE, true);
            bootstrap.remoteAddress(host,port);
            bootstrap.handler(initializer);
            bootstrap.connect().sync();
        } catch (Exception e) {
            shutDown();
            throw e;
        }
    }
    
    
    /** Send request to server, receive response some time in the future */
    public Future<JSONObject> send(JSONObject request) {
        return handler.sendRequest(request);
    }
    
    /** Are we still connected to the Server*/
    public synchronized boolean connectionOpen() {
        return queue != null;
    }
    
    /** Shut down manually*/
    public void shutDown() {
        workGroup.shutdownGracefully();
    }
    
    
    
    
    
}
