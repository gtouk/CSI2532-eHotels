package com.ehotel.service;

import com.ehotel.dto.request.UpdateRoomStatusRequest;
import com.ehotel.dto.response.RoomSummaryResponse;
import com.ehotel.enums.RoomStatus;
import com.ehotel.exception.ResourceNotFoundException;
import com.ehotel.model.Room;
import com.ehotel.repository.RoomRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class EmployeeRoomService {

    private final RoomRepository roomRepository;

    public EmployeeRoomService(RoomRepository roomRepository) {
        this.roomRepository = roomRepository;
    }

    @Transactional(readOnly = true)
    public List<RoomSummaryResponse> getAllRooms() {
        return roomRepository.findAll()
                .stream()
                .map(this::mapToSummary)
                .collect(Collectors.toList());
    }

    @Transactional
    public RoomSummaryResponse updateRoomStatus(Long roomId, UpdateRoomStatusRequest request) {
        Room room = roomRepository.findById(roomId)
                .orElseThrow(() -> new ResourceNotFoundException("Room not found"));

        RoomStatus newStatus = request.getStatus();
        if (newStatus == null) {
            throw new IllegalArgumentException("Room status is required");
        }

        room.setStatus(newStatus);
        Room savedRoom = roomRepository.save(room);
        return mapToSummary(savedRoom);
    }

    private RoomSummaryResponse mapToSummary(Room room) {
        RoomSummaryResponse response = new RoomSummaryResponse();

        response.setRoomId(room.getRoomId());
        response.setHotelId(room.getHotel() != null ? room.getHotel().getHotelId() : null);
        response.setRoomNumber(room.getRoomNumber());
        response.setCapacity(room.getCapacity());
        response.setPricePerNight(room.getPricePerNight());
        response.setViewType(room.getViewType() != null ? room.getViewType().name() : null);
        response.setExtendable(room.getExtendable());
        response.setStatus(room.getStatus() != null ? room.getStatus().name() : null);

        return response;
    }
}