package no.hiof.set.g6.ny;

import org.json.simple.JSONObject;

/**
 * @author Mahmad
 */
public class Locks extends G6Datatype<Locks> {

    public static final String JSON_KEY_LOCK_ID = "Lock ID";
    public static final String JSON_KEY_HUB_ID = "Hub ID";
    public static final String JSON_KEY_DOOR_NAME = "Door Name";
    public static final String JSON_KEY_LOCK_STATUS = "Lock Status";
    public static final String JSON_KEY_BATTERY_STATUS = "Battery Status";
    public static final String JSON_KEY_MECHANICAL_STATUS = "Mechanical Status";

    // ENUM for lock_status ('Locked', 'Unlocked')
    public enum LockStatus {
        LOCKED, UNLOCKED
    }

    // ENUM for mechanical_status ('OK', 'Fault')
    public enum MechanicalStatus {
        OK, FAULT
    }

    public int lockId;               // corresponds to lock_id INT
    public String doorName;          // corresponds to door_name VARCHAR(50)
    public LockStatus lockStatus;    // corresponds to lock_status ENUM('Locked', 'Unlocked')
    public int batteryStatus;        // corresponds to battery_status INT
    public MechanicalStatus mechanicalStatus;  // corresponds to mechanical_status ENUM('OK', 'Fault')

    // Constructor
    public Locks(String doorName, LockStatus lockStatus, int batteryStatus, MechanicalStatus mechanicalStatus) {
        this.doorName = doorName;
        this.lockStatus = lockStatus;
        this.batteryStatus = batteryStatus;
        this.mechanicalStatus = mechanicalStatus;
    }

    // Empty constructor
    public Locks() {
        this.doorName = "";
        this.lockStatus = LockStatus.LOCKED;
        this.batteryStatus = 100;
        this.mechanicalStatus = MechanicalStatus.OK;
    }

    @Override
    public boolean missingFields() {
        return JsonUtils.anyObjectIsNull(doorName,lockStatus,mechanicalStatus);
    }

    @Override
    public void set(Locks lock) {
        if (lock == null) {
            this.lockId = 0;
            this.doorName = "null";
            this.lockStatus = LockStatus.LOCKED;
            this.batteryStatus = 0;
            this.mechanicalStatus = MechanicalStatus.OK;
        } else {
            this.lockId = lock.lockId;
            this.doorName = lock.doorName;
            this.lockStatus = lock.lockStatus;
            this.batteryStatus = lock.batteryStatus;
            this.mechanicalStatus = lock.mechanicalStatus;
        }
    }

    @Override
    public void fromJson(JSONObject jsonObject) throws Exception {
        if (jsonObject == null) throw new Exception("JSONObject is null");
        Object lockIdObject = jsonObject.get(JSON_KEY_LOCK_ID);
        Object hubsObject = jsonObject.get(JSON_KEY_HUB_ID);
        Object doorNameObject = jsonObject.get(JSON_KEY_DOOR_NAME);
        Object lockStatusObject = jsonObject.get(JSON_KEY_LOCK_STATUS);
        Object batteryStatusObject = jsonObject.get(JSON_KEY_BATTERY_STATUS);
        Object mechanicalStatusObject = jsonObject.get(JSON_KEY_MECHANICAL_STATUS);

        if (JsonUtils.anyObjectIsNull(
                lockIdObject, hubsObject, doorNameObject, lockStatusObject,
                batteryStatusObject, mechanicalStatusObject
        )) throw new Exception("JSON to Locks: Missing one or more fields");

        try {
            this.lockId = (Integer) lockIdObject;
            this.doorName = (String) doorNameObject;
            this.lockStatus = LockStatus.valueOf(((String) lockStatusObject).toUpperCase());
            this.batteryStatus = (Integer) batteryStatusObject;
            this.mechanicalStatus = MechanicalStatus.valueOf(((String) mechanicalStatusObject).toUpperCase());
        } catch (ClassCastException e) {
            throw new Exception("JSON to Locks: Invalid format for one or more fields", e);
        }
    }

    @Override
    @SuppressWarnings("unchecked")
    public JSONObject toJson() {
        String doorName = this.doorName == null ? "null" : this.doorName;
        String lockStatus = this.lockStatus == null ? "null" : this.lockStatus.name();
        String mechanicalStatus = this.mechanicalStatus == null ? "null" : this.mechanicalStatus.name();
        int lockId = this.lockId;
        int batteryStatus = this.batteryStatus;

        JSONObject jsonObject = new JSONObject(); {
            jsonObject.put(JSON_KEY_LOCK_ID, lockId);
            jsonObject.put(JSON_KEY_DOOR_NAME, doorName);
            jsonObject.put(JSON_KEY_LOCK_STATUS, lockStatus);
            jsonObject.put(JSON_KEY_BATTERY_STATUS, batteryStatus);
            jsonObject.put(JSON_KEY_MECHANICAL_STATUS, mechanicalStatus);
        }
        return jsonObject;
    }

    // Implement compareTo method
    @Override
    public int compareTo(G6Datatype other) {
        if (other instanceof Locks o) {
            int comp;
            Locks t = this;

            // Compare door names first
            {
                String t_doorName = t.doorName == null ? "" : t.doorName;
                String o_doorName = o.doorName == null ? "" : o.doorName;
                comp = t_doorName.compareTo(o_doorName);
            }

            // If door names are equal, compare lock statuses
            if (comp == 0) {
                String t_lockStatus = t.lockStatus == null ? "" : t.lockStatus.name();
                String o_lockStatus = o.lockStatus == null ? "" : o.lockStatus.name();
                comp = t_lockStatus.compareTo(o_lockStatus);
            }

            // If lock statuses are also equal, compare battery statuses
            if (comp == 0) {
                comp = Integer.compare(t.batteryStatus, o.batteryStatus);
            }

            // If battery statuses are also equal, compare mechanical statuses
            if (comp == 0) {
                String t_mechanicalStatus = t.mechanicalStatus == null ? "" : t.mechanicalStatus.name();
                String o_mechanicalStatus = o.mechanicalStatus == null ? "" : o.mechanicalStatus.name();
                comp = t_mechanicalStatus.compareTo(o_mechanicalStatus);
            }

            return comp;
        }
        return 0;
    }
}
