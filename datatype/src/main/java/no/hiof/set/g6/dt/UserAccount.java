package no.hiof.set.g6.dt;


import java.util.ArrayList;
import java.util.List;


public class UserAccount implements DataType {
    
    public int id;
    public String firstName;
    public String lastName;
    public String email;
    private final Address address;
    private final List<String> phoneNumbers;
    
    public UserAccount() {
        this.firstName = "";
        this.lastName = "";
        this.email = "";
        this.address = new Address();
        this.phoneNumbers = new ArrayList<>();
    }
    
    public Address getAddress() { return address; }
    
    public List<String> getPhoneNumbers() { return phoneNumbers; }
    
    public boolean anyValueIsNull() {
        return firstName == null || lastName == null || email == null;
    }
    
    
}
