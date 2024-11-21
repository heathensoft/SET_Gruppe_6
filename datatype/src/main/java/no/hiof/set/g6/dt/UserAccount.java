package no.hiof.set.g6.dt;


import org.json.simple.JSONObject;

/**
 * @author Mahmad
 */


public final class UserAccount extends G6Datatype<UserAccount> {
    
    public static final String JSON_KEY_ID = "Account ID";
    public static final String JSON_KEY_FIRST_NAME = "First Name";
    public static final String JSON_KEY_LAST_NAME = "Last Name";
    public static final String JSON_KEY_EMAIL = "Email";
    public static final String JSON_KEY_PHONE_NUMBER = "Phone Number";
    public static final String JSON_KEY_ADDRESS = "Home Address";
    
    public int id;                      // corresponds to account_id INT
    public String firstName;            // corresponds to first_name VARCHAR(100)
    public String lastName;             // corresponds to last_name VARCHAR(100)
    public String email;                // corresponds to email VARCHAR(150)
    public String phoneNumber;          //

    public HomeAddress address;        // corresponds to address_id INT (Foreign Key)

    public UserAccount() { super(true); }



    @Override
    public void clearFields() {
        id = NULL;
        firstName = NULL_STRING;
        lastName = NULL_STRING;
        phoneNumber = NULL_STRING;
        email = NULL_STRING;
        if (address == null) {
            address = new HomeAddress();
        } else address.clearFields();
    }

    @Override
    public void ensureFieldsNotNull() {
        firstName = firstName == null ? NULL_STRING : firstName;
        lastName = lastName == null ? NULL_STRING : lastName;
        email = email == null ? NULL_STRING : email;
        phoneNumber = phoneNumber == null ? NULL_STRING : phoneNumber;
    }

    @Override
    public void set(UserAccount other) {
        if (other == null) {
            clearFields();
        } else {
            id = other.id;
            firstName = other.firstName;
            lastName = other.lastName;
            email = other.email;
            phoneNumber = other.phoneNumber;
            address.set(other.address);
        }
    }

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
            Number accountID = (Number) accountIDObject;
            String firstName = (String) firstNameObject;
            String lastName = (String) lastNameObject;
            String email = (String) emailObject;
            String phoneNumber = (String) phoneNumberObject;
            JSONObject homeAddress = (JSONObject) addressObject;
            
            this.id = accountID.intValue();
            this.firstName = firstName;
            this.lastName = lastName;
            this.email = email;
            this.phoneNumber = phoneNumber;
            this.address.fromJson(homeAddress);

            ensureFieldsNotNull();

        } catch (ClassCastException e) {
            throw new Exception("JSON to UserAccount: Invalid format for one or more fields",e);
        }
    }
    
    @Override
    @SuppressWarnings("unchecked")
    public JSONObject toJson() {
        ensureFieldsNotNull();
        JSONObject jsonObject = new JSONObject(); {
            jsonObject.put(JSON_KEY_ID, id);
            jsonObject.put(JSON_KEY_FIRST_NAME,firstName);
            jsonObject.put(JSON_KEY_LAST_NAME,lastName);
            jsonObject.put(JSON_KEY_EMAIL,email);
            jsonObject.put(JSON_KEY_PHONE_NUMBER,phoneNumber);
            jsonObject.put(JSON_KEY_ADDRESS,address.toJson());
        } return jsonObject;
    }
    
    @Override
    public int compareTo(UserAccount o) {
        if (o != null) {
            int comp;
            UserAccount t = this;
            {
                String t_first_name = t.firstName == null ? NULL_STRING : t.firstName;
                String o_first_name = o.firstName == null ? NULL_STRING : o.firstName;
                comp = t_first_name.compareTo(o_first_name);
            }
            if (comp == 0) {
                String t_last_name = t.lastName == null ? NULL_STRING : t.lastName;
                String o_last_name = o.lastName == null ? NULL_STRING : o.lastName;
                comp = t_last_name.compareTo(o_last_name);
            }
            return comp;
        } return 0;
    }
}
