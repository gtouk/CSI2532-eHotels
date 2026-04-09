package com.ehotel.controller;

import com.ehotel.dto.request.ClientCreateReservationRequest;
import com.ehotel.dto.response.ReservationSummaryResponse;
import com.ehotel.model.Customer;
import com.ehotel.model.Room;
import com.ehotel.repository.CustomerRepository;
import com.ehotel.repository.RoomRepository;
import com.ehotel.service.ClientReservationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/client/reservations")
public class ClientReservationController {

    @Autowired
    private ClientReservationService clientReservationService;

    @Autowired
    private RoomRepository roomRepository;

    @Autowired
    private CustomerRepository customerRepository;

    @GetMapping
    public List<ReservationSummaryResponse> getReservationsByClient(@RequestParam Long clientId) {
        return clientReservationService.getReservationsByClientId(clientId);
    }

    @GetMapping("/{reservationId}")
    public ReservationSummaryResponse getReservationById(@PathVariable Long reservationId) {
        return clientReservationService.getReservationById(reservationId);
    }

    @PostMapping
    public ReservationSummaryResponse createReservation(@RequestBody ClientCreateReservationRequest request) {
        Customer client = customerRepository.findById(request.getClientId())
                .orElseThrow(() -> new RuntimeException("Client not found"));

        Room room = roomRepository.findById(request.getRoomId())
                .orElseThrow(() -> new RuntimeException("Room not found"));

        return clientReservationService.createReservation(
                client,
                room,
                LocalDate.parse(request.getStartDate()),
                LocalDate.parse(request.getEndDate())
        );
    }

    @PostMapping("/{reservationId}/cancel")
    public ReservationSummaryResponse cancelReservation(
            @PathVariable Long reservationId,
            @RequestParam Long clientId) {
        return clientReservationService.cancelReservation(reservationId, clientId);
    }
}