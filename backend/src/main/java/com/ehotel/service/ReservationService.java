package com.ehotel.service;

import com.ehotel.model.Reservation;
import com.ehotel.model.Room;
import com.ehotel.model.Customer;
import com.ehotel.repository.ReservationRepository;
import com.ehotel.enums.ReservationStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Service
public class ReservationService {

    @Autowired
    private ReservationRepository reservationRepository;

    public Reservation createReservation(Customer client, Room room, LocalDate startDate, LocalDate endDate) {

        if (!room.getStatus().equals(com.ehotel.enums.RoomStatus.AVAILABLE)) {
            throw new RuntimeException("Room is not available");
        }

        if (!startDate.isBefore(endDate)) {
            throw new RuntimeException("Invalid date range");
        }

        // Vérification des conflits
        List<Reservation> conflicts = reservationRepository.findByRoomAndStatusInAndEndDateAfterAndStartDateBefore(
                room,
                List.of(ReservationStatus.RESERVED, ReservationStatus.COMPLETED),
                startDate,
                endDate);

        if (!conflicts.isEmpty()) {
            throw new RuntimeException("Room is already reserved for these dates");
        }

        // Calcul du prix total
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

        return reservationRepository.save(reservation);
    }
}