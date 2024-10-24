package no.hiof.set.g6.dt.ny;


import org.json.simple.JSONObject;

/**
 * @author Mahmad
 */


public class LocalUser extends G6Datatype<LocalUser> {

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

    @Override
    public void clearFields() {
        accountID = NULL;
        userName = NULL_STRING;
        role = Role.NONE;
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
            accountID = o.accountID;
            userName = o.userName;
            role = o.role;
        }
    }

    // Method to convert JSON to LocalUser object
    @Override
    public void fromJson(JSONObject jsonObject) throws Exception {
        if (jsonObject == null) throw new Exception("JSONObject is null");

        Object accountIDObject = jsonObject.get(JSON_KEY_USER_ACCOUNT);
        Object userNameObject = jsonObject.get(JSON_KEY_USER_NAME);
        Object roleObject = jsonObject.get(JSON_KEY_ROLE);

        if (JsonUtils.anyObjectIsNull(accountIDObject, userNameObject, roleObject))
            throw new Exception("JSON to LocalUser: Missing one or more fields");

        try {
            Integer accountID = (Integer) accountIDObject;
            String userName = (String) userNameObject;
            Integer roleOrdinal = (Integer) roleObject;

            this.accountID = accountID;
            this.userName = userName;
            this.role = Role.getByOrdinal(roleOrdinal);

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
            jsonObject.put(JSON_KEY_USER_ACCOUNT, accountID);
            jsonObject.put(JSON_KEY_USER_NAME, userName);
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
