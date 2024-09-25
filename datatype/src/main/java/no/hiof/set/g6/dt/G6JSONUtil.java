package no.hiof.set.g6.dt;


import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Metodene i denne klassen er fine å kjøre tester på
 */


public class G6JSONUtil {
    
    public static final int VERSION_VALUE = 1;
    public static final String VERSION_STRING = "G6Json-version";
    
    
    public static final String JSON_ARRAY_ADDRESS = "Address List";
    public static final String JSON_ARRAY_USERACC = "User Account List";
    
    public static final String JSON_ADDRESS_COUNTRY_STRING = "Country";
    public static final String JSON_ADDRESS_STATE_STRING = "State";
    public static final String JSON_ADDRESS_CITY_STRING = "City";
    public static final String JSON_ADDRESS_STREET_STRING = "Street Address";
    public static final String JSON_ADDRESS_POSTALCODE_STRING = "Postal Code";
    
    public static final String JSON_USERACC_ID_STRING = "User-ID";
    public static final String JSON_USERACC_FIRSTNAME_STRING = "First Name";
    public static final String JSON_USERACC_LASTNAME_STRING = "Last Name";
    public static final String JSON_USERACC_EMAIL_STRING = "E-Mail";
    public static final String JSON_USERACC_ADDRESS_STRING = "Address";
    public static final String JSON_USERACC_PHONENUMBERS_STRING = "Phone-Numbers";
    
    public enum SupportedObject {
        HOME_ADDRESS("Home Address Array", HomeAddress.class),
        USER_ACCOUNT("User Account Array", UserAccount.class);
        public final String header;
        public final Class<?> clazz;
        final static SupportedObject[] all = values();
        SupportedObject(String header, Class<?> clazz) {
            this.header = header;
            this.clazz = clazz;
        }
    }
    
    /**
     * Convert G6DataType to JSON format.
     * @param object object to convert
     * @return JSONObject or null if the object is null or cannot be converted.
     */
    public static JSONObject toJSON(Object object) {
        if (object instanceof G6DataType dataType) return datatypeToJSON(dataType);
        // It's possible to add classes other than G6DataType later
        // ...
        // ...
        return null;
    }
    
    /**
     * @param collection a collection of objects to convert to JSON-Format
     * @return a JSONArray of converted objects of same Class.
     * Null elements are ignored.
     * The JSONArray will be empty if collection is null or empty.
     * The JSONArray will be empty if collection contains objects of more than one Class
     * The JSONArray will be empty if the collection contain objects that cannot be converted
     */
    @SuppressWarnings("unchecked")
    public static JSONArray toJSONArray(Collection<Object> collection) {
        List<Object> list = listOfNonNull(collection); // Empty list if collection == null
        JSONArray jsonArray = new JSONArray();
        if (allObjectsShareClass(list)) {
          for (Object obj : list) {
              JSONObject jsonObject = toJSON(obj);
              if (jsonObject == null) break; // if this is null, all objects cannot be converted
              jsonArray.add(jsonObject);
          }
        } return jsonArray;
    }
    
    private static JSONObject datatypeToJSON(G6DataType object) {
        if (object instanceof HomeAddress address) return homeAddressToJSON(address);
        if (object instanceof UserAccount userAccount) return userAccountToJSON(userAccount);
        // Legg til flere etterhvert som vi lager nye datatyper:
        // ...
        // ...
        return null;
    }
    
    
    
    /**
     * @return A JSONObject for HomeAddress or null if object is null.
     */
    @SuppressWarnings("unchecked")
    public static JSONObject homeAddressToJSON(HomeAddress object) {
        if (object != null) {
            String country = object.country == null ? "null" : object.country;
            String state = object.state == null ? "null" : object.state;
            String city = object.city == null ? "null" : object.city;
            String street = object.streetAddress == null ? "null" : object.streetAddress;
            int postalCode = object.postalCode;
            
            JSONObject jsonObject = new JSONObject();
            {
                jsonObject.put(JSON_ADDRESS_COUNTRY_STRING,country);
                jsonObject.put(JSON_ADDRESS_STATE_STRING,state);
                jsonObject.put(JSON_ADDRESS_CITY_STRING,city);
                jsonObject.put(JSON_ADDRESS_STREET_STRING,street);
                jsonObject.put(JSON_ADDRESS_POSTALCODE_STRING,postalCode);
            }
            return jsonObject;
        } return null;
    }
    
