-- =====================================================
-- 02_data.sql — Données complètes e-Hotels
-- 5 chaînes × 8 hôtels × 5 chambres + employés + clients
-- =====================================================

-- ===================== AMENITÉS =====================
INSERT INTO amenity (amenity_name) VALUES
  ('TV'),('Climatisation'),('WiFi'),('Minibar'),('Coffre-fort'),
  ('Baignoire'),('Douche italienne'),('Balcon'),('Vue panoramique'),
  ('Cuisine équipée');

-- ===================== ADRESSES =====================
-- Sièges sociaux des chaînes (1-5)
INSERT INTO address (street_number, street_name, city, province, postal_code, country) VALUES
  (100, 'Bay Street',        'Toronto',   'Ontario',                'M5J2T3', 'Canada'),
  (200, 'Sparks Street',     'Ottawa',    'Ontario',                'K1P5B9', 'Canada'),
  (300, 'Sherbrooke Ouest',  'Montreal',  'Quebec',                 'H3G1E8', 'Canada'),
  (400, 'Robson Street',     'Vancouver', 'Colombie-Britannique',   'V6B2B5', 'Canada'),
  (500, 'Jasper Avenue',     'Edmonton',  'Alberta',                'T5J1S8', 'Canada');

-- Adresses hôtels (6-45)
-- CHAÎNE 1 - Marriott
INSERT INTO address (street_number, street_name, city, province, postal_code, country) VALUES
  (101,'Colonel By Drive',  'Ottawa',    'Ontario',                'K1N9H4','Canada'),
  (102,'Sussex Drive',      'Ottawa',    'Ontario',                'K1N1K1','Canada'),
  (201,'Front Street West', 'Toronto',   'Ontario',                'M5V3G1','Canada'),
  (301,'Peel Street',       'Montreal',  'Quebec',                 'H3C2G8','Canada'),
  (401,'Burrard Street',    'Vancouver', 'Colombie-Britannique',   'V6C2B5','Canada'),
  (501,'Centre Street SW',  'Calgary',   'Alberta',                'T2G2C5','Canada'),
  (601,'Grande Allee Est',  'Quebec',    'Quebec',                 'G1R2J5','Canada'),
  (701,'Barrington Street', 'Halifax',   'Nouvelle-Ecosse',        'B3J1Z1','Canada');

-- CHAÎNE 2 - Hilton
INSERT INTO address (street_number, street_name, city, province, postal_code, country) VALUES
  (110,'Rideau Street',     'Ottawa',    'Ontario',                'K1N5Y1','Canada'),
  (111,'Laurier Avenue',    'Ottawa',    'Ontario',                'K1P5J3','Canada'),
  (210,'King Street West',  'Toronto',   'Ontario',                'M5H1J8','Canada'),
  (310,'Rene-Levesque',     'Montreal',  'Quebec',                 'H3B4W8','Canada'),
  (410,'Georgia Street',    'Vancouver', 'Colombie-Britannique',   'V6C1P5','Canada'),
  (510,'8th Avenue SW',     'Calgary',   'Alberta',                'T2R1L9','Canada'),
  (610,'Saint-Louis Street','Quebec',    'Quebec',                 'G1R3Z2','Canada'),
  (710,'Spring Garden Road','Halifax',   'Nouvelle-Ecosse',        'B3J3R4','Canada');

-- CHAÎNE 3 - Westin
INSERT INTO address (street_number, street_name, city, province, postal_code, country) VALUES
  (120,'Elgin Street',      'Ottawa',    'Ontario',                'K2P1L5','Canada'),
  (121,'Bank Street',       'Ottawa',    'Ontario',                'K1P5N8','Canada'),
  (220,'York Street',       'Toronto',   'Ontario',                'M5J1S5','Canada'),
  (320,'Saint-Denis',       'Montreal',  'Quebec',                 'H2X3K4','Canada'),
  (420,'Hastings Street',   'Vancouver', 'Colombie-Britannique',   'V6B1S5','Canada'),
  (520,'Macleod Trail SE',  'Calgary',   'Alberta',                'T2G2L7','Canada'),
  (620,'Cartier Street',    'Quebec',    'Quebec',                 'G1R2L4','Canada'),
  (720,'Hollis Street',     'Halifax',   'Nouvelle-Ecosse',        'B3J3N5','Canada');

