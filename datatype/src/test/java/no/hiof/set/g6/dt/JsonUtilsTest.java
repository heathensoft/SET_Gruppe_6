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
        //Test if parse() correctly handles a valid JSON string
        String jsonString = "{\"key\":\"value\"}"; //Valid JSON format
        JSONObject jsonObject = JsonUtils.parse(jsonString);

        assertNotNull(jsonObject); // Ensure the parsed object is not null
        assertEquals("value", jsonObject.get("key")); // Check that the value matches
    }

    @Test
    @DisplayName("Test parse method with invalid JSON string")
    public void testParseInvalidJson() {
        // Test if parse() throws a ParseException for an invalid JSON string.
        String invalidJsonString = "{key:value}";

        // Expect ParseException when parsing invalid JSON
        assertThrows(ParseException.class, () -> {
            JsonUtils.parse(invalidJsonString);
        });
    }

    @Test
    @DisplayName("Test parse method with null input")
    public void testParseWithNullInput() {
        // Test if parse() handles null input properly by throwing a ParseException.
        assertThrows(ParseException.class, () -> {
            JsonUtils.parse(null); // Null input should trigger an exception
        });
    }

    @Test
    @DisplayName("Test anyObjectIsNull method with null and non-null objects")
    public void testAnyObjectIsNull() {
        // Test anyObjectIsNull() with a mix of null and non-null values.
        assertTrue(JsonUtils.anyObjectIsNull(null, "test", 123)); // Contains a null, should return true
        assertFalse(JsonUtils.anyObjectIsNull("test", 123, new JSONObject())); // No nulls, should return false
    }

    @Test
    @DisplayName("Test anyObjectIsNull method with only non-null objects")
    // Test anyObjectIsNull() with only non-null values to ensure it returns false.
    public void testAnyObjectIsNullWithNoNulls() {
        assertFalse(JsonUtils.anyObjectIsNull("test", 456, 789.0)); // All non-null values, should return false
    }

    @Test
    @DisplayName("Test anyObjectIsNull method with null array")
    public void testAnyObjectIsNullWithNullArray() {
        // Test that anyObjectIsNull() throws an exception when passed a null array.
        assertThrows(IllegalStateException.class, () -> {
            JsonUtils.anyObjectIsNull((Object[]) null); // Null array should trigger an exception
        });
    }

    @Test
    @DisplayName("Test loadFromFile method with a valid JSON file")
    public void testLoadFromFile() throws IOException, ParseException {
        // Create a temporary file with valid JSON content for testing loadFromFile().
        Path tempFile = Files.createTempFile("test-json", ".json");
        String jsonString = "{\"key\":\"value\"}"; // Valid JSON content
        Files.writeString(tempFile, jsonString, StandardOpenOption.WRITE);

        // Load JSON from the file and check its validity.
        JSONObject jsonObject = JsonUtils.loadFromFile(tempFile);
        assertNotNull(jsonObject); // Ensure the loaded object is not null
        assertEquals("value", jsonObject.get("key")); // Check that the value matches

        Files.deleteIfExists(tempFile); // Clean up the temp file
    }

    @Test
    @DisplayName("Test loadFromFile method with a non-JSON file")
    // Create a temporary file with non-JSON content for testing loadFromFile().
    public void testLoadFromFileWithNonJsonContent() throws IOException {
        Path tempFile = Files.createTempFile("test-non-json", ".txt");
        String invalidContent = "Not a JSON string"; // Invalid content
        Files.writeString(tempFile, invalidContent, StandardOpenOption.WRITE);

        // Expect IOException when trying to load non-JSON content.
        assertThrows(IOException.class, () -> {
            JsonUtils.loadFromFile(tempFile);
        });

        Files.deleteIfExists(tempFile); // Clean up the temp file
    }

    @Test
    @DisplayName("Test saveToFile method")
    @SuppressWarnings("unchecked")
    public void testSaveToFile() throws IOException {
        // Create a temporary file for testing saveToFile().
        Path tempFile = Files.createTempFile("test-save-json", ".json");
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("key", "value"); // Sample data to save

        // Save the JSON object to the file.
        JsonUtils.saveToFile(jsonObject, tempFile);

        // Read the file content and verify it contains the correct JSON data.
        String content = Files.readString(tempFile);
        assertNotNull(content); // Ensure the content is not null
        assertTrue(content.contains("\"key\":\"value\"")); // Check if the saved content matches

        Files.deleteIfExists(tempFile); // Clean up the temp file
    }
}
