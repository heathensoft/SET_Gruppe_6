package no.hiof.set.g6.dtdb;

public class LocalUser {

    // ENUM for role in the system
    public enum Role {
        OWNER, RESIDENT, GUEST
    }

    private int localUserId;     // corresponds to local_user_id INT
    private UserAccount userAccount;  // corresponds to account_id INT (Foreign Key)
    private Hubs hubs;             // corresponds to hub_id INT (Foreign Key)
    private String userName;     // corresponds to user_name VARCHAR(100)
    private Role role;           // corresponds to role ENUM('Owner', 'Resident', 'Guest')

    // Constructors, Getters, and Setters
    public LocalUser(int localUserId, UserAccount userAccount, Hubs hubs, String userName, Role role) {
        this.localUserId = localUserId;
        this.userAccount = userAccount;
        this.hubs = hubs;
        this.userName = userName;
        this.role = role;
    }

    public int getLocalUserId() {
        return localUserId;
    }

    public void setLocalUserId(int localUserId) {
        this.localUserId = localUserId;
    }

    public UserAccount getUserAccount() {
        return userAccount;
    }

    public void setUserAccount(UserAccount userAccount) {
        this.userAccount = userAccount;
    }

    public Hubs getHub() {
        return hubs;
    }

    public void setHub(Hubs hubs) {
        this.hubs = hubs;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }
}
