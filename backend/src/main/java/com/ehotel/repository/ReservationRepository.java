package com.ehotel.repository;

import com.ehotel.enums.ReservationStatus;
import com.ehotel.model.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReservationRepository extends JpaRepository<Reservation, Long> {
    List<Reservation> findByStatus(ReservationStatus status);
    List<Reservation> findByCustomer_CustomerId(Long customerId);
    List<Reservation> findByRoom_RoomId(Long roomId);
}