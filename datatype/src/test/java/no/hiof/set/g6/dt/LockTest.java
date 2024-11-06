package no.hiof.set.g6.dt;

import org.json.simple.JSONObject;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class LockTest {

    @Test
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



}
