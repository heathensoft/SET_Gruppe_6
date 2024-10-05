package no.hiof.set.g6.db.net.naive;


import org.json.simple.JSONObject;

import java.util.function.Function;


/**
 * Interface for handling a Client request by Server
 */

public interface RequestHandler extends Function<JSONObject,JSONObject> {
    
    /**
     * @param clientRequest A Json-Encoded request from the client
     * @return A Json-Encoded response by the server or null (No response)
     */
    @Override
    JSONObject apply(JSONObject clientRequest);
}
