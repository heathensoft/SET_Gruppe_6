package no.hiof.set.g6.db.net.ny.handlers;


import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageDecoder;
import no.hiof.set.g6.dt.G6JSON;
import org.json.simple.JSONObject;

import java.util.List;

/**
 * @author Frederik Dahl
 * 10/10/2024
 */


public class StringToJsonObjectConverter extends MessageToMessageDecoder<String> {
    
    @Override
    protected void decode(ChannelHandlerContext channelHandlerContext, String string, List<Object> list) throws Exception {
        JSONObject object = G6JSON.parse(string);
        list.add(object);
    }
}
