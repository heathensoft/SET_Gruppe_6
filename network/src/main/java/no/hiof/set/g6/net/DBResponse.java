package no.hiof.set.g6.net;

import no.hiof.set.g6.dt.DatatypeArray;
import no.hiof.set.g6.dt.LocalUser;
import no.hiof.set.g6.dt.Lock;
import org.json.simple.JSONObject;

public class DBResponse extends RequestADT {

    public static final String JSON_KEY_SUCCESS = "Success";
    public static final String JSON_KEY_USER_LIST = "User List";
    public static final String JSON_KEY_LOCK_LIST = "Lock List";
    public static final String JSON_KEY_ACCESS_DENIED_MESSAGE = "Message";


    public static DBResponse build_response_user_add(boolean success) {
        DBResponse response = new DBResponse();
        response.request_type = RequestType.USER_ADD;
        response.put(JSON_KEY_SUCCESS,success);
        return response;
    }

    public static DBResponse build_response_user_delete(boolean success) {
        DBResponse response = new DBResponse();
        response.request_type = RequestType.USER_DELETE;
        response.put(JSON_KEY_SUCCESS,success);
        return response;
    }

    public static DBResponse build_response_user_list(DatatypeArray<LocalUser> list) {
        if (list == null) throw new IllegalStateException("null arg. list");
        DBResponse response = new DBResponse();
        response.request_type = RequestType.USER_LIST;
        response.put(JSON_KEY_USER_LIST,list.toJson());
        return response;
    }

    public static DBResponse build_response_lock_toggle(boolean success) {
        DBResponse response = new DBResponse();
        response.request_type = RequestType.LOCK_TOGGLE;
        response.put(JSON_KEY_SUCCESS,success);
        return response;
    }

    public static DBResponse build_response_lock_list(DatatypeArray<Lock> list) {
        if (list == null) throw new IllegalStateException("null arg. list");
        DBResponse response = new DBResponse();
        response.request_type = RequestType.LOCK_LIST;
        response.put(JSON_KEY_LOCK_LIST,list.toJson());
        return response;
    }

    public static DBResponse build_response_access_denied_default() {
        return build_response_access_denied("client does not have permission for request");
    }

    public static DBResponse build_response_access_denied(String message) {
        DBResponse response = new DBResponse();
        message = message == null ? "null" : message;
        response.request_type = RequestType.ACCESS_DENIED;
        response.put(JSON_KEY_ACCESS_DENIED_MESSAGE,message);
        return response;
    }

    @Override
    public void fromJson(JSONObject jsonObject) throws Exception {
        this.request_type = getRequestType(jsonObject);
        this.content = getContent(jsonObject);
    }

    @Override
    public JSONObject toJson() {
        JSONObject jsonObject = new JSONObject();
        putContent(jsonObject);
        putType(jsonObject);
        return jsonObject;
    }
}
