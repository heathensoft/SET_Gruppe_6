package no.hiof.set.g6.db;


import no.hiof.set.g6.ny.G6Serializable;

/**
 *
 * @author Frederik Dahl
 * 17/10/2024
 *
 */
 

public abstract class G6DBRequest implements G6Serializable {
 
    public enum Type {
        LIST_USERS(),
        SEARCH_ACCOUNTS(),
        ADD_USER(),
        REMOVE_USER(),
        EDIT_USER(),
        LIST_LOCKS(),
        EDIT_LOCKS()
    }
}
