package no.hiof.set.g6.ny;


import org.json.simple.JSONObject;

/**
 * @author Frederik Dahl
 * 16/10/2024
 */


public abstract class G6Datatype implements G6Serializable, Comparable<G6Datatype> {
    
    
    public G6Datatype(JSONObject object) throws Exception {
        fromJson(object);
    }
    
    public G6Datatype() { }
    
    @Override
    public int compareTo(G6Datatype other) {
        return 0;
    }
}
