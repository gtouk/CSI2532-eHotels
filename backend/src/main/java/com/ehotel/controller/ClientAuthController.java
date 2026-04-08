package com.ehotel.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.ehotel.dto.request.ClientLoginRequest;
import com.ehotel.dto.request.ClientRegisterRequest;
import com.ehotel.dto.response.ApiResponse;
import com.ehotel.dto.response.CustomerAuthData;
import com.ehotel.service.ClientAuthService;

@RestController
@RequestMapping("/client")
public class ClientAuthController {

    private final ClientAuthService service;

    // ✅ Injection propre via constructeur
    public ClientAuthController(ClientAuthService service) {
        this.service = service;
    }

    // 🔐 REGISTER
    @PostMapping("/register")
    public ResponseEntity<ApiResponse<CustomerAuthData>> register(@RequestBody ClientRegisterRequest request) {

        CustomerAuthData data = service.register(request);

        ApiResponse<CustomerAuthData> response = new ApiResponse<>();
        response.setSuccess(true);
        response.setMessage("Client registered successfully");
        response.setData(data);

        return ResponseEntity.ok(response);
    }

    // 🔐 LOGIN
    @PostMapping("/login")
    public ResponseEntity<ApiResponse<CustomerAuthData>> login(@RequestBody ClientLoginRequest request) {

        CustomerAuthData data = service.login(request);

        ApiResponse<CustomerAuthData> response = new ApiResponse<>();
        response.setSuccess(true);
        response.setMessage("Login successful");
        response.setData(data);

        return ResponseEntity.ok(response);
    }

    // 🔐 LOGOUT
    @PostMapping("/logout")
    public ResponseEntity<ApiResponse<String>> logout() {

        ApiResponse<String> response = new ApiResponse<>();
        response.setSuccess(true);
        response.setMessage("Logout successful");

        return ResponseEntity.ok(response);
    }
}