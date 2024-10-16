package no.hiof.set.g6.ny;


/**
 * @author Frederik Dahl
 * 06/10/2024
 */


public enum LocalPermission {
 
    OWNER("Owner"),
    RESIDENT("Resident"),
    GUEST("Guest"),
    NONE("No Permission");
    
    public final String descriptor;
    
    public int id() {
        return this.ordinal();
    }
    
    LocalPermission(String descriptor) {
        this.descriptor = descriptor;
    }
}
