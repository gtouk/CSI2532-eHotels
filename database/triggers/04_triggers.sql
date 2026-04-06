-- =====================================================
-- 04_triggers.sql
-- Triggers pour e-Hotels
-- =====================================================

-- -----------------------------------------------------
-- Trigger 1 : empecher les reservations qui se chevauchent
-- -----------------------------------------------------

CREATE OR REPLACE FUNCTION verifier_chevauchement_reservation()
RETURNS TRIGGER AS $$
BEGIN
    IF EXISTS (
        SELECT 1
        FROM reservation r
        WHERE r.id_chambre = NEW.id_chambre
          AND r.id_reservation <> COALESCE(NEW.id_reservation, -1)
          AND NEW.date_debut < r.date_fin
          AND NEW.date_fin > r.date_debut
    ) THEN
        RAISE EXCEPTION 'Reservation refusee : chevauchement detecte pour cette chambre.';
    END IF;

    IF EXISTS (
        SELECT 1
        FROM location l
        WHERE l.id_chambre = NEW.id_chambre
          AND NEW.date_debut < l.date_fin
          AND NEW.date_fin > l.date_debut
    ) THEN
        RAISE EXCEPTION 'Reservation refusee : la chambre est deja louee sur cette periode.';
    END IF;

    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER trg_verifier_chevauchement_reservation
BEFORE INSERT OR UPDATE ON reservation
FOR EACH ROW
EXECUTE FUNCTION verifier_chevauchement_reservation();

-- -----------------------------------------------------
-- Trigger 2 : empecher les locations qui se chevauchent
-- -----------------------------------------------------

CREATE OR REPLACE FUNCTION verifier_chevauchement_location()
RETURNS TRIGGER AS $$
BEGIN
    IF EXISTS (
        SELECT 1
        FROM location l
        WHERE l.id_chambre = NEW.id_chambre
          AND l.id_location <> COALESCE(NEW.id_location, -1)
          AND NEW.date_debut < l.date_fin
          AND NEW.date_fin > l.date_debut
    ) THEN
        RAISE EXCEPTION 'Location refusee : chevauchement detecte pour cette chambre.';
    END IF;

    IF EXISTS (
        SELECT 1
        FROM reservation r
        WHERE r.id_chambre = NEW.id_chambre
          AND NEW.date_debut < r.date_fin
          AND NEW.date_fin > r.date_debut
          AND (NEW.id_reservation IS NULL OR r.id_reservation <> NEW.id_reservation)
    ) THEN
        RAISE EXCEPTION 'Location refusee : conflit avec une reservation existante.';
    END IF;

    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER trg_verifier_chevauchement_location
BEFORE INSERT OR UPDATE ON location
FOR EACH ROW
EXECUTE FUNCTION verifier_chevauchement_location();