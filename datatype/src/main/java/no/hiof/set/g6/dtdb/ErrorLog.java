package no.hiof.set.g6.dtdb;

import java.sql.Timestamp;

public class ErrorLog implements G6DataType {
    private int errorId;              // corresponds to error_id INT
    private Hubs hubs;                  // corresponds to hub_id INT (Foreign Key)
    private Locks locks;                // corresponds to lock_id INT (nullable Foreign Key)
    private String errorType;         // corresponds to error_type VARCHAR(255)
    private Timestamp timestamp;      // corresponds to timestamp DATETIME

    // Constructors, Getters, and Setters
    public ErrorLog(int errorId, Hubs hubs, Locks locks, String errorType, Timestamp timestamp) {
        this.errorId = errorId;
        this.hubs = hubs;
        this.locks = locks;
        this.errorType = errorType;
        this.timestamp = timestamp;
    }

    public int getErrorId() {
        return errorId;
    }

    public void setErrorId(int errorId) {
        this.errorId = errorId;
    }

    public Hubs getHub() {
        return hubs;
    }

    public void setHub(Hubs hubs) {
        this.hubs = hubs;
    }

    public Locks getLock() {
        return locks;
    }

    public void setLock(Locks locks) {
        this.locks = locks;
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
