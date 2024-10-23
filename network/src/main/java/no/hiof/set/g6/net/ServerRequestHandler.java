package no.hiof.set.g6.net;

import no.hiof.set.g6.net.packet.RequestPacket;
import no.hiof.set.g6.net.packet.RequestPacketADT;
import no.hiof.set.g6.net.packet.ResponsePacket;
import no.hiof.set.g6.ny.DatatypeArray;
import no.hiof.set.g6.ny.LocalUser;
import no.hiof.set.g6.ny.Locks;
import no.hiof.set.g6.ny.UserAccount;
import org.json.simple.JSONObject;

/**
 * This class is responsible for handling database requests and returning appropriate responses
 */

public class ServerRequestHandler {

    private final PrototypeDB database;

    ServerRequestHandler(PrototypeDB database) {
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
        JSONObject request_content = request.content; // Might be null

        //LocalUser.Role permission = database.getUserRole(request_user); ****************

        // Todo: Check permission here. After the feature has been pushed
        // Give Request type a corresponding permission

        switch (request_type) {
            case ACCOUNT_SEARCH -> {
                JSONObject accountJson = request.getOrNull(RequestPacket.JSON_KEY_USER_ACCOUNT_SEARCH);
                if (accountJson == null) throw new Exception("Missing argument for Account Search");
                UserAccount account = new UserAccount();
                account.fromJson(accountJson);
                DatatypeArray<UserAccount> list = database.searchForAccount(account);

                // Todo: build response here
            }
            case USER_LIST -> {
                DatatypeArray<LocalUser> list = database.allStoredLocalUsers();

                // Todo: build response here
            }
            case USER_ADD -> {
                JSONObject userJson = request.getOrNull(RequestPacket.JSON_KEY_USER_TO_ADD);

                // Todo: build response here
            }
            case USER_REMOVE -> {
                JSONObject userJson = request.getOrNull(RequestPacket.JSON_KEY_USER_TO_REMOVE);

                // Todo: build response here
            }
            case USER_EDIT -> {
                JSONObject userJson = request.getOrNull(RequestPacket.JSON_KEY_USER_TO_EDIT);

                // Todo: build response here
            }
            case LOCK_LIST -> {
                DatatypeArray<Locks> list = database.allStoredLocks();

                // Todo: build response here


            }
            case LOCK_EDIT -> {
                JSONObject lockJson = request.getOrNull(RequestPacket.JSON_KEY_LOCK_TO_EDIT);

                // Todo: build response here
            }
            case ACCESS_DENIED -> throw new Exception("Illegal Request Type: " + request_type);
            default -> throw new IllegalStateException("Unexpected value: " + request_type);
        }

        return null;
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
