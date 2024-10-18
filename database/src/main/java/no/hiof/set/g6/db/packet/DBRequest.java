package no.hiof.set.g6.db.packet;


import no.hiof.set.g6.ny.JsonSerializable;

/**
 * @author Frederik Dahl
 * 17/10/2024
 */


public abstract class DBRequest implements JsonSerializable {
    
    protected Type type;
    
    public enum Type {
        
        ACCOUNT_SEARCH("Account Search"),
        USER_LIST("Local User List"),
        USER_ADD("Local User Add"),
        USER_REMOVE("Local User Remove"),
        USER_EDIT("Local User Edit"),
        LOCK_LIST("Lock List"),
        LOCK_EDIT("Lock Edit");
        
        Type(String descriptor) {
            this.descriptor = descriptor;
        }
        public final String descriptor;
        private static final Type[] all;
        static { all = values(); }
        public static Type getByOrdinal(int ordinal) {
            if (ordinal < all.length && ordinal > 0) {
                return all[ordinal];
            } return null;
        }
    }
    
    
    
}
