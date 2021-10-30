package ua.com.foxminded.university.model;

import javax.persistence.*;

@Entity
@Table(name = "addresses")
public class Address {

    @Id
    @Column
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column
    private String country;
    @Column
    private String postalCode;
    @Column
    private String region;
    @Column
    private String city;
    @Column
    private String streetAddress;

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private int id = 0;
        private String country;
        private String postalCode;
        private String region;
        private String city;
        private String streetAddress;

        private Builder() {
        }

        public Builder country(String val) {
            this.country = val;
            return this;
        }

        public Builder id(int val) {
            this.id = val;
            return this;
        }

        public Builder postalCode(String val) {
            this.postalCode = val;
            return this;
        }

        public Builder region(String val) {
            this.region = val;
            return this;
        }

        public Builder city(String val) {
            this.city = val;
            return this;
        }

        public Builder streetAddress(String val) {
            this.streetAddress = val;
            return this;
        }

        public Address build() {
            return new Address(this);
        }
    }

    public Address() {
    }

    private Address(Builder builder) {
        id = builder.id;
        country = builder.country;
        postalCode = builder.postalCode;
        region = builder.region;
        city = builder.city;
        streetAddress = builder.streetAddress;
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

    @Override
    public String toString() {
        return id + ":" + country + ", " + postalCode + ", " + region + ", "
                + city + ", " + streetAddress;
    }
}