-- CHAÎNE 4 - Hyatt
INSERT INTO address (street_number, street_name, city, province, postal_code, country) VALUES
  (130,'Slater Street',     'Ottawa',    'Ontario',                'K1P5H1','Canada'),
  (131,'Metcalfe Street',   'Ottawa',    'Ontario',                'K1P6L7','Canada'),
  (230,'Wellington Street', 'Toronto',   'Ontario',                'M5V3C7','Canada'),
  (330,'Maisonneuve Ouest', 'Montreal',  'Quebec',                 'H3A3K6','Canada'),
  (430,'Granville Street',  'Vancouver', 'Colombie-Britannique',   'V6C1T2','Canada'),
  (530,'4th Street SW',     'Calgary',   'Alberta',                'T2S2L7','Canada'),
  (630,'Auteuil Street',    'Quebec',    'Quebec',                 'G1R4H5','Canada'),
  (730,'Lower Water Street','Halifax',   'Nouvelle-Ecosse',        'B3J3P7','Canada');

-- CHAÎNE 5 - IHG
INSERT INTO address (street_number, street_name, city, province, postal_code, country) VALUES
  (140,'OConnor Street',    'Ottawa',    'Ontario',                'K1P6L2','Canada'),
  (141,'Queen Street',      'Ottawa',    'Ontario',                'K1P5C7','Canada'),
  (240,'Peter Street',      'Toronto',   'Ontario',                'M5V2H1','Canada'),
  (340,'McGill College',    'Montreal',  'Quebec',                 'H3A3J5','Canada'),
  (440,'Seymour Street',    'Vancouver', 'Colombie-Britannique',   'V6B3L1','Canada'),
  (540,'9th Avenue SE',     'Calgary',   'Alberta',                'T2G0R9','Canada'),
  (640,'Cote de la Montagne','Quebec',   'Quebec',                 'G1K4E2','Canada'),
  (740,'Argyle Street',     'Halifax',   'Nouvelle-Ecosse',        'B3J2B5','Canada');

-- Adresses employés (46-85)
INSERT INTO address (street_number, street_name, city, province, postal_code, country)
SELECT
  (100 + n)::integer,
  'Residential Street',
  CASE WHEN n <= 10 THEN 'Ottawa'
       WHEN n <= 20 THEN 'Toronto'
       WHEN n <= 30 THEN 'Montreal'
       WHEN n <= 35 THEN 'Vancouver'
       ELSE 'Calgary' END,
  CASE WHEN n <= 20 THEN 'Ontario'
       WHEN n <= 30 THEN 'Quebec'
       WHEN n <= 35 THEN 'Colombie-Britannique'
       ELSE 'Alberta' END,
  'K1A0A' || (n % 9 + 1)::text,
  'Canada'
FROM generate_series(1,40) AS n;

-- Adresses clients (86-95)
INSERT INTO address (street_number, street_name, city, province, postal_code, country) VALUES
  (1,'Main Street',    'Ottawa',    'Ontario',               'K1A0A1','Canada'),
  (2,'Oak Avenue',     'Toronto',   'Ontario',               'M5V1A2','Canada'),
  (3,'Maple Drive',    'Montreal',  'Quebec',                'H3A1A3','Canada'),
  (4,'Pine Lane',      'Vancouver', 'Colombie-Britannique',  'V6B1A4','Canada'),
  (5,'Elm Street',     'Calgary',   'Alberta',               'T2G1A5','Canada'),
  (6,'Cedar Road',     'Ottawa',    'Ontario',               'K1A0A6','Canada'),
  (7,'Birch Blvd',     'Toronto',   'Ontario',               'M5V1A7','Canada'),
  (8,'Willow Way',     'Montreal',  'Quebec',                'H3A1A8','Canada'),
  (9,'Spruce Cres',    'Vancouver', 'Colombie-Britannique',  'V6B1A9','Canada'),
  (10,'Aspen Trail',   'Halifax',   'Nouvelle-Ecosse',       'B3J1B0','Canada');

