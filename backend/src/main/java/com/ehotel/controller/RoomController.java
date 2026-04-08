package com.ehotel.controller;

import com.ehotel.model.Room;
import com.ehotel.service.RoomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/rooms")
public class RoomController {

    @Autowired
    private RoomService roomService;

    @GetMapping("/search")
    public List<Room> searchRooms(
            @RequestParam Long hotelId,
            @RequestParam String startDate,
            @RequestParam String endDate,
            @RequestParam int capacity) {
        LocalDate start = LocalDate.parse(startDate);
        LocalDate end = LocalDate.parse(endDate);

        return roomService.searchAvailableRooms(hotelId, start, end, capacity);
    }
}