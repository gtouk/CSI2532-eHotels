package com.ehotel.dto.response;

public class EmployeeAuthResponse {

    private Long employeeId;
    private String firstName;
    private String lastName;
    private String email;
    private String role;
    private Long hotelId;

    public EmployeeAuthResponse() {
    }

    public EmployeeAuthResponse(Long employeeId, String firstName, String lastName, String email, String role, Long hotelId) {
        this.employeeId = employeeId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.role = role;
        this.hotelId = hotelId;
    }

    public Long getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(Long employeeId) {
        this.employeeId = employeeId;
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

    public void setEmail(String email) {
        this.email = email;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public Long getHotelId() {
        return hotelId;
    }

    public void setHotelId(Long hotelId) {
        this.hotelId = hotelId;
    }
}