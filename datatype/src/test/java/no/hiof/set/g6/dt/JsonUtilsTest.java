package no.hiof.set.g6.dt;

import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;

import static org.junit.jupiter.api.Assertions.*;

public class JsonUtilsTest {
    @Test
    @DisplayName("Test parse method with valid JSON string")
    public void testParseValidJson() throws ParseException {
        String jsonString = "{\"key\":\"value\"}";
        JSONObject jsonObject = JsonUtils.parse(jsonString);

        assertNotNull(jsonObject);
        assertEquals("value", jsonObject.get("key"));
    }

    @Test
    @DisplayName("Test parse method with invalid JSON string")
    public void testParseInvalidJson() {
        String invalidJsonString = "{key:value}";

        assertThrows(ParseException.class, () -> {
            JsonUtils.parse(invalidJsonString);
        });
    }

    @Test
    @DisplayName("Test parse method with null input")
    public void testParseWithNullInput() {
        assertThrows(ParseException.class, () -> {
            JsonUtils.parse(null);
        });
    }
}
