package no.hiof.set.g6.dt.old;


import org.json.simple.JSONObject;

/**
 * Super class of all Json-serializable Objects.
 * All G6DataTypes are Json-serializable
 */


public interface JsonSerializable {
    
    /**
     * Set the objects fields to values provided by the jsonObject
     * @throws Exception if the jsonObject is null or does not translate to this
     */
    void fromJson(JSONObject jsonObject) throws Exception;
    
    /**
     * Convert the object to Json Object
     * @return JsonObject
     */
    JSONObject toJson();
}
