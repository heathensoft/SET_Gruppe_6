package no.hiof.set.g6.ny;

import org.json.simple.JSONObject;

public class LocalUser extends G6Datatype {

    // ENUM for role in the system
    public enum Role {
        NONE("None"),
        GUEST("Guest"),
        RESIDENT("Resident"),
        OWNER("Owner");
        Role(String descriptor) { this.descriptor = descriptor; }
        public final String descriptor;
        private static final Role[] all;
        static { all = values(); }
        public static Role getByOrdinal(int ordinal) {
            if (ordinal < all.length && ordinal > 0) {
                return all[ordinal];
            } return null;
        }
    }

    // JSON keys
    public static final String JSON_KEY_USER_ACCOUNT = "User Account";
    public static final String JSON_KEY_USER_NAME = "User Name";
    public static final String JSON_KEY_ROLE = "Role";

    private UserAccount userAccount;
    private String userName;
    private Role role;

    // Constructors
    public LocalUser(UserAccount userAccount, String userName, Role role) {
        this.userAccount = userAccount;
        this.userName = userName;
        this.role = role;
    }

    public LocalUser() {
        this.userAccount = new UserAccount();
        this.userName = "";
        this.role = Role.GUEST;  // Default role
    }

    // Getters
    public UserAccount getUserAccount() { return userAccount; }
    public String getUserName() { return userName; }
    public Role getRole() { return role; }

    // Setters
    public void setUserAccount(UserAccount userAccount) { this.userAccount = userAccount; }
    public void setUserName(String userName) { this.userName = userName; }
    public void setRole(Role role) { this.role = role; }

    // Method to convert JSON to LocalUser object
    @Override
    public void fromJson(JSONObject jsonObject) throws Exception {
        if (jsonObject == null) throw new Exception("JSONObject is null");

        Object userAccountObject = jsonObject.get(JSON_KEY_USER_ACCOUNT);
        Object userNameObject = jsonObject.get(JSON_KEY_USER_NAME);
        Object roleObject = jsonObject.get(JSON_KEY_ROLE);

        if (JsonUtils.anyObjectIsNull(userAccountObject, userNameObject, roleObject))
            throw new Exception("JSON to LocalUser: Missing one or more fields");

        try {
            JSONObject userAccountJson = (JSONObject) userAccountObject;
            String userName = (String) userNameObject;
            String roleString = (String) roleObject;

            this.userAccount.fromJson(userAccountJson);
            this.userName = userName;
            this.role = Role.valueOf(roleString.toUpperCase());
        } catch (ClassCastException | IllegalArgumentException e) {
            throw new Exception("JSON to LocalUser: Invalid format for one or more fields", e);
        }
    }

    // Method to convert LocalUser object to JSON
    @Override
    @SuppressWarnings("unchecked")
    public JSONObject toJson() {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put(JSON_KEY_USER_ACCOUNT, userAccount.toJson());
        jsonObject.put(JSON_KEY_USER_NAME, userName == null ? "null" : userName);
        jsonObject.put(JSON_KEY_ROLE, role == null ? Role.GUEST.name() : role.name());
        return jsonObject;
    }

    // Method to compare LocalUser objects
    @Override
    public int compareTo(G6Datatype other) {
        if (other instanceof LocalUser o) {
            int comp;
            LocalUser t = this;

            comp = t.userName.compareTo(o.userName == null ? "" : o.userName);
            if (comp == 0) {
                comp = t.role.compareTo(o.role);
            }
            if (comp == 0) {
                comp = t.userAccount.compareTo(o.userAccount);
            }
            return comp;
        }
        return 0;
    }
}
