package no.hiof.set.g6.ny;


import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;


/**
 * @author Frederik Dahl
 * 16/10/2024
 */


public class JsonUtils {

    // Todo: load / Save Json to file
    
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


    public static JSONObject loadFromFile(Path path) throws IOException {
        JSONObject object;
        String string = Files.readString(path);
        try { object = parse(string);
        } catch (ParseException e) {
            throw new IOException(e);
        } return object;
    }

    /**
     * Save JSONObject to file. (Overwrite existing)
     * @param object object to save
     * @param path path of file
     * @throws IOException ParseException or IO error
     */
    public static void saveToFile(JSONObject object, Path path) throws IOException {
        if (!Files.exists(path)) {
            Files.createFile(path);
        } Files.writeString(path,object.toString());
    }

    public static void main(String[] args) throws Exception {

        Path folder = Path.of("database/workbenchFiles");
        Path file = folder.resolve("UserAccounts.json");
        UserAccount userAccount = new UserAccount();
        saveToFile(userAccount.toJson(),file);



    }


}
