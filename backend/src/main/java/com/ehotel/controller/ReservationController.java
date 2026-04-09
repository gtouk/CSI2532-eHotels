package com.ehotel.controller;

import com.ehotel.model.Reservation;
import com.ehotel.model.Room;
import com.ehotel.model.Customer;
import com.ehotel.service.ReservationService;
import com.ehotel.repository.RoomRepository;
import com.ehotel.repository.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RestController
@RequestMapping("/client/reservations")
public class ReservationController {

    @Autowired
    private ReservationService reservationService;

    @Autowired
    private RoomRepository roomRepository;

    @Autowired
    private CustomerRepository customerRepository;

    @PostMapping
    public Reservation createReservation(@RequestParam Long clientId,
            @RequestParam Long roomId,
            @RequestParam String startDate,
            @RequestParam String endDate) {
        Customer client = customerRepository.findById(clientId)
                .orElseThrow(() -> new RuntimeException("Client not found"));
        Room room = roomRepository.findById(roomId)
                .orElseThrow(() -> new RuntimeException("Room not found"));

        return reservationService.createReservation(client, room, LocalDate.parse(startDate), LocalDate.parse(endDate));
    }
}