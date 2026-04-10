package com.ehotel.service;

import com.ehotel.dto.response.ClientDashboardResponse;
import com.ehotel.dto.response.ClientSummaryResponse;
import com.ehotel.dto.response.ReservationSummaryResponse;
import com.ehotel.enums.ReservationStatus;
import com.ehotel.model.Customer;
import com.ehotel.model.Reservation;
import com.ehotel.repository.CustomerRepository;
import com.ehotel.repository.ReservationRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ClientDashboardService {

    private final CustomerRepository customerRepository;
    private final ReservationRepository reservationRepository;

    public ClientDashboardService(CustomerRepository customerRepository,
                                  ReservationRepository reservationRepository) {
        this.customerRepository = customerRepository;
        this.reservationRepository = reservationRepository;
    }

    @Transactional(readOnly = true)
    public ClientDashboardResponse getDashboard(Long clientId) {
        Customer customer = customerRepository.findById(clientId)
                .orElseThrow(() -> new RuntimeException("Client not found for id: " + clientId));

        List<Reservation> reservations = reservationRepository.findByCustomerCustomerId(clientId);

        List<ReservationSummaryResponse> reservationResponses = reservations.stream()
                .map(this::mapReservationToSummary)
                .collect(Collectors.toList());

        long totalReservations = reservations.size();
        long activeReservations = reservations.stream()
                .filter(r -> r.getStatus() == ReservationStatus.RESERVED)
                .count();
        long cancelledReservations = reservations.stream()
                .filter(r -> r.getStatus() == ReservationStatus.CANCELLED)
                .count();

        ClientDashboardResponse response = new ClientDashboardResponse();
        response.setClient(mapClientToSummary(customer));
        response.setTotalReservations(totalReservations);
        response.setActiveReservations(activeReservations);
        response.setCancelledReservations(cancelledReservations);
        response.setReservations(reservationResponses);

        return response;
    }

    private ClientSummaryResponse mapClientToSummary(Customer customer) {
        ClientSummaryResponse response = new ClientSummaryResponse();

        response.setClientId(customer.getCustomerId());
        response.setFirstName(customer.getFirstName());
        response.setLastName(customer.getLastName());
        response.setEmail(customer.getEmail());
        response.setSsn(customer.getSsn());

        if (customer.getAddress() != null) {
            response.setAddressId(customer.getAddress().getAddressId());
            response.setStreetNumber(customer.getAddress().getStreetNumber());
            response.setStreetName(customer.getAddress().getStreetName());
            response.setCity(customer.getAddress().getCity());
            response.setProvince(customer.getAddress().getProvince());
            response.setPostalCode(customer.getAddress().getPostalCode());
            response.setCountry(customer.getAddress().getCountry());
        }

        return response;
    }

    private ReservationSummaryResponse mapReservationToSummary(Reservation reservation) {
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