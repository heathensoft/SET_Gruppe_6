package no.hiof.set.g6.net.deprecated;

import no.hiof.set.g6.net.deprecated.packet.RequestPacket;
import no.hiof.set.g6.net.deprecated.packet.RequestPacketADT;
import no.hiof.set.g6.net.deprecated.packet.ResponsePacket;
import no.hiof.set.g6.dt.deprecated.DatatypeArray;
import no.hiof.set.g6.dt.deprecated.LocalUser;
import no.hiof.set.g6.dt.deprecated.Locks;
import no.hiof.set.g6.dt.deprecated.UserAccount;
import org.json.simple.JSONObject;

/**
 * This class is responsible for handling database requests and returning appropriate responses
 */

public class ServerRequestHandler {

    private final HUBDatabase database;

    ServerRequestHandler(HUBDatabase database) {
        if (database == null) throw new IllegalStateException("Null arg. database");
        this.database = database;
    }

    /**
     *
     * @param request client database request
     * @return A handled request
     * @throws Exception any Exception thrown here will trigger an INVALID_PACKET response to the client
     */
    protected ResponsePacket handleRequest(RequestPacket request) throws Exception {

        RequestPacketADT.Type request_type = request.request_type;
        LocalUser request_user = request.client_user;

        // Make sure the client user is valid
        if (request_user.missingFields()) {
            throw new Exception("invalid client user data");
        }

        //LocalUser.Role permission = database.getUserRole(request_user); ****************
        // Todo: Check permission here. After the feature has been pushed
        // Give Request type a corresponding permission

        ResponsePacket response;

        switch (request_type) {
            case ACCOUNT_SEARCH -> {

                JSONObject accountJson = request.getOrNull(RequestPacket.JSON_KEY_USER_ACCOUNT_SEARCH);
                if (accountJson == null) throw new Exception("Missing argument for Account Search");
                UserAccount account = new UserAccount();
                account.fromJson(accountJson);
                if (account.missingFields()) throw new Exception("Invalid account to search for");
                DatatypeArray<UserAccount> list = database.searchForAccount(account);
                if (list == null) throw new IllegalStateException("Database should never return null");
                response = ResponsePacket.build_response_account_search(list);

            }
            case USER_LIST -> {

                DatatypeArray<LocalUser> list = database.allStoredLocalUsers();
                if (list == null) throw new IllegalStateException("Database should never return null");
                response = ResponsePacket.build_response_list_users(list);

            }
            case USER_ADD -> {

                JSONObject userJson = request.getOrNull(RequestPacket.JSON_KEY_USER_TO_ADD);
                if (userJson == null) throw new Exception("Missing argument for user add");
                LocalUser user = new LocalUser();
                user.fromJson(userJson);
                if (user.missingFields()) throw new Exception("Invalid user to add");
                boolean success = database.addLocalUser(user);
                response = ResponsePacket.build_response_add_user(success);

            }
            case USER_REMOVE -> {

                JSONObject userJson = request.getOrNull(RequestPacket.JSON_KEY_USER_TO_REMOVE);
                if (userJson == null) throw new Exception("Missing argument for user remove");
                LocalUser user = new LocalUser();
                user.fromJson(userJson);
                if (user.missingFields()) throw new Exception("Invalid user to remove");
                boolean success = database.removeLocalUser(user);
                response = ResponsePacket.build_response_remove_user(success);

            }
            case USER_EDIT -> {

                JSONObject userJson = request.getOrNull(RequestPacket.JSON_KEY_USER_TO_EDIT);
                if (userJson == null) throw new Exception("Missing argument for user edit");
                LocalUser user = new LocalUser();
                user.fromJson(userJson);
                if (user.missingFields()) throw new Exception("Invalid user to edit");
                boolean success = database.editLocalUser(user);
                response = ResponsePacket.build_response_edit_user(success);

            }
            case LOCK_LIST -> {

                DatatypeArray<Locks> list = database.allStoredLocks();
                if (list == null) throw new IllegalStateException("Database should never return null");
                response = ResponsePacket.build_response_list_locks(list);

            }
            case LOCK_EDIT -> {

                JSONObject lockJson = request.getOrNull(RequestPacket.JSON_KEY_LOCK_TO_EDIT);
                if (lockJson == null) throw new Exception("Missing argument for lock edit");
                Locks lock = new Locks();
                lock.fromJson(lockJson);
                if (lock.missingFields()) throw new Exception("Invalid lock to edit");
                boolean success = database.editLock(lock);
                response = ResponsePacket.build_response_edit_lock(success);

            }
            case ACCESS_DENIED -> throw new Exception("Illegal Request Type: " + request_type);
            default -> throw new IllegalStateException("Unexpected value: " + request_type);
        }

        return response;
    }


    /**
     * Called when the G6Server starts
     * @throws Exception if for any reason the database fails to initialize
     */
    protected void onServerStart() throws Exception {
        this.database.load();
    }

    /**
     * Called when the G6Server shuts down
     * @throws Exception if for any reason the database fails to save / close
     */
    protected void onServerShutDown() throws Exception {
        this.database.save();
    }


}
