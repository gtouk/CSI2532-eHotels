-- =========================================================
-- DEV SEED BUSINESS DATA
-- aligned with the real English schema + backend patch
-- =========================================================

-- Addresses
INSERT INTO address (address_id, street_number, street_name, city, province, postal_code, country)
VALUES
    (1, 100, 'Dev Street', 'Ottawa', 'Ontario', 'K1A0A1', 'Canada'),
    (2, 200, 'King Street', 'Ottawa', 'Ontario', 'K1A0A2', 'Canada'),
    (3, 300, 'Queen Street', 'Ottawa', 'Ontario', 'K1A0A3', 'Canada')
ON CONFLICT (address_id) DO NOTHING;

-- Hotel chain
INSERT INTO hotel_chain (chain_id, name, hotel_count, address_id)
VALUES
    (1, 'eHotel Group', 1, 1)
ON CONFLICT (chain_id) DO NOTHING;

-- Hotel
INSERT INTO hotel (hotel_id, chain_id, manager_id, name, category, room_count, address_id)
VALUES
    (1, 1, NULL, 'eHotel Downtown', 4, 2, 2)
ON CONFLICT (hotel_id) DO NOTHING;

-- Rooms
INSERT INTO room (
    room_id,
    hotel_id,
    price,
    capacity,
    view_type,
    is_extendable,
    status,
    room_number
)
VALUES
    (1, 1, 120.00, 'double', 'CITY', FALSE, 'AVAILABLE', '101'),
    (2, 1, 220.00, 'suite',  'CITY', TRUE,  'AVAILABLE', '102')
ON CONFLICT (room_id) DO NOTHING;

-- Customer
INSERT INTO customer (
    customer_id,
    ssn,
    first_name,
    last_name,
    registration_date,
    address_id,
    email,
    phone,
    address,
    id_type,
    id_number
)
VALUES
    (
        1,
        'CUST-001',
        'Alice',
        'Martin',
        CURRENT_DATE,
        3,
        'alice.martin@example.com',
        '6130000000',
        '300 Queen Street, Ottawa, Canada',
        'PASSPORT',
        'P1234567'
    )
ON CONFLICT (customer_id) DO NOTHING;

-- Reservation
INSERT INTO reservation (
    reservation_id,
    room_id,
    customer_id,
    start_date,
    end_date,
    status,
    total_price,
    created_at
)
VALUES
    (
        1,
        1,
        1,
        CURRENT_DATE,
        CURRENT_DATE + 2,
        'RESERVED',
        240.00,
        NOW()
    )
ON CONFLICT (reservation_id) DO NOTHING;