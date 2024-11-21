package no.hiof.set.g6.dt;

import org.json.simple.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class to validate the functionality of JSON serialization and deserialization
 * for the DatatypeArray class.
 */
public class DatatypeArrayTest {
    private DatatypeArray<LocalUser> userArray;
    private LocalUser user1;
    private LocalUser user2;

    /**
     * Setup method that runs before each test to initialize test objects.
     */
    @BeforeEach
    public void setUp() {
        // Initialize a DatatypeArray instance for the LocalUser class
        userArray = new DatatypeArray<>(LocalUser.class);

        // Create and add two LocalUser objects
        user1 = new LocalUser();
        user1.accountID = 101;
        user1.userName = "Alice";
        user1.role = LocalUser.Role.OWNER;

        user2 = new LocalUser();
        user2.accountID = 102;
        user2.userName = "Bob";
        user2.role = LocalUser.Role.GUEST;

        // Add users to the array
        userArray.add(user1);
        userArray.add(user2);
    }

    /**
     * Tests that the toJson() method correctly converts the DatatypeArray to a JSON object.
     */
    @Test
    public void testToJson() {
        // Convert the DatatypeArray to JSON
        JSONObject jsonObject = userArray.toJson();

        // Validate that the JSON object is not null
        assertNotNull(jsonObject);

        // Check that the required keys exist in the JSON object
        assertTrue(jsonObject.containsKey(DatatypeArray.JSON_KEY_ARRAY));
        assertTrue(jsonObject.containsKey(DatatypeArray.JSON_KEY_ARRAY_TYPE));

        // Validate that the type string is correct
        String typeString = (String) jsonObject.get(DatatypeArray.JSON_KEY_ARRAY_TYPE);
        assertEquals("Local User", typeString);

        // Check that the array content is present
        assertNotNull(jsonObject.get(DatatypeArray.JSON_KEY_ARRAY));
    }

    /**
     * Tests that the fromJson() method correctly reconstructs a DatatypeArray from JSON.
     * @throws Exception if there are any errors during JSON deserialization.
     */
    @Test
    public void testFromJson() throws Exception {
        // Convert the original array to JSON
        JSONObject jsonObject = userArray.toJson();

        // Initialize a new DatatypeArray and populate it with data from JSON
        DatatypeArray<LocalUser> newArray = new DatatypeArray<>(LocalUser.class);
        newArray.fromJson(jsonObject);

        // Validate that the size of the array is correct
        assertEquals(2, newArray.size());

        // Check that the data is correct for the first user
        assertEquals(user1.accountID, newArray.get(0).accountID);
        assertEquals(user1.userName, newArray.get(0).userName);
        assertEquals(user1.role, newArray.get(0).role);

        // Check that the data is correct for the second user
        assertEquals(user2.accountID, newArray.get(1).accountID);
        assertEquals(user2.userName, newArray.get(1).userName);
        assertEquals(user2.role, newArray.get(1).role);
    }
}
