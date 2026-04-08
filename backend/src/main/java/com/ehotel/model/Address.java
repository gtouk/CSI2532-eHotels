package com.ehotel.model;

import jakarta.persistence.*;

@Entity
@Table(name = "address")
public class Address {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "address_id")
    private Long addressId;

    @Column(name = "street_number", nullable = false)
    private Integer streetNumber;

<<<<<<< HEAD
    @Column(name = "street_name", nullable = false, length = 150)
=======
    @Column(name = "street_name", nullable = false, length = 255)
>>>>>>> origin/main
    private String streetName;

    @Column(name = "city", nullable = false, length = 100)
    private String city;

    @Column(name = "province", nullable = false, length = 100)
    private String province;

    @Column(name = "postal_code", nullable = false, length = 20)
    private String postalCode;

    @Column(name = "country", nullable = false, length = 100)
    private String country;

    public Address() {
    }

<<<<<<< HEAD
    // Getters & Setters

=======
>>>>>>> origin/main
    public Long getAddressId() {
        return addressId;
    }

<<<<<<< HEAD
=======
    public void setAddressId(Long addressId) {
        this.addressId = addressId;
    }

>>>>>>> origin/main
    public Integer getStreetNumber() {
        return streetNumber;
    }

    public void setStreetNumber(Integer streetNumber) {
        this.streetNumber = streetNumber;
    }

<<<<<<< HEAD
    public void setAddressId(Long addressId) {
        this.addressId = addressId;
    }

=======
>>>>>>> origin/main
    public String getStreetName() {
        return streetName;
    }

    public void setStreetName(String streetName) {
        this.streetName = streetName;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }
}