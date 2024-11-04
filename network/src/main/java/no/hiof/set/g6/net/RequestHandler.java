package no.hiof.set.g6.net;


import no.hiof.set.g6.dt.JsonUtils;
import no.hiof.set.g6.dt.LocalUser;
import org.json.simple.JSONObject;


public class RequestHandler {

    private RequestHandler() {}

    /**
     * @param request client db request
     * @return server db response
     * @throws Exception any Exception thrown here will trigger an INVALID_PACKET response to the client
     */
    public static DBResponse handleLocalDBRequest(LocalSystemDB database, DBRequest request) throws Exception {

        LocalUser client = database.getUserByAccountID(request.client_account);
        if (client == null) throw new Exception("Client not found: not a registered user");
        RequestType request_type = request.request_type;

        switch (request_type) {
            case USER_ADD -> {
                if (client.hasRequiredRole(LocalUser.Role.OWNER)) {
                    JSONObject user_json = request.getJsonObjectOrNull(DBRequest.JSON_KEY_USER_ADD);
                    if (user_json == null) throw new Exception("Request missing content");
                    LocalUser user = new LocalUser();
                    user.fromJson(user_json);
                    boolean success = database.addUser(user);
                    return DBResponse.build_response_user_add(success);
                } else return DBResponse.build_response_access_denied_default();
            }
            case USER_DELETE -> {
                if (client.hasRequiredRole(LocalUser.Role.OWNER)) {
                    Object user_account_json = request.getOrNull(DBRequest.JSON_KEY_USER_DEL);
                    if (user_account_json == null)
                        throw new Exception("Request missing content");
                    try { int account_id = ((Number)user_account_json).intValue();
                        boolean success = database.deleteUser(account_id);
                        return DBResponse.build_response_user_delete(success);
                    } catch (ClassCastException e) {
                        throw new Exception("Invalid class for user account id", e);
                    }
                } else return DBResponse.build_response_access_denied_default();
            }
            case USER_LIST -> {
                if (client.hasRequiredRole(LocalUser.Role.RESIDENT)) {
                    return DBResponse.build_response_user_list(database.getUsers());
                } else return DBResponse.build_response_access_denied_default();
            }
            case LOCK_TOGGLE -> {
                if (client.hasRequiredRole(LocalUser.Role.RESIDENT)) {
                    Object lock_id_obj = request.getOrNull(DBRequest.JSON_KEY_LOCK);
                    Object lock_toggle_obj = request.getOrNull(DBRequest.JSON_KEY_LOCK_TOGGLE);
                    if (JsonUtils.anyObjectIsNull(lock_id_obj,lock_toggle_obj))
                        throw new Exception("Request missing content");
                    try { boolean success;
                        int lock_id = ((Number)lock_id_obj).intValue();
                        Boolean toggle = (Boolean) lock_toggle_obj;
                        if (toggle) success = database.closeLock(lock_id);
                        else success = database.openLock(lock_id);
                        return DBResponse.build_response_lock_toggle(success);
                    } catch (ClassCastException e) {
                        throw new Exception("Invalid class for one or more fields", e);
                    }
                } else return DBResponse.build_response_access_denied_default();
            }
            case LOCK_LIST -> {
                if (client.hasRequiredRole(LocalUser.Role.RESIDENT)) {
                    return DBResponse.build_response_lock_list(database.getLocks());
                } else return DBResponse.build_response_access_denied_default();
            }
            case ACCESS_DENIED -> throw new Exception("Illegal Request Type: " + request_type);
            default -> throw new IllegalStateException("Unexpected value: " + request_type);
        }

    }


}
