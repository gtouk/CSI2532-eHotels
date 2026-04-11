CREATE EXTENSION IF NOT EXISTS pgcrypto;

-- =========================
-- Commodités
-- =========================
INSERT INTO amenity (amenity_name) VALUES
('TV'),
('WiFi'),
('Air Conditioning'),
('Mini Fridge'),
('Coffee Machine'),
('Balcony'),
('Ocean View'),
('Mountain View');

-- =========================
-- Adresses sièges sociaux
-- =========================
INSERT INTO address (street_number, street_name, city, province, postal_code, country) VALUES
(100, 'Boulevard Central', 'Montreal', 'Quebec', 'H1A1A1', 'Canada'),
(200, 'Rue du Lac', 'Quebec', 'Quebec', 'G1A1A1', 'Canada'),
(300, 'King Street', 'Toronto', 'Ontario', 'M5A1A1', 'Canada'),
(400, 'Ocean Avenue', 'Vancouver', 'British Columbia', 'V5K0A1', 'Canada'),
(500, 'Main Street', 'Calgary', 'Alberta', 'T2A1A1', 'Canada');

-- =========================
-- 5 chaînes hôtelières
-- =========================
INSERT INTO hotel_chain (name, hotel_count, address_id) VALUES
('Nordic Stay', 0, 1),
('Maple Suites', 0, 2),
('Urban Crown', 0, 3),
('Pacific Comfort', 0, 4),
('Prairie Luxe', 0, 5);

INSERT INTO hotel_chain_email (chain_id, email) VALUES
(1, 'contact@nordicstay.com'),
(1, 'support@nordicstay.com'),
(2, 'contact@maplesuites.com'),
(2, 'booking@maplesuites.com'),
(3, 'info@urbancrown.com'),
(3, 'corporate@urbancrown.com'),
(4, 'hello@pacificcomfort.com'),
(4, 'service@pacificcomfort.com'),
(5, 'info@prairieluxe.com'),
(5, 'reservations@prairieluxe.com');

INSERT INTO hotel_chain_phone (chain_id, phone) VALUES
(1, '514-555-1000'),
(1, '514-555-1001'),
(2, '418-555-2000'),
(2, '418-555-2001'),
(3, '416-555-3000'),
(3, '416-555-3001'),
(4, '604-555-4000'),
(4, '604-555-4001'),
(5, '403-555-5000'),
(5, '403-555-5001');

-- =========================
-- Adresses hôtels
-- =========================
INSERT INTO address (street_number, street_name, city, province, postal_code, country) VALUES
(11, 'Rue A', 'Montreal', 'Quebec', 'H2A1A1', 'Canada'),
(12, 'Rue B', 'Montreal', 'Quebec', 'H2A1A2', 'Canada'),
(21, 'Rue C', 'Quebec', 'Quebec', 'G1B1B1', 'Canada'),
(22, 'Rue D', 'Quebec', 'Quebec', 'G1B1B2', 'Canada'),
(31, 'King West', 'Toronto', 'Ontario', 'M5V1A1', 'Canada'),
(32, 'King East', 'Toronto', 'Ontario', 'M5V1A2', 'Canada'),
(41, 'Marine Drive', 'Vancouver', 'British Columbia', 'V6A1A1', 'Canada'),
(42, 'Sunset Road', 'Vancouver', 'British Columbia', 'V6A1A2', 'Canada'),
(51, 'Prairie Road', 'Calgary', 'Alberta', 'T3A1A1', 'Canada'),
(52, 'Downtown Ave', 'Calgary', 'Alberta', 'T3A1A2', 'Canada');

-- =========================
-- Hôtels
-- =========================
INSERT INTO hotel (chain_id, manager_id, name, category, room_count, address_id) VALUES
(1, NULL, 'Nordic Stay Montreal Centre', 4, 5, 6),
(1, NULL, 'Nordic Stay Montreal Nord', 3, 5, 7),
(2, NULL, 'Maple Suites Quebec Centre', 5, 5, 8),
(2, NULL, 'Maple Suites Quebec Vieux-Port', 4, 5, 9),
(3, NULL, 'Urban Crown Toronto Downtown', 5, 5, 10),
(3, NULL, 'Urban Crown Toronto East', 3, 5, 11),
(4, NULL, 'Pacific Comfort Vancouver Bay', 4, 5, 12),
(4, NULL, 'Pacific Comfort Vancouver Sunset', 5, 5, 13),
(5, NULL, 'Prairie Luxe Calgary Centre', 4, 5, 14),
(5, NULL, 'Prairie Luxe Calgary South', 3, 5, 15);

INSERT INTO hotel_email (hotel_id, email) VALUES
(1, 'contact1@nordicstay.com'),
(2, 'contact2@nordicstay.com'),
(3, 'contact1@maplesuites.com'),
(4, 'contact2@maplesuites.com'),
(5, 'contact1@urbancrown.com'),
(6, 'contact2@urbancrown.com'),
(7, 'contact1@pacificcomfort.com'),
(8, 'contact2@pacificcomfort.com'),
(9, 'contact1@prairieluxe.com'),
(10, 'contact2@prairieluxe.com');

