package no.hiof.set.g6.app;


import no.hiof.set.g6.dt.DatatypeArray;
import no.hiof.set.g6.dt.LocalUser;
import no.hiof.set.g6.dt.Lock;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class DatabaseTest {



    private final String test_db_dir = "db/test";
    private Database database;
    private LocalUser owner;
    private LocalUser guest;
    private LocalUser resident;
    private Lock test_lock;


    @BeforeEach
    protected void setUp() {

        // Make sure the DB and owner stays the same before each test.
        // We add 3 users of different roles and one lock
        // Since the add methods might not have been tested yet,
        // We have to add the object directly into the DB tables.
        // and auto increment lock id manually


        database = new Database(test_db_dir);

        owner = new LocalUser();
        owner.role = LocalUser.Role.OWNER;
        owner.userName = "owner_user";
        owner.accountID = 1234;
        database.users_table.put(owner.accountID, owner);

        resident = new LocalUser();
        resident.accountID = 3456;
        resident.userName = "resident_user";
        resident.role = LocalUser.Role.RESIDENT;
        database.users_table.put(resident.accountID, resident);

        guest = new LocalUser();
        guest.accountID = 5568;
        guest.userName = "guest_user";
        guest.role = LocalUser.Role.GUEST;
        database.users_table.put(guest.accountID, guest);

        test_lock = new Lock();
        test_lock.doorName = "Test Door";
        test_lock.serialNumber = 123456789;
        test_lock.id = database.lock_auto_increment++;

        database.locks_table.put(test_lock.id,test_lock);
    }

    @Test
    @DisplayName("Assert addUser is working as intended")
    public void testAddUser() {
        LocalUser user1 = new LocalUser();
        user1.accountID = 999999999;
        int count = database.users_table.size();
        boolean added = database.addUser(user1);
        // Make sure the table size increases by 1 and method return true
        assertEquals(count + 1, database.users_table.size());
        assertTrue(added);

        LocalUser user2 = new LocalUser();
        user2.accountID = user1.accountID;
        count = database.users_table.size();
        added = database.addUser(user2);
        // Make sure a user with the same account id can't be added twice
        assertEquals(count, database.users_table.size());
        assertFalse(added);
    }

    @Test
    @DisplayName("Assert deleteUser is working as intended")
    public void testDeleteUser() {
        int count = database.users_table.size();
        boolean deleted = database.deleteUser(guest.accountID);
        assertEquals(count - 1, database.users_table.size());
        assertTrue(deleted);
        assertNull(database.users_table.get(guest.accountID));
    }

    @Test
    @DisplayName("Assert getUserByAccountID is working as intended")
    public void testGetUserByAccountID() {
        assertEquals(resident,database.getUserByAccountID(resident.accountID));
    }

    @Test
    @DisplayName("Assert getUsers is working as intended")
    public void testGetUsers() {
        int count = database.users_table.size();
        DatatypeArray<LocalUser> users = database.getUsers();
        assertTrue(count >= 1);
        assertNotNull(users);
        assertEquals(count, users.size());
    }

    @Test
    @DisplayName("Assert addLock is working as intended")
    public void testAddLock() {
        String lock_name = "Town Portal";
        int size_before = database.locks_table.size();
        int auto_increment_before = database.lock_auto_increment;

        // First try to add lock with existing serial number
        boolean added = database.addLock(test_lock.serialNumber,lock_name);
        assertFalse(added);
        assertEquals(auto_increment_before,database.lock_auto_increment);
        assertEquals(size_before,database.locks_table.size());

        // Then add lock with a new serial number
        added = database.addLock(44444444,lock_name);
        assertTrue(added);
        assertEquals(auto_increment_before + 1,database.lock_auto_increment);
        assertEquals(size_before + 1,database.locks_table.size());

    }

    @Test
    @DisplayName("Assert deleteLock is working as intended")
    public void testDeleteLock() {
        int size_before = database.locks_table.size();
        boolean deleted = database.deleteLock(test_lock.id);
        assertTrue(deleted);
        assertEquals(size_before - 1,database.locks_table.size());
    }

    @Test
    @DisplayName("Assert openLock and closeLock is working as intended")
    public void testOpenCloseLock() {
        test_lock.lockStatus = Lock.LockStatus.LOCKED;
        boolean opened = database.openLock(test_lock.id);
        assertTrue(opened);
        assertEquals(Lock.LockStatus.UNLOCKED, test_lock.lockStatus);
        boolean closed = database.closeLock(test_lock.id);
        assertTrue(closed);
        assertEquals(Lock.LockStatus.LOCKED, test_lock.lockStatus);
    }

    @Test
    @DisplayName("Assert getLocks is working as intended")
    public void testGetLocks() {
        int count = database.locks_table.size();
        DatatypeArray<Lock> locks = database.getLocks();
        assertTrue(count >= 1);
        assertNotNull(locks);
        assertEquals(count, locks.size());
    }

    @Test
    @DisplayName("Assert save and load  is working as intended")
    public void testSaveLoad() throws Exception {

        int locks_count = database.locks_table.size();
        int users_count = database.users_table.size();
        int lock_auto_increment = database.lock_auto_increment;
        assertTrue(locks_count >= 1);
        assertTrue(users_count >= 1);
        assertTrue(lock_auto_increment >= 1);
        database.save();
        Database loaded_database = new Database(test_db_dir);
        loaded_database.load();
        assertEquals(locks_count,loaded_database.locks_table.size());
        assertEquals(users_count,loaded_database.users_table.size());
        assertEquals(lock_auto_increment,loaded_database.lock_auto_increment);

    }

}
