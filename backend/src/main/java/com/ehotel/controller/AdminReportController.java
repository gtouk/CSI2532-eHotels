package com.ehotel.controller;

import com.ehotel.dto.response.AdminReportResponse;
import com.ehotel.dto.response.ApiResponse;
import com.ehotel.service.AdminReportService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/employee/admin/reports")
public class AdminReportController {

    private final AdminReportService adminReportService;

    public AdminReportController(AdminReportService adminReportService) {
        this.adminReportService = adminReportService;
    }

    @GetMapping
    public ApiResponse<AdminReportResponse> getReports() {
        return ApiResponse.success(
                "Reports fetched successfully",
                adminReportService.getReports()
        );
    }
}