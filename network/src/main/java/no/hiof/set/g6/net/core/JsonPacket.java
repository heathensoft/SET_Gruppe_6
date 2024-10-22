package no.hiof.set.g6.net.core;


import io.netty.channel.Channel;
import org.json.simple.JSONObject;

/**
 * @author Frederik Dahl
 * 13/10/2024
 */


public class JsonPacket {
    
    private final Channel channel;
    private final JSONObject payload;
    
    public JsonPacket(JSONObject payload) { this(payload,null); }
    
    public JsonPacket(JSONObject payload, Channel channel) {
        this.channel = channel;
        this.payload = payload;
    }
    
    /** Creates new packet with a reference to this channel*/
    public JsonPacket response(JSONObject payload) {
        return new JsonPacket(payload,channel);
    }
    
    public boolean assignedToChannel() { return channel != null; }
    
    public JSONObject get() { return payload; }
    
    public Channel channel() { return channel; }
    
    public String toString() { return payload == null ? "EMPTY PACKET" : payload.toString(); }
}
