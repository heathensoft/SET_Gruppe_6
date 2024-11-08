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
        LocalUser user = new LocalUser();
        user.accountID = 1;
        user.userName = "JohnDoe";
        user.role = LocalUser.Role.RESIDENT;

        JSONObject jsonObject = user.toJson();

        assertEquals(1, jsonObject.get(LocalUser.JSON_KEY_USER_ACCOUNT));
        assertEquals(Base64.getEncoder().encodeToString("JohnDoe".getBytes()), jsonObject.get(LocalUser.JSON_KEY_USER_NAME));
        assertEquals(LocalUser.Role.RESIDENT.ordinal(), jsonObject.get(LocalUser.JSON_KEY_ROLE));
    }

    @Test
    @DisplayName("Test JSON deserialization (fromJson) for LocalUser")
    @SuppressWarnings("unchecked")
    public void testFromJson() throws Exception {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put(LocalUser.JSON_KEY_USER_ACCOUNT, 1);
        jsonObject.put(LocalUser.JSON_KEY_USER_NAME, Base64.getEncoder().encodeToString("JohnDoe".getBytes()));
        jsonObject.put(LocalUser.JSON_KEY_ROLE, LocalUser.Role.RESIDENT.ordinal());

        LocalUser user = new LocalUser();
        user.fromJson(jsonObject);

        assertEquals(1, user.accountID);
        assertEquals("JohnDoe", user.userName);
        assertEquals(LocalUser.Role.RESIDENT, user.role);
    }

    @Test
    @DisplayName("Test JSON serialization and deserialization for LocalUser object equality")
    public void testJsonConversion() throws Exception {
        // Opprett et originalt LocalUser-objekt
        LocalUser originalUser = new LocalUser();
        originalUser.accountID = 101;
        originalUser.userName = "JohnDoe";
        originalUser.role = LocalUser.Role.OWNER;

        // Converting to lock
        JSONObject jsonObject = originalUser.toJson();

        // Creating a new lock object from json
        LocalUser newUser = new LocalUser();
        newUser.fromJson(jsonObject);

        // Check that the fields in the new object correlates with the original one
        assertEquals(originalUser.accountID, newUser.accountID);
        assertEquals(originalUser.userName, newUser.userName);
        assertEquals(originalUser.role, newUser.role);
    }

    @Test
    @DisplayName("Test ensureFieldsNotNull sets default values for null fields")
    public void testEnsureFieldsNotNull() {
        LocalUser user = new LocalUser();
        user.userName = null;
        user.role = null;

        user.ensureFieldsNotNull();

        assertEquals(LocalUser.Role.NONE, user.role);
        assertEquals(G6Datatype.NULL_STRING, user.userName);
    }

    @Test
    @DisplayName("Test hasRequiredRole method returns correct results based on roles")
    public void testHasRequiredRole() {
        LocalUser user = new LocalUser();
        user.role = LocalUser.Role.RESIDENT;

        assertTrue(user.hasRequiredRole(LocalUser.Role.GUEST));
        assertTrue(user.hasRequiredRole(LocalUser.Role.RESIDENT));
        assertFalse(user.hasRequiredRole(LocalUser.Role.OWNER));
    }

    @Test
    @DisplayName("Test compareTo method for LocalUser object comparison by role and username")
    public void testCompareTo() {
        LocalUser user1 = new LocalUser();
        user1.role = LocalUser.Role.OWNER;
        user1.userName = "Alice";

        LocalUser user2 = new LocalUser();
        user2.role = LocalUser.Role.RESIDENT;
        user2.userName = "Bob";

        LocalUser user3 = new LocalUser();
        user3.role = LocalUser.Role.OWNER;
        user3.userName = "Charlie";

        assertTrue(user1.compareTo(user2) < 0); // OWNER has higher priority than RESIDENT
        assertTrue(user1.compareTo(user3) < 0); // Same role, Alice comes before Charlie
        assertTrue(user2.compareTo(user1) > 0); // RESIDENT is lower priority than OWNER
    }

}
