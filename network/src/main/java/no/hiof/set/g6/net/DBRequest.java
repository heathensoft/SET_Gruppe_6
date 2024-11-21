package no.hiof.set.g6.net;

import no.hiof.set.g6.dt.LocalUser;
import org.json.simple.JSONObject;

public class DBRequest extends RequestADT {

    public static final String JSON_KEY_LOCK = "Lock";
    public static final String JSON_KEY_CLIENT = "Client";
    public static final String JSON_KEY_USER_ADD = "Add User";
    public static final String JSON_KEY_USER_DEL = "Delete User";
    public static final String JSON_KEY_LOCK_TOGGLE = "Toggle";

    /** The user who sent the request */
    public int client_account;

    public static DBRequest build_request_user_add(int client_account, LocalUser user) {
        if (user == null) throw new IllegalStateException("null arg. user");
        DBRequest request = new DBRequest();
        request.client_account = client_account;
        request.request_type = RequestType.USER_ADD;
        request.put(JSON_KEY_USER_ADD,user.toJson());
        return request;
    }

    public static DBRequest build_request_user_delete(int client_account, int user_account) {
        DBRequest request = new DBRequest();
        request.client_account = client_account;
        request.request_type = RequestType.USER_DELETE;
        request.put(JSON_KEY_USER_DEL,user_account);
        return request;
    }

    public static DBRequest build_request_user_list(int client_account) {
        DBRequest request = new DBRequest();
        request.client_account = client_account;
        request.request_type = RequestType.USER_LIST;
        return request;
    }

    public static DBRequest build_request_lock_toggle(int client_account, int lock_id, boolean close) {
        DBRequest request = new DBRequest();
        request.client_account = client_account;
        request.request_type = RequestType.LOCK_TOGGLE;
        request.put(JSON_KEY_LOCK,lock_id);
        request.put(JSON_KEY_LOCK_TOGGLE,close);
        return request;
    }

    public static DBRequest build_request_lock_list(int client_account) {
        DBRequest request = new DBRequest();
        request.client_account = client_account;
        request.request_type = RequestType.LOCK_LIST;
        return request;
    }

    @Override
    public void fromJson(JSONObject jsonObject) throws Exception {
        this.request_type = getRequestType(jsonObject);
        this.content = getContent(jsonObject);
        this.client_account = getClient(jsonObject);
    }

    @Override
    public JSONObject toJson() {
        JSONObject jsonObject = new JSONObject();
        putContent(jsonObject);
        putClient(jsonObject);
        putType(jsonObject);
        return jsonObject;
    }

    @SuppressWarnings("unchecked")
    protected void putClient(JSONObject jsonObject) {
        jsonObject.put(JSON_KEY_CLIENT,client_account);
    }

    /** Fetch client account id */
    protected int getClient(JSONObject jsonObject) throws Exception {
        if (jsonObject == null) throw new Exception("null arg. json object");
        Object client_obj = jsonObject.get(JSON_KEY_CLIENT);
        if (client_obj == null) throw new Exception("missing client account");
        try { Number client_number = (Number) client_obj;
            return client_number.intValue();
        } catch (ClassCastException e) {
            throw new Exception("JSON to request: Invalid class for client account", e);
        }
    }

}
