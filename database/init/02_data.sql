-- =====================================================
-- 02_data.sql — Données complètes e-Hotels
-- 5 chaînes × 8 hôtels × 5+ chambres + employés + clients
-- =====================================================

-- ===================== AMENITÉS =====================
INSERT INTO amenity (amenity_name) VALUES
  ('TV'),('Climatisation'),('WiFi'),('Minibar'),('Coffre-fort'),
  ('Baignoire'),('Douche italienne'),('Balcon'),('Vue panoramique'),
  ('Cuisine équipée');

-- ===================== ADRESSES =====================
-- Sièges sociaux des chaînes (1-5)
INSERT INTO address (street_number, street_name, city, province, postal_code, country) VALUES
  (100, 'Bay Street',        'Toronto',   'Ontario',           'M5J2T3', 'Canada'),   -- 1 Marriott HQ
  (200, 'Sparks Street',     'Ottawa',    'Ontario',           'K1P5B9', 'Canada'),   -- 2 Hilton HQ
  (300, 'Sherbrooke Ouest',  'Montreal',  'Quebec',            'H3G1E8', 'Canada'),   -- 3 Westin HQ
  (400, 'Robson Street',     'Vancouver', 'Colombie-Britannique','V6B2B5','Canada'),  -- 4 Hyatt HQ
  (500, 'Jasper Avenue',     'Edmonton',  'Alberta',           'T5J1S8', 'Canada');   -- 5 IHG HQ

-- Adresses hôtels (6-45, 8 par chaîne)
-- CHAÎNE 1 - Marriott (Ottawa ×2, Toronto, Montreal, Vancouver, Calgary, Quebec, Halifax)
INSERT INTO address (street_number, street_name, city, province, postal_code, country) VALUES
  (101,'Colonel By Drive',  'Ottawa',    'Ontario',           'K1N9H4','Canada'),  -- 6
  (102,'Sussex Drive',      'Ottawa',    'Ontario',           'K1N1K1','Canada'),  -- 7
  (201,'Front Street West', 'Toronto',   'Ontario',           'M5V3G1','Canada'),  -- 8
  (301,'Peel Street',       'Montreal',  'Quebec',            'H3C2G8','Canada'),  -- 9
  (401,'Burrard Street',    'Vancouver', 'Colombie-Britannique','V6C2B5','Canada'),-- 10
  (501,'Centre Street SW',  'Calgary',   'Alberta',           'T2G2C5','Canada'),  -- 11
  (601,'Grande Allée Est',  'Quebec',    'Quebec',            'G1R2J5','Canada'),  -- 12
  (701,'Barrington Street', 'Halifax',   'Nouvelle-Ecosse',   'B3J1Z1','Canada');  -- 13

-- CHAÎNE 2 - Hilton
INSERT INTO address (street_number, street_name, city, province, postal_code, country) VALUES
  (110,'Rideau Street',     'Ottawa',    'Ontario',           'K1N5Y1','Canada'),  -- 14
  (111,'Laurier Avenue',    'Ottawa',    'Ontario',           'K1P5J3','Canada'),  -- 15
  (210,'King Street West',  'Toronto',   'Ontario',           'M5H1J8','Canada'),  -- 16
  (310,'René-Lévesque',     'Montreal',  'Quebec',            'H3B4W8','Canada'),  -- 17
  (410,'Georgia Street',    'Vancouver', 'Colombie-Britannique','V6C1P5','Canada'),-- 18
  (510,'8th Avenue SW',     'Calgary',   'Alberta',           'T2R1L9','Canada'),  -- 19
  (610,'Saint-Louis Street','Quebec',    'Quebec',            'G1R3Z2','Canada'),  -- 20
  (710,'Spring Garden Road','Halifax',   'Nouvelle-Ecosse',   'B3J3R4','Canada');  -- 21

