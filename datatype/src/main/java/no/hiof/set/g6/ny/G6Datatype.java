package no.hiof.set.g6.ny;


import org.json.simple.JSONObject;

/**
 * @author Frederik Dahl
 * 16/10/2024
 */


public abstract class G6Datatype implements G6Serializable, Comparable<G6Datatype> {
    
    
    public G6Datatype() { }
    
    @SuppressWarnings("unchecked")
    public static <T extends G6Datatype> T fromJson(Class<T> clazz, JSONObject jsonObject) throws Exception {
        if (clazz == null || jsonObject == null) throw new IllegalStateException("null arg fromJson");
        G6Datatype dataType;
        if (clazz == HomeAddress.class) {
            dataType = new HomeAddress();
        } else if (clazz == UserAccount.class) {
            dataType = new UserAccount();
        } else throw new Exception("Unsupported DataType");
        dataType.fromJson(jsonObject);
        return (T)dataType;
    }
    
    @Override
    public int compareTo(G6Datatype other) { return 0; }
}
