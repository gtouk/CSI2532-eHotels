-- =====================================================
-- 05_indexes_en.sql
-- Indexes for e-Hotels
-- =====================================================

-- -----------------------------------------------------
-- Index 1
-- Speeds up joins and room searches by hotel
-- -----------------------------------------------------
CREATE INDEX idx_room_hotel_id
ON room(hotel_id);

-- -----------------------------------------------------
-- Index 2
-- Speeds up reservation conflict checks
-- and searches by room and period
-- -----------------------------------------------------
CREATE INDEX idx_reservation_room_dates
ON reservation(room_id, start_date, end_date);

-- -----------------------------------------------------
-- Index 3
-- Speeds up rental conflict checks
-- and searches by room and period
-- -----------------------------------------------------
CREATE INDEX idx_rental_room_dates
ON rental(room_id, start_date, end_date);

-- -----------------------------------------------------
-- Index 4
-- Speeds up hotel searches by chain
-- -----------------------------------------------------
CREATE INDEX idx_hotel_chain_id
ON hotel(chain_id);

-- -----------------------------------------------------
-- Index 5
-- Speeds up hotel searches by address/area
-- -----------------------------------------------------
CREATE INDEX idx_hotel_address_id
ON hotel(address_id);
