package com.ehotel.repository;

import com.ehotel.model.Reservation;
import com.ehotel.model.Room;
import com.ehotel.enums.ReservationStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface ReservationRepository extends JpaRepository<Reservation, Long> {

    List<Reservation> findByRoomAndStatusInAndEndDateAfterAndStartDateBefore(
            Room room,
            List<ReservationStatus> statuses,
            LocalDate start,
            LocalDate end);
}