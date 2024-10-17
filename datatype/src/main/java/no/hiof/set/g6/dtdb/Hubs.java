package no.hiof.set.g6.dtdb;

import java.sql.Timestamp;

public class Hubs {
    private int hubId;                 // corresponds to hub_id INT
    private String hubName;            // corresponds to hub_name VARCHAR(100)
    private Timestamp installationDate; // corresponds to installation_date DATETIME
    private String status;             // corresponds to status ENUM('Active', 'Inactive')

    // Constructors, Getters, and Setters
    public Hubs(int hubId, String hubName, Timestamp installationDate, String status) {
        this.hubId = hubId;
        this.hubName = hubName;
        this.installationDate = installationDate;
        this.status = status;
    }

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
