package no.hiof.set.g6.dt;



public class HomeAddress implements G6DataType {
    
    public String country;
    public String state;
    public String city;
    public String streetAddress;
    public int postalCode;
    
    public HomeAddress(String country,
                       String state,
                       String city,
                       String streetAddress,
                       int postalCode) {
        this.country = country;
        this.state = state;
        this.city = city;
        this.streetAddress = streetAddress;
        this.postalCode = postalCode;
    }
    
    public HomeAddress() {
        this.country = "";
        this.state = "";
        this.city = "";
        this.streetAddress = "";
    }
    
    public void set(HomeAddress address) {
        if (address == null) {
            this.country = "null";
            this.state = "null";
            this.city = "null";
            this.streetAddress = "null";
            this.postalCode = 0;
        } else {
            this.country = address.country;
            this.state = address.state;
            this.city = address.city;
            this.streetAddress = address.streetAddress;
            this.postalCode = address.postalCode;
        }
    }
    
}
