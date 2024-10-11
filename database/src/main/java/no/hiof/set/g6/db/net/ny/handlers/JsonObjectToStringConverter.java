package no.hiof.set.g6.db.net.ny.handlers;


import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageEncoder;
import org.json.simple.JSONObject;

import java.util.List;

/**
 * @author Frederik Dahl
 * 10/10/2024
 */


public class JsonObjectToStringConverter extends MessageToMessageEncoder<JSONObject> {
    
    @Override
    protected void encode(ChannelHandlerContext channelHandlerContext, JSONObject object, List<Object> list) throws Exception {
        String string = object.toJSONString();
        list.add(string);
    }
}
