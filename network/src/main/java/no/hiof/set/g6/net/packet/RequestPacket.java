package no.hiof.set.g6.net.packet;

import no.hiof.set.g6.ny.LocalUser;
import org.json.simple.JSONObject;



public class RequestPacket extends RequestPacketADT {

    public static final String JSON_KEY_REQUEST_USER = "Request User";

    /** The User who made the request */
    public LocalUser request_user;


    @Override
    public void fromJson(JSONObject jsonObject) throws Exception {
        this.request_type = getRequestType(jsonObject);
        this.request_user = getRequestUser(jsonObject);
        this.content = getContent(jsonObject);
    }

    @Override
    public JSONObject toJson() {
        JSONObject jsonObject = new JSONObject();
        putRequestUser(jsonObject);
        putContent(jsonObject);
        putType(jsonObject);
        return jsonObject;
    }

    @SuppressWarnings("unchecked")
    protected void putRequestUser(JSONObject jsonObject) {
        if (request_user == null) throw new IllegalStateException("DBRequest: request user cannot be null");
        jsonObject.put(JSON_KEY_REQUEST_USER,request_user);
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
}
