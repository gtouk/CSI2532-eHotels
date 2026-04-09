package com.ehotel.model;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "customer")
public class Customer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "customer_id")
    private Long customerId;

    @Column(name = "ssn", nullable = false, unique = true, length = 20)
    private String ssn;

    @Column(name = "first_name", nullable = false, length = 100)
    private String firstName;

    @Column(name = "last_name", nullable = false, length = 100)
    private String lastName;

    @Column(name = "registration_date", nullable = false)
    private LocalDate registrationDate;

    @Column(name = "email", unique = true)
    private String email;

    @Column(name = "password_hash", nullable = false)
    private String passwordHash;

    // 🔗 Relation avec Address
    @ManyToOne
    @JoinColumn(name = "address_id", nullable = false)
    private Address address;
    @Transient
    private String role = "CLIENT";

    public Customer() {
    }

    // Getters & Setters

    public Long getCustomerId() {
        return customerId;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public Address getAddress() {
        return address;
    }

    public String getSsn() {
        return ssn;
    }

    public void setSsn(String ssn) {
        this.ssn = ssn;
    }

    public void setPasswordHash(String password) {
        this.passwordHash = password;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public void setCustomerId(Long customerId) {
        this.customerId = customerId;
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

    public LocalDate getRegistrationDate() {
        return registrationDate;
    }

    public String getRole() {
        return role;
    }

    public void setRegistrationDate(LocalDate registrationDate) {
        this.registrationDate = registrationDate;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}