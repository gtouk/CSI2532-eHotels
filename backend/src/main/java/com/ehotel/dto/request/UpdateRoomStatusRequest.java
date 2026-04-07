package com.ehotel.dto.request;

import com.ehotel.enums.RoomStatus;

public class UpdateRoomStatusRequest {

    private RoomStatus status;

    public UpdateRoomStatusRequest() {
    }

    public RoomStatus getStatus() {
        return status;
    }

    public void setStatus(RoomStatus status) {
        this.status = status;
    }
}