package no.hiof.set.g6.net.packet;

import org.json.simple.JSONObject;

public class RequestPacket extends RequestPacketADT {




    @Override
    public void fromJson(JSONObject jsonObject) throws Exception {
        this.request_type = getRequestType(jsonObject);
        this.request_type = getRequestType(jsonObject);
        this.content = getContent(jsonObject);
    }

    @Override
    public JSONObject toJson() {
        JSONObject jsonObject = new JSONObject();

        return null;
    }
}
