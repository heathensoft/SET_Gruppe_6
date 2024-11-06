package no.hiof.set.g6.net;

import no.hiof.set.g6.dt.deprecated.JsonUtils;
import org.json.simple.JSONObject;

import java.util.EmptyStackException;

public class PacketWrapper {

    public static final String JSON_KEY_ID = "Packet ID";
    public static final String JSON_KEY_TYPE = "Packet Type";
    public static final String JSON_KEY_CONTENT = "Packet Content";
    public static final String JSON_KEY_MESSAGE = "Message";
    private int id;
    private PacketType type;
    private JSONObject content;
    private PacketWrapper() { /*...*/ }
    public int id() { return id; }
    public PacketType type() { return type; }
    public JSONObject content() { return content; }

    /** Prepare packet for transmission */
    @SuppressWarnings("unchecked")
    public static JSONObject wrap(JSONObject content, PacketType type, int id) {
        if (content == null || type == null) throw new IllegalStateException("null arg wrap");
        JSONObject payload = new JSONObject();
        payload.put(JSON_KEY_ID,id);
        payload.put(JSON_KEY_TYPE,type.ordinal());
        payload.put(JSON_KEY_CONTENT,content);
        return payload;
    }

    public static PacketWrapper unwrap(JSONObject payload) throws Exception {
        if (payload == null) throw new IllegalStateException("null arg unwrap");
        PacketWrapper packetWrapper;
        Object idObject = payload.get(JSON_KEY_ID);
        Object typeObject = payload.get(JSON_KEY_TYPE);
        Object contentObject = payload.get(JSON_KEY_CONTENT);
        if (JsonUtils.anyObjectIsNull(
                idObject,
                typeObject,
                contentObject
        )) throw new Exception("Unable to unwrap packet, missing fields");
        try {
            Number type_ordinal = (Number) typeObject;
            PacketType packet_type = PacketType.getByOrdinal(type_ordinal.intValue());
            if (packet_type == null) {
                throw new Exception("Invalid packet type");
            } Number packet_id = (Number) idObject;
            JSONObject packet_content = (JSONObject) contentObject;
            packetWrapper = new PacketWrapper();
            packetWrapper.id = packet_id.intValue();
            packetWrapper.type = packet_type;
            packetWrapper.content = packet_content;
        } catch (ClassCastException e) {
            throw new Exception("Unable to unwrap packet: Invalid format of one or more fields",e);
        } return packetWrapper;
    }

    /**
     * Build new response packet for invalid request packet
     * @param id id of request packet
     * @param message message to client
     * @return response payload
     */
    @SuppressWarnings("unchecked")
    public static JSONObject invalidResponse(int id, String message) {
        message = message == null ? "null" : message;
        JSONObject invalid_message = new JSONObject();
        invalid_message.put(JSON_KEY_MESSAGE,message);
        return wrap(invalid_message, PacketType.INVALID_PACKET,id);
    }

    // **************************************************************************************

    // The client can optionally use Packet ID to identify packets.
    // The server response will use the same ID as the request that triggered it.

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

    // **************************************************************************************

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

        void clear() { f = r = p = 0; }
        int size() { return p; }
        int sizeBytes() { return p * Integer.BYTES; }
        int capacity() { return q.length; }
        boolean isEmpty() { return p == 0; }
    }
}
