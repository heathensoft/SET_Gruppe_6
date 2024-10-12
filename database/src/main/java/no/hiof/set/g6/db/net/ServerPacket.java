package no.hiof.set.g6.db.net;


import io.netty.channel.Channel;
import org.json.simple.JSONObject;

/**
 * @author Frederik Dahl
 * 12/10/2024
 */


public class ServerPacket {
    
    private final Channel channel;
    private final JSONObject message;
    
    protected ServerPacket(Channel channel, JSONObject message) {
        this.channel = channel;
        this.message = message;
    }
    
    public Channel channel() { return channel; }
    
    public JSONObject get() { return message; }
    
    
}
