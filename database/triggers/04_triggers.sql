-- =====================================================
-- 04_triggers_en.sql
-- Triggers for e-Hotels
-- =====================================================

-- -----------------------------------------------------
-- Trigger 1: prevent overlapping reservations
-- -----------------------------------------------------

CREATE OR REPLACE FUNCTION validate_reservation_overlap()
RETURNS TRIGGER AS $$
BEGIN
    IF EXISTS (
        SELECT 1
        FROM reservation r
        WHERE r.room_id = NEW.room_id
          AND r.reservation_id <> COALESCE(NEW.reservation_id, -1)
          AND r.status = 'RESERVED'
          AND NEW.start_date < r.end_date
          AND NEW.end_date > r.start_date
    ) THEN
        RAISE EXCEPTION 'Reservation rejected: overlapping booking detected for this room.';
    END IF;

    IF EXISTS (
        SELECT 1
        FROM rental rt
        WHERE rt.room_id = NEW.room_id
          AND rt.status = 'ACTIVE'
          AND NEW.start_date < rt.end_date
          AND NEW.end_date > rt.start_date
    ) THEN
        RAISE EXCEPTION 'Reservation rejected: room is already rented for this period.';
    END IF;

    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER trg_validate_reservation_overlap
BEFORE INSERT OR UPDATE ON reservation
FOR EACH ROW
EXECUTE FUNCTION validate_reservation_overlap();

-- -----------------------------------------------------
-- Trigger 2: prevent overlapping rentals
-- -----------------------------------------------------

CREATE OR REPLACE FUNCTION validate_rental_overlap()
RETURNS TRIGGER AS $$
BEGIN
    IF EXISTS (
        SELECT 1
        FROM rental rt
        WHERE rt.room_id = NEW.room_id
          AND rt.rental_id <> COALESCE(NEW.rental_id, -1)
          AND rt.status = 'ACTIVE'
          AND NEW.start_date < rt.end_date
          AND NEW.end_date > rt.start_date
    ) THEN
        RAISE EXCEPTION 'Rental rejected: overlapping rental detected for this room.';
    END IF;

    IF EXISTS (
        SELECT 1
        FROM reservation r
        WHERE r.room_id = NEW.room_id
          AND r.status = 'RESERVED'
          AND NEW.start_date < r.end_date
          AND NEW.end_date > r.start_date
          AND (NEW.reservation_id IS NULL OR r.reservation_id <> NEW.reservation_id)
    ) THEN
        RAISE EXCEPTION 'Rental rejected: conflict with an existing reservation.';
    END IF;

    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER trg_validate_rental_overlap
BEFORE INSERT OR UPDATE ON rental
FOR EACH ROW
EXECUTE FUNCTION validate_rental_overlap();
