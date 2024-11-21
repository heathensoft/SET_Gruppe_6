package no.hiof.set.g6.dt;


import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;




public class G6JASONTest {
    
    /*
    //---------------------------------------------------------------------------
    // Method: parse()
    
    @Test
    @DisplayName("Assert passing null arg. to parser throws ParseException")
    public void controlParserNullString() {
        Assertions.assertThrows(ParseException.class,() -> G6JSON.parse(null));
    }
    
    @Test
    @DisplayName("Assert passing nonsense to parser throws ParseException")
    public void controlParserGibberish() {
        final String gibberish = "qwertyuiopåasdfghjkløæzxcvbnm";
        Assertions.assertThrows(ParseException.class,() -> G6JSON.parse(gibberish));
    }
    
    @Test
    @DisplayName("Assert passing valid json Formatted string to parser doesn't throw E")
    public void controlParserJsonFormat() {
        final String jsonString = "{\"name\":\"John\", \"age\":30, \"car\":null}";
        Assertions.assertDoesNotThrow(() -> G6JSON.parse(jsonString));
    }
    
    //---------------------------------------------------------------------------
    // Method: CastAndAdd()
    
    @Test
    @DisplayName("Assert castAndAdd throws E if any argument is null")
    public void makeCastAndAddThrowIllegalStateE() {
        Class<Object> clazz = Object.class;
        Assertions.assertThrows(IllegalStateException.class,
                () -> G6JSON.castAndAdd(null,new ArrayList<>(),clazz));
        
    }
    
    @Test
    @SuppressWarnings("unchecked")
    @DisplayName("Assert castAndAdd returns list of correct size")
    public void assertCastAndAddReturnsListWithCorrectCount() {
        JSONArray jsonArray = new JSONArray();
        Class<Object> clazz = Object.class;
        final int count = 3;
        for (int i = 0; i < count; i++) jsonArray.add(new Object());
        List<Object> list = G6JSON.castAndAdd(jsonArray,new ArrayList<>(),clazz);
        Assertions.assertEquals(count,list.size());
    }
    
    //---------------------------------------------------------------------------
    // Conversions
  
    @Test
    @SuppressWarnings("unchecked")
    @DisplayName("Assert every method that converts a single JSONObject to DataType throws E as expected")
    public void controlJsonToDataTypeThrowsE() {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("Key",new Object());
        
        // All methods should throw Exception if the JsonObject argument cannot be converted to the Datatype
        Assertions.assertThrows(Exception.class,() -> G6JSON.userAccountFromJSON(jsonObject));
        Assertions.assertThrows(Exception.class,() -> G6JSON.homeAddressFromJSON(jsonObject));
        // ----
        
        // All methods should throw Exception if the JsonObject argument is null
        Assertions.assertThrows(Exception.class,() -> G6JSON.userAccountFromJSON(null));
        Assertions.assertThrows(Exception.class,() -> G6JSON.homeAddressFromJSON(null));
        // ----
    }
    
    
    @Test
    @DisplayName("Assert every method that converts a single Datatype to JSONObject returns expected")
    public void controlDatatypeToJsonReturnsAppropriate() {
        
        // All methods provided a Datatype should return a JsonObject
        Assertions.assertNotNull(G6JSON.homeAddressToJSON(new HomeAddress()),"HomeAddress");
        Assertions.assertNotNull(G6JSON.userAccountToJSON(new UserAccount()),"UserAccount");
        // ----
        
        // All methods provided a null argument should return null
        Assertions.assertNull(G6JSON.homeAddressToJSON(null),"HomeAddress");
        Assertions.assertNull(G6JSON.userAccountToJSON(null),"UserAccount");
        // ----
    }


     */
    
}
