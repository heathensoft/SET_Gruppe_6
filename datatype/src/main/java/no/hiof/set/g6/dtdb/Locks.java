package no.hiof.set.g6.dtdb;

public class Locks {

    // ENUM for lock_status ('Locked', 'Unlocked')
    public enum LockStatus {
        LOCKED, UNLOCKED
    }

    // ENUM for mechanical_status ('OK', 'Fault')
    public enum MechanicalStatus {
        OK, FAULT
    }

    private int lockId;               // corresponds to lock_id INT
    private Hubs hubs;                // corresponds to hub_id INT (Foreign Key)
    private String doorName;          // corresponds to door_name VARCHAR(50)
    private LockStatus lockStatus;    // corresponds to lock_status ENUM('Locked', 'Unlocked')
    private int batteryStatus;        // corresponds to battery_status INT
    private MechanicalStatus mechanicalStatus;  // corresponds to mechanical_status ENUM('OK', 'Fault')

    // Constructors, Getters, and Setters
    public Locks(int lockId, Hubs hubs, String doorName, LockStatus lockStatus, int batteryStatus, MechanicalStatus mechanicalStatus) {
        this.lockId = lockId;
        this.hubs = hubs;
        this.doorName = doorName;
        this.lockStatus = lockStatus;
        this.batteryStatus = batteryStatus;
        this.mechanicalStatus = mechanicalStatus;
    }

    public int getLockId() {
        return lockId;
    }

    public void setLockId(int lockId) {
        this.lockId = lockId;
    }

    public Hubs getHub() {
        return hubs;
    }

    public void setHub(Hubs hubs) {
        this.hubs = hubs;
    }

    public String getDoorName() {
        return doorName;
    }

    public void setDoorName(String doorName) {
        this.doorName = doorName;
    }

    public LockStatus getLockStatus() {
        return lockStatus;
    }

    public void setLockStatus(LockStatus lockStatus) {
        this.lockStatus = lockStatus;
    }

    public int getBatteryStatus() {
        return batteryStatus;
    }

    public void setBatteryStatus(int batteryStatus) {
        this.batteryStatus = batteryStatus;
    }

    public MechanicalStatus getMechanicalStatus() {
        return mechanicalStatus;
    }

    public void setMechanicalStatus(MechanicalStatus mechanicalStatus) {
        this.mechanicalStatus = mechanicalStatus;
    }
}
