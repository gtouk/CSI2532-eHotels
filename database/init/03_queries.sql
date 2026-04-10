-- =====================================================
-- 03_queries.sql — Requêtes principales e-Hotels
-- =====================================================

-- Requête 1: Recherche de chambres disponibles avec filtres combinés
SELECT
    r.room_id,
    r.room_number,
    h.name         AS hotel_name,
    hc.name        AS chain_name,
    a.city,
    h.category,
    r.capacity,
    r.price,
    r.view_type,
    r.is_extendable,
    r.surface_area,
    STRING_AGG(am.amenity_name, ', ' ORDER BY am.amenity_name) AS amenities
FROM room r
JOIN hotel h       ON r.hotel_id  = h.hotel_id
JOIN hotel_chain hc ON h.chain_id = hc.chain_id
JOIN address a     ON h.address_id = a.address_id
LEFT JOIN room_amenity ra ON r.room_id    = ra.room_id
LEFT JOIN amenity am      ON ra.amenity_id = am.amenity_id
WHERE r.status = 'AVAILABLE'
  AND (:start_date::DATE IS NULL OR r.room_id NOT IN (
      SELECT res.room_id FROM reservation res
      WHERE res.status = 'RESERVED'
        AND :start_date::DATE < res.end_date
        AND :end_date::DATE   > res.start_date))
  AND (:capacity IS NULL OR r.capacity = :capacity)
  AND (:city     IS NULL OR a.city     = :city)
  AND (:category IS NULL OR h.category = :category)
  AND (:chain_id IS NULL OR hc.chain_id = :chain_id)
  AND (:max_price IS NULL OR r.price   <= :max_price)
  AND (:min_surface IS NULL OR r.surface_area >= :min_surface)
GROUP BY r.room_id, h.name, hc.name, a.city, h.category
ORDER BY r.price ASC;

-- Requête 2: Toutes les réservations actives d'un client
SELECT
    res.reservation_id,
    c.first_name || ' ' || c.last_name AS client,
    h.name          AS hotel,
    a.city,
    r.room_number,
    r.capacity,
    res.start_date,
    res.end_date,
    res.status,
    res.total_price
FROM reservation res
JOIN customer c ON res.customer_id = c.customer_id
JOIN room     r ON res.room_id     = r.room_id
JOIN hotel    h ON r.hotel_id      = h.hotel_id
JOIN address  a ON h.address_id    = a.address_id
WHERE c.customer_id = :customer_id
ORDER BY res.start_date DESC;

-- Requête 3: Locations gérées par un employé
SELECT
    ren.rental_id,
    e.first_name || ' ' || e.last_name   AS employe,
    c.first_name || ' ' || c.last_name   AS client,
    h.name        AS hotel,
    r.room_number,
    ren.start_date,
    ren.end_date,
    ren.status,
    CASE WHEN ren.reservation_id IS NOT NULL THEN 'Oui' ELSE 'Non' END AS via_reservation
FROM rental ren
JOIN employee e ON ren.employee_id = e.employee_id
JOIN customer c ON ren.customer_id = c.customer_id
JOIN room     r ON ren.room_id     = r.room_id
JOIN hotel    h ON r.hotel_id      = h.hotel_id
WHERE e.employee_id = :employee_id
ORDER BY ren.start_date DESC;

-- Requête 4: Chambres d'un hôtel avec commodités et disponibilité
SELECT
    r.room_id,
    r.room_number,
    h.name        AS hotel,
    r.capacity,
    r.price,
    r.view_type,
    r.surface_area,
    r.is_extendable,
    r.status,
    STRING_AGG(am.amenity_name, ', ' ORDER BY am.amenity_name) AS amenities
FROM room r
JOIN hotel h ON r.hotel_id = h.hotel_id
LEFT JOIN room_amenity ra ON r.room_id    = ra.room_id
LEFT JOIN amenity am      ON ra.amenity_id = am.amenity_id
WHERE h.hotel_id = :hotel_id
GROUP BY r.room_id, h.name
ORDER BY r.room_number;

-- Requête 5: Statistiques par chaîne hôtelière
SELECT
    hc.name                                     AS chaine,
    COUNT(DISTINCT h.hotel_id)                  AS nb_hotels,
    COUNT(DISTINCT r.room_id)                   AS nb_chambres,
    AVG(r.price)::DECIMAL(10,2)                 AS prix_moyen,
    COUNT(DISTINCT res.reservation_id) FILTER (WHERE res.status = 'RESERVED') AS reservations_actives,
    COUNT(DISTINCT ren.rental_id)      FILTER (WHERE ren.status = 'ACTIVE')   AS locations_actives
FROM hotel_chain hc
LEFT JOIN hotel       h   ON hc.chain_id      = h.chain_id
LEFT JOIN room        r   ON h.hotel_id        = r.hotel_id
LEFT JOIN reservation res ON r.room_id         = res.room_id
LEFT JOIN rental      ren ON r.room_id         = ren.room_id
GROUP BY hc.chain_id, hc.name
ORDER BY nb_hotels DESC;

-- Requête 6: Réservations dont la date de fin est dépassée (pour expiration)
SELECT res.reservation_id, r.room_id, res.end_date
FROM reservation res
JOIN room r ON res.room_id = r.room_id
WHERE res.status = 'RESERVED'
  AND res.end_date < CURRENT_DATE;
