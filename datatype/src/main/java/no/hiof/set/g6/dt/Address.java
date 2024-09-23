package no.hiof.set.g6.dt;


public final class Address { // Ren data type. Verdier kan være hva som helst
 
 public String country;
 public String state;
 public String city;
 public String streetAddress;
 public int houseNumber;
 public int postalCode;
 
 public Address(String country,
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
 
 public Address() {}
 
 public boolean anyValueIsNull() {
  return country == null || state == null || city == null || streetAddress == null;
 }
 
}