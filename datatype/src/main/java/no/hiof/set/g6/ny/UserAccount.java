package no.hiof.set.g6.ny;

import org.json.simple.JSONObject;

/**
 * @author Frederik Dahl
 * 16/10/2024
 */


public class UserAccount extends G6Datatype {
    
    public static final String JSON_KEY_FIRST_NAME = "First Name";
    public static final String JSON_KEY_LAST_NAME = "Last Name";
    public static final String JSON_KEY_EMAIL = "Email";
    public static final String JSON_KEY_PHONE_NUMBER = "Phone Number";
    public static final String JSON_KEY_ADDRESS = "Home Address";
    
    public String firstName;
    public String lastName;
    public String email;
    public String phoneNumber;
    private final HomeAddress address;
    
    public UserAccount(String firstName, String lastName, String email, String phoneNumber) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.address = new HomeAddress();
    }
    
    public UserAccount() {
        this.firstName = "";
        this.lastName = "";
        this.email = "";
        this.phoneNumber = "";
        this.address = new HomeAddress();
    }
    
    public HomeAddress getAddress() { return address; }
    
    
    @Override
    public void fromJson(JSONObject jsonObject) throws Exception {
        if (jsonObject == null) throw new Exception("JSONObject is null");
        Object firstNameObject = jsonObject.get(JSON_KEY_FIRST_NAME);
        Object lastNameObject = jsonObject.get(JSON_KEY_LAST_NAME);
        Object emailObject = jsonObject.get(JSON_KEY_EMAIL);
        Object phoneNumberObject = jsonObject.get(JSON_KEY_PHONE_NUMBER);
        Object addressObject = jsonObject.get(JSON_KEY_ADDRESS);
        
        if (JsonUtils.anyObjectIsNull(
                firstNameObject,
                lastNameObject,
                emailObject,
                addressObject,
                phoneNumberObject
        )) throw new Exception("JSON to UserAccount: Missing one or more fields");
        try {
            String firstName = (String) firstNameObject;
            String lastName = (String) lastNameObject;
            String email = (String) emailObject;
            String phoneNumber = (String) phoneNumberObject;
            JSONObject homeAddress = (JSONObject) addressObject;
            
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
