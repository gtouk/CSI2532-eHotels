-- NOTE:
-- The uploaded 02_data.sql does not contain seed INSERT statements.
-- It duplicates the schema definition instead of loading sample data.
-- Keep this file as a placeholder or replace it with actual English INSERT data.

INSERT INTO amenity (name) VALUES
('TV'),
('WiFi'),
('Air Conditioning'),
('Mini Fridge'),
('Balcony');

INSERT INTO room_amenity (room_id, amenity_id) VALUES
(1, 1), (1, 2),
(2, 1), (2, 2), (2, 3),
(3, 2), (3, 4),
(4, 1), (4, 5);