package com.ehotel.service;

import com.ehotel.dto.response.ReservationSummaryResponse;
import com.ehotel.exception.ResourceNotFoundException;
import com.ehotel.model.Customer;
import com.ehotel.model.Reservation;
import com.ehotel.model.Room;
import com.ehotel.repository.ReservationRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class EmployeeReservationService {

    private final ReservationRepository reservationRepository;

    public EmployeeReservationService(ReservationRepository reservationRepository) {
        this.reservationRepository = reservationRepository;
    }

    @Transactional(readOnly = true)
    public List<ReservationSummaryResponse> getAllReservations() {
        return reservationRepository.findAll()
                .stream()
                .map(this::mapToSummary)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public ReservationSummaryResponse getReservationById(Long reservationId) {
        Reservation reservation = reservationRepository.findById(reservationId)
                .orElseThrow(() -> new ResourceNotFoundException("Reservation not found"));

        return mapToSummary(reservation);
    }

    private ReservationSummaryResponse mapToSummary(Reservation reservation) {
        ReservationSummaryResponse response = new ReservationSummaryResponse();

        response.setReservationId(reservation.getReservationId());

        Customer customer = reservation.getCustomer();
        if (customer != null) {
            response.setCustomerId(customer.getCustomerId());
            response.setCustomerName(customer.getFirstName() + " " + customer.getLastName());
        }

        Room room = reservation.getRoom();
        if (room != null) {
            response.setRoomId(room.getRoomId());
            response.setRoomNumber(room.getRoomNumber());
        }

        response.setStartDate(reservation.getStartDate());
        response.setEndDate(reservation.getEndDate());
        response.setStatus(reservation.getStatus() != null ? reservation.getStatus().name() : null);
        response.setTotalPrice(reservation.getTotalPrice());

        return response;
    }
}