package no.hiof.set.g6.net;

import no.hiof.set.g6.dt.DatatypeArray;
import no.hiof.set.g6.dt.LocalUser;
import no.hiof.set.g6.dt.Lock;

/**
 * PROOF OF CONCEPT Database Interface for HUB. Implementation agnostic.
 *
 */

public interface LocalSystemDB {

    // *******************************************************************************************
    // USER

    /**
     * Attempt to add user to DB.
     * The user will not be added if a user already exist with the same account ID
     * @param user user to add
     * @return true if user was successfully added to DB
     */
    boolean addUser(LocalUser user);

    /**
     * Attempt to delete user from DB
     * @param user_id id of user to delete
     * @return true if user was successfully deleted from DB
     */
    boolean deleteUser(int user_id);

    /**
     * Fetch user by account ID
     * @param account_id the account ID of user
     * @return a user object or null
     */
    LocalUser getUserByAccountID(int account_id);

    /**
     * @return list of all users in DB
     */
    DatatypeArray<LocalUser> getUsers();


    // *******************************************************************************************
    // LOCK

    /**
     * Attempt to add lock to DB.
     * The lock will not be added if a lock already exist with the same serial number
     * @param lock lock to add
     * @return true if lock was successfully added to DB
     */
    boolean addLock(Lock lock);

    /**
     * Attempt to delete lock from DB
     * @param lock_id id of lock to delete
     * @return true if lock was successfully deleted from DB
     */
    boolean deleteLock(int lock_id);

    /**
     * Change status of lock to "Unlocked"
     * @param lock_id id of lock to open
     * @return true if lock was successfully opened. (even if lock was previously open)
     */
    boolean openLock(int lock_id);

    /**
     * Change status of lock to "Locked"
     * @param lock_id id of lock to open
     * @return true if lock was successfully closed. (even if lock was previously closed)
     */
    boolean closeLock(int lock_id);

    /**
     * @return list of all locks in DB
     */
    DatatypeArray<Lock> getLocks();

    // *******************************************************************************************

    /**
     * loads the DB (If file based). Called when the server program starts
     */
    default void load() throws Exception {}

    /**
     * saves the DB (If file based). Called when server program shuts down
     */
    default void save() throws Exception {};
}
