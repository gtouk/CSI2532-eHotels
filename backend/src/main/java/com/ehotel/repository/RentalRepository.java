package com.ehotel.repository;

import com.ehotel.enums.RentalStatus;
import com.ehotel.model.Rental;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RentalRepository extends JpaRepository<Rental, Long> {
    List<Rental> findByStatus(RentalStatus status);
    List<Rental> findByCustomer_CustomerId(Long customerId);
    List<Rental> findByRoom_RoomId(Long roomId);
}