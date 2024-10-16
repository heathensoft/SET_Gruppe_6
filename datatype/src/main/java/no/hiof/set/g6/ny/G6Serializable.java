package no.hiof.set.g6.ny;


import org.json.simple.JSONObject;

/**
 * @author Frederik Dahl
 * 16/10/2024
 */


public interface G6Serializable {
    
    /**@throws Exception if the jsonObject is null or does not relate to this*/
    void fromJson(JSONObject jsonObject) throws Exception;
    
    JSONObject toJson();
}
