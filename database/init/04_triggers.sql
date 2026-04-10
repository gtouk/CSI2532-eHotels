-- =====================================================
-- 04_triggers.sql — Triggers e-Hotels
-- =====================================================

-- -----------------------------------------------------
-- Trigger 1 : Anti-chevauchement des réservations
-- -----------------------------------------------------
CREATE OR REPLACE FUNCTION validate_reservation_overlap()
RETURNS TRIGGER AS $$
BEGIN
    -- Vérifier chevauchement avec réservations actives
    IF EXISTS (
        SELECT 1 FROM reservation r
        WHERE r.room_id = NEW.room_id
          AND r.reservation_id <> COALESCE(NEW.reservation_id, -1)
          AND r.status = 'RESERVED'
          AND NEW.start_date < r.end_date
          AND NEW.end_date > r.start_date
    ) THEN
        RAISE EXCEPTION 'Réservation refusée : chevauchement avec une réservation existante.';
    END IF;

    -- Vérifier chevauchement avec locations actives
    IF EXISTS (
        SELECT 1 FROM rental rt
        WHERE rt.room_id = NEW.room_id
          AND rt.status = 'ACTIVE'
          AND NEW.start_date < rt.end_date
          AND NEW.end_date > rt.start_date
    ) THEN
        RAISE EXCEPTION 'Réservation refusée : la chambre est déjà louée sur cette période.';
    END IF;

    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER trg_validate_reservation_overlap
BEFORE INSERT OR UPDATE ON reservation
FOR EACH ROW
EXECUTE FUNCTION validate_reservation_overlap();

-- -----------------------------------------------------
-- Trigger 2 : Anti-chevauchement des locations
-- -----------------------------------------------------
CREATE OR REPLACE FUNCTION validate_rental_overlap()
RETURNS TRIGGER AS $$
BEGIN
    IF EXISTS (
        SELECT 1 FROM rental rt
        WHERE rt.room_id = NEW.room_id
          AND rt.rental_id <> COALESCE(NEW.rental_id, -1)
          AND rt.status = 'ACTIVE'
          AND NEW.start_date < rt.end_date
          AND NEW.end_date > rt.start_date
    ) THEN
        RAISE EXCEPTION 'Location refusée : chevauchement avec une location existante.';
    END IF;

    IF EXISTS (
        SELECT 1 FROM reservation r
        WHERE r.room_id = NEW.room_id
          AND r.status = 'RESERVED'
          AND NEW.start_date < r.end_date
          AND NEW.end_date > r.start_date
          AND (NEW.reservation_id IS NULL OR r.reservation_id <> NEW.reservation_id)
    ) THEN
        RAISE EXCEPTION 'Location refusée : conflit avec une réservation existante.';
    END IF;

    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER trg_validate_rental_overlap
BEFORE INSERT OR UPDATE ON rental
FOR EACH ROW
EXECUTE FUNCTION validate_rental_overlap();

-- -----------------------------------------------------
-- Trigger 3 : Mise à jour auto du statut de la chambre
-- lors d'une réservation ou d'une location
-- -----------------------------------------------------
CREATE OR REPLACE FUNCTION sync_room_status_on_reservation()
RETURNS TRIGGER AS $$
BEGIN
    -- Nouvelle réservation active → chambre RESERVED
    IF TG_OP = 'INSERT' AND NEW.status = 'RESERVED' THEN
        UPDATE room SET status = 'RESERVED' WHERE room_id = NEW.room_id;

    -- Réservation annulée ou complétée → chambre AVAILABLE (si pas d'autre reservation active)
    ELSIF TG_OP = 'UPDATE'
      AND OLD.status = 'RESERVED'
      AND NEW.status IN ('CANCELLED','COMPLETED') THEN
        IF NOT EXISTS (
            SELECT 1 FROM reservation
            WHERE room_id = NEW.room_id
              AND status = 'RESERVED'
              AND reservation_id <> NEW.reservation_id
        ) AND NOT EXISTS (
            SELECT 1 FROM rental
            WHERE room_id = NEW.room_id AND status = 'ACTIVE'
        ) THEN
            UPDATE room SET status = 'AVAILABLE' WHERE room_id = NEW.room_id;
        END IF;
    END IF;

    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER trg_sync_room_status_reservation
AFTER INSERT OR UPDATE ON reservation
FOR EACH ROW
EXECUTE FUNCTION sync_room_status_on_reservation();

-- -----------------------------------------------------
-- Trigger 4 : Mise à jour auto du statut de la chambre
-- lors d'une location
-- -----------------------------------------------------
CREATE OR REPLACE FUNCTION sync_room_status_on_rental()
RETURNS TRIGGER AS $$
BEGIN
    -- Nouvelle location active → chambre OCCUPIED
    IF TG_OP = 'INSERT' AND NEW.status = 'ACTIVE' THEN
        UPDATE room SET status = 'OCCUPIED' WHERE room_id = NEW.room_id;

    -- Location terminée ou annulée → chambre AVAILABLE
    ELSIF TG_OP = 'UPDATE'
      AND OLD.status = 'ACTIVE'
      AND NEW.status IN ('COMPLETED','CANCELLED') THEN
        IF NOT EXISTS (
            SELECT 1 FROM rental
            WHERE room_id = NEW.room_id
              AND status = 'ACTIVE'
              AND rental_id <> NEW.rental_id
        ) AND NOT EXISTS (
            SELECT 1 FROM reservation
            WHERE room_id = NEW.room_id AND status = 'RESERVED'
        ) THEN
            UPDATE room SET status = 'AVAILABLE' WHERE room_id = NEW.room_id;
        END IF;
    END IF;

    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER trg_sync_room_status_rental
AFTER INSERT OR UPDATE ON rental
FOR EACH ROW
EXECUTE FUNCTION sync_room_status_on_rental();

-- -----------------------------------------------------
-- Trigger 5 : Expiration automatique des réservations
-- Appelé par le scheduler Java OU manuellement
-- -----------------------------------------------------
CREATE OR REPLACE FUNCTION expire_overdue_reservations()
RETURNS void AS $$
BEGIN
    UPDATE reservation
    SET status = 'CANCELLED'
    WHERE status = 'RESERVED'
      AND end_date < CURRENT_DATE;

    -- Les rooms associées redeviennent AVAILABLE via trigger 3
END;
$$ LANGUAGE plpgsql;
