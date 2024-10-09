package no.hiof.set.g6.db.net;


import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import no.hiof.set.g6.dt.G6JSON;
import org.json.simple.JSONObject;

import java.net.InetSocketAddress;

/**
 *  Testing
 */


public class Client {
    
    private static final String JSON_STRING = "{\"E-Mail\":\"geir@hotmail.com\",\"Address\":{\"Postal Code\":1706,\"State\":\"�stfold\",\"Street Address\":\"Gate 34b\",\"Country\":\"Norge\",\"City\":\"Sarpsborg\"},\"First Name\":\"Geir\",\"Last Name\":\"Seter\",\"Phone-Numbers\":[\"123456789\",\"987654321\"],\"User-ID\":300}";
    
    
    public static void main(String[] args) throws Exception {
        JSONObject object = G6JSON.parse(JSON_STRING);
        send(object,"localhost",8080);
    }
    
    public void connectToServer() {
    
    }
    
    
    
    public static void send(JSONObject object, String host, int port) throws Exception {
        try {
            EventLoopGroup group = new NioEventLoopGroup();
            try {
                Bootstrap b = new Bootstrap();
                b.group(group);
                b.channel(NioSocketChannel.class);
                b.remoteAddress(new InetSocketAddress(host,port));
                b.handler(new ClientInitializer());
                ChannelFuture f = b.connect().sync();
                
                if (f.isSuccess()) {
                    f.channel().writeAndFlush(object)
                            .addListener((ChannelFutureListener)
                                                 future -> System.out.println(
                                                         future.isSuccess()
                                                                 ? "Object sent to server"
                                                                 : "sending failed"));
                }
                
                
                f.channel().closeFuture().sync();
            } finally {
                group.shutdownGracefully().sync();
            }
            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    private static final class ClientInitializer extends ChannelInitializer<SocketChannel> {
        
        @Override
        protected void initChannel(SocketChannel ch) throws Exception {
            ch.pipeline().addLast(Encoders.lengthEncoder());
            ch.pipeline().addLast(Encoders.stringEncoder());
            ch.pipeline().addLast(Encoders.objectToStringConverter());
        }
    }
    
    
}
