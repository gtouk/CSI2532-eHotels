package com.ehotel.service;

import com.ehotel.dto.request.AdminRoomRequest;
import com.ehotel.dto.response.RoomSummaryResponse;
import com.ehotel.exception.ResourceNotFoundException;
import com.ehotel.model.Hotel;
import com.ehotel.model.Room;
import com.ehotel.repository.HotelRepository;
import com.ehotel.repository.RoomRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class AdminRoomService {

    private final RoomRepository roomRepository;
    private final HotelRepository hotelRepository;

    public AdminRoomService(RoomRepository roomRepository, HotelRepository hotelRepository) {
        this.roomRepository = roomRepository;
        this.hotelRepository = hotelRepository;
    }

    @Transactional(readOnly = true)
    public List<RoomSummaryResponse> getAllRooms() {
        return roomRepository.findAll().stream().map(this::mapToSummary).collect(Collectors.toList());
    }

    @Transactional
    public RoomSummaryResponse createRoom(AdminRoomRequest request) {
        Hotel hotel = hotelRepository.findById(Objects.requireNonNull(request.getHotelId()))
                .orElseThrow(() -> new ResourceNotFoundException("Hotel not found"));

        Room room = new Room();
        room.setHotel(hotel);
        room.setRoomNumber(request.getRoomNumber());
        room.setCapacity(request.getCapacity());
        room.setPricePerNight(request.getPricePerNight());
        room.setViewType(request.getViewType());
        room.setExtendable(request.getExtendable() != null ? request.getExtendable() : false);
        room.setSurfaceArea(request.getSurfaceArea());
        room.setStatus(request.getStatus() != null ? request.getStatus() : com.ehotel.enums.RoomStatus.AVAILABLE);

        // Incrémenter le compteur de chambres de l'hôtel
        hotel.setRoomCount(hotel.getRoomCount() + 1);
        hotelRepository.save(hotel);

        return mapToSummary(roomRepository.save(room));
    }

    @Transactional
    public RoomSummaryResponse updateRoom(Long roomId, AdminRoomRequest request) {
        Room room = roomRepository.findById(Objects.requireNonNull(roomId))
                .orElseThrow(() -> new ResourceNotFoundException("Room not found"));

        Hotel hotel = hotelRepository.findById(Objects.requireNonNull(request.getHotelId()))
                .orElseThrow(() -> new ResourceNotFoundException("Hotel not found"));

        room.setHotel(hotel);
        room.setRoomNumber(request.getRoomNumber());
        room.setCapacity(request.getCapacity());
        room.setPricePerNight(request.getPricePerNight());
        room.setViewType(request.getViewType());
        room.setExtendable(request.getExtendable() != null ? request.getExtendable() : false);
        room.setSurfaceArea(request.getSurfaceArea());
        if (request.getStatus() != null) room.setStatus(request.getStatus());

        return mapToSummary(roomRepository.save(room));
    }

    @Transactional
    public void deleteRoom(Long roomId) {
        Room room = roomRepository.findById(Objects.requireNonNull(roomId))
                .orElseThrow(() -> new ResourceNotFoundException("Room not found"));

        // Décrémenter le compteur
        Hotel hotel = room.getHotel();
        if (hotel != null && hotel.getRoomCount() > 0) {
            hotel.setRoomCount(hotel.getRoomCount() - 1);
            hotelRepository.save(hotel);
        }

        roomRepository.delete(room);
    }

    private RoomSummaryResponse mapToSummary(Room room) {
        RoomSummaryResponse response = new RoomSummaryResponse();
        response.setRoomId(room.getRoomId());
        response.setHotelId(room.getHotel() != null ? room.getHotel().getHotelId() : null);
        response.setHotelName(room.getHotel() != null ? room.getHotel().getName() : null);
        response.setRoomNumber(room.getRoomNumber());
        response.setCapacity(room.getCapacity());
        response.setPricePerNight(room.getPricePerNight());
        response.setViewType(room.getViewType() != null ? room.getViewType().name() : null);
        response.setExtendable(room.getExtendable());
        response.setSurfaceArea(room.getSurfaceArea());
        response.setStatus(room.getStatus() != null ? room.getStatus().name() : null);
        return response;
    }
}
