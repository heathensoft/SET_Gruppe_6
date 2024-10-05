package no.hiof.set.g6.dt;


import java.util.ArrayList;
import java.util.List;


public final class UserAccount implements G6DataType {
    
    public String firstName;
    public String lastName;
    public String email;
    private final HomeAddress address;          // If you change the class, never let this be null
    private final List<String> phoneNumbers;    // If you change the class, never let this be null
    
    public UserAccount( String firstName, String lastName, String email) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.address = new HomeAddress();
        this.phoneNumbers = new ArrayList<>();
    }
    
    public UserAccount() {
        this.firstName = "";
        this.lastName = "";
        this.email = "";
        this.address = new HomeAddress();
        this.phoneNumbers = new ArrayList<>();
    }
    
    public HomeAddress getAddress() { return address; }
    
    public List<String> getPhoneNumbers() { return phoneNumbers; }
    
    
}
