package com.ehotel.controller;

import com.ehotel.dto.response.ClientDashboardResponse;
import com.ehotel.service.ClientDashboardService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/client")
public class ClientDashboardController {

    private final ClientDashboardService clientDashboardService;

    public ClientDashboardController(ClientDashboardService clientDashboardService) {
        this.clientDashboardService = clientDashboardService;
    }

    @GetMapping("/dashboard")
    public ClientDashboardResponse getDashboard(@RequestParam Long clientId) {
        return clientDashboardService.getDashboard(clientId);
    }
}