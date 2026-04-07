DROP TABLE IF EXISTS room_amenity CASCADE;
DROP TABLE IF EXISTS amenity CASCADE;
DROP TABLE IF EXISTS hotel_phone CASCADE;
DROP TABLE IF EXISTS hotel_email CASCADE;
DROP TABLE IF EXISTS hotel_chain_phone CASCADE;
DROP TABLE IF EXISTS hotel_chain_email CASCADE;
DROP TABLE IF EXISTS rental CASCADE;
DROP TABLE IF EXISTS reservation CASCADE;
DROP TABLE IF EXISTS employee CASCADE;
DROP TABLE IF EXISTS customer CASCADE;
DROP TABLE IF EXISTS room CASCADE;
DROP TABLE IF EXISTS hotel CASCADE;
DROP TABLE IF EXISTS hotel_chain CASCADE;
DROP TABLE IF EXISTS address CASCADE;

CREATE TABLE address (
    address_id SERIAL PRIMARY KEY,
    street_number INTEGER NOT NULL,
    street_name VARCHAR(150) NOT NULL,
    city VARCHAR(100) NOT NULL,
    province VARCHAR(100) NOT NULL,
    postal_code VARCHAR(20) NOT NULL,
    country VARCHAR(100) NOT NULL
);

CREATE TABLE hotel_chain (
    chain_id SERIAL PRIMARY KEY,
    name VARCHAR(150) NOT NULL,
    hotel_count INTEGER NOT NULL CHECK (hotel_count >= 0),
    address_id INTEGER NOT NULL,
    CONSTRAINT fk_hotel_chain_address
        FOREIGN KEY (address_id) REFERENCES address(address_id)
);

CREATE TABLE hotel_chain_email (
    chain_id INTEGER NOT NULL,
    email VARCHAR(150) NOT NULL,
    PRIMARY KEY (chain_id, email),
    CONSTRAINT fk_hotel_chain_email_chain
        FOREIGN KEY (chain_id) REFERENCES hotel_chain(chain_id)
            ON DELETE CASCADE
);

CREATE TABLE hotel_chain_phone (
    chain_id INTEGER NOT NULL,
    phone VARCHAR(30) NOT NULL,
    PRIMARY KEY (chain_id, phone),
    CONSTRAINT fk_hotel_chain_phone_chain
        FOREIGN KEY (chain_id) REFERENCES hotel_chain(chain_id)
            ON DELETE CASCADE
);

CREATE TABLE hotel (
    hotel_id SERIAL PRIMARY KEY,
    chain_id INTEGER NOT NULL,
    manager_id INTEGER,
    name VARCHAR(150) NOT NULL,
    category INTEGER NOT NULL CHECK (category BETWEEN 1 AND 5),
    room_count INTEGER NOT NULL CHECK (room_count >= 0),
    address_id INTEGER NOT NULL,
    CONSTRAINT fk_hotel_chain
        FOREIGN KEY (chain_id) REFERENCES hotel_chain(chain_id),
    CONSTRAINT fk_hotel_address
        FOREIGN KEY (address_id) REFERENCES address(address_id)
);

CREATE TABLE hotel_email (
    hotel_id INTEGER NOT NULL,
    email VARCHAR(150) NOT NULL,
    PRIMARY KEY (hotel_id, email),
    CONSTRAINT fk_hotel_email_hotel
        FOREIGN KEY (hotel_id) REFERENCES hotel(hotel_id)
            ON DELETE CASCADE
);

CREATE TABLE hotel_phone (
    hotel_id INTEGER NOT NULL,
    phone VARCHAR(30) NOT NULL,
    PRIMARY KEY (hotel_id, phone),
    CONSTRAINT fk_hotel_phone_hotel
        FOREIGN KEY (hotel_id) REFERENCES hotel(hotel_id)
            ON DELETE CASCADE
);

