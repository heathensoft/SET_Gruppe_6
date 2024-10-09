package no.hiof.set.g6.dtdb;

import java.sql.Timestamp;

public class AccessLog {
    private int logId;
    private Users user;  // Foreign key reference to Users
    private Locks lock;  // Foreign key reference to Locks
    private String action;
    private java.sql.Timestamp timestamp;

    public AccessLog(int logId, Users user, Locks lock, String action, java.sql.Timestamp timestamp) {
        this.logId = logId;
        this.user = user;
        this.lock = lock;
        this.action = action;
        this.timestamp = timestamp;
    }


    //Har generert gettere og settere
    public AccessLog() {}

    public int getLogId() {
        return logId;
    }

    public void setLogId(int logId) {
        this.logId = logId;
    }

    public Users getUser() {
        return user;
    }

    public void setUser(Users user) {
        this.user = user;
    }

    public Locks getLock() {
        return lock;
    }

    public void setLock(Locks lock) {
        this.lock = lock;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public Timestamp getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Timestamp timestamp) {
        this.timestamp = timestamp;
    }
}
