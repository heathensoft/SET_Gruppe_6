package no.hiof.set.g6.dtdb;

public class UserAccount implements G6DataType {
    private int accountId;        // corresponds to account_id INT
    private String firstName;     // corresponds to first_name VARCHAR(100)
    private String lastName;      // corresponds to last_name VARCHAR(100)
    private String email;         // corresponds to email VARCHAR(150)
    private Address address;  // corresponds to address_id INT (Foreign Key)

    // Constructors, Getters, and Setters
    public UserAccount(int accountId, String firstName, String lastName, String email, Address address) {
        this.accountId = accountId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.address = address;
    }

    public int getAccountId() {
        return accountId;
    }

    public void setAccountId(int accountId) {
        this.accountId = accountId;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }
}
