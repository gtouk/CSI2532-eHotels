-- =====================================================
-- 05_indexes.sql
-- Index pour e-Hotels
-- =====================================================

-- -----------------------------------------------------
-- Index 1
-- Accelere les jointures et recherches des chambres par hotel
-- -----------------------------------------------------
CREATE INDEX idx_chambre_id_hotel
ON chambre(id_hotel);

-- -----------------------------------------------------
-- Index 2
-- Accelere la verification des conflits de reservations
-- et les recherches par chambre et periode
-- -----------------------------------------------------
CREATE INDEX idx_reservation_chambre_dates
ON reservation(id_chambre, date_debut, date_fin);

-- -----------------------------------------------------
-- Index 3
-- Accelere la verification des conflits de locations
-- et les recherches par chambre et periode
-- -----------------------------------------------------
CREATE INDEX idx_location_chambre_dates
ON location(id_chambre, date_debut, date_fin);

-- -----------------------------------------------------
-- Index 4
-- Accelere la recherche des hotels par chaine
-- -----------------------------------------------------
CREATE INDEX idx_hotel_id_chaine
ON hotel(id_chaine);

-- -----------------------------------------------------
-- Index 5
-- Accelere la recherche des hotels par adresse/zone
-- -----------------------------------------------------
CREATE INDEX idx_hotel_id_adresse
ON hotel(id_adresse);