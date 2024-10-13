package no.hiof.set.g6.dtdb;

import java.sql.Timestamp;

public class Owners implements G6DataType {
    private int ownerId;         // corresponds to owner_id INT
    private LocalUser localUser; // corresponds to local_user_id INT (Foreign Key)
    private Timestamp purchaseDate; // corresponds to purchase_date DATETIME

    // Constructors, Getters, and Setters
    public Owners(int ownerId, LocalUser localUser, Timestamp purchaseDate) {
        this.ownerId = ownerId;
        this.localUser = localUser;
        this.purchaseDate = purchaseDate;
    }

    public int getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(int ownerId) {
        this.ownerId = ownerId;
    }

    public LocalUser getLocalUser() {
        return localUser;
    }

    public void setLocalUser(LocalUser localUser) {
        this.localUser = localUser;
    }

    public Timestamp getPurchaseDate() {
        return purchaseDate;
    }

    public void setPurchaseDate(Timestamp purchaseDate) {
        this.purchaseDate = purchaseDate;
    }
}