-- CHAÎNE 3 - Westin
INSERT INTO address (street_number, street_name, city, province, postal_code, country) VALUES
  (120,'Elgin Street',      'Ottawa',    'Ontario',           'K2P1L5','Canada'),  -- 22
  (121,'Bank Street',       'Ottawa',    'Ontario',           'K1P5N8','Canada'),  -- 23
  (220,'York Street',       'Toronto',   'Ontario',           'M5J1S5','Canada'),  -- 24
  (320,'Saint-Denis',       'Montreal',  'Quebec',            'H2X3K4','Canada'),  -- 25
  (420,'Hastings Street',   'Vancouver', 'Colombie-Britannique','V6B1S5','Canada'),-- 26
  (520,'Macleod Trail SE',  'Calgary',   'Alberta',           'T2G2L7','Canada'),  -- 27
  (620,'Cartier Street',    'Quebec',    'Quebec',            'G1R2L4','Canada'),  -- 28
  (720,'Hollis Street',     'Halifax',   'Nouvelle-Ecosse',   'B3J3N5','Canada');  -- 29

-- CHAÎNE 4 - Hyatt
INSERT INTO address (street_number, street_name, city, province, postal_code, country) VALUES
  (130,'Slater Street',     'Ottawa',    'Ontario',           'K1P5H1','Canada'),  -- 30
  (131,'Metcalfe Street',   'Ottawa',    'Ontario',           'K1P6L7','Canada'),  -- 31
  (230,'Wellington Street', 'Toronto',   'Ontario',           'M5V3C7','Canada'),  -- 32
  (330,'Maisonneuve Ouest', 'Montreal',  'Quebec',            'H3A3K6','Canada'),  -- 33
  (430,'Granville Street',  'Vancouver', 'Colombie-Britannique','V6C1T2','Canada'),-- 34
  (530,'4th Street SW',     'Calgary',   'Alberta',           'T2S2L7','Canada'),  -- 35
  (630,'D''Auteuil Street', 'Quebec',    'Quebec',            'G1R4H5','Canada'),  -- 36
  (730,'Lower Water Street','Halifax',   'Nouvelle-Ecosse',   'B3J3P7','Canada');  -- 37

-- CHAÎNE 5 - IHG
INSERT INTO address (street_number, street_name, city, province, postal_code, country) VALUES
  (140,'O''Connor Street',  'Ottawa',    'Ontario',           'K1P6L2','Canada'),  -- 38
  (141,'Queen Street',      'Ottawa',    'Ontario',           'K1P5C7','Canada'),  -- 39
  (240,'Peter Street',      'Toronto',   'Ontario',           'M5V2H1','Canada'),  -- 40
  (340,'McGill College',    'Montreal',  'Quebec',            'H3A3J5','Canada'),  -- 41
  (440,'Seymour Street',    'Vancouver', 'Colombie-Britannique','V6B3L1','Canada'),-- 42
  (540,'9th Avenue SE',     'Calgary',   'Alberta',           'T2G0R9','Canada'),  -- 43
  (640,'Côte de la Montagne','Quebec',   'Quebec',            'G1K4E2','Canada'),  -- 44
  (740,'Argyle Street',     'Halifax',   'Nouvelle-Ecosse',   'B3J2B5','Canada');  -- 45

-- Adresses employés (46-85)
INSERT INTO address (street_number, street_name, city, province, postal_code, country)
SELECT
  (100 + n)::integer, 'Residential Street', city, province, 'K1A0'||(chr(65+(n%26))), 'Canada'
FROM (
  SELECT n, CASE WHEN n <= 16 THEN 'Ottawa' WHEN n <= 32 THEN 'Toronto' WHEN n <= 40 THEN 'Montreal' WHEN n <= 48 THEN 'Vancouver' ELSE 'Calgary' END AS city,
           CASE WHEN n <= 16 THEN 'Ontario' WHEN n <= 32 THEN 'Ontario' WHEN n <= 40 THEN 'Quebec' WHEN n <= 48 THEN 'Colombie-Britannique' ELSE 'Alberta' END AS province
  FROM generate_series(1,40) AS n
) t;

-- Adresses clients (86-95)
INSERT INTO address (street_number, street_name, city, province, postal_code, country) VALUES
  (1,'Main Street',   'Ottawa',    'Ontario',  'K1A0A1','Canada'),
  (2,'Oak Avenue',    'Toronto',   'Ontario',  'M5V1A2','Canada'),
  (3,'Maple Drive',   'Montreal',  'Quebec',   'H3A1A3','Canada'),
  (4,'Pine Lane',     'Vancouver', 'Colombie-Britannique','V6B1A4','Canada'),
  (5,'Elm Street',    'Calgary',   'Alberta',  'T2G1A5','Canada'),
  (6,'Cedar Road',    'Ottawa',    'Ontario',  'K1A0A6','Canada'),
  (7,'Birch Blvd',    'Toronto',   'Ontario',  'M5V1A7','Canada'),
  (8,'Willow Way',    'Montreal',  'Quebec',   'H3A1A8','Canada'),
  (9,'Spruce Cres',   'Vancouver', 'Colombie-Britannique','V6B1A9','Canada'),
  (10,'Aspen Trail',  'Halifax',   'Nouvelle-Ecosse','B3J1B0','Canada');

