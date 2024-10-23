package no.hiof.set.g6.net;

import no.hiof.set.g6.net.packet.RequestPacket;
import no.hiof.set.g6.net.packet.ResponsePacket;

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
