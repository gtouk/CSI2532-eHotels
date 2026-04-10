package com.ehotel.service;

import com.ehotel.dto.response.ReservationSummaryResponse;
import com.ehotel.enums.ReservationStatus;
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

    @Autowired private ReservationRepository reservationRepository;
    @Autowired private RoomRepository roomRepository;
    @Autowired private RentalRepository rentalRepository;

    @Transactional
    public ReservationSummaryResponse createReservation(Customer client, Room room,
                                                         LocalDate startDate, LocalDate endDate) {
        if (startDate == null || endDate == null)
            throw new RuntimeException("Les dates sont obligatoires");
        if (!startDate.isBefore(endDate))
            throw new RuntimeException("La date d'arrivée doit être avant la date de départ");
        if (startDate.isBefore(LocalDate.now()))
            throw new RuntimeException("La date d'arrivée ne peut pas être dans le passé");

        // Vérifier disponibilité
        boolean occupied = !reservationRepository
                .findByRoomAndStatusInAndEndDateAfterAndStartDateBefore(
                        room, List.of(ReservationStatus.RESERVED), startDate, endDate)
                .isEmpty();
        if (occupied)
            throw new RuntimeException("Cette chambre est déjà réservée pour ces dates");

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

        // Le trigger SQL mettra automatiquement la chambre à RESERVED
        reservation = reservationRepository.save(reservation);

        return mapToSummary(reservation);
    }

    @Transactional(readOnly = true)
    public List<ReservationSummaryResponse> getReservationsByClientId(Long clientId) {
        return reservationRepository.findByCustomerCustomerId(clientId)
                .stream().map(this::mapToSummary).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public ReservationSummaryResponse getReservationById(Long reservationId) {
        Reservation reservation = reservationRepository.findById(reservationId)
                .orElseThrow(() -> new RuntimeException("Réservation introuvable"));
        return mapToSummary(reservation);
    }

    @Transactional
    public ReservationSummaryResponse cancelReservation(Long reservationId, Long clientId) {
        Reservation reservation = reservationRepository.findById(reservationId)
                .orElseThrow(() -> new RuntimeException("Réservation introuvable"));

        if (!reservation.getCustomer().getCustomerId().equals(clientId))
            throw new RuntimeException("Vous ne pouvez annuler que vos propres réservations");
        if (rentalRepository.existsByReservation_ReservationId(reservationId))
            throw new RuntimeException("Cette réservation a déjà été convertie en location");
        if (reservation.getStatus() == ReservationStatus.CANCELLED)
            throw new RuntimeException("Cette réservation est déjà annulée");

        reservation.setStatus(ReservationStatus.CANCELLED);
        // Le trigger SQL remettra automatiquement la chambre à AVAILABLE
        reservation = reservationRepository.save(reservation);

        return mapToSummary(reservation);
    }

    private ReservationSummaryResponse mapToSummary(Reservation reservation) {
        ReservationSummaryResponse response = new ReservationSummaryResponse();
        response.setReservationId(reservation.getReservationId());

        if (reservation.getCustomer() != null) {
            response.setCustomerId(reservation.getCustomer().getCustomerId());
            response.setCustomerName(reservation.getCustomer().getFirstName()
                    + " " + reservation.getCustomer().getLastName());
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