-- ===================== CHAÎNES =====================
INSERT INTO hotel_chain (chain_id, name, hotel_count, address_id) VALUES
  (1, 'Marriott International',   8, 1),
  (2, 'Hilton Worldwide',         8, 2),
  (3, 'Westin Hotels & Resorts',  8, 3),
  (4, 'Hyatt Hotels Corporation', 8, 4),
  (5, 'IHG Hotels & Resorts',     8, 5);

INSERT INTO hotel_chain_email VALUES
  (1,'contact@marriott-canada.com'),(1,'support@marriott-canada.com'),
  (2,'info@hilton-canada.com'),     (2,'reservations@hilton-canada.com'),
  (3,'hello@westin-canada.com'),    (3,'booking@westin-canada.com'),
  (4,'contact@hyatt-canada.com'),   (4,'service@hyatt-canada.com'),
  (5,'info@ihg-canada.com'),        (5,'support@ihg-canada.com');

INSERT INTO hotel_chain_phone VALUES
  (1,'1-800-627-7468'),(2,'1-800-445-8667'),(3,'1-800-937-8461'),
  (4,'1-800-492-8804'),(5,'1-800-465-4329');

-- ===================== HÔTELS =====================
INSERT INTO hotel (chain_id, manager_id, name, category, room_count, address_id) VALUES
  (1,NULL,'Marriott Ottawa Riverside',     4, 5, 6),
  (1,NULL,'Marriott Ottawa Parliament',    5, 5, 7),
  (1,NULL,'Marriott Toronto Centre',       4, 5, 8),
  (1,NULL,'Marriott Montreal Downtown',    3, 5, 9),
  (1,NULL,'Marriott Vancouver Harbour',    5, 5,10),
  (1,NULL,'Marriott Calgary Tower',        4, 5,11),
  (1,NULL,'Marriott Quebec City',          3, 5,12),
  (1,NULL,'Marriott Halifax Waterfront',   4, 5,13),
  (2,NULL,'Hilton Ottawa Garden Inn',      3, 5,14),
  (2,NULL,'Hilton Ottawa Embassy Row',     5, 5,15),
  (2,NULL,'Hilton Toronto Airport',        4, 5,16),
  (2,NULL,'Hilton Montreal Bonaventure',   4, 5,17),
  (2,NULL,'Hilton Vancouver Metrotown',    3, 5,18),
  (2,NULL,'Hilton Calgary Airport',        4, 5,19),
  (2,NULL,'Hilton Quebec Vieux-Port',      5, 5,20),
  (2,NULL,'Hilton Halifax Casino',         3, 5,21),
  (3,NULL,'Westin Ottawa Suites',          5, 5,22),
  (3,NULL,'Westin Ottawa Canal',           4, 5,23),
  (3,NULL,'Westin Toronto Harbour Castle', 5, 5,24),
  (3,NULL,'Westin Montreal',               4, 5,25),
  (3,NULL,'Westin Grand Vancouver',        5, 5,26),
  (3,NULL,'Westin Calgary',                4, 5,27),
  (3,NULL,'Westin Quebec Grande Allee',    3, 5,28),
  (3,NULL,'Westin Nova Scotian Halifax',   5, 5,29),
  (4,NULL,'Hyatt Regency Ottawa',          5, 5,30),
  (4,NULL,'Hyatt Place Ottawa',            3, 5,31),
  (4,NULL,'Hyatt Regency Toronto',         5, 5,32),
  (4,NULL,'Hyatt Montreal',                4, 5,33),
  (4,NULL,'Hyatt Regency Vancouver',       5, 5,34),
  (4,NULL,'Hyatt House Calgary',           3, 5,35),
  (4,NULL,'Hyatt Regency Quebec City',     4, 5,36),
  (4,NULL,'Hyatt Place Halifax',           3, 5,37),
  (5,NULL,'IHG Holiday Inn Ottawa',        3, 5,38),
  (5,NULL,'IHG Crowne Plaza Ottawa',       4, 5,39),
  (5,NULL,'IHG Holiday Inn Toronto',       3, 5,40),
  (5,NULL,'IHG InterContinental Montreal', 5, 5,41),
  (5,NULL,'IHG Crowne Plaza Vancouver',    4, 5,42),
  (5,NULL,'IHG Holiday Inn Calgary',       3, 5,43),
  (5,NULL,'IHG InterContinental Quebec',   5, 5,44),
  (5,NULL,'IHG Crowne Plaza Halifax',      4, 5,45);

