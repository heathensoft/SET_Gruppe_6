package no.hiof.set.g6.net;

import org.json.simple.JSONObject;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class PacketWrapperTest {


    @Test
    @SuppressWarnings("unchecked")
    void testPacketWrapperJsonConversion() throws Exception {
        int id = 3;
        PacketType type = PacketType.DATABASE_REQUEST;
        String content_string = "content";
        JSONObject content = new JSONObject();
        content.put(content_string,content_string);
        JSONObject packet = PacketWrapper.wrap(content,type,id);
        PacketWrapper wrapper = PacketWrapper.unwrap(packet);
        assertEquals(id,wrapper.id()); // id stays the same
        assertEquals(type,wrapper.type()); // type stays the same
        assertNotNull(wrapper.content()); // has content
        Object content_obj = wrapper.content().get(content_string);
        assertNotNull(content_obj); // check the content exists
        assertInstanceOf(String.class, content_obj);
        assertEquals(content_string,content_obj); // and is the same
    }
}
