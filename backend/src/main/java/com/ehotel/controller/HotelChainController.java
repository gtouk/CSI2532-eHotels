package com.ehotel.controller;

import com.ehotel.dto.response.HotelChainDetailsResponse;
import com.ehotel.dto.response.HotelChainSummaryResponse;
import com.ehotel.service.HotelChainPublicService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/chains")
public class HotelChainController {

    private final HotelChainPublicService hotelChainPublicService;

    public HotelChainController(HotelChainPublicService hotelChainPublicService) {
        this.hotelChainPublicService = hotelChainPublicService;
    }

    @GetMapping
    public List<HotelChainSummaryResponse> getAllChains() {
        return hotelChainPublicService.getAllChains();
    }

    @GetMapping("/{chainId}")
    public HotelChainDetailsResponse getChainById(@PathVariable Long chainId) {
        return hotelChainPublicService.getChainById(chainId);
    }
}