    /**
     * @throws Exception if the jsonObject is null or does not convert to HomeAddress
     */
    public static HomeAddress homeAddressFromJSON(JSONObject jsonObject) throws Exception {
        if (jsonObject == null) throw new Exception("JSONObject is null");
        Object countryObject = jsonObject.get(JSON_ADDRESS_COUNTRY_STRING);
        Object stateObject = jsonObject.get(JSON_ADDRESS_STATE_STRING);
        Object cityObject = jsonObject.get(JSON_ADDRESS_CITY_STRING);
        Object streetObject = jsonObject.get(JSON_ADDRESS_STREET_STRING);
        Object postalCodeObject = jsonObject.get(JSON_ADDRESS_POSTALCODE_STRING);
        if (anyObjectIsNull(
                countryObject,
                stateObject,
                cityObject,
                streetObject,
                postalCodeObject
        )) throw new Exception("JSON to HomeAddress: Missing one or more fields");
        try {
            String country = (String) countryObject;
            String state = (String) stateObject;
            String city = (String) cityObject;
            String street = (String) streetObject;
            Integer postalCode = (Integer) postalCodeObject;
            return new HomeAddress(country,state,city, street,postalCode);
            
        } catch (ClassCastException e) {
            throw new Exception("JSON to HomeAddress: Invalid format for one or more fields",e);
        }
    }
    
    /**
     * @return A JSONObject for UserAccount or null if object is null.
     */
    @SuppressWarnings("unchecked")
    public static JSONObject userAccountToJSON(UserAccount object) {
        if (object != null) {
            int id = object.id;
            String firstName = object.firstName == null ? "null" : object.firstName;
            String lastName = object.lastName == null ? "null" : object.lastName;
            String email = object.email == null ? "null" : object.email;
            
            // These would never be null. Check anyway in case the classes are changed later.
            HomeAddress address = object.getAddress();
            List<String> phoneNumbers = object.getPhoneNumbers();
            address = address == null ? new HomeAddress() : address;
            phoneNumbers = phoneNumbers == null ? new ArrayList<>() : phoneNumbers;
            
            JSONObject jsonObject = new JSONObject();
            {
                JSONArray jsonArray = new JSONArray();
                jsonArray.addAll(phoneNumbers);
                jsonObject.put(JSON_USERACC_ID_STRING,id);
                jsonObject.put(JSON_USERACC_FIRSTNAME_STRING,firstName);
                jsonObject.put(JSON_USERACC_LASTNAME_STRING,lastName);
                jsonObject.put(JSON_USERACC_EMAIL_STRING,email);
                jsonObject.put(JSON_USERACC_ADDRESS_STRING, homeAddressToJSON(address));
                jsonObject.put(JSON_USERACC_PHONENUMBERS_STRING,jsonArray);
            }
            return jsonObject;
        } return null;
    }
    
    public static UserAccount userAccountFromJSON(JSONObject jsonObject) throws Exception {
        if (jsonObject == null) throw new Exception("JSONObject is null");
        
        Object idObject = jsonObject.get(JSON_USERACC_ID_STRING);
        Object firstNameObject = jsonObject.get(JSON_USERACC_FIRSTNAME_STRING);
        Object lastNameObject = jsonObject.get(JSON_USERACC_LASTNAME_STRING);
        Object emailObject = jsonObject.get(JSON_USERACC_EMAIL_STRING);
        Object addressObject = jsonObject.get(JSON_USERACC_ADDRESS_STRING);
        Object phoneNumbersObject = jsonObject.get(JSON_USERACC_PHONENUMBERS_STRING);
        if (anyObjectIsNull(
                idObject,
                firstNameObject,
                lastNameObject,
                emailObject,
                addressObject,
                phoneNumbersObject
        )) throw new Exception("JSON to UserAccount: Missing one or more fields");
        try {
            Integer id = (Integer) idObject;
            String firstName = (String) firstNameObject;
            String lastName = (String) lastNameObject;
            String email = (String) emailObject;
            HomeAddress homeAddress = homeAddressFromJSON((JSONObject) addressObject);
            List<String> phoneNumbers = castAndAdd((JSONArray) phoneNumbersObject,new ArrayList<>(), String.class);
            UserAccount userAccount = new UserAccount(id,firstName,lastName,email);
            userAccount.getPhoneNumbers().addAll(phoneNumbers);
            userAccount.getAddress().set(homeAddress);
            return userAccount;
        } catch (ClassCastException e) {
            throw new Exception("JSON to UserAccount: Invalid format for one or more fields",e);
        }
        
    }
    
