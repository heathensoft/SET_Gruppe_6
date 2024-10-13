package no.hiof.set.g6.db.net.ny;


import io.netty.channel.Channel;
import org.json.simple.JSONObject;

/**
 * @author Frederik Dahl
 * 13/10/2024
 */


public class G6Packet {
 
    private final Channel channel;
    private final JSONObject payload;
    
    public G6Packet(JSONObject payload) { this(payload,null); }
    
    public G6Packet(JSONObject payload, Channel channel) {
        this.channel = channel;
        this.payload = payload;
    }
    
    /** Creates new packet with a reference to this channel*/
    public G6Packet response(JSONObject payload) {
        return new G6Packet(payload,channel);
    }
    
    public boolean assignedToChannel() { return channel != null; }
    
    public JSONObject get() { return payload; }
    
    public Channel channel() { return channel; }
    
}
