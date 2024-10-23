package no.hiof.set.g6.net.packet;


import no.hiof.set.g6.ny.JsonSerializable;
import no.hiof.set.g6.ny.LocalUser;
import org.json.simple.JSONObject;

/**
 * @author Frederik Dahl
 * 17/10/2024
 */


public abstract class RequestPacketADT implements JsonSerializable {

    public static final String JSON_KEY_REQUEST_TYPE = "Request Type";
    public static final String JSON_KEY_REQUEST_USER = "Request User";
    public static final String JSON_KEY_CONTENT = "Request Content";

    public enum Type {

        ACCOUNT_SEARCH("Account Search"),
        USER_LIST("Local User List"),
        USER_ADD("Local User Add"),
        USER_REMOVE("Local User Remove"),
        USER_EDIT("Local User Edit"),
        LOCK_LIST("Lock List"),
        LOCK_EDIT("Lock Edit"),
        ACCESS_DENIED("Access Denied");

        Type(String descriptor) {
            this.descriptor = descriptor;
        }
        public final String descriptor;
        private static final Type[] all;
        static { all = values(); }
        public static Type getByOrdinal(int ordinal) {
            if (ordinal < all.length && ordinal > 0) {
                return all[ordinal];
            } return null;
        }
    }

    /** The Type of the request / response */
    protected Type request_type;
    /** The User who made the request. Only used by request, not response */
    protected LocalUser request_user;
    /** Request / Response Packet content */
    protected JSONObject content;



    @SuppressWarnings("unchecked")
    protected void putType(JSONObject jsonObject) {
        if (request_type == null) throw new IllegalStateException("DBRequest: type cannot be null");
        jsonObject.put(JSON_KEY_REQUEST_TYPE, request_type.ordinal());
    }

    @SuppressWarnings("unchecked")
    protected void putRequestUser(JSONObject jsonObject) {
        if (request_user == null) throw new IllegalStateException("DBRequest: request user cannot be null");
        jsonObject.put(JSON_KEY_REQUEST_USER,request_user);
    }

    // Content can be null
    @SuppressWarnings("unchecked")
    protected void putContent(JSONObject jsonObject) {
        if (content != null) jsonObject.put(JSON_KEY_CONTENT,content);
    }

    /** Fetch the Request User (The User who made the request) of the DB request */
    protected LocalUser getRequestUser(JSONObject jsonObject) throws Exception {
        if (jsonObject == null) throw new Exception("JSONObject is null");
        Object userObject = jsonObject.get(JSON_KEY_REQUEST_USER);
        if (userObject == null) throw new Exception("JSON to DBRequest: Request User not found");
        LocalUser request_user;
        try { request_user = (LocalUser) userObject;
        } catch (ClassCastException e) {
            throw new Exception("JSON to DBRequest: Invalid format for LocalUser", e);
        } return request_user;
    }

    /** Fetch the Type of the DB request / response */
    protected Type getRequestType(JSONObject jsonObject) throws Exception {
        if (jsonObject == null) throw new Exception("JSONObject is null");
        Object typeObject = jsonObject.get(JSON_KEY_REQUEST_TYPE);
        if (typeObject == null) throw new Exception("JSON to DBRequest: Type not found");
        Type request_type;
        try { Integer request_type_integer = (Integer) typeObject;
            request_type = Type.getByOrdinal(request_type_integer);
            if (request_type == null) throw new Exception("JSON to DBRequest: Unknown Request Type");
        } catch (ClassCastException e) {
            throw new Exception("JSON to DBRequest: Invalid format for Type", e);
        } return request_type;
    }

    /** Fetch the content of the DB request / response. Content can be null */
    protected JSONObject getContent(JSONObject jsonObject) throws Exception {
        if (jsonObject == null) throw new Exception("JSONObject is null");
        Object contentObject = jsonObject.get(JSON_KEY_CONTENT);
        if (contentObject != null) {
            JSONObject content;
            try { content = (JSONObject) contentObject;
            } catch (ClassCastException e) {
                throw new Exception("JSON to DBRequest: Invalid format for Content", e);
            } return content;
        } return null;
    }

    
}
