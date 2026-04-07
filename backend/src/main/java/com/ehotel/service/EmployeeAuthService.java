package com.ehotel.service;

import com.ehotel.dto.request.EmployeeLoginRequest;
import com.ehotel.dto.response.EmployeeAuthResponse;
import com.ehotel.exception.ResourceNotFoundException;
import com.ehotel.model.Employee;
import com.ehotel.repository.EmployeeRepository;
import org.springframework.stereotype.Service;

@Service
public class EmployeeAuthService {

    private final EmployeeRepository employeeRepository;

    public EmployeeAuthService(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }

    public EmployeeAuthResponse login(EmployeeLoginRequest request) {
        Employee employee = employeeRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new ResourceNotFoundException("Employee not found"));

        if (employee.getPassword() == null || !employee.getPassword().equals(request.getPassword())) {
            throw new IllegalArgumentException("Invalid email or password");
        }

        if (Boolean.FALSE.equals(employee.getActive())) {
            throw new IllegalArgumentException("Employee account is inactive");
        }

        Long hotelId = employee.getHotel() != null ? employee.getHotel().getHotelId() : null;

        return new EmployeeAuthResponse(
                employee.getEmployeeId(),
                employee.getFirstName(),
                employee.getLastName(),
                employee.getEmail(),
                employee.getRole().name(),
                hotelId
        );
    }
}