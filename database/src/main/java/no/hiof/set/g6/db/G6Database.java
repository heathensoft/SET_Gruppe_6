package no.hiof.set.g6.db;


import io.netty.channel.Channel;
import no.hiof.set.g6.db.net.JsonPacket;
import org.json.simple.JSONObject;

/**
 * @author Frederik Dahl
 * 07/10/2024
 */


public abstract class G6Database {


    
    
    public JsonPacket serveRequest(JsonPacket request) throws Exception {
        
        
        int packet_id;
        JSONObject packet_content;
        G6Packet.Type packet_type;
        Channel channel = request.channel();
        
        {
            JSONObject request_payload = request.get();
            G6Packet.Wrapper unwrapped = G6Packet.unwrap(request_payload);
            packet_content = unwrapped.packetContent();
            packet_type = unwrapped.packetType();
            packet_id = unwrapped.packetID();
        }
        
        
        
        
        
        return null;
    }
    
    protected abstract JSONObject getListOfUsers(JSONObject request) throws Exception;
    
    
    
    
}
