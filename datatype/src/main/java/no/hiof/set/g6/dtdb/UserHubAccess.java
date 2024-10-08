package no.hiof.set.g6.dtdb;

import java.sql.Timestamp;

public class UserHubAccess {
    private int accessId;
    private Hubs hub;   // Foreign key reference to Hubs
    private Users user; // Foreign key reference to Users
    private String accessRole;
    private java.sql.Timestamp accessStartTime;
    private java.sql.Timestamp accessEndTime;

    public UserHubAccess(int accessId, Hubs hub, Users user, String accessRole, java.sql.Timestamp accessStartTime, java.sql.Timestamp accessEndTime) {
        this.accessId = accessId;
        this.hub = hub;
        this.user = user;
        this.accessRole = accessRole;
        this.accessStartTime = accessStartTime;
        this.accessEndTime = accessEndTime;
    }

    public UserHubAccess() {}

    //Har generert gettere og settere..
    public int getAccessId() {
        return accessId;
    }

    public void setAccessId(int accessId) {
        this.accessId = accessId;
    }

    public Hubs getHub() {
        return hub;
    }

    public void setHub(Hubs hub) {
        this.hub = hub;
    }

    public Users getUser() {
        return user;
    }

    public void setUser(Users user) {
        this.user = user;
    }

    public String getAccessRole() {
        return accessRole;
    }

    public void setAccessRole(String accessRole) {
        this.accessRole = accessRole;
    }

    public Timestamp getAccessStartTime() {
        return accessStartTime;
    }

    public void setAccessStartTime(Timestamp accessStartTime) {
        this.accessStartTime = accessStartTime;
    }

    public Timestamp getAccessEndTime() {
        return accessEndTime;
    }

    public void setAccessEndTime(Timestamp accessEndTime) {
        this.accessEndTime = accessEndTime;
    }
}
