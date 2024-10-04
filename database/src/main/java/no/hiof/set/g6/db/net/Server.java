package no.hiof.set.g6.db.net;


import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import org.json.simple.JSONObject;

/**
 * Testing
 */


public class Server {
    
    
    public static void main(String[] args) throws Exception {
        new Server(8080).run();
    }
    
    private final int port;
    
    public Server(int port) {
        this.port = port;
    }
    
    public void run() throws Exception {
        EventLoopGroup bossGroup = new NioEventLoopGroup(); // (1)
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        try {
            ServerBootstrap b = new ServerBootstrap();
            b.group(bossGroup,workerGroup);
            b.channel(NioServerSocketChannel.class);
            b.childHandler(new ServerInitializer());
            b.localAddress(port);
            ChannelFuture f = b.bind().sync();
            f.channel().closeFuture().sync();
        } finally {
            workerGroup.shutdownGracefully();
            bossGroup.shutdownGracefully();
        }
    }
    
    
    private static final class ServerHandler extends ChannelInboundHandlerAdapter {
        
        @Override
        public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
            try { JSONObject object = (JSONObject) msg;
                System.out.println(object.toString());
            } finally {
                ctx.close();
            }
        }
        
        @Override
        public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
            cause.printStackTrace();
            ctx.close();
        }
    }
    
    private static final class ServerInitializer extends ChannelInitializer<SocketChannel> {
        
        @Override
        protected void initChannel(SocketChannel ch) throws Exception {
            ch.pipeline().addLast(Decoders.lengthDecoder());
            ch.pipeline().addLast(Decoders.stringDecoder());
            ch.pipeline().addLast(Decoders.stringToObjectConverter());
            ch.pipeline().addLast(new ServerHandler());
        }
    }
}
