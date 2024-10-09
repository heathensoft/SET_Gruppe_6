package no.hiof.set.g6.dtdb;

import java.sql.Timestamp;

public class Hubs {
    private int hubId;
    private String hubName;
    private Owners owner;  // Foreign key reference to Owners
    private java.sql.Timestamp installationDate;
    private String status;

    public Hubs(int hubId, String hubName, Owners owner, java.sql.Timestamp installationDate, String status) {
        this.hubId = hubId;
        this.hubName = hubName;
        this.owner = owner;
        this.installationDate = installationDate;
        this.status = status;
    }

    public Hubs() {}

    // Har generert gettere og settere
    public int getHubId() {
        return hubId;
    }

    public void setHubId(int hubId) {
        this.hubId = hubId;
    }

    public String getHubName() {
        return hubName;
    }

    public void setHubName(String hubName) {
        this.hubName = hubName;
    }

    public Owners getOwner() {
        return owner;
    }

    public void setOwner(Owners owner) {
        this.owner = owner;
    }

    public Timestamp getInstallationDate() {
        return installationDate;
    }

    public void setInstallationDate(Timestamp installationDate) {
        this.installationDate = installationDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
