package no.hiof.set.g6.dt;


import org.json.simple.JSONObject;

/**
 * @author Mahmad
 */
public final class Lock extends G6Datatype<Lock> implements Product {

    /** Battery goes from 1.0 (FULLY CHARGED) -> 0.0 (DEPLETED) */
    // if you want the value from 1 to 100: batteryStatus x 100
    public static final float FULLY_CHARGED = 1.0f;

    public static final String JSON_KEY_LOCK_ID = "Lock ID";
    public static final String JSON_KEY_DOOR_NAME = "Door Name";
    public static final String JSON_KEY_LOCK_STATUS = "Lock Status";
    public static final String JSON_KEY_BATTERY_STATUS = "Battery Status";
    public static final String JSON_KEY_MECHANICAL_STATUS = "Mechanical Status";




    public enum LockStatus { // ENUM for lock_status ('Locked', 'Unlocked')
        LOCKED("Locked"),
        UNLOCKED("Unlocked");
        LockStatus(String descriptor) { this.descriptor = descriptor; }
        public final String descriptor;
        private static final LockStatus[] all;
        static { all = values(); }
        public static LockStatus getByOrdinal(int ordinal) {
            if (ordinal < all.length && ordinal > 0) {
                return all[ordinal];
            } return null;
        }
    }

    public enum MechanicalStatus { // ENUM for mechanical_status ('OK', 'Fault')
        OK("OK"),
        FAULT("Fault");
        MechanicalStatus(String descriptor) { this.descriptor = descriptor; }
        public final String descriptor;
        private static final MechanicalStatus[] all;
        static { all = values(); }
        public static MechanicalStatus getByOrdinal(int ordinal) {
            if (ordinal < all.length && ordinal > 0) {
                return all[ordinal];
            } return null;
        }
    }

    public int id;                              // corresponds to lock_id INT
    public int serialNumber;
    public float batteryStatus;                 // corresponds to battery_status INT
    public String doorName;                     // corresponds to door_name VARCHAR(50)
    public LockStatus lockStatus;               // corresponds to lock_status ENUM('Locked', 'Unlocked')
    public MechanicalStatus mechanicalStatus;   // corresponds to mechanical_status ENUM('OK', 'Fault')

    public Lock() { super(true); }

    /** Clamp the battery to a value between 0.0 and 1.0 */
    public void clampBattery() {
        batteryStatus = Math.min(Math.max(0,batteryStatus),1.0f);
    }

    @Override
    public void clearFields() {
        id = NULL;
        serialNumber = NULL;
        doorName = NULL_STRING;
        batteryStatus = FULLY_CHARGED;
        lockStatus = LockStatus.LOCKED;
        mechanicalStatus = MechanicalStatus.OK;
    }

    @Override
    public void ensureFieldsNotNull() {
        doorName = doorName == null ? NULL_STRING : doorName;
        lockStatus = lockStatus == null ? LockStatus.LOCKED : lockStatus;
        mechanicalStatus = mechanicalStatus == null ? MechanicalStatus.OK : mechanicalStatus;
    }

    @Override
    public void set(Lock lock) {
        if (lock == null) {
            clearFields();
        } else {
            this.id = lock.id;
            this.doorName = lock.doorName;
            this.lockStatus = lock.lockStatus;
            this.serialNumber = lock.serialNumber;
            this.batteryStatus = lock.batteryStatus;
            this.mechanicalStatus = lock.mechanicalStatus;
        }
    }

    @Override
    public void fromJson(JSONObject jsonObject) throws Exception {
        if (jsonObject == null) throw new Exception("JSONObject is null");

        Object lockIdObject = jsonObject.get(JSON_KEY_LOCK_ID);
        Object doorNameObject = jsonObject.get(JSON_KEY_DOOR_NAME);
        Object lockStatusObject = jsonObject.get(JSON_KEY_LOCK_STATUS);
        Object serialNumberObject = jsonObject.get(JSON_KEY_SERIAL_NUMBER);
        Object batteryStatusObject = jsonObject.get(JSON_KEY_BATTERY_STATUS);
        Object mechanicalStatusObject = jsonObject.get(JSON_KEY_MECHANICAL_STATUS);

        if (JsonUtils.anyObjectIsNull(
                lockIdObject,
                doorNameObject,
                lockStatusObject,
                serialNumberObject,
                batteryStatusObject,
                mechanicalStatusObject
        )) throw new Exception("JSON to Locks: Missing one or more fields");

        try {
            Number id = (Number) lockIdObject;
            String doorName = (String) doorNameObject;
            Number batteryStatus = (Number) batteryStatusObject;
            Number serialNumber = (Number) serialNumberObject;
            Number LockStatusOrdinal = (Number) lockStatusObject;
            Number mechanicalOrdinal = (Number) mechanicalStatusObject;

            this.id = id.intValue();
            this.doorName = doorName;
            this.serialNumber = serialNumber.intValue();
            this.batteryStatus = batteryStatus.floatValue();
            this.lockStatus = LockStatus.getByOrdinal(LockStatusOrdinal.intValue());
            this.mechanicalStatus = MechanicalStatus.getByOrdinal(mechanicalOrdinal.intValue());

            ensureFieldsNotNull();
            clampBattery();
        } catch (ClassCastException e) {
            throw new Exception("JSON to Locks: Invalid format for one or more fields", e);
        }
    }

    @Override
    public int serialNumber() {
        return serialNumber;
    }

    @Override
    @SuppressWarnings("unchecked")
    public JSONObject toJson() {
        clampBattery();
        ensureFieldsNotNull();
        JSONObject jsonObject = new JSONObject(); {
            jsonObject.put(JSON_KEY_LOCK_ID, id);
            jsonObject.put(JSON_KEY_DOOR_NAME, doorName);
            jsonObject.put(JSON_KEY_SERIAL_NUMBER,serialNumber);
            jsonObject.put(JSON_KEY_BATTERY_STATUS, batteryStatus);
            jsonObject.put(JSON_KEY_LOCK_STATUS, lockStatus.ordinal());
            jsonObject.put(JSON_KEY_MECHANICAL_STATUS, mechanicalStatus.ordinal());
        } return jsonObject;
    }

    // Implement compareTo method
    @Override
    public int compareTo(Lock o) {
        if (o != null) {
            int comp; Lock t = this;
            {   // Compare door names first
                String t_doorName = t.doorName == null ? NULL_STRING : t.doorName;
                String o_doorName = o.doorName == null ? NULL_STRING : o.doorName;
                comp = t_doorName.compareTo(o_doorName);
            }   // If lock statuses are also equal, compare battery statuses
            if (comp == 0) comp = Float.compare(o.batteryStatus,t.batteryStatus);
            return comp;
        } return 0;
    }

    @Override
    public String toString() {
        return "Lock {" +
                " id = " + id +
                ", serial-number = " + serialNumber +
                ", door = '" + doorName + '\'' +
                ", battery = " + batteryStatus +
                ", lock-status = " + lockStatus +
                ", mechanical-status = " + mechanicalStatus +
                '}';
    }

}
