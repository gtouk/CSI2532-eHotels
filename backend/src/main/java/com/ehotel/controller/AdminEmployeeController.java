package com.ehotel.controller;

import com.ehotel.dto.request.AdminEmployeeRequest;
import com.ehotel.dto.response.ApiResponse;
import com.ehotel.dto.response.EmployeeSummaryResponse;
import com.ehotel.service.AdminEmployeeService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/employee/admin/employees")
public class AdminEmployeeController {

    private final AdminEmployeeService adminEmployeeService;

    public AdminEmployeeController(AdminEmployeeService adminEmployeeService) {
        this.adminEmployeeService = adminEmployeeService;
    }

    @GetMapping
    public ApiResponse<List<EmployeeSummaryResponse>> getAllEmployees() {
        return ApiResponse.success(
                "Employees fetched successfully",
                adminEmployeeService.getAllEmployees()
        );
    }

    @PostMapping
    public ApiResponse<EmployeeSummaryResponse> createEmployee(@RequestBody AdminEmployeeRequest request) {
        return ApiResponse.success(
                "Employee created successfully",
                adminEmployeeService.createEmployee(request)
        );
    }

    @PostMapping("/{employeeId}/update")
    public ApiResponse<EmployeeSummaryResponse> updateEmployee(
            @PathVariable Long employeeId,
            @RequestBody AdminEmployeeRequest request
    ) {
        return ApiResponse.success(
                "Employee updated successfully",
                adminEmployeeService.updateEmployee(employeeId, request)
        );
    }

    @PostMapping("/{employeeId}/delete")
    public ApiResponse<Object> deleteEmployee(@PathVariable Long employeeId) {
        adminEmployeeService.deleteEmployee(employeeId);
        return ApiResponse.success("Employee deleted successfully", null);
    }
}