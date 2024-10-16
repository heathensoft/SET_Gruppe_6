package no.hiof.set.g6.db.net.packets;


import org.json.simple.JSONObject;

/**
 * @author Frederik Dahl
 * 16/10/2024
 */


public class G6PacketWrapper {
    
    public static final String ID_KEY = "PACKET_ID";
    public static final String TYPE_KEY = "PACKET_TYPE";
    public static final String CONTENT_KEY = "PACKET_CONTENT";
    
    private int id;
    private PacketType type;
    private JSONObject content;
    private G6PacketWrapper() {}
    
    
    @SuppressWarnings("unchecked")
    public static JSONObject wrap(JSONObject content, PacketType type, int packet_id) {
        if (content == null || type == null) throw new IllegalStateException("null arg wrap");
        JSONObject payload = new JSONObject();
        payload.put(ID_KEY,packet_id);
        payload.put(TYPE_KEY,type.ordinal());
        payload.put(CONTENT_KEY,content);
        return payload;
    }
    
    public static G6PacketWrapper unwrap(JSONObject payload) throws Exception {
        if (payload == null) throw new IllegalStateException("null arg unwrap");
        G6PacketWrapper wrapper;
        Object idObject = payload.get(CONTENT_KEY);
        Object typeObject = payload.get(CONTENT_KEY);
        Object contentObject = payload.get(CONTENT_KEY);
        if (anyObjectIsNull(
                idObject,
                typeObject,
                contentObject
        )) throw new Exception("Unable to unwrap G6Packet, missing content");
        try {
            Integer type_ordinal = (Integer) typeObject;
            PacketType packet_type = PacketType.getByOrdinal(type_ordinal);
            if (packet_type == null) {
                throw new Exception("Invalid Packet Type");
            } Integer packet_id = (Integer) idObject;
            JSONObject packet_content = (JSONObject) contentObject;
            wrapper = new G6PacketWrapper();
            wrapper.id = packet_id;
            wrapper.type = packet_type;
            wrapper.content = packet_content;
        } catch (ClassCastException e) {
            throw new Exception("JSON to G6Packet: Invalid format of one or more fields",e);
        } return wrapper;
    }
    
    /**
     * Check for null object
     * @param objects 0 or more objects
     * @return true if any object is null
     */
    private static boolean anyObjectIsNull(Object ... objects) {
        if (objects == null) throw new IllegalStateException("Provided Illegal null argument for private method");
        for (Object object : objects) {
            if (object == null) return true;
        } return false;
    }
    
}
