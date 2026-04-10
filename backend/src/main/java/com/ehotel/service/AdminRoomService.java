package com.ehotel.service;

import com.ehotel.dto.request.AdminRoomRequest;
import com.ehotel.dto.response.RoomSummaryResponse;
import com.ehotel.enums.RoomStatus;
import com.ehotel.enums.ViewType;
import com.ehotel.model.Amenity;
import com.ehotel.model.Hotel;
import com.ehotel.model.Room;
import com.ehotel.repository.AmenityRepository;
import com.ehotel.repository.HotelRepository;
import com.ehotel.repository.RoomRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@Transactional
public class AdminRoomService {

    private final RoomRepository roomRepository;
    private final HotelRepository hotelRepository;
    private final AmenityRepository amenityRepository;
    private final RoomDtoMapper roomDtoMapper;

    public AdminRoomService(
            RoomRepository roomRepository,
            HotelRepository hotelRepository,
            AmenityRepository amenityRepository,
            RoomDtoMapper roomDtoMapper
    ) {
        this.roomRepository = roomRepository;
        this.hotelRepository = hotelRepository;
        this.amenityRepository = amenityRepository;
        this.roomDtoMapper = roomDtoMapper;
    }

    @Transactional(readOnly = true)
    public List<RoomSummaryResponse> getAllRooms() {
        return roomRepository.findAll()
                .stream()
                .map(roomDtoMapper::toSummary)
                .toList();
    }

    public RoomSummaryResponse createRoom(AdminRoomRequest request) {
        Hotel hotel = hotelRepository.findById(request.getHotelId())
                .orElseThrow(() -> new RuntimeException("Hotel not found"));

        Room room = new Room();
        applyRequest(room, request, hotel);

        Room saved = roomRepository.save(room);
        return roomDtoMapper.toSummary(saved);
    }

    public RoomSummaryResponse updateRoom(Long roomId, AdminRoomRequest request) {
        Room room = roomRepository.findById(roomId)
                .orElseThrow(() -> new RuntimeException("Room not found"));

        Hotel hotel = hotelRepository.findById(request.getHotelId())
                .orElseThrow(() -> new RuntimeException("Hotel not found"));

        applyRequest(room, request, hotel);

        Room saved = roomRepository.save(room);
        return roomDtoMapper.toSummary(saved);
    }

    public void deleteRoom(Long roomId) {
        Room room = roomRepository.findById(roomId)
                .orElseThrow(() -> new RuntimeException("Room not found"));
        roomRepository.delete(room);
    }

    private void applyRequest(Room room, AdminRoomRequest request, Hotel hotel) {
        room.setHotel(hotel);
        room.setRoomNumber(request.getRoomNumber());
        room.setCapacity(request.getCapacity());
        room.setPricePerNight(request.getPricePerNight());
        room.setViewType(request.getViewType() != null ? request.getViewType() : ViewType.NONE);
        room.setExtendable(Boolean.TRUE.equals(request.getExtendable()));
        room.setStatus(request.getStatus() != null ? request.getStatus() : RoomStatus.AVAILABLE);
        room.setAmenities(resolveAmenities(request.getAmenities()));
    }

    private Set<Amenity> resolveAmenities(List<String> names) {
        Set<Amenity> result = new HashSet<>();

        if (names == null) {
            return result;
        }

        for (String rawName : names) {
            if (rawName == null || rawName.isBlank()) {
                continue;
            }

            String normalized = rawName.trim();

            Amenity amenity = amenityRepository.findByNameIgnoreCase(normalized)
                    .orElseGet(() -> amenityRepository.save(new Amenity(normalized)));

            result.add(amenity);
        }

        return result;
    }
}