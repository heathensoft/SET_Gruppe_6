package no.hiof.set.g6.ny;


import org.json.simple.JSONObject;

/**
 * @author Mahmad
 */


public class LocalUser extends G6Datatype {
    
    public static final String JSON_KEY_ID = "User ID";
    public static final String JSON_KEY_USER_NAME = "User Name";
    public static final String JSON_KEY_USER_ROLE = "User Role";
    public static final String JSON_KEY_USER_ACCOUNT = "User Account";
    
    // ENUM for role in the system
    public enum Role {
        
        OWNER("Owner"),
        RESIDENT("Resident"),
        GUEST("Guest"),
        NONE("None");
        
        Role(String descriptor) {
            this.descriptor = descriptor;
        }
        public final String descriptor;
        private static final Role[] all;
        static { all = values(); }
        public static Role getByOrdinal(int ordinal) {
            if (ordinal < all.length && ordinal > 0) {
                return all[ordinal];
            } return null;
        }
    }
    
    private final UserAccount account;  // corresponds to account_id INT (Foreign Key)
    public String userName;             // corresponds to user_name VARCHAR(100)
    public Role role;                   // corresponds to role ENUM('Owner', 'Resident', 'Guest')
    public int userID;                  // corresponds to local_user_id INT
    
    // Constructors, Getters, and Setters
    public LocalUser() {
        this.account = new UserAccount();
        this.role = Role.NONE;
        this.userName = "";
    }
    
    public UserAccount getAccount() { return account; }
    
    @Override
    public void fromJson(JSONObject jsonObject) throws Exception {
        if (jsonObject == null) throw new Exception("JSONObject is null");
        Object userIDObject = jsonObject.get(JSON_KEY_ID);
        Object roleObject = jsonObject.get(JSON_KEY_USER_ROLE);
        Object userNameObject = jsonObject.get(JSON_KEY_USER_NAME);
        Object accountObject = jsonObject.get(JSON_KEY_USER_ACCOUNT);
        if (JsonUtils.anyObjectIsNull(
                userIDObject,
                roleObject,
                accountObject,
                userNameObject
        )) throw new Exception("JSON to LocalUser: Missing one or more fields");
        try {
            
            Integer userID = (Integer) userIDObject;
            String userName = (String) userNameObject;
            Role userRole = Role.getByOrdinal((Integer) roleObject);
            JSONObject jsonAccount = (JSONObject) accountObject;
            
            this.account.fromJson(jsonAccount);
            this.userName = userName;
            this.role = userRole;
            this.userID = userID;
            
        } catch (ClassCastException e) {
            throw new Exception("JSON to LocalUser: Invalid format for one or more fields",e);
        }
    }
    
    @Override
    @SuppressWarnings("unchecked")
    public JSONObject toJson() {
        UserAccount account = this.account;
        Role role = this.role == null ? Role.NONE : this.role;
        String userName = this.userName == null ? "null" : this.userName;
        JSONObject jsonObject = new JSONObject(); {
            jsonObject.put(JSON_KEY_ID,userID);
            jsonObject.put(JSON_KEY_USER_NAME,userName);
            jsonObject.put(JSON_KEY_USER_ROLE,role.ordinal());
            jsonObject.put(JSON_KEY_USER_ACCOUNT,account.toJson());
        } return jsonObject;
    }
    
    // Just for sorting purposes
    
    @Override
    public int compareTo(G6Datatype other) {
        if (other instanceof LocalUser o) {
            int comp;
            LocalUser t = this;
            {
                int t_role = t.role == null ? Role.NONE.ordinal() : t.role.ordinal();
                int o_role = o.role == null ? Role.NONE.ordinal() : o.role.ordinal();
                comp = Integer.compare(t_role,o_role);
            }
            if (comp == 0) {
                String t_last_name = t.userName == null ? "" : t.userName;
                String o_last_name = o.userName == null ? "" : o.userName;
                comp = t_last_name.compareTo(o_last_name);
            }
            if (comp == 0) {
                comp = t.account.compareTo(o.account);
            }
            return comp;
        } return 0;
    }
}
