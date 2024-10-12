package no.hiof.set.g6.db.net.naive;


import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.codec.LengthFieldPrepender;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import org.json.simple.JSONObject;

/**
 * @author Frederik Dahl
 * 11/10/2024
 */


public abstract class G6Server extends ChannelInitializer<SocketChannel> {
    
    
    private final EventLoopGroup masterGroup;
    private final EventLoopGroup workerGroup;
    
    
    public G6Server(int port) throws Exception {
        masterGroup = new NioEventLoopGroup();
        workerGroup = new NioEventLoopGroup();
        try {
            ServerBootstrap serverBootstrap = new ServerBootstrap();
            serverBootstrap.group(masterGroup,workerGroup);
            serverBootstrap.channel(NioServerSocketChannel.class);
            serverBootstrap.option(ChannelOption.SO_BACKLOG,32);
            serverBootstrap.childOption(ChannelOption.SO_KEEPALIVE,true);
            serverBootstrap.childHandler(this);
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
    
    /** En exception ble kastet et sted i pipeline, kan f.eks. logge cause*/
    protected abstract void channelExceptionCaught(Throwable cause);
    
    /** JDBC Database Kode går her */
    protected abstract JSONObject handleRequest(JSONObject request) throws Exception;
    
    
    @Override
    protected void initChannel(SocketChannel channel) throws Exception {
        
        /*
        Bygger en Channel "pipeline". En rekke med steg som dataen må gå igjennom
        for å ende opp som et JsonObject vi kan tolke. Både Server og Client har behov for å sende
        motta data.
        
        https://netty.io/4.0/api/io/netty/channel/ChannelPipeline.html
        
        
        IN  - lengthDecoder (Decode incoming bytes to single ByteBuff)
        OUT - lengthEncoder (Encode outgoing ByteBuff with length Header)
        IN  - StringDecoder (Decodes incoming ByteBuff to String)
        OUT - StringEncoder (Encodes Outgoing String to ByteBuff)
        IN  - StringToObjectConverter (Decodes String to JsonObject)
        OUT - ObjectToStringConverter (Encodes JsonObject to String)
        IN / OUT - ServerRequestHandler
         */
        
        // En "frame" er hvordan Netty deler opp innkommende / utgående TCP pakker
        // 256 KB er arbitrært, men holder til vårt formål
        final int MAX_FRAME_LENGTH = 256 * 1024;
        
        // Headeren er en 4 byte integer som beskriver payload størrelsen
        final int LENGTH_HEADER_SIZE_BYTES = Integer.BYTES;
        
        
        ChannelPipeline pipeline = channel.pipeline();
        pipeline.addLast(new LengthFieldBasedFrameDecoder(
                MAX_FRAME_LENGTH,
                0,
                LENGTH_HEADER_SIZE_BYTES,
                0,
                LENGTH_HEADER_SIZE_BYTES))
        ;
        pipeline.addLast(new LengthFieldPrepender(LENGTH_HEADER_SIZE_BYTES));
        pipeline.addLast(new StringDecoder());
        pipeline.addLast(new StringEncoder());
        pipeline.addLast(new StringToJsonObjectConverter());
        pipeline.addLast(new JsonObjectToStringConverter());
        pipeline.addLast(new ServerRequestHandler(this));
        
    }
    
    private static final class ServerRequestHandler extends ChannelInboundHandlerAdapter {
        
        private final G6Server serverInstance;
        
        ServerRequestHandler(G6Server serverInstance) { this.serverInstance = serverInstance; }
        
        @Override
        public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
            JSONObject request = (JSONObject) msg;
            JSONObject response;
            synchronized (serverInstance) {
                response = serverInstance.handleRequest(request);
            } ctx.writeAndFlush(response);
        }
        
        @Override
        public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
            serverInstance.channelExceptionCaught(cause);
        }
    }
}
