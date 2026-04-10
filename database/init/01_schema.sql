-- =====================================================
-- 01_schema.sql — Schéma complet e-Hotels
-- PostgreSQL 16
-- =====================================================

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
    address_id    SERIAL PRIMARY KEY,
    street_number INTEGER      NOT NULL,
    street_name   VARCHAR(150) NOT NULL,
    city          VARCHAR(100) NOT NULL,
    province      VARCHAR(100) NOT NULL,
    postal_code   VARCHAR(20)  NOT NULL,
    country       VARCHAR(100) NOT NULL DEFAULT 'Canada'
);

CREATE TABLE hotel_chain (
    chain_id    SERIAL PRIMARY KEY,
    name        VARCHAR(150) NOT NULL,
    hotel_count INTEGER      NOT NULL DEFAULT 0 CHECK (hotel_count >= 0),
    address_id  INTEGER      NOT NULL,
    CONSTRAINT fk_hc_address FOREIGN KEY (address_id) REFERENCES address(address_id)
);

CREATE TABLE hotel_chain_email (
    chain_id INTEGER      NOT NULL,
    email    VARCHAR(150) NOT NULL,
    PRIMARY KEY (chain_id, email),
    CONSTRAINT fk_hce_chain FOREIGN KEY (chain_id) REFERENCES hotel_chain(chain_id) ON DELETE CASCADE
);

CREATE TABLE hotel_chain_phone (
    chain_id INTEGER     NOT NULL,
    phone    VARCHAR(30) NOT NULL,
    PRIMARY KEY (chain_id, phone),
    CONSTRAINT fk_hcp_chain FOREIGN KEY (chain_id) REFERENCES hotel_chain(chain_id) ON DELETE CASCADE
);

CREATE TABLE hotel (
    hotel_id   SERIAL PRIMARY KEY,
    chain_id   INTEGER      NOT NULL,
    manager_id INTEGER,
    name       VARCHAR(150) NOT NULL,
    category   INTEGER      NOT NULL CHECK (category BETWEEN 1 AND 5),
    room_count INTEGER      NOT NULL DEFAULT 0 CHECK (room_count >= 0),
    address_id INTEGER      NOT NULL,
    CONSTRAINT fk_hotel_chain   FOREIGN KEY (chain_id)   REFERENCES hotel_chain(chain_id),
    CONSTRAINT fk_hotel_address FOREIGN KEY (address_id) REFERENCES address(address_id)
);

CREATE TABLE hotel_email (
    hotel_id INTEGER      NOT NULL,
    email    VARCHAR(150) NOT NULL,
    PRIMARY KEY (hotel_id, email),
    CONSTRAINT fk_he_hotel FOREIGN KEY (hotel_id) REFERENCES hotel(hotel_id) ON DELETE CASCADE
);

CREATE TABLE hotel_phone (
    hotel_id INTEGER     NOT NULL,
    phone    VARCHAR(30) NOT NULL,
    PRIMARY KEY (hotel_id, phone),
    CONSTRAINT fk_hp_hotel FOREIGN KEY (hotel_id) REFERENCES hotel(hotel_id) ON DELETE CASCADE
);

CREATE TABLE room (
    room_id       SERIAL PRIMARY KEY,
    hotel_id      INTEGER       NOT NULL,
    room_number   VARCHAR(20),
    price         DECIMAL(10,2) NOT NULL CHECK (price > 0),
    capacity      VARCHAR(50)   NOT NULL CHECK (capacity IN ('single','double','triple','suite','family')),
    view_type     VARCHAR(50)   CHECK (view_type IN ('SEA','MOUNTAIN','CITY','NONE')),
    is_extendable BOOLEAN       NOT NULL DEFAULT FALSE,
    surface_area  INTEGER       CHECK (surface_area > 0),
    status        VARCHAR(30)   NOT NULL DEFAULT 'AVAILABLE'
                                CHECK (status IN ('AVAILABLE','RESERVED','OCCUPIED','MAINTENANCE')),
    CONSTRAINT fk_room_hotel FOREIGN KEY (hotel_id) REFERENCES hotel(hotel_id)
);

CREATE TABLE amenity (
    amenity_id   SERIAL PRIMARY KEY,
    amenity_name VARCHAR(100) NOT NULL UNIQUE
);

