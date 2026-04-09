package com.ehotel.controller;

import com.ehotel.dto.request.ClientUpdateProfileRequest;
import com.ehotel.dto.response.ClientSummaryResponse;
import com.ehotel.service.ClientProfileService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/client")
public class ClientProfileController {

    private final ClientProfileService clientProfileService;

    public ClientProfileController(ClientProfileService clientProfileService) {
        this.clientProfileService = clientProfileService;
    }

    @GetMapping("/profile")
    public ClientSummaryResponse getProfile(@RequestParam Long clientId) {
        return clientProfileService.getProfile(clientId);
    }

    @PostMapping("/profile/update")
    public ClientSummaryResponse updateProfile(@RequestBody ClientUpdateProfileRequest request) {
        return clientProfileService.updateProfile(request);
    }
}