package no.hiof.set.g6.net;

import no.hiof.set.g6.ny.DatatypeArray;
import no.hiof.set.g6.ny.LocalUser;
import no.hiof.set.g6.ny.Locks;
import no.hiof.set.g6.ny.UserAccount;

/**
 * Prototype Database Interface for HUB
 * Implementation agnostic Prototype Database.
 * Whether we are storing the database as files or use MySQL, both implementations
 * must implement the HUBDatabase interface.
 *
 * Before any of the methods are called, the arguments are validated.
 * They are not null and all fields are not null.
 *
 */

public interface HUBDatabase {

    // Mangler Access Logs


    // Search by Email
    /**
     * Get the Role of the LocalUser
     * @param user LocalUser
     * @return The Role of the User or Role.NONE: if User is not in the Database
     */
    LocalUser.Role getUserRole(LocalUser user) throws Exception;


    // Not sure how you want to do this
    /**
     * NOTE: UserAccounts are not stored in HUB database, but separately on the Central Database.
     * For the purpose of the Prototype, UserAccounts are stored in the HUB Database.
     *
     * Search the Central Database for UserAccounts by firstname, lastname or phone-number
     * @param account A UserAccount object to search for. (Not all it's fields need to be set)
     * @return A list of UserAccounts matching the search criteria
     */
    DatatypeArray<UserAccount> searchForAccount(UserAccount account) throws Exception;

    /**
     * @return All LocalUsers in the HUB Database
     */
    DatatypeArray<LocalUser> allStoredLocalUsers() throws Exception;


    // Search by Email
    /**
     * Adds a new User to HUB Database
     * @param user The User to add
     * @return true if user was successfully added, false if user already exists
     */
    boolean addLocalUser(LocalUser user) throws Exception;


    // Search by Email
    /**
     * Remove a User from HUB Database
     * @param user The User to remove
     * @return true if user was successfully removed, false if user was not found
     */
    boolean removeLocalUser(LocalUser user) throws Exception;

    // Search by Email
    /**
     * Edit a User in HUB Database
     * @param user The User to edit
     * @return true if user was successfully edited, false if user was not found
     */
    boolean editLocalUser(LocalUser user) throws Exception;

    /**
     * @return All Locks in the HUB Database
     */
    DatatypeArray<Locks> allStoredLocks() throws Exception;

    // find the lock by primary key, edit the lock and save the lock
    /**
     * Edit a lock in HUB Database (Open lock / Close lock / change name)
     * @param lock The Lock to edit
     * @return true if the lock exists and was edited, false if the lock does not exist
     */
    boolean editLock(Locks lock) throws Exception;

    /**
     * If the Database needs to be initialized or loaded into memory.
     * Called when the server starts up
     */
    default void load() throws Exception {}

    /**
     * If the Database needs to be disposed or saved to file before exiting the Server program
     */
    default void save() throws Exception {};
}
