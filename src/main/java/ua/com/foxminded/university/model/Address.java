package ua.com.foxminded.university.model;

public class Address {

    private String country;
    private String postalCode;
    private String region;
    private String city;
    private String streetAddress;

    public Address(String country, String postalCode, String region, String city, String streetAddress) {
	this.country = country;
	this.postalCode = postalCode;
	this.region = region;
	this.city = city;
	this.streetAddress = streetAddress;
    }

    public String getCountry() {
	return country;
    }

    public void setCountry(String country) {
	this.country = country;
    }

    public String getPostalCode() {
	return postalCode;
    }

    public void setPostalCode(String postalCode) {
	this.postalCode = postalCode;
    }

    public String getRegion() {
	return region;
    }

    public void setRegion(String region) {
	this.region = region;
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
}