-- ===================== CHAÎNES =====================
INSERT INTO hotel_chain (chain_id, name, hotel_count, address_id) VALUES
  (1, 'Marriott International',  8, 1),
  (2, 'Hilton Worldwide',        8, 2),
  (3, 'Westin Hotels & Resorts', 8, 3),
  (4, 'Hyatt Hotels Corporation',8, 4),
  (5, 'IHG Hotels & Resorts',    8, 5);

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
-- Chaîne 1 - Marriott (catégories 3,4,5 variées; 2 hôtels à Ottawa)
INSERT INTO hotel (chain_id, manager_id, name, category, room_count, address_id) VALUES
  (1,NULL,'Marriott Ottawa Riverside',    4, 8, 6),
  (1,NULL,'Marriott Ottawa Parliament',   5, 8, 7),
  (1,NULL,'Marriott Toronto Centre',      4, 8, 8),
  (1,NULL,'Marriott Montreal Downtown',   3, 8, 9),
  (1,NULL,'Marriott Vancouver Harbour',   5, 8,10),
  (1,NULL,'Marriott Calgary Tower',       4, 8,11),
  (1,NULL,'Marriott Quebec City',         3, 8,12),
  (1,NULL,'Marriott Halifax Waterfront',  4, 8,13);

-- Chaîne 2 - Hilton (2 hôtels à Ottawa)
INSERT INTO hotel (chain_id, manager_id, name, category, room_count, address_id) VALUES
  (2,NULL,'Hilton Ottawa Garden Inn',     3, 8,14),
  (2,NULL,'Hilton Ottawa Embassy Row',    5, 8,15),
  (2,NULL,'Hilton Toronto Airport',       4, 8,16),
  (2,NULL,'Hilton Montreal Bonaventure',  4, 8,17),
  (2,NULL,'Hilton Vancouver Metrotown',   3, 8,18),
  (2,NULL,'Hilton Calgary Airport',       4, 8,19),
  (2,NULL,'Hilton Quebec Vieux-Port',     5, 8,20),
  (2,NULL,'Hilton Halifax Casino',        3, 8,21);

-- Chaîne 3 - Westin (2 hôtels à Ottawa)
INSERT INTO hotel (chain_id, manager_id, name, category, room_count, address_id) VALUES
  (3,NULL,'Westin Ottawa Suites',         5, 8,22),
  (3,NULL,'Westin Ottawa Canal',          4, 8,23),
  (3,NULL,'Westin Toronto Harbour Castle',5, 8,24),
  (3,NULL,'Westin Montreal',              4, 8,25),
  (3,NULL,'Westin Grand Vancouver',       5, 8,26),
  (3,NULL,'Westin Calgary',               4, 8,27),
  (3,NULL,'Westin Quebec Grande Allée',   3, 8,28),
  (3,NULL,'Westin Nova Scotian Halifax',  5, 8,29);

-- Chaîne 4 - Hyatt (2 hôtels à Ottawa)
INSERT INTO hotel (chain_id, manager_id, name, category, room_count, address_id) VALUES
  (4,NULL,'Hyatt Regency Ottawa',         5, 8,30),
  (4,NULL,'Hyatt Place Ottawa',           3, 8,31),
  (4,NULL,'Hyatt Regency Toronto',        5, 8,32),
  (4,NULL,'Hyatt Montreal',               4, 8,33),
  (4,NULL,'Hyatt Regency Vancouver',      5, 8,34),
  (4,NULL,'Hyatt House Calgary',          3, 8,35),
  (4,NULL,'Hyatt Regency Quebec City',    4, 8,36),
  (4,NULL,'Hyatt Place Halifax',          3, 8,37);

