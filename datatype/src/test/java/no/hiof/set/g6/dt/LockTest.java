package no.hiof.set.g6.dt;

import org.json.simple.JSONObject;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class LockTest {

    @Test
    @DisplayName("Test JSON serialization of Lock object")
    public void testToJson() {
        Lock lock = new Lock();
        lock.id = 1;
        lock.doorName = "Front Door";
        lock.serialNumber = 12345;
        lock.batteryStatus = 0.8f;
        lock.lockStatus = Lock.LockStatus.LOCKED;
        lock.mechanicalStatus = Lock.MechanicalStatus.OK;

        JSONObject jsonObject = lock.toJson();

        assertEquals(1, jsonObject.get(Lock.JSON_KEY_LOCK_ID));
        assertEquals("Front Door", jsonObject.get(Lock.JSON_KEY_DOOR_NAME));
        assertEquals(12345, jsonObject.get(Lock.JSON_KEY_SERIAL_NUMBER));
        assertEquals(0.8f, jsonObject.get(Lock.JSON_KEY_BATTERY_STATUS));
        assertEquals(Lock.LockStatus.LOCKED.ordinal(), jsonObject.get(Lock.JSON_KEY_LOCK_STATUS));
        assertEquals(Lock.MechanicalStatus.OK.ordinal(), jsonObject.get(Lock.JSON_KEY_MECHANICAL_STATUS));
    }

    @Test
    @DisplayName("Test JSON deserialization into lock object")
    @SuppressWarnings("unchecked")
    public void testFromJson() throws Exception {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put(Lock.JSON_KEY_LOCK_ID, 1);
        jsonObject.put(Lock.JSON_KEY_DOOR_NAME, "Front Door");
        jsonObject.put(Lock.JSON_KEY_SERIAL_NUMBER, 12345);
        jsonObject.put(Lock.JSON_KEY_BATTERY_STATUS, 0.8f);
        jsonObject.put(Lock.JSON_KEY_LOCK_STATUS, Lock.LockStatus.LOCKED.ordinal());
        jsonObject.put(Lock.JSON_KEY_MECHANICAL_STATUS, Lock.MechanicalStatus.OK.ordinal());

        Lock lock = new Lock();
        lock.fromJson(jsonObject);

        assertEquals(1, lock.id);
        assertEquals("Front Door", lock.doorName);
        assertEquals(12345, lock.serialNumber);
        assertEquals(0.8f, lock.batteryStatus);
        assertEquals(Lock.LockStatus.LOCKED, lock.lockStatus);
        assertEquals(Lock.MechanicalStatus.OK, lock.mechanicalStatus);
    }

    @Test
    @DisplayName("Test JSON serialization and deserialization for Lock object equality")
    public void testJsonConversion() throws Exception {
        // Creating lock object
        Lock originalLock = new Lock();
        originalLock.id = 1;
        originalLock.doorName = "Main Entrance";
        originalLock.serialNumber = 98765;
        originalLock.batteryStatus = 0.9f;
        originalLock.lockStatus = Lock.LockStatus.UNLOCKED;
        originalLock.mechanicalStatus = Lock.MechanicalStatus.OK;

        // converting to JSON
        JSONObject jsonObject = originalLock.toJson();

        // Creating a new lock object
        Lock newLock = new Lock();
        newLock.fromJson(jsonObject);

        // Checking that the two objects are alike
        assertEquals(originalLock.id, newLock.id);
        assertEquals(originalLock.doorName, newLock.doorName);
        assertEquals(originalLock.serialNumber, newLock.serialNumber);
        assertEquals(originalLock.batteryStatus, newLock.batteryStatus, 0.001);
        assertEquals(originalLock.lockStatus, newLock.lockStatus);
        assertEquals(originalLock.mechanicalStatus, newLock.mechanicalStatus);
    }

    @Test
    @DisplayName("Test clamping battery status between 0.0 and 1.0")
    public void testClampBattery() {
        Lock lock = new Lock();
        lock.batteryStatus = 1.2f;
        lock.clampBattery();
        assertEquals(1.0f, lock.batteryStatus);

        lock.batteryStatus = -0.5f;
        lock.clampBattery();
        assertEquals(0.0f, lock.batteryStatus);
    }

    @Test
    @DisplayName("Test ensuring non-null default values for fields in Lock")
    public void testEnsureFieldsNotNull() {
        Lock lock = new Lock();
        lock.doorName = null;
        lock.lockStatus = null;
        lock.mechanicalStatus = null;

        lock.ensureFieldsNotNull();

        assertEquals(Lock.NULL_STRING, lock.doorName);
        assertEquals(Lock.LockStatus.LOCKED, lock.lockStatus);
        assertEquals(Lock.MechanicalStatus.OK, lock.mechanicalStatus);
    }

    @Test
    @DisplayName("Test Lock compareTo method for sorting by door name and battery status")
    public void testCompareTo() {
        Lock lock1 = new Lock();
        lock1.doorName = "Back Door";
        lock1.batteryStatus = 0.7f;

        Lock lock2 = new Lock();
        lock2.doorName = "Back Door";
        lock2.batteryStatus = 0.5f;

        Lock lock3 = new Lock();
        lock3.doorName = "Garage Door";
        lock3.batteryStatus = 0.9f;

        // Testing the same door name, higher battery status
        assertTrue(lock1.compareTo(lock2) < 0);
        // Testing a different door name
        assertTrue(lock1.compareTo(lock3) < 0);
    }

    @Test
    @DisplayName("Test LockStatus enum getByOrdinal method")
    public void testEnumLockStatusGetByOrdinal() {
        assertEquals(Lock.LockStatus.LOCKED, Lock.LockStatus.getByOrdinal(0));
        assertEquals(Lock.LockStatus.UNLOCKED, Lock.LockStatus.getByOrdinal(1));
        assertNull(Lock.LockStatus.getByOrdinal(3));  // Out of bounds check
    }

    @Test
    @DisplayName("Test MechanicalStatus enum getByOrdinal method")
    public void testEnumMechanicalStatusGetByOrdinal() {
        assertEquals(Lock.MechanicalStatus.OK, Lock.MechanicalStatus.getByOrdinal(0));
        assertEquals(Lock.MechanicalStatus.FAULT, Lock.MechanicalStatus.getByOrdinal(1));
        assertNull(Lock.MechanicalStatus.getByOrdinal(3));  // Out of bounds check
    }

    @Test
    @DisplayName("Test set method updates fields correctly")
    public void testSetMethod() {
        // Create the first Lock object with test data
        Lock lock1 = new Lock();
        lock1.id = 1;
        lock1.serialNumber = 12345;
        lock1.batteryStatus = 0.8f;
        lock1.doorName = "Front Door";
        lock1.lockStatus = Lock.LockStatus.UNLOCKED;
        lock1.mechanicalStatus = Lock.MechanicalStatus.OK;

        // Create the second Lock object
        Lock lock2 = new Lock();

        // Call the set method
        lock2.set(lock1);

        // Verify that lock2 has been updated to match lock1
        assertEquals(lock1.id, lock2.id);
        assertEquals(lock1.serialNumber, lock2.serialNumber);
        assertEquals(lock1.batteryStatus, lock2.batteryStatus);
        assertEquals(lock1.doorName, lock2.doorName);
        assertEquals(lock1.lockStatus, lock2.lockStatus);
        assertEquals(lock1.mechanicalStatus, lock2.mechanicalStatus);

        // Verify that calling set with null clears the fields
        lock2.set(null);
        assertEquals(G6Datatype.NULL, lock2.id);
        assertEquals(G6Datatype.NULL, lock2.serialNumber);
        assertEquals(Lock.FULLY_CHARGED, lock2.batteryStatus);
        assertEquals(G6Datatype.NULL_STRING, lock2.doorName);
        assertEquals(Lock.LockStatus.LOCKED, lock2.lockStatus);
        assertEquals(Lock.MechanicalStatus.OK, lock2.mechanicalStatus);
    }


}
