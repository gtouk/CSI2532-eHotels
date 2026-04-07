package com.ehotel.controller;

import com.ehotel.dto.response.ApiResponse;
import com.ehotel.dto.response.ReservationSummaryResponse;
import com.ehotel.service.EmployeeReservationService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/employee/reservations")
public class EmployeeReservationController {

    private final EmployeeReservationService employeeReservationService;

    public EmployeeReservationController(EmployeeReservationService employeeReservationService) {
        this.employeeReservationService = employeeReservationService;
    }

    @GetMapping
    public ApiResponse<List<ReservationSummaryResponse>> getAllReservations() {
        return ApiResponse.success(
                "Reservations fetched successfully",
                employeeReservationService.getAllReservations()
        );
    }

    @GetMapping("/{reservationId}")
    public ApiResponse<ReservationSummaryResponse> getReservationById(@PathVariable Long reservationId) {
        return ApiResponse.success(
                "Reservation fetched successfully",
                employeeReservationService.getReservationById(reservationId)
        );
    }
}