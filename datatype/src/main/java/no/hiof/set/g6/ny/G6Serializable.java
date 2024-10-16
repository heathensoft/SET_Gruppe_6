package no.hiof.set.g6.ny;


import org.json.simple.JSONObject;

/**
 * Super class of all Json-serializable Objects.
 * All G6DataTypes are Json-serializable
 */


public interface G6Serializable {
    
    /**
     * Set the object fields to values provided by the jsonObject
     * @throws Exception if the jsonObject is null or does not relate to this
     */
    void fromJson(JSONObject jsonObject) throws Exception;
    
    /**
     * Serialize the object to Json-Format
     * @return serialized JsonObject
     */
    JSONObject toJson();
}
