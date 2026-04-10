package com.ehotel.dto.request;

import com.ehotel.enums.RoomStatus;
import com.ehotel.enums.ViewType;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class AdminRoomRequest {

    private Long hotelId;
    private String roomNumber;
    private String capacity;
    private BigDecimal pricePerNight;
    private ViewType viewType;
    private Boolean extendable;
    private RoomStatus status;
    private List<String> amenities = new ArrayList<>();


    public AdminRoomRequest() {
    }

    public Long getHotelId() {
        return hotelId;
    }

    public void setHotelId(Long hotelId) {
        this.hotelId = hotelId;
    }

    public String getRoomNumber() {
        return roomNumber;
    }

    public void setRoomNumber(String roomNumber) {
        this.roomNumber = roomNumber;
    }

    public String getCapacity() {
        return capacity;
    }

    public void setCapacity(String capacity) {
        this.capacity = capacity;
    }

    public BigDecimal getPricePerNight() {
        return pricePerNight;
    }

    public void setPricePerNight(BigDecimal pricePerNight) {
        this.pricePerNight = pricePerNight;
    }

    public ViewType getViewType() {
        return viewType;
    }

    public void setViewType(ViewType viewType) {
        this.viewType = viewType;
    }

    public Boolean getExtendable() {
        return extendable;
    }

    public void setExtendable(Boolean extendable) {
        this.extendable = extendable;
    }

    public RoomStatus getStatus() {
        return status;
    }

    public void setStatus(RoomStatus status) {
        this.status = status;
    }

    public List<String> getAmenities() {
        return amenities;
    }

    public void setAmenities(List<String> amenities) {
        this.amenities = amenities;
    }
}