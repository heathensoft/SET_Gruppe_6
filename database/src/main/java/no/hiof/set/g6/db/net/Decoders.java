package no.hiof.set.g6.db.net;


import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.codec.MessageToMessageDecoder;
import io.netty.handler.codec.string.StringDecoder;
import no.hiof.set.g6.dt.G6JSON;

import java.util.List;

/**
 * Incoming
 */


public class Decoders {
    
    
    
    // 1
    public static LengthFieldBasedFrameDecoder lengthDecoder() {
        return new LengthFieldBasedFrameDecoder(1024 * 1024,0,Integer.BYTES,0,Integer.BYTES);
    }
    
    // 2
    public static StringDecoder stringDecoder() {
        return new StringDecoder(); // UTF8
    }
    
    // 3
    public static StringToJsonObjectConverter stringToObjectConverter() {
        return new StringToJsonObjectConverter();
    }
    
    public static final class StringToJsonObjectConverter extends MessageToMessageDecoder<String> {
        protected void decode(ChannelHandlerContext channelHandlerContext, String string, List<Object> list) throws Exception {
            list.add(G6JSON.parse(string));
        }
    }
}
