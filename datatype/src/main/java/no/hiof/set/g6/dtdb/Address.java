package no.hiof.set.g6.dtdb;

public class Address implements G6DataType {
    private int addressId;      // corresponds to address_id INT
    private String country;     // corresponds to country VARCHAR(100)
    private String state;       // corresponds to state VARCHAR(100)
    private String city;        // corresponds to city VARCHAR(100)
    private String streetAddress; // corresponds to street_address VARCHAR(255)
    private int postalCode;     // corresponds to postal_code INT

    // Constructors, Getters, and Setters
    public Address(int addressId, String country, String state, String city, String streetAddress, int postalCode) {
        this.addressId = addressId;
        this.country = country;
        this.state = state;
        this.city = city;
        this.streetAddress = streetAddress;
        this.postalCode = postalCode;
    }

    public int getAddressId() {
        return addressId;
    }

    public void setAddressId(int addressId) {
        this.addressId = addressId;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getStreetAddress() {
        return streetAddress;
    }

    public void setStreetAddress(String streetAddress) {
        this.streetAddress = streetAddress;
    }

    public int getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(int postalCode) {
        this.postalCode = postalCode;
    }
}