CREATE TABLE room (
    room_id SERIAL PRIMARY KEY,
    hotel_id INTEGER NOT NULL,
    price DECIMAL(10,2) NOT NULL CHECK (price > 0),
    capacity VARCHAR(50) NOT NULL,
    view_type VARCHAR(50),
    is_extendable BOOLEAN NOT NULL DEFAULT FALSE,
    status VARCHAR(100) NOT NULL,
    CONSTRAINT fk_room_hotel
        FOREIGN KEY (hotel_id) REFERENCES hotel(hotel_id)
);

CREATE TABLE amenity (
    amenity_id SERIAL PRIMARY KEY,
    amenity_name VARCHAR(100) NOT NULL UNIQUE
);

CREATE TABLE room_amenity (
    room_id INTEGER NOT NULL,
    amenity_id INTEGER NOT NULL,
    PRIMARY KEY (room_id, amenity_id),
    CONSTRAINT fk_room_amenity_room
        FOREIGN KEY (room_id) REFERENCES room(room_id)
            ON DELETE CASCADE,
    CONSTRAINT fk_room_amenity_amenity
        FOREIGN KEY (amenity_id) REFERENCES amenity(amenity_id)
            ON DELETE CASCADE
);

CREATE TABLE customer (
    customer_id SERIAL PRIMARY KEY,
    ssn VARCHAR(20) NOT NULL UNIQUE,
    first_name VARCHAR(100) NOT NULL,
    last_name VARCHAR(100) NOT NULL,
    registration_date DATE NOT NULL,
    address_id INTEGER NOT NULL,
    CONSTRAINT fk_customer_address
        FOREIGN KEY (address_id) REFERENCES address(address_id)
);

CREATE TABLE employee (
    employee_id SERIAL PRIMARY KEY,
    hotel_id INTEGER NOT NULL,
    first_name VARCHAR(100) NOT NULL,
    last_name VARCHAR(100) NOT NULL,
    ssn VARCHAR(20) NOT NULL UNIQUE,
    role VARCHAR(100) NOT NULL,
    address_id INTEGER NOT NULL,
    CONSTRAINT fk_employee_hotel
        FOREIGN KEY (hotel_id) REFERENCES hotel(hotel_id),
    CONSTRAINT fk_employee_address
        FOREIGN KEY (address_id) REFERENCES address(address_id)
);

CREATE TABLE reservation (
    reservation_id SERIAL PRIMARY KEY,
    room_id INTEGER NOT NULL,
    customer_id INTEGER NOT NULL,
    start_date DATE NOT NULL,
    end_date DATE NOT NULL,
    CONSTRAINT fk_reservation_room
        FOREIGN KEY (room_id) REFERENCES room(room_id),
    CONSTRAINT fk_reservation_customer
        FOREIGN KEY (customer_id) REFERENCES customer(customer_id),
    CONSTRAINT chk_reservation_dates
        CHECK (start_date < end_date)
);

CREATE TABLE rental (
    rental_id SERIAL PRIMARY KEY,
    customer_id INTEGER NOT NULL,
    employee_id INTEGER NOT NULL,
    reservation_id INTEGER NULL,
    room_id INTEGER NOT NULL,
    start_date DATE NOT NULL,
    end_date DATE NOT NULL,
    CONSTRAINT fk_rental_customer
        FOREIGN KEY (customer_id) REFERENCES customer(customer_id),
    CONSTRAINT fk_rental_employee
        FOREIGN KEY (employee_id) REFERENCES employee(employee_id),
    CONSTRAINT fk_rental_reservation
        FOREIGN KEY (reservation_id) REFERENCES reservation(reservation_id),
    CONSTRAINT fk_rental_room
        FOREIGN KEY (room_id) REFERENCES room(room_id),
    CONSTRAINT chk_rental_dates
        CHECK (start_date < end_date)
);

ALTER TABLE hotel
ADD CONSTRAINT fk_hotel_manager
FOREIGN KEY (manager_id) REFERENCES employee(employee_id);
