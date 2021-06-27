package ua.com.foxminded.university.model;

public class Address {

    private int id;
    private String country;
    private String postalCode;
    private String region;
    private String city;
    private String streetAddress;

    public Address() {
    }

    public Address(String country, String postalCode, String region, String city, String streetAddress) {
	this.country = country;
	this.postalCode = postalCode;
	this.region = region;
	this.city = city;
	this.streetAddress = streetAddress;
    }

    public Address(int id, String country, String postalCode, String region, String city, String streetAddress) {
	this.id = id;
	this.country = country;
	this.postalCode = postalCode;
	this.region = region;
	this.city = city;
	this.streetAddress = streetAddress;
    }

    public int getId() {
	return id;
    }

    public void setId(int id) {
	this.id = id;
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

    @Override
    public int hashCode() {
	final int prime = 31;
	int result = 1;
	result = prime * result + ((city == null) ? 0 : city.hashCode());
	result = prime * result + ((country == null) ? 0 : country.hashCode());
	result = prime * result + id;
	result = prime * result + ((postalCode == null) ? 0 : postalCode.hashCode());
	result = prime * result + ((region == null) ? 0 : region.hashCode());
	result = prime * result + ((streetAddress == null) ? 0 : streetAddress.hashCode());
	return result;
    }

    @Override
    public boolean equals(Object obj) {
	if (this == obj)
	    return true;
	if (obj == null)
	    return false;
	if (getClass() != obj.getClass())
	    return false;
	Address other = (Address) obj;
	if (city == null) {
	    if (other.city != null)
		return false;
	} else if (!city.equals(other.city))
	    return false;
	if (country == null) {
	    if (other.country != null)
		return false;
	} else if (!country.equals(other.country))
	    return false;
	if (id != other.id)
	    return false;
	if (postalCode == null) {
	    if (other.postalCode != null)
		return false;
	} else if (!postalCode.equals(other.postalCode))
	    return false;
	if (region == null) {
	    if (other.region != null)
		return false;
	} else if (!region.equals(other.region))
	    return false;
	if (streetAddress == null) {
	    if (other.streetAddress != null)
		return false;
	} else if (!streetAddress.equals(other.streetAddress))
	    return false;
	return true;
    }
}
