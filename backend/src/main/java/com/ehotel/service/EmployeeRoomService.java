package com.ehotel.service;

import com.ehotel.dto.request.UpdateRoomStatusRequest;
import com.ehotel.enums.RoomStatus;
import com.ehotel.exception.ResourceNotFoundException;
import com.ehotel.model.Room;
import com.ehotel.repository.RoomRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class EmployeeRoomService {

    private final RoomRepository roomRepository;

    public EmployeeRoomService(RoomRepository roomRepository) {
        this.roomRepository = roomRepository;
    }

    public List<Room> getAllRooms() {
        return roomRepository.findAll();
    }

    @Transactional
    public Room updateRoomStatus(Long roomId, UpdateRoomStatusRequest request) {
        Room room = roomRepository.findById(roomId)
                .orElseThrow(() -> new ResourceNotFoundException("Room not found"));

        RoomStatus newStatus = request.getStatus();
        if (newStatus == null) {
            throw new IllegalArgumentException("Room status is required");
        }

        room.setStatus(newStatus);
        return roomRepository.save(room);
    }
}