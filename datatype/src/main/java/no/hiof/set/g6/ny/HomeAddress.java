package no.hiof.set.g6.ny;

import org.json.simple.JSONObject;

/**
 * @author Mahmad
 */

public class HomeAddress extends G6Datatype<HomeAddress> {

    public static final String JSON_KEY_ID = "Address ID";
    public static final String JSON_KEY_COUNTRY = "Country";
    public static final String JSON_KEY_STATE = "State";
    public static final String JSON_KEY_CITY = "City";
    public static final String JSON_KEY_STREET = "Street Address";
    public static final String JSON_KEY_POSTAL_CODE = "Postal Code";

    public int addressID;   // corresponds to address_id INT
    public String country;  // corresponds to country VARCHAR(100)
    public String state;    // corresponds to state VARCHAR(100)
    public String city;     // corresponds to city VARCHAR(100)
    public String street;   // corresponds to street_address VARCHAR(255)
    public int postalCode;  // corresponds to postal_code INT

    // Konstruktør som tar fem argumenter (country, state, city, street, postalCode)
    public HomeAddress(String country, String state, String city, String street, int postalCode) {
        this.country = country;
        this.state = state;
        this.city = city;
        this.street = street;
        this.postalCode = postalCode;
    }

    // Standard konstruktør uten argumenter
    public HomeAddress() {
        this.country = "";
        this.state = "";
        this.city = "";
        this.street = "";
    }

    @Override
    public boolean missingFields() {
        return JsonUtils.anyObjectIsNull(country,state,city,street);
    }

    public void set(HomeAddress address) {
        if (address == null) {
            this.country = "null";
            this.state = "null";
            this.city = "null";
            this.street = "null";
            this.postalCode = 0;
            this.addressID = 0;
        } else {
            this.country = address.country;
            this.state = address.state;
            this.city = address.city;
            this.street = address.street;
            this.postalCode = address.postalCode;
            this.addressID = address.addressID;
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

            this.addressID = addressID;
            this.country = country;
            this.state = state;
            this.city = city;
            this.street = street;
            this.postalCode = postalCode;
        } catch (ClassCastException e) {
            throw new Exception("JSON to HomeAddress: Invalid format for one or more fields", e);
        }
    }

    @Override
    @SuppressWarnings("unchecked")
    public JSONObject toJson() {
        String country = this.country == null ? "null" : this.country;
        String state = this.state == null ? "null" : this.state;
        String city = this.city == null ? "null" : this.city;
        String street = this.street == null ? "null" : this.street;
        int postalCode = this.postalCode;
        int addressID = this.addressID;
        JSONObject jsonObject = new JSONObject(); {
            jsonObject.put(JSON_KEY_ID, addressID);
            jsonObject.put(JSON_KEY_COUNTRY, country);
            jsonObject.put(JSON_KEY_STATE, state);
            jsonObject.put(JSON_KEY_CITY, city);
            jsonObject.put(JSON_KEY_STREET, street);
            jsonObject.put(JSON_KEY_POSTAL_CODE, postalCode);
        }
        return jsonObject;
    }

    // Bare for sorteringsformål
    @Override
    public int compareTo(G6Datatype other) {
        if (other instanceof HomeAddress o) {
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
