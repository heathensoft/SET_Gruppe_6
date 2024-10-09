package no.hiof.set.g6.dtdb;

import java.sql.Timestamp;

public class TemporaryKeys {
    private int keyId;
    private Users user;       // Foreign key reference to Users
    private Users recipient;  // Foreign key reference to Users
    private Locks lock;       // Foreign key reference to Locks
    private String qrCode;
    private java.sql.Timestamp validFrom;
    private java.sql.Timestamp validUntil;

    public TemporaryKeys(int keyId, Users user, Users recipient, Locks lock, String qrCode, java.sql.Timestamp validFrom, java.sql.Timestamp validUntil) {
        this.keyId = keyId;
        this.user = user;
        this.recipient = recipient;
        this.lock = lock;
        this.qrCode = qrCode;
        this.validFrom = validFrom;
        this.validUntil = validUntil;
    }

    public TemporaryKeys() {}

    //Har generert gettere og settere
    public int getKeyId() {
        return keyId;
    }

    public void setKeyId(int keyId) {
        this.keyId = keyId;
    }

    public Users getUser() {
        return user;
    }

    public void setUser(Users user) {
        this.user = user;
    }

    public Users getRecipient() {
        return recipient;
    }

    public void setRecipient(Users recipient) {
        this.recipient = recipient;
    }

    public Locks getLock() {
        return lock;
    }

    public void setLock(Locks lock) {
        this.lock = lock;
    }

    public String getQrCode() {
        return qrCode;
    }

    public void setQrCode(String qrCode) {
        this.qrCode = qrCode;
    }

    public Timestamp getValidFrom() {
        return validFrom;
    }

    public void setValidFrom(Timestamp validFrom) {
        this.validFrom = validFrom;
    }

    public Timestamp getValidUntil() {
        return validUntil;
    }

    public void setValidUntil(Timestamp validUntil) {
        this.validUntil = validUntil;
    }
}
