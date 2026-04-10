package com.ehotel.dto.response;

import java.util.List;

public class ClientDashboardResponse {

    private ClientSummaryResponse client;
    private Long totalReservations;
    private Long activeReservations;
    private Long cancelledReservations;
    private List<ReservationSummaryResponse> reservations;

    public ClientDashboardResponse() {
    }

    public ClientSummaryResponse getClient() {
        return client;
    }

    public void setClient(ClientSummaryResponse client) {
        this.client = client;
    }

    public Long getTotalReservations() {
        return totalReservations;
    }

    public void setTotalReservations(Long totalReservations) {
        this.totalReservations = totalReservations;
    }

    public Long getActiveReservations() {
        return activeReservations;
    }

    public void setActiveReservations(Long activeReservations) {
        this.activeReservations = activeReservations;
    }

    public Long getCancelledReservations() {
        return cancelledReservations;
    }

    public void setCancelledReservations(Long cancelledReservations) {
        this.cancelledReservations = cancelledReservations;
    }

    public List<ReservationSummaryResponse> getReservations() {
        return reservations;
    }

    public void setReservations(List<ReservationSummaryResponse> reservations) {
        this.reservations = reservations;
    }
}