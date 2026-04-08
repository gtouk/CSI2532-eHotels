package com.ehotel.dto.response;

public class CustomerAuthData {
    private Long clientId;
    private String email;
    private String role = "CLIENT";

    // Setters
    public void setEmail(String email) {
        this.email = email;
    }

    // Getters
    public String getEmail() {
        return email;
    }

    // setters
    public void setClientId(Long clientId) {
        this.clientId = clientId;
    }

    // Getters
    public Long getclientId() {
        return clientId;
    }

    // Getters
    public String getrole() {
        return role;
    }

}
