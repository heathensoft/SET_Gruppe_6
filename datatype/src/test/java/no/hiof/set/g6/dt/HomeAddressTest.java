package no.hiof.set.g6.dt;

import org.json.simple.JSONObject;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;


public class HomeAddressTest {

    @Test
    @DisplayName("Test JSON serialization")
    public void testToJson() {
        HomeAddress address = new HomeAddress();
        address.country = "Norway";
        address.state = "Vestfold";
        address.city = "Tønsberg";
        address.street = "Nedre Langgate";
        address.postalCode = 3100;

        JSONObject json = address.toJson();

        Assertions.assertEquals("Norway", json.get(HomeAddress.JSON_KEY_COUNTRY));
        Assertions.assertEquals("Vestfold", json.get(HomeAddress.JSON_KEY_STATE));
        Assertions.assertEquals("Tønsberg", json.get(HomeAddress.JSON_KEY_CITY));
        Assertions.assertEquals("Nedre Langgate", json.get(HomeAddress.JSON_KEY_STREET));
        Assertions.assertEquals(3100, json.get(HomeAddress.JSON_KEY_POSTAL_CODE));
    }

    @Test
    @DisplayName("Test JSON deserialization")
    @SuppressWarnings("unchecked")
    public void testFromJson() throws Exception {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put(HomeAddress.JSON_KEY_COUNTRY, "Norway");
        jsonObject.put(HomeAddress.JSON_KEY_STATE, "Hordaland");
        jsonObject.put(HomeAddress.JSON_KEY_CITY, "Bergen");
        jsonObject.put(HomeAddress.JSON_KEY_STREET, "Bryggen");
        jsonObject.put(HomeAddress.JSON_KEY_POSTAL_CODE, 5003);

        HomeAddress address = new HomeAddress();
        address.fromJson(jsonObject);

        Assertions.assertEquals("Norway", address.country);
        Assertions.assertEquals("Hordaland", address.state);
        Assertions.assertEquals("Bergen", address.city);
        Assertions.assertEquals("Bryggen", address.street);
        Assertions.assertEquals(5003, address.postalCode);
    }

    @Test
    @DisplayName("Test JSON serialization and deserialization for HomeAddress object equality")
    public void testJsonConversion() throws Exception {
        // Oppretter et HomeAddress-objekt
        HomeAddress originalAddress = new HomeAddress();
        originalAddress.country = "Norway";
        originalAddress.state = "Viken";
        originalAddress.city = "Oslo";
        originalAddress.street = "Karl Johans gate";
        originalAddress.postalCode = 0162;

        // Converting to JSON
        JSONObject jsonObject = originalAddress.toJson();

        // Creating new HomeAdress object
        HomeAddress newAddress = new HomeAddress();
        newAddress.fromJson(jsonObject);

        // Checking that the two objects are alike
        assertEquals(originalAddress.country, newAddress.country);
        assertEquals(originalAddress.state, newAddress.state);
        assertEquals(originalAddress.city, newAddress.city);
        assertEquals(originalAddress.street, newAddress.street);
        assertEquals(originalAddress.postalCode, newAddress.postalCode);
    }
    @Test
    @DisplayName("Test ensureFieldsNotNull sets null fields to default values")
    public void testEnsureFieldsNotNull() {
        HomeAddress address = new HomeAddress();
        address.country = null;
        address.state = null;
        address.city = null;
        address.street = null;

        // Ensure fields are not null
        address.ensureFieldsNotNull();

        // Assert that null fields are replaced by the default value "null"
        Assertions.assertEquals("null", address.country);
        Assertions.assertEquals("null", address.state);
        Assertions.assertEquals("null", address.city);
        Assertions.assertEquals("null", address.street);
    }

    @Test
    @DisplayName("Test clearing fields in HomeAddress")
    public void testClearFields() {
        HomeAddress address = new HomeAddress();
        address.country = "Norway";
        address.state = "Østfold";
        address.city = "Fredrikstad";
        address.street = "Example Street";
        address.postalCode = 1600;

        address.clearFields();

        Assertions.assertEquals("null", address.country);
        Assertions.assertEquals("null", address.state);
        Assertions.assertEquals("null", address.city);
        Assertions.assertEquals("null", address.street);
        Assertions.assertEquals(0, address.postalCode);
    }

    @Test
    @DisplayName("Test equality between HomeAddress objects")
    public void testEquals() {
        HomeAddress address1 = new HomeAddress();
        address1.country = "Norway";
        address1.state = "Trøndelag";
        address1.city = "Trondheim";
        address1.street = "Olav Tryggvasons gate";
        address1.postalCode = 7011;

        HomeAddress address2 = new HomeAddress();
        address2.country = "Norway";
        address2.state = "Trøndelag";
        address2.city = "Trondheim";
        address2.street = "Olav Tryggvasons gate";
        address2.postalCode = 7011;

        Assertions.assertTrue(address1.equals(address2));

        address2.postalCode = 7000;
        Assertions.assertFalse(address1.equals(address2));
    }


    @Test
    @DisplayName("Test set method updates fields correctly")
    public void testSetMethod() {
        HomeAddress address1 = new HomeAddress();
        address1.country = "Norway";
        address1.state = "Oslo";
        address1.city = "Oslo";
        address1.street = "Main Street 1";
        address1.postalCode = 1234;

        HomeAddress address2 = new HomeAddress();
        address2.set(address1);

        // Verify that the fields in address2 have been updated to match address1
        assertEquals("Norway", address2.country);
        assertEquals("Oslo", address2.state);
        assertEquals("Oslo", address2.city);
        assertEquals("Main Street 1", address2.street);
        assertEquals(1234, address2.postalCode);

        // Verify that calling set with null clears the fields
        address2.set(null);
        assertEquals(HomeAddress.NULL_STRING, address2.country);
        assertEquals(HomeAddress.NULL_STRING, address2.state);
        assertEquals(HomeAddress.NULL_STRING, address2.city);
        assertEquals(HomeAddress.NULL_STRING, address2.street);
        assertEquals(HomeAddress.NULL, address2.postalCode);
    }
}
