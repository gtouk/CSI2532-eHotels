package com.ehotel.controller;

import com.ehotel.dto.request.AdminHotelRequest;
import com.ehotel.dto.response.ApiResponse;
import com.ehotel.dto.response.HotelSummaryResponse;
import com.ehotel.service.AdminHotelService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/employee/admin/hotels")
public class AdminHotelController {

    private final AdminHotelService adminHotelService;

    public AdminHotelController(AdminHotelService adminHotelService) {
        this.adminHotelService = adminHotelService;
    }

    @GetMapping
    public ApiResponse<List<HotelSummaryResponse>> getAllHotels() {
        return ApiResponse.success(
                "Hotels fetched successfully",
                adminHotelService.getAllHotels()
        );
    }

    @PostMapping
    public ApiResponse<HotelSummaryResponse> createHotel(@RequestBody AdminHotelRequest request) {
        return ApiResponse.success(
                "Hotel created successfully",
                adminHotelService.createHotel(request)
        );
    }

    @PostMapping("/{hotelId}/update")
    public ApiResponse<HotelSummaryResponse> updateHotel(
            @PathVariable Long hotelId,
            @RequestBody AdminHotelRequest request
    ) {
        return ApiResponse.success(
                "Hotel updated successfully",
                adminHotelService.updateHotel(hotelId, request)
        );
    }

    @PostMapping("/{hotelId}/delete")
    public ApiResponse<Object> deleteHotel(@PathVariable Long hotelId) {
        adminHotelService.deleteHotel(hotelId);
        return ApiResponse.success("Hotel deleted successfully", null);
    }
}