-- Chaîne 5 - IHG (2 hôtels à Ottawa)
INSERT INTO hotel (chain_id, manager_id, name, category, room_count, address_id) VALUES
  (5,NULL,'IHG Holiday Inn Ottawa',       3, 8,38),
  (5,NULL,'IHG Crowne Plaza Ottawa',      4, 8,39),
  (5,NULL,'IHG Holiday Inn Toronto',      3, 8,40),
  (5,NULL,'IHG InterContinental Montreal',5, 8,41),
  (5,NULL,'IHG Crowne Plaza Vancouver',   4, 8,42),
  (5,NULL,'IHG Holiday Inn Calgary',      3, 8,43),
  (5,NULL,'IHG InterContinental Quebec',  5, 8,44),
  (5,NULL,'IHG Crowne Plaza Halifax',     4, 8,45);

-- Emails et téléphones des hôtels (un échantillon, le reste via trigger)
INSERT INTO hotel_email SELECT hotel_id, lower(replace(name,' ','.')) || '@ehotels.ca' FROM hotel;
INSERT INTO hotel_phone SELECT hotel_id, '1-800-' || lpad(hotel_id::text, 3,'0') || '-0000' FROM hotel;

-- ===================== EMPLOYÉS =====================
-- 1 gestionnaire par hôtel (40 au total), puis des employés ordinaires
DO $$
DECLARE
  h_id INTEGER;
  addr_base INTEGER := 46; -- adresses employés commencent à l'index 46
  i INTEGER := 0;
  emp_id INTEGER;
  fn TEXT[] := ARRAY['Alice','Bob','Claire','David','Emma','François','Gina','Henri',
                     'Isabelle','Julien','Karen','Luc','Marie','Noel','Olivia','Pierre',
                     'Quentin','Rachel','Samuel','Thomas','Ursula','Victor','Wendy','Xavier',
                     'Yasmine','Zacharie','Amelie','Bruno','Cecile','Denis',
                     'Elise','Fabrice','Grace','Hugo','Iris','Jerome','Katia','Laurent',
                     'Manon','Nicolas'];
  ln TEXT[] := ARRAY['Martin','Roy','Tremblay','Gagnon','Lee','Bouchard','Morin','Lavoie',
                     'Fortin','Gauthier','Bergeron','Pelletier','Lefebvre','Cote','Bélanger',
                     'Thibault','Paquette','Ouellet','Hebert','Savard','Lacombe','Beaulieu',
                     'Chartrand','Perron','Blais','Girard','Leclerc','Landry','Vachon','Denis',
                     'Couture','Grenier','Poirier','Archambault','Desrosiers','Lemay','Brodeur',
                     'Champagne','Lepage','Deschamps'];
BEGIN
  FOR h_id IN SELECT hotel_id FROM hotel ORDER BY hotel_id LOOP
    i := i + 1;
    -- Gestionnaire
    INSERT INTO employee (hotel_id, first_name, last_name, ssn, email, phone, role, address_id, password, active)
    VALUES (
      h_id,
      fn[i], ln[i],
      'SSN-MGR-' || lpad(i::text,3,'0'),
      lower(fn[i]) || '.' || lower(ln[i]) || '.mgr@ehotels.ca',
      '613-' || lpad(i::text,3,'0') || '-0001',
      'GESTIONNAIRE',
      addr_base + i - 1,
      'password123',
      TRUE
    ) RETURNING employee_id INTO emp_id;

    -- Assigner comme manager de l'hôtel
    UPDATE hotel SET manager_id = emp_id WHERE hotel_id = h_id;

    -- 1 employé ordinaire par hôtel
    INSERT INTO employee (hotel_id, first_name, last_name, ssn, email, phone, role, address_id, password, active)
    VALUES (
      h_id,
      fn[((i * 2) % 40) + 1], ln[((i * 3) % 40) + 1],
      'SSN-EMP-' || lpad((i + 40)::text,3,'0'),
      'emp' || (i + 40) || '@ehotels.ca',
      '613-' || lpad((i+40)::text,3,'0') || '-0002',
      'EMPLOYE',
      addr_base + ((i + 19) % 40),
      'password123',
      TRUE
    );
  END LOOP;
END $$;

