package no.hiof.set.g6.db.net;


import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.LengthFieldPrepender;
import io.netty.handler.codec.MessageToMessageEncoder;
import io.netty.handler.codec.string.StringEncoder;
import org.json.simple.JSONObject;

import java.util.List;

/**
 * Outgoing
 */


public class Encoders {
    
    
    // 1
    public static JsonObjectToStringConverter objectToStringConverter() {
        return new JsonObjectToStringConverter();
    }
    
    // 2
    public static StringEncoder stringEncoder() {
        return new StringEncoder();
    }
    
    // 3
    public static LengthFieldPrepender lengthEncoder() {
        return new LengthFieldPrepender(Integer.BYTES);
    }
    
    
    public static final class JsonObjectToStringConverter extends MessageToMessageEncoder<JSONObject> {
        protected void encode(ChannelHandlerContext channelHandlerContext, JSONObject jsonObject, List<Object> list) throws Exception {
            list.add(jsonObject.toJSONString());
        }
    }
    
}
