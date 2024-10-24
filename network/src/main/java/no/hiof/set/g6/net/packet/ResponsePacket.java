package no.hiof.set.g6.net.packet;

import no.hiof.set.g6.ny.*;
import org.json.simple.JSONObject;

/**
 * A database response packet. Sent from a Server to a Local Client
 */

public class ResponsePacket extends RequestPacketADT {

    public static final String JSON_KEY_ACCOUNT_LIST = "Account List";
    public static final String JSON_KEY_USER_LIST = "User List";
    public static final String JSON_KEY_LOCK_LIST = "Lock List";
    public static final String JSON_KEY_RESPONSE_BOOL = "Response Bool";
    public static final String JSON_KEY_ACCESS_DENIED_STRING = "Access Denied";

    // Response Builders

    public static ResponsePacket build_response_account_search(DatatypeArray<UserAccount> list) {
        if (JsonUtils.anyObjectIsNull(list)) throw new IllegalStateException("null arg. response builder");
        ResponsePacket packet = new ResponsePacket();
        packet.request_type = Type.ACCOUNT_SEARCH;
        packet.putReplace(JSON_KEY_ACCOUNT_LIST,list.toJson());
        return packet;
    }

    public static ResponsePacket build_response_list_users(DatatypeArray<LocalUser> list) {
        if (JsonUtils.anyObjectIsNull(list)) throw new IllegalStateException("null arg. response builder");
        ResponsePacket packet = new ResponsePacket();
        packet.request_type = Type.USER_LIST;
        packet.putReplace(JSON_KEY_USER_LIST,list.toJson());
        return packet;
    }

    public static ResponsePacket build_response_add_user(Boolean bool) {
        if (JsonUtils.anyObjectIsNull(bool)) throw new IllegalStateException("null arg. response builder");
        ResponsePacket packet = new ResponsePacket();
        packet.request_type = Type.USER_ADD;
        packet.putReplace(JSON_KEY_RESPONSE_BOOL,bool);
        return packet;
    }

    public static ResponsePacket build_response_remove_user(Boolean bool) {
        if (JsonUtils.anyObjectIsNull(bool)) throw new IllegalStateException("null arg. response builder");
        ResponsePacket packet = new ResponsePacket();
        packet.request_type = Type.USER_REMOVE;
        packet.putReplace(JSON_KEY_RESPONSE_BOOL,bool);
        return packet;
    }

    public static ResponsePacket build_response_edit_user(Boolean bool) {
        if (JsonUtils.anyObjectIsNull(bool)) throw new IllegalStateException("null arg. response builder");
        ResponsePacket packet = new ResponsePacket();
        packet.request_type = Type.USER_EDIT;
        packet.putReplace(JSON_KEY_RESPONSE_BOOL,bool);
        return packet;
    }

    public static ResponsePacket build_response_list_locks(DatatypeArray<Locks> locks) {
        if (JsonUtils.anyObjectIsNull(locks)) throw new IllegalStateException("null arg. response builder");
        ResponsePacket packet = new ResponsePacket();
        packet.request_type = Type.LOCK_LIST;
        packet.putReplace(JSON_KEY_LOCK_LIST,locks.toJson());
        return packet;
    }

    public static ResponsePacket build_response_edit_lock(Boolean bool) {
        if (JsonUtils.anyObjectIsNull(bool)) throw new IllegalStateException("null arg. response builder");
        ResponsePacket packet = new ResponsePacket();
        packet.request_type = Type.LOCK_EDIT;
        packet.putReplace(JSON_KEY_RESPONSE_BOOL,bool);
        return packet;
    }

    public static ResponsePacket build_response_access_denied(String message) {
        ResponsePacket packet = new ResponsePacket();
        packet.request_type = Type.ACCESS_DENIED;
        message = message == null ? "null" : message;
        packet.putReplace(JSON_KEY_ACCESS_DENIED_STRING,message);
        return packet;
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
