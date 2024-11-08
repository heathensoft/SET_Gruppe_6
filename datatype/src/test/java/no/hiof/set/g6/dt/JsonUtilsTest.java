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

    @Test
    @DisplayName("Test anyObjectIsNull method with null and non-null objects")
    public void testAnyObjectIsNull() {
        assertTrue(JsonUtils.anyObjectIsNull(null, "test", 123));
        assertFalse(JsonUtils.anyObjectIsNull("test", 123, new JSONObject()));
    }

    @Test
    @DisplayName("Test anyObjectIsNull method with only non-null objects")
    public void testAnyObjectIsNullWithNoNulls() {
        assertFalse(JsonUtils.anyObjectIsNull("test", 456, 789.0));
    }

    @Test
    @DisplayName("Test anyObjectIsNull method with null array")
    public void testAnyObjectIsNullWithNullArray() {
        assertThrows(IllegalStateException.class, () -> {
            JsonUtils.anyObjectIsNull((Object[]) null);
        });
    }

    @Test
    @DisplayName("Test loadFromFile method with a valid JSON file")
    public void testLoadFromFile() throws IOException, ParseException {
        Path tempFile = Files.createTempFile("test-json", ".json");
        String jsonString = "{\"key\":\"value\"}";
        Files.writeString(tempFile, jsonString, StandardOpenOption.WRITE);

        JSONObject jsonObject = JsonUtils.loadFromFile(tempFile);
        assertNotNull(jsonObject);
        assertEquals("value", jsonObject.get("key"));

        Files.deleteIfExists(tempFile); // Clean up the temp file
    }

    @Test
    @DisplayName("Test loadFromFile method with a non-JSON file")
    public void testLoadFromFileWithNonJsonContent() throws IOException {
        Path tempFile = Files.createTempFile("test-non-json", ".txt");
        String invalidContent = "Not a JSON string";
        Files.writeString(tempFile, invalidContent, StandardOpenOption.WRITE);

        assertThrows(IOException.class, () -> {
            JsonUtils.loadFromFile(tempFile);
        });

        Files.deleteIfExists(tempFile); // Clean up the temp file
    }

    @Test
    @DisplayName("Test saveToFile method")
    @SuppressWarnings("unchecked")
    public void testSaveToFile() throws IOException {
        Path tempFile = Files.createTempFile("test-save-json", ".json");
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("key", "value");

        JsonUtils.saveToFile(jsonObject, tempFile);

        String content = Files.readString(tempFile);
        assertNotNull(content);
        assertTrue(content.contains("\"key\":\"value\""));

        Files.deleteIfExists(tempFile); // Clean up the temp file
    }
}
