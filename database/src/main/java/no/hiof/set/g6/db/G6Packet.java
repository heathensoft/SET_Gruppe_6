package no.hiof.set.g6.db;


import no.hiof.set.g6.ny.JsonUtils;
import org.json.simple.JSONObject;

import java.util.EmptyStackException;

/**
 * Utility class for G6 packets.
 *
 * @author Frederik Dahl
 * 16/10/2024
 */


public class G6Packet {
    
    public enum Type {
        
        /**Response from Peer if the Content of a Packet is corrupted*/
        INVALID_PACKET("Invalid Packet"),
        
        /**If the User lacks the proper permission for Request*/
        ACCESS_DENIED("Access Denied"),
        
        /**User attempt to establish connection to Peer*/
        ESTABLISH_CONNECTION("Establish Connection"),
        
        /**User making a Request for a Database Operation*/
        DATABASE_REQUEST("Database Request"),
        
        /**Server Response to Database Request*/
        DATABASE_RESPONSE("Database Response"),
        
        /**Not Supported at the moment*/
        MESSAGE("Message");
        
        Type(String descriptor) {
            this.descriptor = descriptor;
        } public final String descriptor;
        private static final Type[] all;
        static { all = values(); }
        public static Type getByOrdinal(int ordinal) {
            if (ordinal < all.length && ordinal > 0) {
                return all[ordinal];
            } return null;
        }
    }
    
    /**The Container JsonObject for G6 Packets
     * Used on both ends of a connection to identify a Packet and it's Content*/
    public static final class Wrapper {
        public static final String JSON_KEY_ID = "Packet ID";
        public static final String JSON_KEY_TYPE = "Packet Type";
        public static final String JSON_KEY_CONTENT = "Packet Content";
        public static final String JSON_KEY_MESSAGE = "Message";
        
        private int id;
        private Type type;
        private JSONObject content;
        private Wrapper() { /*...*/ }
        
        public int packetID() { return id; }
        public Type packetType() { return type; }
        public JSONObject packetContent() { return content; }
    }
    
    
    private static int next;
    private static final IntQueue id_pool;
    
    static {
        final int initial_cap = 256;
        id_pool = new IntQueue(initial_cap);
        for (int i = 0; i < initial_cap; i++) {
            id_pool.enqueue(next++);
        }
    }
    
    /**Obtain Unique packet ID from ID pool*/
    public static int obtainID() {
        if (id_pool.isEmpty()) return next++;
        return id_pool.dequeue();
    }
    
    /**Return Unique packet ID to ID pool */
    public static void returnID(int id) {
        id_pool.enqueue(id);
    }
    
    
    
    @SuppressWarnings("unchecked")
    public static JSONObject wrap(JSONObject content, Type type, int packet_id) {
        if (content == null || type == null) throw new IllegalStateException("null arg wrap");
        JSONObject payload = new JSONObject();
        payload.put(Wrapper.JSON_KEY_ID,packet_id);
        payload.put(Wrapper.JSON_KEY_TYPE,type.ordinal());
        payload.put(Wrapper.JSON_KEY_CONTENT,content);
        return payload;
    }
    
    public static Wrapper unwrap(JSONObject payload) throws Exception {
        if (payload == null) throw new IllegalStateException("null arg unwrap");
        Wrapper wrapper;
        Object idObject = payload.get(Wrapper.JSON_KEY_ID);
        Object typeObject = payload.get(Wrapper.JSON_KEY_TYPE);
        Object contentObject = payload.get(Wrapper.JSON_KEY_CONTENT);
        if (JsonUtils.anyObjectIsNull(
                idObject,
                typeObject,
                contentObject
        )) throw new Exception("Unable to unwrap packet, missing fields");
        try {
            Integer type_ordinal = (Integer) typeObject;
            Type packet_type = Type.getByOrdinal(type_ordinal);
            if (packet_type == null) {
                throw new Exception("Invalid packet type");
            } Integer packet_id = (Integer) idObject;
            JSONObject packet_content = (JSONObject) contentObject;
            wrapper = new Wrapper();
            wrapper.id = packet_id;
            wrapper.type = packet_type;
            wrapper.content = packet_content;
        } catch (ClassCastException e) {
            throw new Exception("Unable to unwrap packet: Invalid format of one or more fields",e);
        } return wrapper;
    }
    
    /**
     * Build new response packet for invalid request packet
     * @param packet_id id of request packet
     * @param message message to client
     * @return response payload
     */
    @SuppressWarnings("unchecked")
    public static JSONObject invalidPacket(int packet_id, String message) {
        message = message == null ? "null" : message;
        JSONObject invalid_message = new JSONObject();
        invalid_message.put(Wrapper.JSON_KEY_MESSAGE,message);
        return wrap(invalid_message,Type.INVALID_PACKET,packet_id);
    }
    
    /**
     * Build new response packet for access denied
     * @param packet_id id of request packet
     * @param message message to client
     * @return response payload
     */
    @SuppressWarnings("unchecked")
    public static JSONObject accessDenied(int packet_id, String message) {
        message = message == null ? "null" : message;
        JSONObject invalid_message = new JSONObject();
        invalid_message.put(Wrapper.JSON_KEY_MESSAGE,message);
        return wrap(invalid_message,Type.ACCESS_DENIED,packet_id);
    }
    
    
    /**Utility class: Circular Integer Queue*/
    private static final class IntQueue {
        
        private int[] q;
        private int f,r,p;
        
        IntQueue(int capacity) {
            if (capacity < 0) throw new NegativeArraySizeException("capacity < 0: " + capacity);
            q = new int[capacity];
        }
        
        void enqueue(int i) {
            if (p == q.length) {
                int[] tmp = q;
                q = new int[p * 2 + 1];
                for (int v = 0; v < p; v++)
                    q[v] = tmp[(f+v) % p];
                r = p; f = 0;
            } q[r] = i;
            r = (r+1) % q.length;
            p++;
        }
        
        int dequeue() {
            if (p == 0) throw new EmptyStackException();
            int v = q[f];
            if (--p == 0) f = r = p;
            else f = (f+1) % q.length;
            return v;
        }
        
        void ensureCapacity(int size) {
            if (size > q.length) {
                int[] tmp = q;
                q = new int[size];
                if (isEmpty()) return;
                for (int v = 0; v < p; v++)
                    q[v] = tmp[(f+v) % tmp.length];
                r = p; f = 0;
            }
        }
        
        public void clear() { f = r = p = 0; }
        public int size() { return p; }
        public int sizeBytes() { return p * Integer.BYTES; }
        public int capacity() { return q.length; }
        public boolean isEmpty() { return p == 0; }
    }
    
}