CREATE TABLE room_amenity (
    room_id    INTEGER NOT NULL,
    amenity_id INTEGER NOT NULL,
    PRIMARY KEY (room_id, amenity_id),
    CONSTRAINT fk_ra_room    FOREIGN KEY (room_id)    REFERENCES room(room_id)    ON DELETE CASCADE,
    CONSTRAINT fk_ra_amenity FOREIGN KEY (amenity_id) REFERENCES amenity(amenity_id) ON DELETE CASCADE
);

CREATE TABLE customer (
    customer_id       SERIAL PRIMARY KEY,
    ssn               VARCHAR(20)  NOT NULL UNIQUE,
    first_name        VARCHAR(100) NOT NULL,
    last_name         VARCHAR(100) NOT NULL,
    registration_date DATE         NOT NULL DEFAULT CURRENT_DATE,
    address_id        INTEGER      NOT NULL,
    email             VARCHAR(255) UNIQUE,
    password_hash     TEXT         NOT NULL,
    CONSTRAINT fk_customer_address FOREIGN KEY (address_id) REFERENCES address(address_id)
);

CREATE TABLE employee (
    employee_id SERIAL PRIMARY KEY,
    hotel_id    INTEGER      NOT NULL,
    first_name  VARCHAR(100) NOT NULL,
    last_name   VARCHAR(100) NOT NULL,
    ssn         VARCHAR(20)  NOT NULL UNIQUE,
    email       VARCHAR(255) NOT NULL UNIQUE,
    phone       VARCHAR(30),
    role        VARCHAR(30)  NOT NULL CHECK (role IN ('EMPLOYE','GESTIONNAIRE')),
    address_id  INTEGER      NOT NULL,
    password    VARCHAR(255) NOT NULL,
    active      BOOLEAN      NOT NULL DEFAULT TRUE,
    CONSTRAINT fk_employee_hotel   FOREIGN KEY (hotel_id)   REFERENCES hotel(hotel_id),
    CONSTRAINT fk_employee_address FOREIGN KEY (address_id) REFERENCES address(address_id)
);

ALTER TABLE hotel ADD CONSTRAINT fk_hotel_manager FOREIGN KEY (manager_id) REFERENCES employee(employee_id);

CREATE TABLE reservation (
    reservation_id SERIAL PRIMARY KEY,
    room_id        INTEGER       NOT NULL,
    customer_id    INTEGER       NOT NULL,
    start_date     DATE          NOT NULL,
    end_date       DATE          NOT NULL,
    status         VARCHAR(30)   NOT NULL DEFAULT 'RESERVED'
                                 CHECK (status IN ('RESERVED','CANCELLED','COMPLETED')),
    total_price    DECIMAL(10,2),
    created_at     TIMESTAMP     DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_res_room     FOREIGN KEY (room_id)     REFERENCES room(room_id),
    CONSTRAINT fk_res_customer FOREIGN KEY (customer_id) REFERENCES customer(customer_id),
    CONSTRAINT chk_res_dates   CHECK (start_date < end_date)
);

CREATE TABLE rental (
    rental_id      SERIAL PRIMARY KEY,
    customer_id    INTEGER     NOT NULL,
    employee_id    INTEGER     NOT NULL,
    reservation_id INTEGER,
    room_id        INTEGER     NOT NULL,
    start_date     DATE        NOT NULL,
    end_date       DATE        NOT NULL,
    status         VARCHAR(30) NOT NULL DEFAULT 'ACTIVE'
                               CHECK (status IN ('ACTIVE','COMPLETED','CANCELLED')),
    CONSTRAINT fk_rental_customer    FOREIGN KEY (customer_id)    REFERENCES customer(customer_id),
    CONSTRAINT fk_rental_employee    FOREIGN KEY (employee_id)    REFERENCES employee(employee_id),
    CONSTRAINT fk_rental_reservation FOREIGN KEY (reservation_id) REFERENCES reservation(reservation_id),
    CONSTRAINT fk_rental_room        FOREIGN KEY (room_id)        REFERENCES room(room_id),
    CONSTRAINT chk_rental_dates      CHECK (start_date < end_date)
);
