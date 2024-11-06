package no.hiof.set.g6.app;


import no.hiof.set.g6.dt.DatatypeArray;
import no.hiof.set.g6.dt.LocalUser;
import no.hiof.set.g6.dt.Lock;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class DatabaseTest {




    private Database database;
    private LocalUser owner;
    private LocalUser guest;
    private LocalUser resident;
    private Lock test_lock;


    @BeforeEach
    protected void setUpDB() {

        // Make sure the DB and owner stays the same before each test.
        // We add 3 users of different roles and one lock
        // Since the add methods might not have been tested yet,
        // We have to add the object directly into the DB tables.
        // and auto increment lock id manually

        String test_db_dir = "app/db/test";
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
        assertNotNull(users);
        assertEquals(count, users.size());
    }





}
