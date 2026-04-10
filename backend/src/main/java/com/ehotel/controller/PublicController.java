package com.ehotel.controller;

import com.ehotel.dto.response.HotelDetailsResponse;
import com.ehotel.dto.response.HotelSummaryResponse;
import com.ehotel.dto.response.RoomDetailsResponse;
import com.ehotel.dto.response.RoomSummaryResponse;
import com.ehotel.service.PublicService;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
public class PublicController {

    private final PublicService publicService;

    public PublicController(PublicService publicService) {
        this.publicService = publicService;
    }

    @GetMapping("/")
    public String home() { return publicService.getHomeMessage(); }

    @GetMapping("/hotels")
    public List<HotelSummaryResponse> getHotels() { return publicService.getAllHotels(); }

    @GetMapping("/hotels/{hotelId}")
    public HotelDetailsResponse getHotelById(@PathVariable Long hotelId) {
        return publicService.getHotelById(hotelId);
    }

    @GetMapping("/rooms/{roomId}")
    public RoomDetailsResponse getRoomById(@PathVariable Long roomId) {
        return publicService.getRoomById(roomId);
    }

    /**
     * Recherche de chambres avec tous les filtres du frontend.
     * Paramètres optionnels : chainId, category, capacity, viewType,
     * extendable, minPrice, maxPrice, minSurface, hotelId.
     */
    @GetMapping("/rooms/search")
    public List<RoomSummaryResponse> searchRooms(
            @RequestParam String startDate,
            @RequestParam String endDate,
            @RequestParam(required = false) Long chainId,
            @RequestParam(required = false) Long hotelId,
            @RequestParam(required = false) Integer category,
            @RequestParam(required = false) String capacity,
            @RequestParam(required = false) String viewType,
            @RequestParam(required = false) Boolean extendable,
            @RequestParam(required = false) Double minPrice,
            @RequestParam(required = false) Double maxPrice,
            @RequestParam(required = false) Integer minSurface
    ) {
        return publicService.searchRooms(
                LocalDate.parse(startDate),
                LocalDate.parse(endDate),
                chainId, hotelId, category, capacity,
                viewType, extendable, minPrice, maxPrice, minSurface
        );
    }
}
