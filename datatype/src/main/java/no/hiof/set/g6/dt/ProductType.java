package no.hiof.set.g6.dt;


/**
 * @author Frederik Dahl
 * 24/09/2024
 */


public enum ProductType {
    
    PHYSICAL_DEVICE("Physical Device"),
    SOFTWARE("Software"),
    NOT_DEFINED("Undefined");
    
    public final String descriptor;
    
    public int id() {
        return this.ordinal();
    }
    
    ProductType(String descriptor) {
        this.descriptor = descriptor;
    }
}
