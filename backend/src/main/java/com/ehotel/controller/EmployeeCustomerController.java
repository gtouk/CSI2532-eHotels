package com.ehotel.controller;

import com.ehotel.dto.response.ApiResponse;
import com.ehotel.dto.response.CustomerDetailsResponse;
import com.ehotel.dto.response.CustomerSummaryResponse;
import com.ehotel.service.EmployeeCustomerService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/employee/customers")
public class EmployeeCustomerController {

    private final EmployeeCustomerService employeeCustomerService;

    public EmployeeCustomerController(EmployeeCustomerService employeeCustomerService) {
        this.employeeCustomerService = employeeCustomerService;
    }

    @GetMapping
    public ApiResponse<List<CustomerSummaryResponse>> getAllCustomers() {
        return ApiResponse.success(
                "Customers fetched successfully",
                employeeCustomerService.getAllCustomers()
        );
    }

    @GetMapping("/{customerId}")
    public ApiResponse<CustomerDetailsResponse> getCustomerById(@PathVariable Long customerId) {
        return ApiResponse.success(
                "Customer fetched successfully",
                employeeCustomerService.getCustomerById(customerId)
        );
    }
}