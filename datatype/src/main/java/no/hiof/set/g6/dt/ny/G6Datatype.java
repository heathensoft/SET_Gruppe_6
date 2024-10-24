package no.hiof.set.g6.dt.ny;

import org.json.simple.JSONObject;

/**
 * The Base Class of all our Datatypes.
 * It's important that all G6Datatypes got an empty constructor !!!
 * (Why? Because we are using java reflection to create new Datatype Instances)
 *
 */

public abstract class G6Datatype<T extends G6Datatype<T>> implements JsonSerializable, Comparable<T> {

    public static final String NULL_STRING = "null";
    public static final int NULL = 0;

    /**
     * Creates new Object with clear fields.
     * The bool argument ensures that G6Datatypes calls this super constructor
     */
    protected G6Datatype(boolean clear) { clearFields(); }

    /**
     * Creates a new G6Datatype of Class clazz, using values from a JSONObject.
     * @param clazz the class of the new G6Datatype of type T
     * @param jsonObject JSONObject with appropriate content
     * @return new G6Datatype of type T
     * @throws Exception If the JSONObject could not be translated into G6Datatype T
     * or if the G6Datatype Class doesn't have an empty constructor
     */
    public static <T extends G6Datatype<T>> T fromJson(Class<T> clazz, JSONObject jsonObject) throws Exception {
        if (clazz == null || jsonObject == null) throw new IllegalStateException("null arg fromJson");
        try { T datatype = clazz.getDeclaredConstructor().newInstance();
            datatype.fromJson(jsonObject);
            return datatype;
        } catch (NoSuchMethodException e) {
            throw new Exception(e);
        }
    }

    /**
     * Set the fields to the same as if the object was just created.
     */
    public abstract void clearFields();

    /**
     * Should ensure that all the fields are not null.
     * For instance, null String fields are set to "null"
     */
    public abstract void ensureFieldsNotNull();

    /**
     * Set these fields with the fields of "other".
     * @param o the other object
     */
    public abstract void set(T o);

    /* Datatype does not have to override this (Sorting purposes)*/
    @Override
    public int compareTo(T o) { return 0; }
}
