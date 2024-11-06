package no.hiof.set.g6.net;

import org.json.simple.JSONObject;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class RequestADTTest {

    @Test
    void testRequestTypeJsonConversion() throws Exception {
        DBRequest request = new DBRequest();
        request.request_type = RequestType.USER_ADD;
        JSONObject jsonObject = new JSONObject();
        request.putType(jsonObject);
        RequestType type = RequestADT.getRequestType(jsonObject);
        assertEquals(request.request_type,type);
    }

    @Test
    void testRequestContentJsonConversion() throws Exception {
        String content_string = "content";
        DBRequest request = new DBRequest();
        request.put(content_string,content_string);
        JSONObject jsonObject = new JSONObject();
        request.putContent(jsonObject);
        JSONObject content = RequestADT.getContent(jsonObject);
        assertNotNull(content);
        Object content_obj = content.get(content_string);
        assertNotNull(content_obj);
        assertInstanceOf(String.class,content_obj);
        assertEquals(content_string,content_obj);
    }

}
