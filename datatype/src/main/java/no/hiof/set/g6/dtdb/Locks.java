package no.hiof.set.g6.dtdb;

public class Locks {
    private int lockId;
    private Hubs hub;  // Foreign key reference to Hubs
    private String doorName;
    private String lockStatus;
    private int batteryStatus;
    private String mechanicalStatus;

    public Locks(int lockId, Hubs hub, String doorName, String lockStatus, int batteryStatus, String mechanicalStatus) {
        this.lockId = lockId;
        this.hub = hub;
        this.doorName = doorName;
        this.lockStatus = lockStatus;
        this.batteryStatus = batteryStatus;
        this.mechanicalStatus = mechanicalStatus;
    }

    public Locks() {}

    // Har generert gettere og settere
    public int getLockId() {
        return lockId;
    }

    public void setLockId(int lockId) {
        this.lockId = lockId;
    }

    public Hubs getHub() {
        return hub;
    }

    public void setHub(Hubs hub) {
        this.hub = hub;
    }

    public String getDoorName() {
        return doorName;
    }

    public void setDoorName(String doorName) {
        this.doorName = doorName;
    }

    public String getLockStatus() {
        return lockStatus;
    }

    public void setLockStatus(String lockStatus) {
        this.lockStatus = lockStatus;
    }

    public int getBatteryStatus() {
        return batteryStatus;
    }

    public void setBatteryStatus(int batteryStatus) {
        this.batteryStatus = batteryStatus;
    }

    public String getMechanicalStatus() {
        return mechanicalStatus;
    }

    public void setMechanicalStatus(String mechanicalStatus) {
        this.mechanicalStatus = mechanicalStatus;
    }
}
