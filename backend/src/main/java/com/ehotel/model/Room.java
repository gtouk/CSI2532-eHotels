package com.ehotel.model;

import com.ehotel.enums.RoomStatus;
import com.ehotel.enums.ViewType;
import jakarta.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "room")
public class Room {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "room_id")
    private Long roomId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "hotel_id", nullable = false)
    private Hotel hotel;

    @Column(name = "room_number", length = 20)
    private String roomNumber;

    @Column(name = "price", nullable = false, precision = 10, scale = 2)
    private BigDecimal pricePerNight;

    @Column(name = "capacity", nullable = false, length = 50)
    private String capacity;

    @Enumerated(EnumType.STRING)
    @Column(name = "view_type", length = 50)
    private ViewType viewType;

    @Column(name = "is_extendable", nullable = false)
    private Boolean extendable = false;

    @Column(name = "surface_area")
    private Integer surfaceArea;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 30)
    private RoomStatus status = RoomStatus.AVAILABLE;

    public Room() {}

    public Long getRoomId() { return roomId; }
    public void setRoomId(Long roomId) { this.roomId = roomId; }
    public Hotel getHotel() { return hotel; }
    public void setHotel(Hotel hotel) { this.hotel = hotel; }
    public String getRoomNumber() { return roomNumber; }
    public void setRoomNumber(String roomNumber) { this.roomNumber = roomNumber; }
    public BigDecimal getPricePerNight() { return pricePerNight; }
    public void setPricePerNight(BigDecimal pricePerNight) { this.pricePerNight = pricePerNight; }
    public String getCapacity() { return capacity; }
    public void setCapacity(String capacity) { this.capacity = capacity; }
    public ViewType getViewType() { return viewType; }
    public void setViewType(ViewType viewType) { this.viewType = viewType; }
    public Boolean getExtendable() { return extendable; }
    public void setExtendable(Boolean extendable) { this.extendable = extendable; }
    public Integer getSurfaceArea() { return surfaceArea; }
    public void setSurfaceArea(Integer surfaceArea) { this.surfaceArea = surfaceArea; }
    public RoomStatus getStatus() { return status; }
    public void setStatus(RoomStatus status) { this.status = status; }
}
