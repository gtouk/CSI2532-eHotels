package com.ehotel.controller;

import com.ehotel.dto.request.EmployeeLoginRequest;
import com.ehotel.dto.response.ApiResponse;
import com.ehotel.dto.response.EmployeeAuthResponse;
import com.ehotel.service.EmployeeAuthService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/employee")
public class EmployeeAuthController {

    private final EmployeeAuthService employeeAuthService;

    public EmployeeAuthController(EmployeeAuthService employeeAuthService) {
        this.employeeAuthService = employeeAuthService;
    }

    @PostMapping("/login")
    public ApiResponse<EmployeeAuthResponse> login(@RequestBody EmployeeLoginRequest request) {
        EmployeeAuthResponse response = employeeAuthService.login(request);
        return ApiResponse.success("Login successful", response);
    }
}