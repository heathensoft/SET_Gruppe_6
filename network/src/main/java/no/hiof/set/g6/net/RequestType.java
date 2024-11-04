package no.hiof.set.g6.net;

/**
 * PROOF OF CONCEPT
 *
 *
 */
public enum RequestType {

    USER_ADD("User Add"),
    USER_DELETE("User Delete"),
    USER_LIST("User List"),
    LOCK_TOGGLE("Lock Toggle"),
    LOCK_LIST("Lock List"),
    /** If the User has insufficient Role */
    ACCESS_DENIED("Access Denied");

    RequestType(String descriptor) {
        this.descriptor = descriptor;
    } public String toString() {
        return descriptor;
    }

    public final String descriptor;
    private static final RequestType[] all;
    static { all = values(); }
    public static RequestType getByOrdinal(int ordinal) {
        if (ordinal < all.length && ordinal > 0) {
            return all[ordinal];
        } return null;
    }
}
