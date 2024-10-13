package no.hiof.set.g6.dtdb;

import java.sql.Timestamp;

public class TemporaryKeys implements G6DataType {
    private int keyId;                // corresponds to key_id INT
    private LocalUser sender;         // corresponds to sender_local_user_id INT (Foreign Key)
    private LocalUser recipient;      // corresponds to recipient_local_user_id INT (Foreign Key)
    private Locks locks;                // corresponds to lock_id INT (Foreign Key)
    private String qrCode;            // corresponds to qr_code VARCHAR(255)
    private Timestamp validFrom;      // corresponds to valid_from DATETIME
    private Timestamp validUntil;     // corresponds to valid_until DATETIME

    // Constructors, Getters, and Setters
    public TemporaryKeys(int keyId, LocalUser sender, LocalUser recipient, Locks locks, String qrCode, Timestamp validFrom, Timestamp validUntil) {
        this.keyId = keyId;
        this.sender = sender;
        this.recipient = recipient;
        this.locks = locks;
        this.qrCode = qrCode;
        this.validFrom = validFrom;
        this.validUntil = validUntil;
    }

    public int getKeyId() {
        return keyId;
    }

    public void setKeyId(int keyId) {
        this.keyId = keyId;
    }

    public LocalUser getSender() {
        return sender;
    }

    public void setSender(LocalUser sender) {
        this.sender = sender;
    }

    public LocalUser getRecipient() {
        return recipient;
    }

    public void setRecipient(LocalUser recipient) {
        this.recipient = recipient;
    }

    public Locks getLock() {
        return locks;
    }

    public void setLock(Locks locks) {
        this.locks = locks;
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
