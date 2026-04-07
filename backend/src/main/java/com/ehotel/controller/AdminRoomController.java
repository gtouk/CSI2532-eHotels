package com.ehotel.controller;

import com.ehotel.dto.request.AdminRoomRequest;
import com.ehotel.dto.response.ApiResponse;
import com.ehotel.dto.response.RoomSummaryResponse;
import com.ehotel.service.AdminRoomService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/employee/admin/rooms")
public class AdminRoomController {

    private final AdminRoomService adminRoomService;

    public AdminRoomController(AdminRoomService adminRoomService) {
        this.adminRoomService = adminRoomService;
    }

    @GetMapping
    public ApiResponse<List<RoomSummaryResponse>> getAllRooms() {
        return ApiResponse.success(
                "Admin rooms fetched successfully",
                adminRoomService.getAllRooms()
        );
    }

    @PostMapping
    public ApiResponse<RoomSummaryResponse> createRoom(@RequestBody AdminRoomRequest request) {
        return ApiResponse.success(
                "Room created successfully",
                adminRoomService.createRoom(request)
        );
    }

    @PostMapping("/{roomId}/update")
    public ApiResponse<RoomSummaryResponse> updateRoom(
            @PathVariable Long roomId,
            @RequestBody AdminRoomRequest request
    ) {
        return ApiResponse.success(
                "Room updated successfully",
                adminRoomService.updateRoom(roomId, request)
        );
    }

    @PostMapping("/{roomId}/delete")
    public ApiResponse<Object> deleteRoom(@PathVariable Long roomId) {
        adminRoomService.deleteRoom(roomId);
        return ApiResponse.success("Room deleted successfully", null);
    }
}