-- Minimal patch to make the English schema compatible with the current backend code.

-- Employee login support
ALTER TABLE employee
    ADD COLUMN IF NOT EXISTS email VARCHAR(255) UNIQUE,
    ADD COLUMN IF NOT EXISTS phone VARCHAR(30),
    ADD COLUMN IF NOT EXISTS password VARCHAR(255),
    ADD COLUMN IF NOT EXISTS active BOOLEAN NOT NULL DEFAULT TRUE;

-- Customer fields expected by current Java entity
ALTER TABLE customer
    ADD COLUMN IF NOT EXISTS email VARCHAR(255),
    ADD COLUMN IF NOT EXISTS phone VARCHAR(30),
    ADD COLUMN IF NOT EXISTS address VARCHAR(500),
    ADD COLUMN IF NOT EXISTS id_type VARCHAR(50),
    ADD COLUMN IF NOT EXISTS id_number VARCHAR(100);

-- Room fields expected by current Java entity / DTOs
ALTER TABLE room
    ADD COLUMN IF NOT EXISTS room_number VARCHAR(20);

-- Reservation fields expected by current Java entity / services
ALTER TABLE reservation
    ADD COLUMN IF NOT EXISTS status VARCHAR(30) NOT NULL DEFAULT 'RESERVED',
    ADD COLUMN IF NOT EXISTS total_price DECIMAL(10,2),
    ADD COLUMN IF NOT EXISTS created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP;

-- Rental fields expected by current Java entity / services
ALTER TABLE rental
    ADD COLUMN IF NOT EXISTS status VARCHAR(30) NOT NULL DEFAULT 'ACTIVE';