    /**
     * Cast and Add content of JSONArray to the list
     * @param clazz class uses for casting objects
     * @return the provided list
     * @throws ClassCastException If the content of the JSONArray can't be cast to T
     * @throws IllegalStateException If any argument is null
     */
    public static <T> List<T> castAndAdd(JSONArray jsonArray, List<T> list, Class<T> clazz) throws ClassCastException, IllegalStateException {
        if (anyObjectIsNull(jsonArray,list,clazz)) throw new IllegalStateException("Provided one ore more null argument");
        for (Object object : jsonArray) {
            list.add(clazz.cast(object));
        } return list;
    }
    
    private static void validateHeader(JSONObject jsonObject) throws Exception {
        if (jsonObject == null) throw new Exception("JSONObject is null");
        
    }
    
    //private static boolean validateHeader(JSONObject jsonObject) {
    //    if (jsonObject != null) {
    //        Object versionObject = jsonObject.get(VERSION_STRING);
    //        if (versionObject instanceof Integer version) {
    //            return version == VERSION_VALUE;
    //        }
    //    } return false;
    //}
    
    /**
     * @param string json-formatted string
     * @return a JSONObject
     * @throws ParseException if the string cannot be parsed
     */
    private static JSONObject stringToJSONObject(String string) throws ParseException {
        string = string == null ? "" : string;
        JSONParser parser = new JSONParser();
        return (JSONObject) parser.parse(string);
    }
    
    /**
     * Null Objects are not allowed.
     * @param objects list of objects
     * @return true if -> list.isEmpty() || All objects share the same class (.getClass == .getClass), false if not.
     * @throws IllegalStateException if argument == null OR any object is null
     */
    private static boolean allObjectsShareClass(List<Object> objects) throws IllegalStateException {
        if (objects == null) throw new IllegalStateException("Argument should never be null ATP");
        Class<?> clazz = null;
        for (Object obj : objects) {
            if (obj == null) throw new IllegalStateException("No object should be null ATP");
            if (clazz == null) {
                clazz = obj.getClass();
            } else if (!(clazz == obj.getClass())) {
                return false;
            }
        } return true;
    }
    
    /**
     * @param objects a collection of objects
     * @return a new List of all non-null elements in collection.
     * (Empty List if the argument is null)
     */
    private static List<Object> listOfNonNull(Collection<Object> objects) {
        List<Object> list = new ArrayList<>();
        if (objects != null) {
            for (Object obj : objects) {
                if (obj != null) {
                    list.add(obj);
                }
            }
        } return list;
    }
    
    
    /**
     * Check for null object
     * @param objects 0 or more objects
     * @return true if any object is null
     */
    private static boolean anyObjectIsNull(Object ... objects) {
        if (objects == null) throw new IllegalStateException("Provided Illegal null argument for private method");
        for (Object object : objects) {
            if (object == null) return true;
        } return false;
    }
    
