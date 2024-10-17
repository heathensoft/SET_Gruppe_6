package no.hiof.set.g6.dtdb;

import java.sql.Timestamp;

public class AccessLog implements G6DataType {
    private int logId;                // corresponds to log_id INT
    private LocalUser localUser;      // corresponds to local_user_id INT (Foreign Key)
    private Locks locks;                // corresponds to lock_id INT (Foreign Key)
    private String action;            // corresponds to action ENUM('Locked', 'Unlocked')
    private Timestamp timestamp;      // corresponds to timestamp DATETIME

    // Constructors, Getters, and Setters
    public AccessLog(int logId, LocalUser localUser, Locks locks, String action, Timestamp timestamp) {
        this.logId = logId;
        this.localUser = localUser;
        this.locks = locks;
        this.action = action;
        this.timestamp = timestamp;
    }

    public int getLogId() {
        return logId;
    }

    public void setLogId(int logId) {
        this.logId = logId;
    }

    public LocalUser getLocalUser() {
        return localUser;
    }

    public void setLocalUser(LocalUser localUser) {
        this.localUser = localUser;
    }

    public Locks getLock() {
        return locks;
    }

    public void setLock(Locks locks) {
        this.locks = locks;
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