-- Emails et téléphones des hôtels
INSERT INTO hotel_email (hotel_id, email)
SELECT hotel_id, 'hotel' || hotel_id || '@ehotels.ca' FROM hotel;

INSERT INTO hotel_phone (hotel_id, phone)
SELECT hotel_id, '1-800-' || lpad(hotel_id::text, 3,'0') || '-0000' FROM hotel;

-- ===================== EMPLOYÉS =====================
DO $$
DECLARE
  h_id INTEGER;
  addr_base INTEGER := 46;
  i INTEGER := 0;
  emp_id BIGINT;
  fn TEXT[] := ARRAY['Alice','Bob','Claire','David','Emma','Francois','Gina','Henri',
                     'Isabelle','Julien','Karen','Luc','Marie','Noel','Olivia','Pierre',
                     'Quentin','Rachel','Samuel','Thomas','Ursula','Victor','Wendy','Xavier',
                     'Yasmine','Zacharie','Amelie','Bruno','Cecile','Denis',
                     'Elise','Fabrice','Grace','Hugo','Iris','Jerome','Katia','Laurent',
                     'Manon','Nicolas'];
  ln TEXT[] := ARRAY['Martin','Roy','Tremblay','Gagnon','Lee','Bouchard','Morin','Lavoie',
                     'Fortin','Gauthier','Bergeron','Pelletier','Lefebvre','Cote','Belanger',
                     'Thibault','Paquette','Ouellet','Hebert','Savard','Lacombe','Beaulieu',
                     'Chartrand','Perron','Blais','Girard','Leclerc','Landry','Vachon','Denis',
                     'Couture','Grenier','Poirier','Archambault','Desrosiers','Lemay','Brodeur',
                     'Champagne','Lepage','Deschamps'];
  addr_idx INTEGER;
BEGIN
  FOR h_id IN SELECT hotel_id FROM hotel ORDER BY hotel_id LOOP
    i := i + 1;
    addr_idx := addr_base + i - 1;
    IF addr_idx > 85 THEN addr_idx := 46 + ((i-1) % 40); END IF;

    -- Gestionnaire
    INSERT INTO employee (hotel_id, first_name, last_name, ssn, email, phone, role, address_id, password, active)
    VALUES (
      h_id,
      fn[i], ln[i],
      'SSN-MGR-' || lpad(i::text,3,'0'),
      lower(fn[i]) || '.' || lower(ln[i]) || '.mgr@ehotels.ca',
      '613-' || lpad(i::text,3,'0') || '-0001',
      'GESTIONNAIRE',
      addr_idx,
      'password123',
      TRUE
    ) RETURNING employee_id INTO emp_id;

    UPDATE hotel SET manager_id = emp_id WHERE hotel_id = h_id;

    -- Employé ordinaire
    INSERT INTO employee (hotel_id, first_name, last_name, ssn, email, phone, role, address_id, password, active)
    VALUES (
      h_id,
      fn[((i * 2 - 1) % 40) + 1],
      ln[((i * 3 - 1) % 40) + 1],
      'SSN-EMP-' || lpad((i + 40)::text,3,'0'),
      'emp' || (i + 40) || '@ehotels.ca',
      '613-' || lpad((i+40)::text,3,'0') || '-0002',
      'EMPLOYE',
      addr_idx,
      'password123',
      TRUE
    );
  END LOOP;
