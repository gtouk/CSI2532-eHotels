package com.ehotel.controller;

import com.ehotel.dto.request.UpdateRoomStatusRequest;
import com.ehotel.dto.response.ApiResponse;
import com.ehotel.dto.response.RoomSummaryResponse;
import com.ehotel.service.EmployeeRoomService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/employee/rooms")
public class EmployeeRoomController {

    private final EmployeeRoomService employeeRoomService;

    public EmployeeRoomController(EmployeeRoomService employeeRoomService) {
        this.employeeRoomService = employeeRoomService;
    }

    @GetMapping
    public ApiResponse<List<RoomSummaryResponse>> getAllRooms() {
        return ApiResponse.success(
                "Rooms fetched successfully",
                employeeRoomService.getAllRooms()
        );
    }

    @PostMapping("/{roomId}/status")
    public ApiResponse<RoomSummaryResponse> updateRoomStatus(
            @PathVariable Long roomId,
            @RequestBody UpdateRoomStatusRequest request
    ) {
        return ApiResponse.success(
                "Room status updated successfully",
                employeeRoomService.updateRoomStatus(roomId, request)
        );
    }
}