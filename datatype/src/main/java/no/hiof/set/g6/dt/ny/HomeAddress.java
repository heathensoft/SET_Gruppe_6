package no.hiof.set.g6.dt.ny;

import org.json.simple.JSONObject;

/**
 * @author Mahmad
 */

public final class HomeAddress extends G6Datatype<HomeAddress> {

    public static final String JSON_KEY_ID = "Address ID";
    public static final String JSON_KEY_COUNTRY = "Country";
    public static final String JSON_KEY_STATE = "State";
    public static final String JSON_KEY_CITY = "City";
    public static final String JSON_KEY_STREET = "Street Address";
    public static final String JSON_KEY_POSTAL_CODE = "Postal Code";

    public int id;          // corresponds to address_id INT
    public String country;  // corresponds to country VARCHAR(100)
    public String state;    // corresponds to state VARCHAR(100)
    public String city;     // corresponds to city VARCHAR(100)
    public String street;   // corresponds to street_address VARCHAR(255)
    public int postalCode;  // corresponds to postal_code INT


    public HomeAddress() { super(true); }

    @Override
    public void clearFields() {
        this.country = NULL_STRING;
        this.state = NULL_STRING;
        this.city = NULL_STRING;
        this.street = NULL_STRING;
        this.postalCode = NULL;
        this.id = NULL;
    }

    @Override
    public void ensureFieldsNotNull() {
        country = country == null ? NULL_STRING : country;
        state = state == null ? NULL_STRING : state;
        city = city == null ? NULL_STRING : city;
        street = street == null ? NULL_STRING : street;
    }

    public void set(HomeAddress o) {
        if (o == null) {
            clearFields();
        } else {
            this.country = o.country;
            this.state = o.state;
            this.city = o.city;
            this.street = o.street;
            this.postalCode = o.postalCode;
            this.id = o.id;
        }
    }

    @Override
    public void fromJson(JSONObject jsonObject) throws Exception {
        if (jsonObject == null) throw new Exception("JSONObject is null");
        Object addressIDObject = jsonObject.get(JSON_KEY_ID);
        Object countryObject = jsonObject.get(JSON_KEY_COUNTRY);
        Object stateObject = jsonObject.get(JSON_KEY_STATE);
        Object cityObject = jsonObject.get(JSON_KEY_CITY);
        Object streetObject = jsonObject.get(JSON_KEY_STREET);
        Object postalCodeObject = jsonObject.get(JSON_KEY_POSTAL_CODE);
        if (JsonUtils.anyObjectIsNull(
                addressIDObject,
                countryObject,
                stateObject,
                cityObject,
                streetObject,
                postalCodeObject
        )) throw new Exception("JSON to HomeAddress: Missing one or more fields");
        try {
            Integer addressID = (Integer) addressIDObject;
            String country = (String) countryObject;
            String state = (String) stateObject;
            String city = (String) cityObject;
            String street = (String) streetObject;
            Integer postalCode = (Integer) postalCodeObject;

            this.id = addressID;
            this.country = country;
            this.state = state;
            this.city = city;
            this.street = street;
            this.postalCode = postalCode;

            ensureFieldsNotNull();
        } catch (ClassCastException e) {
            throw new Exception("JSON to HomeAddress: Invalid format for one or more fields", e);
        }
    }

    @Override
    @SuppressWarnings("unchecked")
    public JSONObject toJson() {
        ensureFieldsNotNull();
        JSONObject jsonObject = new JSONObject(); {
            jsonObject.put(JSON_KEY_ID, id);
            jsonObject.put(JSON_KEY_COUNTRY, country);
            jsonObject.put(JSON_KEY_STATE, state);
            jsonObject.put(JSON_KEY_CITY, city);
            jsonObject.put(JSON_KEY_STREET, street);
            jsonObject.put(JSON_KEY_POSTAL_CODE, postalCode);
        } return jsonObject;
    }

    @Override
    public int compareTo(HomeAddress o) {
        if (o != null) {
            int comp;
            HomeAddress t = this;
            {
                String t_country = t.country == null ? "" : t.country;
                String o_country = o.country == null ? "" : o.country;
                comp = t_country.compareTo(o_country);
            }
            if (comp == 0) {
                String t_state = t.state == null ? "" : t.state;
                String o_state = o.state == null ? "" : o.state;
                comp = t_state.compareTo(o_state);
            }
            if (comp == 0) {
                String t_city = t.city == null ? "" : t.city;
                String o_city = o.city == null ? "" : o.city;
                comp = t_city.compareTo(o_city);
            }
            return comp;
        }
        return 0;
    }
}
