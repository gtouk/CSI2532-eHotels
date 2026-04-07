package com.ehotel.controller;

import com.ehotel.dto.request.CreateRentalRequest;
import com.ehotel.dto.response.ApiResponse;
import com.ehotel.dto.response.RentalSummaryResponse;
import com.ehotel.service.EmployeeRentalService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/employee/rentals")
public class EmployeeRentalController {

    private final EmployeeRentalService employeeRentalService;

    public EmployeeRentalController(EmployeeRentalService employeeRentalService) {
        this.employeeRentalService = employeeRentalService;
    }

    @GetMapping
    public ApiResponse<List<RentalSummaryResponse>> getAllRentals() {
        return ApiResponse.success(
                "Rentals fetched successfully",
                employeeRentalService.getAllRentals()
        );
    }

    @GetMapping("/{rentalId}")
    public ApiResponse<RentalSummaryResponse> getRentalById(@PathVariable Long rentalId) {
        return ApiResponse.success(
                "Rental fetched successfully",
                employeeRentalService.getRentalById(rentalId)
        );
    }

    @PostMapping
    public ApiResponse<RentalSummaryResponse> createRental(@RequestBody CreateRentalRequest request) {
        return ApiResponse.success(
                "Rental created successfully",
                employeeRentalService.createRental(request)
        );
    }

    @PostMapping("/{rentalId}/checkout")
    public ApiResponse<RentalSummaryResponse> checkoutRental(@PathVariable Long rentalId) {
        return ApiResponse.success(
                "Rental checked out successfully",
                employeeRentalService.checkoutRental(rentalId)
        );
    }
}