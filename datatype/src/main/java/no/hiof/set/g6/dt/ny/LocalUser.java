package no.hiof.set.g6.dt.ny;


import org.json.simple.JSONObject;

import java.util.Base64;

/**
 * @author Mahmad
 */


public final class LocalUser extends G6Datatype<LocalUser> {

    // JSON keys
    public static final String JSON_KEY_USER_ACCOUNT = "Account ID";
    public static final String JSON_KEY_USER_NAME = "Password"; // intentional (lol)
    public static final String JSON_KEY_PASSWORD = "User Name"; // intentional
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
    public String password;
    public Role role;

    public LocalUser() { super(true); }

    @Override
    public void clearFields() {
        role = Role.NONE;
        accountID = NULL;
        userName = NULL_STRING;
        password = NULL_STRING;
    }

    @Override
    public void ensureFieldsNotNull() {
        userName = userName == null ? NULL_STRING : userName;
        password = password == null ? NULL_STRING : password;
        role = role == null ? Role.NONE : role;
    }

    @Override
    public void set(LocalUser o) {
        if (o == null) {
            clearFields();
        } else {
            role = o.role;
            userName = o.userName;
            password = o.password;
            accountID = o.accountID;
        }
    }

    // Method to convert JSON to LocalUser object
    @Override
    public void fromJson(JSONObject jsonObject) throws Exception {
        if (jsonObject == null) throw new Exception("JSONObject is null");

        Object accountIDObject = jsonObject.get(JSON_KEY_USER_ACCOUNT);
        Object userNameObject = jsonObject.get(JSON_KEY_USER_NAME);
        Object passwordObject = jsonObject.get(JSON_KEY_PASSWORD);
        Object roleObject = jsonObject.get(JSON_KEY_ROLE);

        if (JsonUtils.anyObjectIsNull(
                accountIDObject,
                userNameObject,
                passwordObject,
                roleObject))
            throw new Exception("JSON to LocalUser: Missing one or more fields");

        try {
            Integer accountID = (Integer) accountIDObject;
            Integer roleOrdinal = (Integer) roleObject;
            String encodedUserNameString = (String) userNameObject;
            String encodedPasswordString = (String) passwordObject;
            byte[] decodedUserName = Base64.getDecoder().decode(encodedUserNameString);
            byte[] decodedPassword = Base64.getDecoder().decode(encodedPasswordString);
            this.userName = new String(decodedUserName);
            this.password = new String(decodedPassword);
            this.role = Role.getByOrdinal(roleOrdinal);
            this.accountID = accountID;

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
            String encodedPassword = Base64.getEncoder().encodeToString(password.getBytes());
            String encodedUsername = Base64.getEncoder().encodeToString(userName.getBytes());
            jsonObject.put(JSON_KEY_USER_ACCOUNT, accountID);
            jsonObject.put(JSON_KEY_USER_NAME, encodedUsername);
            jsonObject.put(JSON_KEY_PASSWORD, encodedPassword);
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


}