-- ===================== CLIENTS =====================
INSERT INTO customer (ssn, first_name, last_name, registration_date, address_id, email, password_hash) VALUES
  ('NAS-001','Sophie',   'Bernard',  '2024-01-15', 86,  'sophie.bernard@email.com',  '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhy2'),
  ('NAS-002','Marc',     'Dupont',   '2024-02-20', 87,  'marc.dupont@email.com',     '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhy2'),
  ('NAS-003','Julie',    'Lambert',  '2024-03-10', 88,  'julie.lambert@email.com',   '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhy2'),
  ('NAS-004','Pierre',   'Leblanc',  '2024-04-05', 89,  'pierre.leblanc@email.com',  '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhy2'),
  ('NAS-005','Anne',     'Moreau',   '2024-05-12', 90,  'anne.moreau@email.com',     '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhy2'),
  ('NAS-006','Thomas',   'Simon',    '2024-06-18', 91,  'thomas.simon@email.com',    '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhy2'),
  ('NAS-007','Isabelle', 'Michel',   '2024-07-22', 92,  'isabelle.michel@email.com', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhy2'),
  ('NAS-008','Robert',   'Garcia',   '2024-08-30', 93,  'robert.garcia@email.com',   '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhy2'),
  ('NAS-009','Nathalie', 'Martinez', '2024-09-14', 94,  'nathalie.martinez@email.com','$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhy2'),
  ('NAS-010','Michel',   'Anderson', '2024-10-01', 95,  'michel.anderson@email.com', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhy2');
-- Mot de passe haché = "password123"

-- ===================== CHAMBRES =====================
-- 5 chambres par hôtel (40 hôtels = 200 chambres)
-- Capacités variées: single, double, triple, suite, family
DO $$
DECLARE
  h INTEGER;
  base_price DECIMAL;
  cat INTEGER;
  views TEXT[] := ARRAY['SEA','MOUNTAIN','CITY','NONE','CITY'];
  caps TEXT[]  := ARRAY['single','double','triple','suite','family'];
  surfaces INTEGER[] := ARRAY[20,30,40,60,80];
  amenity_sets INTEGER[][] := ARRAY[
    ARRAY[1,3],    -- TV, WiFi
    ARRAY[1,2,3],  -- TV, AC, WiFi
    ARRAY[1,2,3,4],-- TV, AC, WiFi, Minibar
    ARRAY[1,2,3,4,5,6], -- full suite
    ARRAY[1,2,3,7] -- TV, AC, WiFi, Balcony
  ];
  r_id INTEGER;
  room_num TEXT;
BEGIN
  FOR h IN SELECT hotel_id FROM hotel ORDER BY hotel_id LOOP
    SELECT category INTO cat FROM hotel WHERE hotel_id = h;
    base_price := cat * 50.00; -- 1★=50, 2★=100, 3★=150, 4★=200, 5★=250

    FOR i IN 1..5 LOOP
      room_num := lpad(h::text,2,'0') || lpad(i::text,2,'0');
      INSERT INTO room (hotel_id, room_number, price, capacity, view_type, is_extendable, surface_area, status)
      VALUES (
        h,
        room_num,
        base_price * (0.8 + i * 0.2)::decimal(10,2),
        caps[i],
        views[i],
        (i % 2 = 0),
        surfaces[i],
        'AVAILABLE'
      ) RETURNING room_id INTO r_id;

      -- Amenités selon type de chambre
      IF i = 1 THEN
        INSERT INTO room_amenity (room_id, amenity_id) VALUES (r_id, 1), (r_id, 3);
      ELSIF i = 2 THEN
        INSERT INTO room_amenity (room_id, amenity_id) VALUES (r_id, 1), (r_id, 2), (r_id, 3);
      ELSIF i = 3 THEN
        INSERT INTO room_amenity (room_id, amenity_id) VALUES (r_id, 1), (r_id, 2), (r_id, 3), (r_id, 4);
      ELSIF i = 4 THEN
        INSERT INTO room_amenity (room_id, amenity_id) VALUES (r_id, 1),(r_id,2),(r_id,3),(r_id,4),(r_id,5),(r_id,6);
      ELSE
        INSERT INTO room_amenity (room_id, amenity_id) VALUES (r_id, 1), (r_id, 2), (r_id, 3), (r_id, 7);
      END IF;
    END LOOP;
  END LOOP;
END $$;
