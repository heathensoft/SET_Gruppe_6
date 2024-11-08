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
}
