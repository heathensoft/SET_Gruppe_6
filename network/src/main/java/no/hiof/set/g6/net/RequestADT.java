package no.hiof.set.g6.net;

import no.hiof.set.g6.dt.JsonSerializable;
import no.hiof.set.g6.dt.JsonUtils;
import org.json.simple.JSONObject;

/**
 * Super class of Request and Response
 */

public abstract class RequestADT implements JsonSerializable {

    public static final String JSON_KEY_REQUEST_TYPE = "Request Type";
    public static final String JSON_KEY_CONTENT = "Request Content";

    /** The type of the request / response */
    public RequestType request_type;
    /** Request / Response content. Can be null */
    public JSONObject content;

    /**
     * Put object into contents
     * @param key the key
     * @param object the object
     */
    @SuppressWarnings("unchecked")
    public void put(String key, Object object) {
        if (JsonUtils.anyObjectIsNull(key,object)) throw new IllegalStateException("null arg. contents");
        if (content == null) {
            content = new JSONObject();
        } content.put(key,object);
    }

    /**
     * Get object from contents
     * @param key the key
     * @return the object or null
     */
    public Object getOrNull(String key) {
        if (content != null) {
          return content.get(key);
        } return null;
    }

    /**
     * Get object if object instanceof JsonObject from contents
     * @param key the key
     * @return the jsonObject or null
     */
    public JSONObject getJsonObjectOrNull(String key) {
        if (content != null) {
            Object object = content.get(key);
            if (object instanceof JSONObject jsonObject) {
                return jsonObject;
            }
        } return null;
    }

    @SuppressWarnings("unchecked")
    protected void putType(JSONObject jsonObject) {
        if (request_type == null) throw new IllegalStateException("type cannot be null");
        jsonObject.put(JSON_KEY_REQUEST_TYPE, request_type.ordinal());
    }

    // Content can be null
    @SuppressWarnings("unchecked")
    protected void putContent(JSONObject jsonObject) {
        if (content != null) jsonObject.put(JSON_KEY_CONTENT,content);
    }

    protected static RequestType getRequestType(JSONObject jsonObject) throws Exception {
        if (jsonObject == null) throw new Exception("JSONObject is null");
        Object typeObject = jsonObject.get(JSON_KEY_REQUEST_TYPE);
        if (typeObject == null) throw new Exception("JSON to RequestType: Type not found");
        RequestType request_type;
        try { Number request_type_number = (Number) typeObject;
            request_type = RequestType.getByOrdinal(request_type_number.intValue());
            if (request_type == null) throw new Exception("JSON to RequestType: Invalid Request Type");
        } catch (ClassCastException e) {
            throw new Exception("Invalid format class for Type", e);
        } return request_type;
    }

    /** Fetch the content of the DB request / response. Content can be null */
    protected static JSONObject getContent(JSONObject jsonObject) throws Exception {
        if (jsonObject == null) throw new Exception("JSONObject is null");
        Object contentObject = jsonObject.get(JSON_KEY_CONTENT);
        if (contentObject != null) {
            JSONObject content;
            try { content = (JSONObject) contentObject;
            } catch (ClassCastException e) {
                throw new Exception("Invalid class for Content", e);
            } return content;
        } return null;
    }
}