    @SuppressWarnings("unchecked")
    public static void main(String[] args) throws Exception {
        
        
        
        HomeAddress address = new HomeAddress(
                "Norge",
                "Østfold",
                "Sarpsborg",
                "Gate 34b",
                1706
        );
        
        UserAccount account = new UserAccount(300,"Geir","Seter","geir@hotmail.com");
        account.getAddress().set(address);
        account.getPhoneNumbers().add("123456789");
        account.getPhoneNumbers().add("987654321");
        
        JSONObject jsonObject = userAccountToJSON(account);
        System.out.println(jsonObject);
        account = userAccountFromJSON(jsonObject);
        
        boolean b = anyObjectIsNull();
        System.out.println(b);
        
        JSONObject test = new JSONObject();
        test.put("T",4);
        String s = test.toString();
        
        JSONParser parser = new JSONParser();
        Object o = parser.parse(s);
        
        account = new UserAccount();
        account.getPhoneNumbers().add("123456789");
        account.getPhoneNumbers().add("222256789");
        
        jsonObject = toJSON(account);
        System.out.println(jsonObject.toString());
        
        
        ArrayList<Object> arrayList = new ArrayList<>();
        System.out.println();
        System.out.println(allObjectsShareClass(arrayList)); // true
        arrayList.add(1);
        arrayList.add(455);
        System.out.println(allObjectsShareClass(arrayList)); // true
        arrayList.add("ddddd");
        System.out.println(allObjectsShareClass(arrayList)); // false
        arrayList.clear();
        System.out.println(allObjectsShareClass(arrayList)); // true
        
        
        List<Object> accounts = new ArrayList<>();
        for (int i = 0; i < 4; i++) {
            UserAccount a = new UserAccount();
            a.getPhoneNumbers().add("123456789");
            a.firstName = "LOL";
            a.lastName = "LMFAO";
            a.email = "abs@gmial.com";
            a.id = i;
            a.getAddress().city = "Fredrikstad";
            accounts.add(a);
        }
        
        JSONArray array = toJSONArray(accounts);
        
        System.out.println(array);
        
        accounts.add(null);
        
        array = toJSONArray(accounts);
        
        System.out.println(array);
        
        accounts.add(new Object());
        
        array = toJSONArray(accounts);
        
        System.out.println(array);
        
    }
    
    /*
    public enum SUPPORTED_DATA_TYPE {
        HOME_ADDRESS("Home Address Array", HomeAddress.class),
        USER_ACCOUNT("User Account Array", UserAccount.class);
        public final String header;
        public final Class<?> clazz;
        final static SUPPORTED_DATA_TYPE[] all = values();
        SUPPORTED_DATA_TYPE(String header, Class<?> clazz) {
            this.header = header;
            this.clazz = clazz;
        }
    }
    
    public static final class Wrapper {
        private SUPPORTED_DATA_TYPE dataType;
        private JSONObject object;
        private int numObjects;
        
        
        public Wrapper(Object obj, SUPPORTED_DATA_TYPE dataType) throws Exception {
            if (obj == null || obj.getClass() != dataType.clazz) throw new Exception("Unsupported Datatype");
            List<Object> list = new ArrayList<>(1);
            list.add(obj);
            JSONArray jsonArray = toJSONArray(list); // Never null
            this.numObjects = jsonArray.size();
            this.dataType = dataType;
            JSONObject jsonObject = new JSONObject();
            jsonObject.put(dataType.header,jsonArray);
            jsonObject.put(JSON_CONVERTER_VERSION_STRING,JSON_CONVERTER_VERSION);
        }
        
        public Wrapper(Collection<Object> collection, SUPPORTED_DATA_TYPE dataType) throws Exception {
            JSONArray jsonArray = toJSONArray(collection);
            //if (!supports(clazz)) throw new Exception("Unsupported Datatype");
            
        }
        
    }
    
    
    
    public static boolean supports(Object object) {
        return (object instanceof G6DataType dataType) && supports(dataType);
    }
    
    private static boolean supports(G6DataType dataType) {
        if (dataType != null) {
            for (SUPPORTED_DATA_TYPE sdt : SUPPORTED_DATA_TYPE.all) {
                if (sdt.clazz == dataType.getClass()) {
                    return true;
                }
            }
        } return false;
    }
    
    public static boolean supports(Class<?> clazz) {
        if (clazz != null) {
            for (SUPPORTED_DATA_TYPE sdt : SUPPORTED_DATA_TYPE.all) {
                if (sdt.clazz == clazz) {
                    return true;
                }
            }
        } return false;
    }
    
     */
    
    
    
}
