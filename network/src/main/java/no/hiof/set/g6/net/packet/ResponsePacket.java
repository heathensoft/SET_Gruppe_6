package no.hiof.set.g6.net.packet;

import org.json.simple.JSONObject;

/**
 * A database response packet
 */

public class ResponsePacket extends RequestPacketADT {


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
