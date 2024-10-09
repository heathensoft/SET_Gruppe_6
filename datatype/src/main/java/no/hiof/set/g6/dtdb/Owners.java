package no.hiof.set.g6.dtdb;

import java.sql.Timestamp;

public class Owners {
    private int ownerId;
    private Users user; // Foreign key reference to Users
    private Hubs hub;   // Foreign key reference to Hubs
    private java.sql.Timestamp purchaseDate;

    public Owners(int ownerId, Users user, Hubs hub, java.sql.Timestamp purchaseDate) {
        this.ownerId = ownerId;
        this.user = user;
        this.hub = hub;
        this.purchaseDate = purchaseDate;
    }

    public Owners() {}

    //Har generert gettere og settere
    public int getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(int ownerId) {
        this.ownerId = ownerId;
    }

    public Users getUser() {
        return user;
    }

    public void setUser(Users user) {
        this.user = user;
    }

    public Hubs getHub() {
        return hub;
    }

    public void setHub(Hubs hub) {
        this.hub = hub;
    }

    public Timestamp getPurchaseDate() {
        return purchaseDate;
    }

    public void setPurchaseDate(Timestamp purchaseDate) {
        this.purchaseDate = purchaseDate;
    }
}

