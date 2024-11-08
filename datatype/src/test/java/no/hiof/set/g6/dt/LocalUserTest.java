package no.hiof.set.g6.dt;

import org.json.simple.JSONObject;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import java.util.Base64;

import static org.junit.jupiter.api.Assertions.*;
public class LocalUserTest {
    @Test
    @DisplayName("Test JSON serialization (toJson) for LocalUser")
    public void testToJson() {
        // Creates a LocalUser object and set its fields
        LocalUser user = new LocalUser();
        user.accountID = 1;
        user.userName = "JohnDoe";
        user.role = LocalUser.Role.RESIDENT;

        // Convert object to a JSON representation
        JSONObject jsonObject = user.toJson();

        //Verify that the JSON object contains the expected values
        assertEquals(1, jsonObject.get(LocalUser.JSON_KEY_USER_ACCOUNT));
        assertEquals(Base64.getEncoder().encodeToString("JohnDoe".getBytes()), jsonObject.get(LocalUser.JSON_KEY_USER_NAME));
        assertEquals(LocalUser.Role.RESIDENT.ordinal(), jsonObject.get(LocalUser.JSON_KEY_ROLE));
    }

    @Test
    @DisplayName("Test JSON deserialization (fromJson) for LocalUser")
    @SuppressWarnings("unchecked")
    public void testFromJson() throws Exception {
        //Create a JSON object and set its data
        JSONObject jsonObject = new JSONObject();
        jsonObject.put(LocalUser.JSON_KEY_USER_ACCOUNT, 1);
        jsonObject.put(LocalUser.JSON_KEY_USER_NAME, Base64.getEncoder().encodeToString("JohnDoe".getBytes()));
        jsonObject.put(LocalUser.JSON_KEY_ROLE, LocalUser.Role.RESIDENT.ordinal());

        // Create a new LocalUser object and parse the JSON data into it
        LocalUser user = new LocalUser();
        user.fromJson(jsonObject);

        //Verify that the fields in the LocalUser object matches the values in the JSON
        assertEquals(1, user.accountID);
        assertEquals("JohnDoe", user.userName);
        assertEquals(LocalUser.Role.RESIDENT, user.role);
    }

    @Test
    @DisplayName("Test JSON serialization and deserialization for LocalUser object equality")
    public void testJsonConversion() throws Exception {
        // Create an original LocalUser object and set its fields
        LocalUser originalUser = new LocalUser();
        originalUser.accountID = 101;
        originalUser.userName = "JohnDoe";
        originalUser.role = LocalUser.Role.OWNER;

        // Convert the object to JSON and then back to a new LocalUser object
        JSONObject jsonObject = originalUser.toJson();
        LocalUser newUser = new LocalUser();
        newUser.fromJson(jsonObject);

        // Verify that the new object has the same field values as the original
        assertEquals(originalUser.accountID, newUser.accountID);
        assertEquals(originalUser.userName, newUser.userName);
        assertEquals(originalUser.role, newUser.role);
    }

    @Test
    @DisplayName("Test ensureFieldsNotNull sets default values for null fields")
    public void testEnsureFieldsNotNull() {
        // Create a LocalUser object with null fields
        LocalUser user = new LocalUser();
        user.userName = null;
        user.role = null;

        // Call ensureFieldsNotNull to set default values for null fields
        user.ensureFieldsNotNull();

        // Verify that the null fields have been set to their default values
        assertEquals(LocalUser.Role.NONE, user.role);
        assertEquals(G6Datatype.NULL_STRING, user.userName);
    }

    @Test
    @DisplayName("Test hasRequiredRole method returns correct results based on roles")
    public void testHasRequiredRole() {
        // Create a LocalUser object and set its role
        LocalUser user = new LocalUser();
        user.role = LocalUser.Role.RESIDENT;

        // Verify that hasRequiredRole returns true for roles lower or equal to RESIDENT
        assertTrue(user.hasRequiredRole(LocalUser.Role.GUEST)); // Resident is HIGHER than GUEST
        assertTrue(user.hasRequiredRole(LocalUser.Role.RESIDENT)); // Same role
        assertFalse(user.hasRequiredRole(LocalUser.Role.OWNER)); // RESIDENT is lower than OWNER
    }

    @Test
    @DisplayName("Test compareTo method for LocalUser object comparison by role and username")
    public void testCompareTo() {
        // Create LocalUser objects with different roles and usernames
        LocalUser user1 = new LocalUser();
        user1.role = LocalUser.Role.OWNER;
        user1.userName = "Alice";

        LocalUser user2 = new LocalUser();
        user2.role = LocalUser.Role.RESIDENT;
        user2.userName = "Bob";

        LocalUser user3 = new LocalUser();
        user3.role = LocalUser.Role.OWNER;
        user3.userName = "Charlie";

        // Verify the results of the compareTo method based on role and username
        assertTrue(user1.compareTo(user2) < 0); // OWNER has higher priority than RESIDENT
        assertTrue(user1.compareTo(user3) < 0); // Same role, Alice comes before Charlie
        assertTrue(user2.compareTo(user1) > 0); // RESIDENT is lower priority than OWNER
    }

}
