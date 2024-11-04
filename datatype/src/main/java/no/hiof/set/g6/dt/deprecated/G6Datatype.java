package no.hiof.set.g6.dt.deprecated;


import org.json.simple.JSONObject;

/**
 * The Base Class of all our Datatypes.
 * It's important that all G6Datatypes got an empty constructor !!!
 * (Why? Because we are using java reflection to create new Datatype Instances)
 *
 */

// Todo: Method to validate (Check that the object fields are not null)

public abstract class G6Datatype<T extends G6Datatype<T>> implements JsonSerializable, Comparable<G6Datatype<?>> {
    
    public G6Datatype() { }
    
    /**
     * Creates a new G6Datatype of Class clazz, using values from a JSONObject.
     * @param clazz the class of the new G6Datatype of type T
     * @param jsonObject JSONObject with appropriate content
     * @return new G6Datatype of type T
     * @throws Exception If the JSONObject could not be translated into G6Datatype T
     * or if the G6Datatype Class doesn't have an empty constructor
     */
    public static <T extends G6Datatype<?>> T fromJson(Class<T> clazz, JSONObject jsonObject) throws Exception {
        if (clazz == null || jsonObject == null) throw new IllegalStateException("null arg fromJson");
        try { T datatype = clazz.getDeclaredConstructor().newInstance();
            datatype.fromJson(jsonObject);
            return datatype;
        } catch (NoSuchMethodException e) {
            throw new Exception(e);
        }
    }

    /**
     * Validates the Object. Making sure all it's fields are set
     * @return true if the Object has missing fields (null fields)
     */
    public abstract boolean missingFields();

    /**
     * Set these fields with the fields of "other".
     * @param other the other object
     */
    public abstract void set(T other);
    
    /* Datatype does not have to override this (Sorting purposes)*/
    @Override
    public int compareTo(G6Datatype other) { return 0; }
}
