package no.hiof.set.g6.db.net.ny.pipeline;


import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.codec.LengthFieldPrepender;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import no.hiof.set.g6.db.net.ny.handlers.JsonObjectToStringConverter;
import no.hiof.set.g6.db.net.ny.handlers.StringToJsonObjectConverter;

/**
 * Bygger en SocketChannel (TCP) "pipeline". En rekke med steg som dataen må gå igjennom
 * for å ende opp som et JsonObject vi kan tolke. Både Server og Client har behov for å sende
 * motta data, og bortsett fra "endepunktet" (requestHandler) er pipeline identisk.
 */


abstract class G6ChannelInitializer extends ChannelInitializer<SocketChannel> {
    
    // En "frame" er hvordan Netty deler opp innkommende / utgående TCP pakker
    // 256 KB er arbitrært, men holder til vårt formål
    private static final int MAX_FRAME_LENGTH = 256 * 1024;
    
    // Headeren er en 4 byte integer som beskriver payload størrelsen
    private static final int LENGTH_HEADER_SIZE_BYTES = Integer.BYTES;
    
    private final ChannelInboundHandlerAdapter requestHandler;
    
    protected G6ChannelInitializer(ChannelInboundHandlerAdapter requestHandler) {
        this.requestHandler = requestHandler;
    }
    
    @Override
    protected void initChannel(SocketChannel ch) throws Exception {
        
        // https://netty.io/4.0/api/io/netty/channel/ChannelPipeline.html
        
        /*
        IN  - lengthDecoder (Decode incoming bytes to single ByteBuff)
        OUT - lengthEncoder (Encode outgoing ByteBuff with length Header)
        IN  - StringDecoder (Decodes incoming ByteBuff to String)
        OUT - StringEncoder (Encodes Outgoing String to ByteBuff)
        IN  - StringToObjectConverter (Decodes String to JsonObject)
        OUT - ObjectToStringConverter (Encodes JsonObject to String)
        IN / OUT - requestHandler
         */
        
        
        ChannelPipeline pipeline = ch.pipeline();
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
        pipeline.addLast(requestHandler);
    }
}
