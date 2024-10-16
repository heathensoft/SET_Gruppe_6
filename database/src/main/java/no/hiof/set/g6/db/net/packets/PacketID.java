package no.hiof.set.g6.db.net.packets;


import java.util.EmptyStackException;

/**
 * @author Frederik Dahl
 * 16/10/2024
 */


public class PacketID {
    
    
    private static int next_id;
    private static final IntQueue id_pool;
    
    static {
        final int initial_cap = 256;
        id_pool = new IntQueue(initial_cap);
        for (int i = 0; i < initial_cap; i++) {
            id_pool.enqueue(next_id++);
        }
    }
    
    public static int obtain() {
        if (id_pool.isEmpty()) return next_id++;
        return id_pool.dequeue();
    }
    
    public static void returnID(int id) {
        id_pool.enqueue(id);
    }
    
    
    /**Circular Integer queue*/
    private static final class IntQueue {
        
        private int[] q;
        private int f,r,p;
        
        IntQueue(int cap) {
            if (cap < 0) throw new NegativeArraySizeException("cap < 0: " + cap);
            q = new int[cap];
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
        
        public void clear() {
            f = r = p = 0;
        }
        public int size() {
            return p;
        }
        public int sizeBytes() {
            return p * Integer.BYTES;
        }
        public int capacity() {
            return q.length;
        }
        public boolean isEmpty() {
            return p == 0;
        }
    }
    
}
