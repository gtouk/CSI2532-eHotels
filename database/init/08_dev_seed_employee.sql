-- Minimal dev seed so /employee/login can be tested.
-- Safe to run after 01_schema_en.sql + 07_backend_alignment_patch.sql.

INSERT INTO address (street_number, street_name, city, province, postal_code, country)
SELECT 100, 'Dev Street', 'Ottawa', 'Ontario', 'K1A0A1', 'Canada'
WHERE NOT EXISTS (
    SELECT 1 FROM address WHERE street_number = 100 AND street_name = 'Dev Street'
);

INSERT INTO hotel_chain (name, hotel_count, address_id)
SELECT 'Dev Chain', 1, address_id
FROM address
WHERE street_number = 100 AND street_name = 'Dev Street'
  AND NOT EXISTS (SELECT 1 FROM hotel_chain WHERE name = 'Dev Chain');

INSERT INTO hotel (chain_id, manager_id, name, category, room_count, address_id)
SELECT hc.chain_id, NULL, 'Dev Hotel', 4, 10, a.address_id
FROM hotel_chain hc
JOIN address a ON a.street_number = 100 AND a.street_name = 'Dev Street'
WHERE hc.name = 'Dev Chain'
  AND NOT EXISTS (SELECT 1 FROM hotel WHERE name = 'Dev Hotel');

INSERT INTO employee (hotel_id, first_name, last_name, ssn, role, address_id, email, phone, password, active)
SELECT h.hotel_id, 'Test', 'Employee', 'DEV-EMP-001', 'EMPLOYE', a.address_id,
       'employee1@ehotel.com', '555-000-0001', 'password123', TRUE
FROM hotel h
JOIN address a ON a.street_number = 100 AND a.street_name = 'Dev Street'
WHERE h.name = 'Dev Hotel'
  AND NOT EXISTS (SELECT 1 FROM employee WHERE email = 'employee1@ehotel.com');
