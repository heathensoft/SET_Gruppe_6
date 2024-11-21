package no.hiof.set.g6.dt;

import org.json.simple.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;



public class UserAccountTest {

    private UserAccount userAccount;
    private HomeAddress address;

    @BeforeEach
    public void setUp() {
        userAccount = new UserAccount();
        address = new HomeAddress();
        address.country = "Norway";
        address.state = "Viken";
        address.city = "Oslo";
        address.street = "Testveien 1";
        address.postalCode = 1234;
    }

    @Test
    public void testClearFields() {
        userAccount.firstName = "John";
        userAccount.lastName = "Doe";
        userAccount.clearFields();
        assertEquals(UserAccount.NULL_STRING, userAccount.firstName);
        assertEquals(UserAccount.NULL_STRING, userAccount.lastName);
    }

    @Test
    public void testEnsureFieldsNotNull() {
        userAccount.firstName = null;
        userAccount.lastName = null;
        userAccount.ensureFieldsNotNull();
        assertEquals(UserAccount.NULL_STRING, userAccount.firstName);
        assertEquals(UserAccount.NULL_STRING, userAccount.lastName);
    }

    @Test
    public void testSet() {
        UserAccount otherUser = new UserAccount();
        otherUser.firstName = "Jane";
        otherUser.lastName = "Smith";
        otherUser.email = "jane.smith@example.com";
        otherUser.phoneNumber = "12345678";
        otherUser.address = address;

        userAccount.set(otherUser);

        assertEquals("Jane", userAccount.firstName);
        assertEquals("Smith", userAccount.lastName);
        assertEquals("jane.smith@example.com", userAccount.email);
        assertEquals("12345678", userAccount.phoneNumber);
        assertEquals(address, userAccount.address);
    }

    @Test
    @DisplayName("Assert UserAccount converted to Json, converts back correctly")
    public void testJsonConversion() throws Exception {
        // Create account and set the fields
        UserAccount account1 = new UserAccount();
        account1.id = 1;
        account1.firstName = "John";
        account1.lastName = "Doe";
        account1.email = "john.doe@example.com";
        account1.phoneNumber = "12345678";
        account1.address = new HomeAddress();
        account1.address.country = "Norway";
        account1.address.state = "Viken";
        account1.address.city = "Oslo";
        account1.address.street = "Testveien 1";
        account1.address.postalCode = 1234;
        // convert account to json
        JSONObject jsonObject = account1.toJson();

        // covert back from json (new account object so we can compare)
        UserAccount account2 = new UserAccount();
        account2.fromJson(jsonObject);

        // test to make sure the two accounts are equal
        assertEquals(account1.id, account2.id);
        assertEquals(account1.firstName, account2.firstName);
        assertEquals(account1.lastName, account2.lastName);
        assertEquals(account1.email, account2.email);
        assertEquals(account1.phoneNumber, account2.phoneNumber);
        assertEquals(account1.address.country, account2.address.country);
        assertEquals(account1.address.state, account2.address.state);
        assertEquals(account1.address.city, account2.address.city);
        assertEquals(account1.address.street, account2.address.street);
        assertEquals(account1.address.postalCode, account2.address.postalCode);
    }

    @Test
    @SuppressWarnings("unchecked")
    public void testFromJson() throws Exception {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put(UserAccount.JSON_KEY_ID, 1);
        jsonObject.put(UserAccount.JSON_KEY_FIRST_NAME, "John");
        jsonObject.put(UserAccount.JSON_KEY_LAST_NAME, "Doe");
        jsonObject.put(UserAccount.JSON_KEY_EMAIL, "john.doe@example.com");
        jsonObject.put(UserAccount.JSON_KEY_PHONE_NUMBER, "12345678");

        JSONObject addressJson = new JSONObject();
        addressJson.put(HomeAddress.JSON_KEY_COUNTRY, "Norway");
        addressJson.put(HomeAddress.JSON_KEY_STATE, "Viken");
        addressJson.put(HomeAddress.JSON_KEY_CITY, "Oslo");
        addressJson.put(HomeAddress.JSON_KEY_STREET, "Testveien 1");
        addressJson.put(HomeAddress.JSON_KEY_POSTAL_CODE, 1234);

        jsonObject.put(UserAccount.JSON_KEY_ADDRESS, addressJson);

        userAccount.fromJson(jsonObject);

        assertEquals(1, userAccount.id);
        assertEquals("John", userAccount.firstName);
        assertEquals("Doe", userAccount.lastName);
        assertEquals("john.doe@example.com", userAccount.email);
        assertEquals("12345678", userAccount.phoneNumber);
        assertEquals("Norway", userAccount.address.country);
        assertEquals("Viken", userAccount.address.state);
        assertEquals("Oslo", userAccount.address.city);
        assertEquals("Testveien 1", userAccount.address.street);
        assertEquals(1234, userAccount.address.postalCode);
    }

    @Test
    public void testToJson() {
        userAccount.id = 1;
        userAccount.firstName = "John";
        userAccount.lastName = "Doe";
        userAccount.email = "john.doe@example.com";
        userAccount.phoneNumber = "12345678";
        userAccount.address = address;

        JSONObject jsonObject = userAccount.toJson();

        assertEquals(1, jsonObject.get(UserAccount.JSON_KEY_ID));
        assertEquals("John", jsonObject.get(UserAccount.JSON_KEY_FIRST_NAME));
        assertEquals("Doe", jsonObject.get(UserAccount.JSON_KEY_LAST_NAME));
        assertEquals("john.doe@example.com", jsonObject.get(UserAccount.JSON_KEY_EMAIL));
        assertEquals("12345678", jsonObject.get(UserAccount.JSON_KEY_PHONE_NUMBER));

        JSONObject addressJson = (JSONObject) jsonObject.get(UserAccount.JSON_KEY_ADDRESS);
        assertEquals("Norway", addressJson.get(HomeAddress.JSON_KEY_COUNTRY));
        assertEquals("Viken", addressJson.get(HomeAddress.JSON_KEY_STATE));
        assertEquals("Oslo", addressJson.get(HomeAddress.JSON_KEY_CITY));
        assertEquals("Testveien 1", addressJson.get(HomeAddress.JSON_KEY_STREET));
        assertEquals(1234, addressJson.get(HomeAddress.JSON_KEY_POSTAL_CODE));
    }

    @Test
    public void testCompareTo() {
        UserAccount user1 = new UserAccount();
        user1.firstName = "John";
        user1.lastName = "Doe";

        UserAccount user2 = new UserAccount();
        user2.firstName = "John";
        user2.lastName = "Doe";

        assertEquals(0, user1.compareTo(user2));

        user2.lastName = "Smith";
        assertTrue(user1.compareTo(user2) < 0);
    }
}
