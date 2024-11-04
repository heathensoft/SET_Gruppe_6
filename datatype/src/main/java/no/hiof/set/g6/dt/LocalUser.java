package no.hiof.set.g6.dt;


import org.json.simple.JSONObject;

import java.util.Base64;

/**
 * @author Mahmad
 */


public final class LocalUser extends G6Datatype<LocalUser> {

    // JSON keys
    public static final String JSON_KEY_USER_ACCOUNT = "Account ID";
    public static final String JSON_KEY_USER_NAME = "User Name";
    public static final String JSON_KEY_ROLE = "Role";

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


    public int accountID;
    public String userName;
    public Role role;

    public LocalUser() { super(true); }

    public boolean hasRequiredRole(Role required_role) {
        if (required_role == null) throw new IllegalStateException("null arg. role");
        role = role == null ? Role.NONE : role;
        return role.ordinal() >= required_role.ordinal();
    }

    @Override
    public void clearFields() {
        role = Role.NONE;
        accountID = NULL;
        userName = NULL_STRING;
    }

    @Override
    public void ensureFieldsNotNull() {
        userName = userName == null ? NULL_STRING : userName;
        role = role == null ? Role.NONE : role;
    }

    @Override
    public void set(LocalUser o) {
        if (o == null) {
            clearFields();
        } else {
            role = o.role;
            userName = o.userName;
            accountID = o.accountID;
        }
    }

    // Method to convert JSON to LocalUser object
    @Override
    public void fromJson(JSONObject jsonObject) throws Exception {
        if (jsonObject == null) throw new Exception("JSONObject is null");

        Object accountIDObject = jsonObject.get(JSON_KEY_USER_ACCOUNT);
        Object userNameObject = jsonObject.get(JSON_KEY_USER_NAME);
        Object roleObject = jsonObject.get(JSON_KEY_ROLE);

        if (JsonUtils.anyObjectIsNull(
                accountIDObject,
                userNameObject,
                roleObject))
            throw new Exception("JSON to LocalUser: Missing one or more fields");

        try {
            Number accountID = (Number) accountIDObject;
            Number roleOrdinal = (Number) roleObject;
            String encodedUserNameString = (String) userNameObject;
            byte[] decodedUserName = Base64.getDecoder().decode(encodedUserNameString);
            this.userName = new String(decodedUserName);
            this.role = Role.getByOrdinal(roleOrdinal.intValue());
            this.accountID = accountID.intValue();

            ensureFieldsNotNull();
        } catch (ClassCastException e) {
            throw new Exception("JSON to LocalUser: Invalid format for one or more fields", e);
        }
    }

    // Method to convert LocalUser object to JSON
    @Override
    @SuppressWarnings("unchecked")
    public JSONObject toJson() {
        ensureFieldsNotNull();
        JSONObject jsonObject = new JSONObject(); {
            String encodedUsername = Base64.getEncoder().encodeToString(userName.getBytes());
            jsonObject.put(JSON_KEY_USER_ACCOUNT, accountID);
            jsonObject.put(JSON_KEY_USER_NAME, encodedUsername);
            jsonObject.put(JSON_KEY_ROLE, role.ordinal());
        } return jsonObject;
    }

    // Method to compare LocalUser objects
    @Override
    public int compareTo(LocalUser o) {
        if (o != null) {
            int comp;
            LocalUser t = this;
            {   // Sort by Role first (owner -> none)
                Role t_role = t.role == null ? Role.NONE : t.role;
                Role o_role = o.role == null ? Role.NONE : o.role;
                comp = Integer.compare(o_role.ordinal(),t_role.ordinal());
            } // Then sort by name
            if (comp == 0){
                String t_username = t.userName == null ? NULL_STRING : t.userName;
                String o_username = o.userName == null ? NULL_STRING : o.userName;
                comp = t_username.compareTo(o_username);
            } return comp;
        } return 0;
    }

    @Override
    public String toString() {
        return "User {" +
                " account = " + accountID +
                ", username = '" + userName + '\'' +
                ", role = " + role +
                '}';
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof LocalUser user) {
            return accountID == user.accountID;
        } return false;
    }
}
