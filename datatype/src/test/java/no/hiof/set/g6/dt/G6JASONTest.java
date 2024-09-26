package no.hiof.set.g6.dt;


import org.json.simple.JSONArray;
import org.json.simple.parser.ParseException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;


/**
 * Pain pepeHands
 */


public class G6JASONTest {
    
    //---------------------------------------------------------------------------
    @Test
    @DisplayName("Assert Passing null arg. to parser throws ParseException")
    public void controlParserNullString() {
        Assertions.assertThrows(ParseException.class,() -> G6JSON.parse(null));
    }
    
    @Test
    @DisplayName("Assert Passing gibberish to parser throws ParseException")
    public void controlParserGibberish() {
        final String gibberish = "qwertyuiopåasdfghjkløæzxcvbnm";
        Assertions.assertThrows(ParseException.class,() -> G6JSON.parse(gibberish));
    }
    
    @Test
    @DisplayName("Assert Passing Valid Format to parser does not throw an Exception")
    public void controlParserJsonFormat() {
        final String jsonString = "{\"name\":\"John\", \"age\":30, \"car\":null}";
        Assertions.assertDoesNotThrow(() -> G6JSON.parse(jsonString));
    }
    //---------------------------------------------------------------------------
    
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
        jsonArray.add(new Object());
        jsonArray.add(new Object());
        jsonArray.add(new Object());
        List<Object> list = G6JSON.castAndAdd(jsonArray,new ArrayList<>(),clazz);
        Assertions.assertEquals(3,list.size());
    }
    //---------------------------------------------------------------------------
    
    
    
}
