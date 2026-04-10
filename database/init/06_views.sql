-- =====================================================
-- 06_views.sql — Vues SQL e-Hotels
-- =====================================================

-- Vue 1: Nombre de chambres disponibles par zone (ville)
CREATE OR REPLACE VIEW available_rooms_by_area AS
SELECT
    a.city                 AS zone,
    hc.name                AS chaine,
    h.category,
    COUNT(r.room_id)       AS nb_chambres_disponibles,
    AVG(r.price)::DECIMAL(10,2) AS prix_moyen
FROM room r
JOIN hotel       h  ON r.hotel_id  = h.hotel_id
JOIN hotel_chain hc ON h.chain_id  = hc.chain_id
JOIN address     a  ON h.address_id = a.address_id
WHERE r.status = 'AVAILABLE'
  AND r.room_id NOT IN (
      SELECT res.room_id FROM reservation res
      WHERE res.status = 'RESERVED'
        AND CURRENT_DATE BETWEEN res.start_date AND res.end_date - 1
  )
  AND r.room_id NOT IN (
      SELECT ren.room_id FROM rental ren
      WHERE ren.status = 'ACTIVE'
        AND CURRENT_DATE BETWEEN ren.start_date AND ren.end_date - 1
  )
GROUP BY a.city, hc.name, h.category
ORDER BY a.city, hc.name;

-- Vue 2: Capacité totale des chambres par hôtel
CREATE OR REPLACE VIEW hotel_total_capacity AS
SELECT
    h.hotel_id,
    h.name                    AS hotel_name,
    hc.name                   AS chaine,
    h.category,
    COUNT(r.room_id)          AS nb_chambres,
    SUM(
        CASE r.capacity
            WHEN 'single' THEN 1
            WHEN 'double' THEN 2
            WHEN 'triple' THEN 3
            WHEN 'suite'  THEN 4
            WHEN 'family' THEN 5
            ELSE 0
        END
    )                         AS capacite_totale_personnes
FROM hotel h
JOIN hotel_chain hc ON h.chain_id = hc.chain_id
LEFT JOIN room r    ON h.hotel_id = r.hotel_id
GROUP BY h.hotel_id, h.name, hc.name, h.category
ORDER BY h.hotel_id;
