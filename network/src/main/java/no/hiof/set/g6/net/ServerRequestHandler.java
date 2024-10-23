package no.hiof.set.g6.net;

import no.hiof.set.g6.net.packet.RequestPacket;
import no.hiof.set.g6.net.packet.RequestPacketADT;
import no.hiof.set.g6.net.packet.ResponsePacket;
import no.hiof.set.g6.ny.LocalUser;
import org.json.simple.JSONObject;

/**
 * This class is responsible for handling database requests and returning appropriate response
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
        // ATP the incoming request is never null and all field are not null
        RequestPacketADT.Type request_type = request.request_type;
        LocalUser request_user = request.request_user;
        JSONObject request_content = request.content;

        switch (request_type) {
            case ACCOUNT_SEARCH -> {

            }
            case USER_LIST -> {
            }
            case USER_ADD -> {
            }
            case USER_REMOVE -> {
            }
            case USER_EDIT -> {
            }
            case LOCK_LIST -> {
            }
            case LOCK_EDIT -> {
            }
            case ACCESS_DENIED -> {
            }
            default -> throw new IllegalStateException("Unexpected value: " + request_type);
        }

        return null;
    }


    /**
     * Called when the G6Server starts
     * @throws Exception if for some reason the database fails to initialize
     */
    protected void onServerStart() throws Exception {
        this.database.load();
    }

    /**
     * Called when the G6Server shuts down
     * @throws Exception if for some reason the database fails to save / close
     */
    protected void onServerShutDown() throws Exception {
        this.database.save();
    }


}
