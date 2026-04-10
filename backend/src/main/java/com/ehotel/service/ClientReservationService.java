package com.ehotel.service;

import com.ehotel.dto.response.ReservationSummaryResponse;
import com.ehotel.enums.ReservationStatus;
import com.ehotel.enums.RoomStatus;
import com.ehotel.model.Customer;
import com.ehotel.model.Reservation;
import com.ehotel.model.Room;
import com.ehotel.repository.ReservationRepository;
import com.ehotel.repository.RentalRepository;
import com.ehotel.repository.RoomRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ClientReservationService {

    @Autowired
    private ReservationRepository reservationRepository;

    @Autowired
    private RoomRepository roomRepository;

    @Autowired
    private RentalRepository rentalRepository;

    public ReservationSummaryResponse createReservation(Customer client, Room room, LocalDate startDate, LocalDate endDate) {
        if (startDate == null || endDate == null) {
            throw new RuntimeException("Start date and end date are required");
        }

        if (!startDate.isBefore(endDate)) {
            throw new RuntimeException("startDate must be before endDate");
        }

        if (room.getStatus() != RoomStatus.AVAILABLE) {
            throw new RuntimeException("Room is not available");
        }

        long nights = ChronoUnit.DAYS.between(startDate, endDate);
        BigDecimal totalPrice = room.getPricePerNight().multiply(BigDecimal.valueOf(nights));

        Reservation reservation = new Reservation();
        reservation.setCustomer(client);
        reservation.setRoom(room);
        reservation.setStartDate(startDate);
        reservation.setEndDate(endDate);
        reservation.setStatus(ReservationStatus.RESERVED);
        reservation.setTotalPrice(totalPrice);
        reservation.setCreatedAt(LocalDateTime.now());

        reservation = reservationRepository.save(reservation);

        room.setStatus(RoomStatus.RESERVED);
        roomRepository.save(room);

        return mapToSummary(reservation);
    }

    @Transactional(readOnly = true)
    public List<ReservationSummaryResponse> getReservationsByClientId(Long clientId) {
        return reservationRepository.findByCustomerCustomerId(clientId)
                .stream()
                .map(this::mapToSummary)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public ReservationSummaryResponse getReservationById(Long reservationId) {
        Reservation reservation = reservationRepository.findById(reservationId)
                .orElseThrow(() -> new RuntimeException("Reservation not found"));

        return mapToSummary(reservation);
    }

    @Transactional
    public ReservationSummaryResponse cancelReservation(Long reservationId, Long clientId) {
        Reservation reservation = reservationRepository.findById(reservationId)
                .orElseThrow(() -> new RuntimeException("Reservation not found"));

        if (reservation.getCustomer() == null || !reservation.getCustomer().getCustomerId().equals(clientId)) {
            throw new RuntimeException("Unauthorized reservation access");
        }

        reservation.setStatus(ReservationStatus.CANCELLED);
        reservationRepository.save(reservation);

        Room room = reservation.getRoom();
        if (room != null) {
            room.setStatus(RoomStatus.AVAILABLE);
            roomRepository.save(room);
        }

        return mapToSummary(reservation);
    }

    private ReservationSummaryResponse mapToSummary(Reservation reservation) {
        ReservationSummaryResponse response = new ReservationSummaryResponse();

        response.setReservationId(reservation.getReservationId());

        if (reservation.getCustomer() != null) {
            response.setCustomerId(reservation.getCustomer().getCustomerId());
            response.setCustomerName(
                    reservation.getCustomer().getFirstName() + " " + reservation.getCustomer().getLastName()
            );
        }

        if (reservation.getRoom() != null) {
            response.setRoomId(reservation.getRoom().getRoomId());
            response.setRoomNumber(reservation.getRoom().getRoomNumber());
        }

        response.setStartDate(reservation.getStartDate());
        response.setEndDate(reservation.getEndDate());
        response.setStatus(reservation.getStatus() != null ? reservation.getStatus().name() : null);
        response.setTotalPrice(reservation.getTotalPrice());

        return response;
    }
}