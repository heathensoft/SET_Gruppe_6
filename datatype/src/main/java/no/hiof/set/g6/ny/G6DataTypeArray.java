package no.hiof.set.g6.ny;


import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.util.*;

/**
 * @author Frederik Dahl
 * 16/10/2024
 */


public class G6DataTypeArray<T extends G6Datatype> implements G6Serializable, Iterable<T> {
    
    public static final String JSON_KEY_ARRAY_TYPE = "Type";
    public static final String JSON_KEY_ARRAY = "Array";
    public static final String JSON_TYPE_UNDEFINED = "Undefined";
    
    private static final Map<Class<?>,String> class_map;
    static {
        
        // Add Datatypes Here as we create new:
        class_map = new HashMap<>();
        class_map.put(HomeAddress.class,"Home Address");
        class_map.put(UserAccount.class,"User Account");
    }
    
    private final List<T> list;
    private final Class<T> clazz;
    
    public G6DataTypeArray(Class<T> clazz) {
        this(clazz,16);
    }
    
    public G6DataTypeArray(Class<T> clazz, int capacity) {
        if (clazz == null) throw new IllegalStateException("null arg class");
        this.list = new ArrayList<>(Math.max(capacity,0));
        this.clazz = clazz;
    }
    
    public void add(T dataType) { list.add(dataType); }
    public T get(int index) { return list.get(index); }
    public T remove(int index) { return list.remove(index); }
    public void sort() { Collections.sort(list); }
    public int size() { return list.size(); }
    public boolean isEmpty() { return list.isEmpty(); }
    public void clear() { list.clear(); }
    public Class<T> dataTypeClass() { return clazz; }
    public Iterator<T> iterator() { return list.iterator(); }
    
    
    @Override
    public void fromJson(JSONObject jsonObject) throws Exception {
        if (jsonObject == null) throw new Exception("JSONObject is null");
        Object typeObject = jsonObject.get(JSON_KEY_ARRAY_TYPE);
        Object arrayObject = jsonObject.get(JSON_KEY_ARRAY);
        if (typeObject == null) throw new Exception("JSON to DataTypeArray: Missing one or more fields");
        
        try {
            String objectTypeString = (String) typeObject;
            String thisTypeString = class_map.get(clazz);
            if (!objectTypeString.equals(thisTypeString)) {
                throw new Exception("JSON to DataTypeArray: Invalid Datatype");
            }
            if (arrayObject == null) clear();
            else {
                JSONArray jsonArray = (JSONArray) arrayObject;
                clear();
                for (Object o : jsonArray)
                    list.add(G6Datatype.fromJson(clazz,(JSONObject) o));
            }
        } catch (ClassCastException e) {
            throw new Exception("JSON to DataTypeArray: Invalid format for one or more fields",e);
        }
    }
    
    @Override
    @SuppressWarnings("unchecked")
    public JSONObject toJson() {
        JSONObject jsonObject = new JSONObject();
        String type_string = class_map.getOrDefault(clazz,JSON_TYPE_UNDEFINED);
        jsonObject.put(JSON_KEY_ARRAY_TYPE,type_string);
        if (!isEmpty()) {
            JSONArray jsonArray = new JSONArray();
            for (T object : list) {
                jsonArray.add(object.toJson());
            } jsonObject.put(JSON_KEY_ARRAY,jsonArray);
        } return jsonObject;
    }
    
    
    public static void main(String[] args) {
        G6DataTypeArray<UserAccount> userAccounts = new G6DataTypeArray<>(UserAccount.class);
        for (int i = 0; i < 10; i++) {
            UserAccount userAccount = new UserAccount();
            userAccount.firstName = "Teddy " + i;
            userAccounts.add(userAccount);
        } userAccounts.sort();
        System.out.println(userAccounts.toJson().toString());
    }
    
}
