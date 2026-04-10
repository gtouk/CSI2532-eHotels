package com.ehotel.service;

import com.ehotel.dto.response.RoomDetailsResponse;
import com.ehotel.dto.response.RoomSummaryResponse;
import com.ehotel.model.Amenity;
import com.ehotel.model.Room;
import org.springframework.stereotype.Component;

import java.util.Comparator;
import java.util.List;

@Component
public class RoomDtoMapper {

    public RoomSummaryResponse toSummary(Room room) {
        RoomSummaryResponse dto = new RoomSummaryResponse();
        dto.setRoomId(room.getRoomId());
        dto.setHotelId(room.getHotel() != null ? room.getHotel().getHotelId() : null);
        dto.setHotelName(room.getHotel() != null ? room.getHotel().getName() : null);
        dto.setChainName(null);
        dto.setCategory(room.getHotel() != null ? room.getHotel().getCategory() : null);
        dto.setRoomNumber(room.getRoomNumber());
        dto.setCapacity(room.getCapacity());
        dto.setPricePerNight(room.getPricePerNight());
        dto.setViewType(room.getViewType() != null ? room.getViewType().name() : null);
        dto.setExtendable(room.getExtendable());
        dto.setStatus(room.getStatus() != null ? room.getStatus().name() : null);
        dto.setAmenities(extractAmenities(room));
        return dto;
    }

    public RoomDetailsResponse toDetails(Room room) {
        RoomDetailsResponse dto = new RoomDetailsResponse();
        dto.setRoomId(room.getRoomId());
        dto.setHotelId(room.getHotel() != null ? room.getHotel().getHotelId() : null);
        dto.setHotelName(room.getHotel() != null ? room.getHotel().getName() : null);
        dto.setChainName(null);
        dto.setCategory(room.getHotel() != null ? room.getHotel().getCategory() : null);
        dto.setRoomNumber(room.getRoomNumber());
        dto.setCapacity(room.getCapacity());
        dto.setPricePerNight(room.getPricePerNight());
        dto.setViewType(room.getViewType() != null ? room.getViewType().name() : null);
        dto.setExtendable(room.getExtendable());
        dto.setStatus(room.getStatus() != null ? room.getStatus().name() : null);
        dto.setAmenities(extractAmenities(room));
        return dto;
    }

    private List<String> extractAmenities(Room room) {
        if (room.getAmenities() == null) {
            return List.of();
        }

        return room.getAmenities().stream()
                .map(Amenity::getName)
                .sorted(Comparator.naturalOrder())
                .toList();
    }
}