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
}
