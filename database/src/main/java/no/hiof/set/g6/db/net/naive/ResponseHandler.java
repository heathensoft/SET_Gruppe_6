package no.hiof.set.g6.db.net.naive;


import org.json.simple.JSONObject;

import java.util.function.Consumer;

/**
 * Interface for handling a Server response by Client
 */


public interface ResponseHandler extends Consumer<JSONObject> {
    
    /**
     * @param serverResponse A Json-Encoded response by the server
     */
    @Override
    void accept(JSONObject serverResponse);
}
