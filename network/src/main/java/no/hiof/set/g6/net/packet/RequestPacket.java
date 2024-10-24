package no.hiof.set.g6.net.packet;

import no.hiof.set.g6.ny.JsonUtils;
import no.hiof.set.g6.ny.LocalUser;
import no.hiof.set.g6.ny.Locks;
import no.hiof.set.g6.ny.UserAccount;
import org.json.simple.JSONObject;


/**
 * A database request packet. Sent from a Client to the HUB server
 *
 */

public class RequestPacket extends RequestPacketADT {

    public static final String JSON_KEY_CLIENT_USER = "Client";
    public static final String JSON_KEY_USER_ACCOUNT_SEARCH = "Account Search";
    public static final String JSON_KEY_USER_TO_ADD = "User Add";
    public static final String JSON_KEY_USER_TO_REMOVE = "User Remove";
    public static final String JSON_KEY_USER_TO_EDIT = "User Edit";
    public static final String JSON_KEY_LOCK_TO_EDIT = "Lock";


    /** The User who sent the request */
    public LocalUser client_user;


    // Request Builders

    public static RequestPacket build_request_account_search(LocalUser client, UserAccount search) {
        if (JsonUtils.anyObjectIsNull(client,search)) throw new IllegalStateException("null arg. request builder");
        RequestPacket packet = new RequestPacket();
        packet.request_type = Type.ACCOUNT_SEARCH;
        packet.client_user = client;
        packet.putReplace(JSON_KEY_USER_ACCOUNT_SEARCH,search.toJson());
        return packet;
    }

    public static RequestPacket build_request_list_users(LocalUser client) throws Exception {
        if (JsonUtils.anyObjectIsNull(client)) throw new IllegalStateException("null arg. request builder");
        RequestPacket packet = new RequestPacket();
        packet.request_type = Type.ACCOUNT_SEARCH;
        packet.client_user = client;
        return packet;
    }

    public static RequestPacket build_request_add_user(LocalUser client, LocalUser user) throws Exception {
        if (JsonUtils.anyObjectIsNull(client,user)) throw new IllegalStateException("null arg. request builder");
        RequestPacket packet = new RequestPacket();
        packet.request_type = Type.USER_ADD;
        packet.client_user = client;
        packet.putReplace(JSON_KEY_USER_TO_ADD,user.toJson());
        return packet;
    }

    public static RequestPacket build_request_remove_user(LocalUser client, LocalUser user) throws Exception {
        if (JsonUtils.anyObjectIsNull(client,user)) throw new IllegalStateException("null arg. request builder");
        RequestPacket packet = new RequestPacket();
        packet.request_type = Type.USER_REMOVE;
        packet.client_user = client;
        packet.putReplace(JSON_KEY_USER_TO_REMOVE,user.toJson());
        return packet;
    }

    public static RequestPacket build_request_edit_user(LocalUser client, LocalUser user) throws Exception {
        if (JsonUtils.anyObjectIsNull(client,user)) throw new IllegalStateException("null arg. request builder");
        RequestPacket packet = new RequestPacket();
        packet.request_type = Type.USER_EDIT;
        packet.client_user = client;
        packet.putReplace(JSON_KEY_USER_TO_EDIT,user.toJson());
        return packet;
    }

    public static RequestPacket build_request_list_locks(LocalUser client) throws Exception {
        if (JsonUtils.anyObjectIsNull(client)) throw new IllegalStateException("null arg. request builder");
        RequestPacket packet = new RequestPacket();
        packet.request_type = Type.LOCK_LIST;
        packet.client_user = client;
        return packet;
    }

    public static RequestPacket build_request_edit_lock(LocalUser client, Locks lock) throws Exception {
        if (JsonUtils.anyObjectIsNull(client,lock)) throw new IllegalStateException("null arg. request builder");
        RequestPacket packet = new RequestPacket();
        packet.request_type = Type.LOCK_EDIT;
        packet.client_user = client;
        packet.putReplace(JSON_KEY_LOCK_TO_EDIT,lock.toJson());
        return packet;
    }


    @Override
    public void fromJson(JSONObject jsonObject) throws Exception {
        this.request_type = getRequestType(jsonObject);
        this.client_user = getClientUser(jsonObject);
        this.content = getContent(jsonObject);
    }

    @Override
    public JSONObject toJson() {
        JSONObject jsonObject = new JSONObject();
        putClientUser(jsonObject);
        putContent(jsonObject);
        putType(jsonObject);
        return jsonObject;
    }

    @SuppressWarnings("unchecked")
    protected void putClientUser(JSONObject jsonObject) {
        if (client_user == null) throw new IllegalStateException("DBRequest: request user cannot be null");
        jsonObject.put(JSON_KEY_CLIENT_USER, client_user);
    }

    /** Fetch the Request User (The User who made the request) of the DB request */
    protected LocalUser getClientUser(JSONObject jsonObject) throws Exception {
        if (jsonObject == null) throw new Exception("JSONObject is null");
        Object userObject = jsonObject.get(JSON_KEY_CLIENT_USER);
        if (userObject == null) throw new Exception("JSON to DBRequest: Request User not found");
        LocalUser request_user;
        try { request_user = (LocalUser) userObject;
        } catch (ClassCastException e) {
            throw new Exception("JSON to DBRequest: Invalid format for LocalUser", e);
        } return request_user;
    }
}
