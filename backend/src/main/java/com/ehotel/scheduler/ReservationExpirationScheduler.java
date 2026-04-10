package com.ehotel.scheduler;

import com.ehotel.enums.ReservationStatus;
import com.ehotel.model.Reservation;
import com.ehotel.repository.ReservationRepository;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

/**
 * Scheduler qui expire automatiquement les réservations dont la date de fin
 * est passée. S'exécute tous les jours à 2h du matin.
 *
 * Lorsqu'une réservation passe à CANCELLED, le trigger SQL
 * trg_sync_room_status_reservation remet automatiquement la chambre
 * à AVAILABLE (si aucune autre réservation/location active).
 */
@Component
public class ReservationExpirationScheduler {

    private final ReservationRepository reservationRepository;

    public ReservationExpirationScheduler(ReservationRepository reservationRepository) {
        this.reservationRepository = reservationRepository;
    }

    @Scheduled(cron = "0 0 2 * * *")   // Tous les jours à 02h00
    @Transactional
    public void expireOverdueReservations() {
        List<Reservation> expired = reservationRepository
                .findByStatusAndEndDateBefore(ReservationStatus.RESERVED, LocalDate.now());

        if (expired.isEmpty()) return;

        System.out.printf("[Scheduler] %d réservation(s) expirée(s) → annulation automatique%n",
                expired.size());

        expired.forEach(r -> r.setStatus(ReservationStatus.CANCELLED));
        reservationRepository.saveAll(expired);
        // Le trigger SQL gère le retour à AVAILABLE pour chaque chambre
    }
}
