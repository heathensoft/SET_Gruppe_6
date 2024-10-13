package no.hiof.set.g6.dtdb;

import no.hiof.set.g6.dt.ProductType;
import no.hiof.set.g6.dt.UserAccount;


//Taken from dt/Frederik
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