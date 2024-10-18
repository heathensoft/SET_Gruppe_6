package no.hiof.set.g6.ny;


import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;


/**
 * @author Frederik Dahl
 * 16/10/2024
 */


public class JsonUtils {
    
    
    /**
     * @param string json-formatted string
     * @return a JSONObject
     * @throws ParseException if the string is not recognised as Json-format
     */
    public static JSONObject parse(String string) throws ParseException {
        string = string == null ? "" : string;
        JSONParser parser = new JSONParser();
        return (JSONObject) parser.parse(string);
    }
    
    /**
     * Check for null object
     * @param objects 0 or more objects
     * @return true if any object is null
     */
    public static boolean anyObjectIsNull(Object ... objects) {
        if (objects == null) throw new IllegalStateException("Provided Illegal null argument for private method");
        for (Object object : objects) {
            if (object == null) return true;
        } return false;
    }
}
