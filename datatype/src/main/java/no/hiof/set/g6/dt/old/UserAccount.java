package no.hiof.set.g6.dt.old;

import org.json.simple.JSONObject;

/**
 * @author Mahmad
 */


public class UserAccount extends G6Datatype<UserAccount> {
    
    public static final String JSON_KEY_ID = "Account ID";
    public static final String JSON_KEY_FIRST_NAME = "First Name";
    public static final String JSON_KEY_LAST_NAME = "Last Name";
    public static final String JSON_KEY_EMAIL = "Email";
    public static final String JSON_KEY_PHONE_NUMBER = "Phone Number";
    public static final String JSON_KEY_ADDRESS = "Home Address";
    
    public int accountID;               // corresponds to account_id INT
    public String firstName;            // corresponds to first_name VARCHAR(100)
    public String lastName;             // corresponds to last_name VARCHAR(100)
    public String email;                // corresponds to email VARCHAR(150)
    public String phoneNumber;          //
    private final HomeAddress address;  // corresponds to address_id INT (Foreign Key)

    public UserAccount(String firstName, String lastName, String email, String phoneNumber) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.address = new HomeAddress(); // Make sure address is initialized with an empty HomeAddress object
    }

    // Constructors, Getters, and Setters
    public UserAccount() {
        this.firstName = "";
        this.lastName = "";
        this.email = "";
        this.phoneNumber = "";
        this.address = new HomeAddress();
    }

    @Override
    public boolean missingFields() {
        if (!JsonUtils.anyObjectIsNull(firstName,lastName,email,phoneNumber,address)) {
            return address.missingFields();
        } return true;
    }

    @Override
    public void set(UserAccount other) {
        if (other == null) {
            firstName = "null";
            lastName = "null";
            email = "null";
            phoneNumber = "null";
            address.set(null);
        } else {
            firstName = other.firstName;
            lastName = other.lastName;
            email = other.email;
            phoneNumber = other.phoneNumber;
            address.set(other.address);
        }
    }

    public HomeAddress getAddress() { return address; }
    
    
    @Override
    public void fromJson(JSONObject jsonObject) throws Exception {
        if (jsonObject == null) throw new Exception("JSONObject is null");
        Object accountIDObject = jsonObject.get(JSON_KEY_ID);
        Object firstNameObject = jsonObject.get(JSON_KEY_FIRST_NAME);
        Object lastNameObject = jsonObject.get(JSON_KEY_LAST_NAME);
        Object emailObject = jsonObject.get(JSON_KEY_EMAIL);
        Object phoneNumberObject = jsonObject.get(JSON_KEY_PHONE_NUMBER);
        Object addressObject = jsonObject.get(JSON_KEY_ADDRESS);
        
        if (JsonUtils.anyObjectIsNull(
                accountIDObject,
                firstNameObject,
                lastNameObject,
                emailObject,
                addressObject,
                phoneNumberObject
        )) throw new Exception("JSON to UserAccount: Missing one or more fields");
        try {
            Integer accountID = (Integer) accountIDObject;
            String firstName = (String) firstNameObject;
            String lastName = (String) lastNameObject;
            String email = (String) emailObject;
            String phoneNumber = (String) phoneNumberObject;
            JSONObject homeAddress = (JSONObject) addressObject;
            
            this.accountID = accountID;
            this.firstName = firstName;
            this.lastName = lastName;
            this.email = email;
            this.phoneNumber = phoneNumber;
            this.address.fromJson(homeAddress);
            
        } catch (ClassCastException e) {
            throw new Exception("JSON to UserAccount: Invalid format for one or more fields",e);
        }
    }
    
    @Override
    @SuppressWarnings("unchecked")
    public JSONObject toJson() {
        String firstName = this.firstName == null ? "null" : this.firstName;
        String lastName = this.lastName == null ? "null" : this.lastName;
        String email = this.email == null ? "null" : this.email;
        String phoneNumber = this.phoneNumber == null ? "null" : this.phoneNumber;
        HomeAddress address = this.address == null ? new HomeAddress() : this.address;
        JSONObject jsonObject = new JSONObject(); {
            jsonObject.put(JSON_KEY_ID,accountID);
            jsonObject.put(JSON_KEY_FIRST_NAME,firstName);
            jsonObject.put(JSON_KEY_LAST_NAME,lastName);
            jsonObject.put(JSON_KEY_EMAIL,email);
            jsonObject.put(JSON_KEY_PHONE_NUMBER,phoneNumber);
            jsonObject.put(JSON_KEY_ADDRESS,address.toJson());
        } return jsonObject;
    }
    
    @Override
    public int compareTo(G6Datatype other) { // used to sort a list of accounts for display
        if (other instanceof UserAccount o) {
            int comp;
            UserAccount t = this;
            {
                String t_first_name = t.firstName == null ? "" : t.firstName;
                String o_first_name = o.firstName == null ? "" : o.firstName;
                comp = t_first_name.compareTo(o_first_name);
            }
            if (comp == 0) {
                String t_last_name = t.lastName == null ? "" : t.lastName;
                String o_last_name = o.lastName == null ? "" : o.lastName;
                comp = t_last_name.compareTo(o_last_name);
            }
            if (comp == 0) {
                comp = t.address.compareTo(o.address);
            }
            return comp;
        } return 0;
    }
}
