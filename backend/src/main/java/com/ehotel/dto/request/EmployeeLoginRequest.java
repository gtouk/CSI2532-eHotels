package com.ehotel.dto.request;

public class EmployeeLoginRequest {

    private String email;
    private String password;

    public EmployeeLoginRequest() {
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}