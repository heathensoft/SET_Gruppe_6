package no.hiof.set.g6.net;

import org.json.simple.JSONObject;

import java.util.EmptyStackException;

public class Packet {

    public static final String JSON_KEY_ID = "Packet ID";
    public static final String JSON_KEY_TYPE = "Packet Type";
    public static final String JSON_KEY_CONTENT = "Packet Content";
    public static final String JSON_KEY_MESSAGE = "Message";
    private int id;
    private PacketType type;
    private JSONObject content;
    private Packet() { /*...*/ }
    public int packetID() { return id; }
    public PacketType packetType() { return type; }
    public JSONObject packetContent() { return content; }

    // **************************************************************************************

    // The client can optionally use Packet ID to identify packets.
    // The server response will always contain this ID

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