INSERT INTO hotel_phone (hotel_id, phone) VALUES
(1, '514-600-0001'),
(2, '514-600-0002'),
(3, '418-600-0001'),
(4, '418-600-0002'),
(5, '416-600-0001'),
(6, '416-600-0002'),
(7, '604-600-0001'),
(8, '604-600-0002'),
(9, '403-600-0001'),
(10, '403-600-0002');

-- =========================
-- Employés / gestionnaires
-- Mot de passe: password
-- =========================
INSERT INTO address (street_number, street_name, city, province, postal_code, country) VALUES
(601, 'Manager Road', 'Montreal', 'Quebec', 'H3A1A1', 'Canada'),
(602, 'Staff Road', 'Montreal', 'Quebec', 'H3A1A2', 'Canada'),
(603, 'Manager Road', 'Quebec', 'Quebec', 'G2A1A1', 'Canada'),
(604, 'Staff Road', 'Quebec', 'Quebec', 'G2A1A2', 'Canada');

INSERT INTO employee (
  hotel_id, first_name, last_name, ssn, role, address_id, email, phone, password, active
) VALUES
(1, 'Marie', 'Manager', 'MGR-001', 'GESTIONNAIRE', 16, 'manager1@ehotel.local', '514-555-9001', crypt('password', gen_salt('bf')), TRUE),
(1, 'Paul', 'Employe', 'EMP-001', 'EMPLOYE', 17, 'employee1@ehotel.local', '514-555-9002', crypt('password', gen_salt('bf')), TRUE),
(3, 'Nora', 'Manager', 'MGR-002', 'GESTIONNAIRE', 18, 'manager2@ehotel.local', '418-555-9001', crypt('password', gen_salt('bf')), TRUE),
(3, 'Leo', 'Employe', 'EMP-002', 'EMPLOYE', 19, 'employee2@ehotel.local', '418-555-9002', crypt('password', gen_salt('bf')), TRUE);

UPDATE hotel SET manager_id = 1 WHERE hotel_id = 1;
UPDATE hotel SET manager_id = 3 WHERE hotel_id = 3;

-- =========================
-- Clients
-- Mot de passe: password
-- =========================
INSERT INTO address (street_number, street_name, city, province, postal_code, country) VALUES
(701, 'Client Street', 'Gatineau', 'Quebec', 'J8X1A1', 'Canada'),
(702, 'Client Street', 'Ottawa', 'Ontario', 'K1A1A1', 'Canada');

INSERT INTO customer (
  ssn, first_name, last_name, registration_date, address_id, email, password_hash
) VALUES
('CLI-001', 'Alice', 'Martin', CURRENT_DATE, 20, 'alice@ehotel.local', crypt('password', gen_salt('bf'))),
('CLI-002', 'Bilal', 'Khan', CURRENT_DATE, 21, 'bilal@ehotel.local', crypt('password', gen_salt('bf')));

-- =========================
-- Chambres (5 par hôtel)
-- =========================
INSERT INTO room (hotel_id, room_number, price, capacity, view_type, is_extendable, status) VALUES
(1, '101', 120.00, 'single', 'CITY', FALSE, 'AVAILABLE'),
(1, '102', 150.00, 'double', 'CITY', TRUE, 'AVAILABLE'),
(1, '103', 180.00, 'triple', 'CITY', TRUE, 'AVAILABLE'),
(1, '104', 220.00, 'suite', 'NONE', TRUE, 'AVAILABLE'),
(1, '105', 250.00, 'family', 'CITY', TRUE, 'AVAILABLE'),

(2, '201', 110.00, 'single', 'CITY', FALSE, 'AVAILABLE'),
(2, '202', 145.00, 'double', 'CITY', TRUE, 'AVAILABLE'),
(2, '203', 175.00, 'triple', 'CITY', TRUE, 'AVAILABLE'),
(2, '204', 215.00, 'suite', 'NONE', TRUE, 'AVAILABLE'),
(2, '205', 245.00, 'family', 'CITY', TRUE, 'AVAILABLE'),

(3, '301', 130.00, 'single', 'CITY', FALSE, 'AVAILABLE'),
(3, '302', 160.00, 'double', 'CITY', TRUE, 'AVAILABLE'),
(3, '303', 190.00, 'triple', 'CITY', TRUE, 'AVAILABLE'),
(3, '304', 240.00, 'suite', 'NONE', TRUE, 'AVAILABLE'),
(3, '305', 270.00, 'family', 'CITY', TRUE, 'AVAILABLE'),

