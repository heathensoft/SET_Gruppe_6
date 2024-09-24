package no.hiof.set.g6.dt;


public final class HomeAddress implements G6DataType {
 
 public String country;
 public String state;
 public String city;
 public String streetAddress;
 public int houseNumber;
 public int postalCode;
 
 public HomeAddress(String country,
                    String state,
                    String city,
                    String streetAddress,
                    int houseNumber,
                    int postalCode) {
  this.country = country;
  this.state = state;
  this.city = city;
  this.streetAddress = streetAddress;
  this.houseNumber = houseNumber;
  this.postalCode = postalCode;
 }
 
 public HomeAddress() {}
 
 public boolean anyValueIsNull() {
  return country == null || state == null || city == null || streetAddress == null;
 }
 
}