END $$;

-- ===================== CLIENTS =====================
-- Mot de passe haché = "password123"
INSERT INTO customer (ssn, first_name, last_name, registration_date, address_id, email, password_hash) VALUES
  ('NAS-001','Sophie',   'Bernard',  '2024-01-15', 86, 'sophie.bernard@email.com',   '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhy2'),
  ('NAS-002','Marc',     'Dupont',   '2024-02-20', 87, 'marc.dupont@email.com',      '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhy2'),
  ('NAS-003','Julie',    'Lambert',  '2024-03-10', 88, 'julie.lambert@email.com',    '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhy2'),
  ('NAS-004','Pierre',   'Leblanc',  '2024-04-05', 89, 'pierre.leblanc@email.com',   '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhy2'),
  ('NAS-005','Anne',     'Moreau',   '2024-05-12', 90, 'anne.moreau@email.com',      '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhy2'),
  ('NAS-006','Thomas',   'Simon',    '2024-06-18', 91, 'thomas.simon@email.com',     '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhy2'),
  ('NAS-007','Isabelle', 'Michel',   '2024-07-22', 92, 'isabelle.michel@email.com',  '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhy2'),
  ('NAS-008','Robert',   'Garcia',   '2024-08-30', 93, 'robert.garcia@email.com',    '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhy2'),
  ('NAS-009','Nathalie', 'Martinez', '2024-09-14', 94, 'nathalie.martinez@email.com','$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhy2'),
  ('NAS-010','Michel',   'Anderson', '2024-10-01', 95, 'michel.anderson@email.com',  '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhy2');

-- ===================== CHAMBRES =====================
-- 5 chambres par hôtel, capacités variées
DO $$
DECLARE
  h INTEGER;
  cat INTEGER;
  base_price DECIMAL;
  r_id BIGINT;
  room_num TEXT;
  caps  TEXT[]    := ARRAY['single','double','triple','suite','family'];
  views TEXT[]    := ARRAY['CITY','SEA','MOUNTAIN','CITY','NONE'];
  surfs INTEGER[] := ARRAY[20, 30, 40, 60, 80];
  exts  BOOLEAN[] := ARRAY[FALSE, TRUE, FALSE, TRUE, FALSE];
BEGIN
  FOR h IN SELECT hotel_id FROM hotel ORDER BY hotel_id LOOP
    SELECT category INTO cat FROM hotel WHERE hotel_id = h;
    base_price := (cat * 50)::DECIMAL;

    FOR i IN 1..5 LOOP
      room_num := lpad(h::text, 2, '0') || lpad(i::text, 2, '0');

      INSERT INTO room (hotel_id, room_number, price, capacity, view_type, is_extendable, surface_area, status)
      VALUES (
        h,
        room_num,
        ROUND((base_price * (0.8 + i * 0.15))::numeric, 2),
        caps[i],
        views[i],
        exts[i],
        surfs[i],
        'AVAILABLE'
      ) RETURNING room_id INTO r_id;

      -- Amenités selon type de chambre
      IF i = 1 THEN
        INSERT INTO room_amenity (room_id, amenity_id) VALUES (r_id,1),(r_id,3);
      ELSIF i = 2 THEN
        INSERT INTO room_amenity (room_id, amenity_id) VALUES (r_id,1),(r_id,2),(r_id,3);
      ELSIF i = 3 THEN
        INSERT INTO room_amenity (room_id, amenity_id) VALUES (r_id,1),(r_id,2),(r_id,3),(r_id,4);
      ELSIF i = 4 THEN
        INSERT INTO room_amenity (room_id, amenity_id) VALUES (r_id,1),(r_id,2),(r_id,3),(r_id,4),(r_id,5),(r_id,6);
      ELSE
        INSERT INTO room_amenity (room_id, amenity_id) VALUES (r_id,1),(r_id,2),(r_id,3),(r_id,7);
      END IF;
    END LOOP;
  END LOOP;
END $$;
