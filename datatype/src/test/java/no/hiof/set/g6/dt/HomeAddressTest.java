package no.hiof.set.g6.dt;

import org.json.simple.JSONObject;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;


public class HomeAddressTest {

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

        // Konverterer til JSON
        JSONObject jsonObject = originalAddress.toJson();

        // Oppretter et nytt HomeAddress-objekt
        HomeAddress newAddress = new HomeAddress();
        newAddress.fromJson(jsonObject);

        // Sjekker at de to objektene er like
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
}
