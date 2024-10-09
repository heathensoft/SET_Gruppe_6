package no.hiof.set.g6.dtdb;

import java.sql.Timestamp;

public class ErrorLog {
    private int errorId;
    private Hubs hub;     // Foreign key reference to Hubs
    private Locks lock;   //Foreign key reference to Locks
    private String errorType;
    private java.sql.Timestamp timestamp;

    public ErrorLog(int errorId, Hubs hub, Locks lock, String errorType, java.sql.Timestamp timestamp) {
        this.errorId = errorId;
        this.hub = hub;
        this.lock = lock;
        this.errorType = errorType;
        this.timestamp = timestamp;
    }

    public ErrorLog() {}

    //Har generert gettere og settere
    public int getErrorId() {
        return errorId;
    }

    public void setErrorId(int errorId) {
        this.errorId = errorId;
    }

    public Hubs getHub() {
        return hub;
    }

    public void setHub(Hubs hub) {
        this.hub = hub;
    }

    public Locks getLock() {
        return lock;
    }

    public void setLock(Locks lock) {
        this.lock = lock;
    }

    public String getErrorType() {
        return errorType;
    }

    public void setErrorType(String errorType) {
        this.errorType = errorType;
    }

    public Timestamp getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Timestamp timestamp) {
        this.timestamp = timestamp;
    }
}
