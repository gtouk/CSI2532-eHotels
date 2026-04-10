-- =====================================================
-- 05_indexes.sql — Index e-Hotels
-- =====================================================

-- Index 1: Recherche de chambres par hôtel (jointure fréquente)
CREATE INDEX idx_room_hotel_id ON room(hotel_id);

-- Index 2: Vérification chevauchement réservations (critique pour les triggers)
CREATE INDEX idx_reservation_room_dates ON reservation(room_id, start_date, end_date);

-- Index 3: Vérification chevauchement locations (critique pour les triggers)
CREATE INDEX idx_rental_room_dates ON rental(room_id, start_date, end_date);

-- Index 4: Recherche hôtels par chaîne (filtre fréquent dans les recherches)
CREATE INDEX idx_hotel_chain_id ON hotel(chain_id);

-- Index 5: Recherche hôtels par adresse/ville (vue available_rooms_by_area)
CREATE INDEX idx_hotel_address_id ON hotel(address_id);

-- Index 6: Recherche réservations expirées (scheduler d'expiration)
CREATE INDEX idx_reservation_status_enddate ON reservation(status, end_date);

-- Index 7: Recherche chambres disponibles par prix (filtre fréquent)
CREATE INDEX idx_room_status_price ON room(status, price);

-- Index 8: Connexion client/employé par email (login)
CREATE INDEX idx_customer_email ON customer(email);
CREATE INDEX idx_employee_email ON employee(email);
