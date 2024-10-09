package no.hiof.set.g6.dt;


/**
 * Et Produkt er noe som bedriften selger. F.eks. En lås-enhet eller HUB.
 *
 */


public interface Product {
    
    /**
     * @return en unik id for produktet. Bare en simmulert verdi for nå.
     */
    int productID();
    
    /**
     * Hvis produktet er solgt, er det registrert på en eier.
     * @return Eier Konto eller null
     */
    UserAccount registeredOwner();
    
    /**
     * Type Produkt vare. Udefinert som default. Kan overrides.
     * @return Type Produkt
     */
    default ProductType type() {
        return ProductType.NOT_DEFINED;
    }
    
}