(4, '401', 125.00, 'single', 'CITY', FALSE, 'AVAILABLE'),
(4, '402', 155.00, 'double', 'CITY', TRUE, 'AVAILABLE'),
(4, '403', 185.00, 'triple', 'CITY', TRUE, 'AVAILABLE'),
(4, '404', 230.00, 'suite', 'NONE', TRUE, 'AVAILABLE'),
(4, '405', 260.00, 'family', 'CITY', TRUE, 'AVAILABLE'),

(5, '501', 140.00, 'single', 'CITY', FALSE, 'AVAILABLE'),
(5, '502', 170.00, 'double', 'CITY', TRUE, 'AVAILABLE'),
(5, '503', 200.00, 'triple', 'CITY', TRUE, 'AVAILABLE'),
(5, '504', 255.00, 'suite', 'NONE', TRUE, 'AVAILABLE'),
(5, '505', 285.00, 'family', 'CITY', TRUE, 'AVAILABLE'),

(6, '601', 115.00, 'single', 'CITY', FALSE, 'AVAILABLE'),
(6, '602', 150.00, 'double', 'CITY', TRUE, 'AVAILABLE'),
(6, '603', 180.00, 'triple', 'CITY', TRUE, 'AVAILABLE'),
(6, '604', 225.00, 'suite', 'NONE', TRUE, 'AVAILABLE'),
(6, '605', 255.00, 'family', 'CITY', TRUE, 'AVAILABLE'),

(7, '701', 135.00, 'single', 'SEA', FALSE, 'AVAILABLE'),
(7, '702', 175.00, 'double', 'SEA', TRUE, 'AVAILABLE'),
(7, '703', 205.00, 'triple', 'SEA', TRUE, 'AVAILABLE'),
(7, '704', 260.00, 'suite', 'SEA', TRUE, 'AVAILABLE'),
(7, '705', 295.00, 'family', 'SEA', TRUE, 'AVAILABLE'),

(8, '801', 145.00, 'single', 'SEA', FALSE, 'AVAILABLE'),
(8, '802', 180.00, 'double', 'SEA', TRUE, 'AVAILABLE'),
(8, '803', 210.00, 'triple', 'SEA', TRUE, 'AVAILABLE'),
(8, '804', 265.00, 'suite', 'SEA', TRUE, 'AVAILABLE'),
(8, '805', 300.00, 'family', 'SEA', TRUE, 'AVAILABLE'),

(9, '901', 118.00, 'single', 'MOUNTAIN', FALSE, 'AVAILABLE'),
(9, '902', 148.00, 'double', 'MOUNTAIN', TRUE, 'AVAILABLE'),
(9, '903', 178.00, 'triple', 'MOUNTAIN', TRUE, 'AVAILABLE'),
(9, '904', 228.00, 'suite', 'MOUNTAIN', TRUE, 'AVAILABLE'),
(9, '905', 258.00, 'family', 'MOUNTAIN', TRUE, 'AVAILABLE'),

(10, '1001', 122.00, 'single', 'MOUNTAIN', FALSE, 'AVAILABLE'),
(10, '1002', 152.00, 'double', 'MOUNTAIN', TRUE, 'AVAILABLE'),
(10, '1003', 182.00, 'triple', 'MOUNTAIN', TRUE, 'AVAILABLE'),
(10, '1004', 232.00, 'suite', 'MOUNTAIN', TRUE, 'AVAILABLE'),
(10, '1005', 262.00, 'family', 'MOUNTAIN', TRUE, 'AVAILABLE');

-- =========================
-- Room amenities
-- =========================
INSERT INTO room_amenity (room_id, amenity_id) VALUES
(1, 1), (1, 2), (1, 3),
(2, 1), (2, 2), (2, 4),
(4, 1), (4, 2), (4, 3), (4, 5),
(7, 1), (7, 2),
(10, 1), (10, 2), (10, 5),
(31, 1), (31, 2), (31, 6),
(36, 1), (36, 2), (36, 6),
(41, 1), (41, 2), (41, 8),
(46, 1), (46, 2), (46, 8);

-- =========================
-- Réservations
-- =========================
INSERT INTO reservation (
  room_id, customer_id, start_date, end_date, status, total_price, created_at
) VALUES
(2, 1, CURRENT_DATE + 7, CURRENT_DATE + 10, 'RESERVED', 450.00, NOW()),
(12, 2, CURRENT_DATE + 5, CURRENT_DATE + 7, 'RESERVED', 320.00, NOW());

-- =========================
-- Locations
-- =========================
INSERT INTO rental (
  customer_id, employee_id, reservation_id, room_id, start_date, end_date, status
) VALUES
(1, 2, NULL, 4, CURRENT_DATE, CURRENT_DATE + 2, 'ACTIVE');

UPDATE room SET status = 'OCCUPIED' WHERE room_id = 4;
UPDATE room SET status = 'RESERVED' WHERE room_id IN (2, 12);

-- =========================
-- Recalcul hotel_count
-- =========================
UPDATE hotel_chain hc
SET hotel_count = (
  SELECT COUNT(*) FROM hotel h WHERE h.chain_id = hc.chain_id
);