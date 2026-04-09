package com.ehotel.service;

import com.ehotel.model.Room;
import com.ehotel.model.Reservation;
import com.ehotel.repository.ReservationRepository;
import com.ehotel.repository.RoomRepository;
import com.ehotel.enums.ReservationStatus;
import com.ehotel.enums.RoomStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
public class RoomService {

    @Autowired
    private RoomRepository roomRepository;

    @Autowired
    private ReservationRepository reservationRepository;

    public List<Room> searchAvailableRooms(Long hotelId, LocalDate startDate, LocalDate endDate, int capacity) {
        List<Room> rooms = roomRepository.findByHotel_HotelIdAndStatus(hotelId, RoomStatus.AVAILABLE);
        List<Room> availableRooms = new ArrayList<>();

        for (Room room : rooms) {
            // Vérifie s’il y a des réservations conflictuelles
            List<Reservation> conflicts = reservationRepository.findByRoomAndStatusInAndEndDateAfterAndStartDateBefore(
                    room,
                    List.of(ReservationStatus.RESERVED, ReservationStatus.COMPLETED),
                    startDate,
                    endDate);

            if (conflicts.isEmpty() && Integer.parseInt(room.getCapacity()) >= capacity) {
                availableRooms.add(room);
            }
        }

        return availableRooms;
    }
}