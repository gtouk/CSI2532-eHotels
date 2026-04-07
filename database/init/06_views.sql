-- =====================================================
-- 06_views_en.sql
-- SQL views for e-Hotels
-- =====================================================

-- -----------------------------------------------------
-- View 1
-- Number of available rooms by area
-- Here, the area is represented by the city
-- -----------------------------------------------------
CREATE OR REPLACE VIEW available_rooms_by_area AS
SELECT
    a.city AS area,
    COUNT(rm.room_id) AS available_room_count
FROM room rm
JOIN hotel h ON rm.hotel_id = h.hotel_id
JOIN address a ON h.address_id = a.address_id
WHERE rm.room_id NOT IN (
    SELECT r.room_id
    FROM reservation r
    WHERE CURRENT_DATE < r.end_date
      AND CURRENT_DATE >= r.start_date
)
AND rm.room_id NOT IN (
    SELECT rt.room_id
    FROM rental rt
    WHERE CURRENT_DATE < rt.end_date
      AND CURRENT_DATE >= rt.start_date
)
GROUP BY a.city
ORDER BY a.city;

-- -----------------------------------------------------
-- View 2
-- Total room capacity for a hotel
-- Converts textual capacity into numeric values
-- -----------------------------------------------------
CREATE OR REPLACE VIEW hotel_total_capacity AS
SELECT
    h.hotel_id,
    h.name AS hotel_name,
    SUM(
        CASE rm.capacity
            WHEN 'simple' THEN 1
            WHEN 'double' THEN 2
            WHEN 'triple' THEN 3
            WHEN 'suite' THEN 4
            WHEN 'familiale' THEN 5
            ELSE 0
        END
    ) AS total_capacity
FROM hotel h
JOIN room rm ON h.hotel_id = rm.hotel_id
GROUP BY h.hotel_id, h.name
ORDER BY h.hotel_id;
