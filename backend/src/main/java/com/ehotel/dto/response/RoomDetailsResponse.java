package com.ehotel.dto.response;

import java.math.BigDecimal;

public class RoomDetailsResponse {

    private Long roomId;
    private Long hotelId;
    private String roomNumber;
    private String capacity;
    private BigDecimal pricePerNight;
    private String viewType;
    private Boolean extendable;
    private String status;

    public RoomDetailsResponse() {
    }

    public Long getRoomId() {
        return roomId;
    }

    public void setRoomId(Long roomId) {
        this.roomId = roomId;
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

    public String getViewType() {
        return viewType;
    }

    public void setViewType(String viewType) {
        this.viewType = viewType;
    }

    public Boolean getExtendable() {
        return extendable;
    }

    public void setExtendable(Boolean extendable) {
        this.extendable = extendable;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}