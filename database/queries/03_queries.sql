-- =====================================================
-- 03_queries.sql
-- Main queries for e-Hotels
-- =====================================================

-- -----------------------------------------------------
-- Query 1
-- Search available rooms with combined filters
-- Parameters to replace:
-- :start_date, :end_date, :capacity, :city, :category, :max_price
-- -----------------------------------------------------

SELECT
    r.room_id,
    h.name AS hotel_name,
    hc.name AS chain_name,
    a.city,
    h.category,
    r.capacity,
    r.price,
    r.view_type,
    r.is_extendable
FROM room r
JOIN hotel h ON r.hotel_id = h.hotel_id
JOIN hotel_chain hc ON h.chain_id = hc.chain_id
JOIN address a ON h.address_id = a.address_id
WHERE r.capacity = :capacity
  AND a.city = :city
  AND h.category = :category
  AND r.price <= :max_price
  AND r.room_id NOT IN (
      SELECT res.room_id
      FROM reservation res
      WHERE (:start_date < res.end_date AND :end_date > res.start_date)
  )
  AND r.room_id NOT IN (
      SELECT ren.room_id
      FROM rental ren
      WHERE (:start_date < ren.end_date AND :end_date > ren.start_date)
  )
ORDER BY r.price ASC;

-- -----------------------------------------------------
-- Query 2
-- Show all reservations for a customer
-- Parameter: :customer_id
-- -----------------------------------------------------

SELECT
    res.reservation_id,
    c.first_name || ' ' || c.last_name AS customer,
    h.name AS hotel,
    r.room_id,
    res.start_date,
    res.end_date
FROM reservation res
JOIN customer c ON res.customer_id = c.customer_id
JOIN room r ON res.room_id = r.room_id
JOIN hotel h ON r.hotel_id = h.hotel_id
WHERE c.customer_id = :customer_id
ORDER BY res.start_date;

-- -----------------------------------------------------
-- Query 3
-- Show rentals managed by an employee
-- Parameter: :employee_id
-- -----------------------------------------------------

SELECT
    ren.rental_id,
    e.first_name || ' ' || e.last_name AS employee,
    c.first_name || ' ' || c.last_name AS customer,
    h.name AS hotel,
    ren.room_id,
    ren.start_date,
    ren.end_date
FROM rental ren
JOIN employee e ON ren.employee_id = e.employee_id
JOIN customer c ON ren.customer_id = c.customer_id
JOIN room r ON ren.room_id = r.room_id
JOIN hotel h ON r.hotel_id = h.hotel_id
WHERE e.employee_id = :employee_id
ORDER BY ren.start_date;

-- -----------------------------------------------------
-- Query 4
-- Show rooms in a hotel with their amenities
-- Parameter: :hotel_id
-- -----------------------------------------------------

SELECT
    r.room_id,
    h.name AS hotel,
    r.capacity,
    r.price,
    r.view_type,
    r.status,
    STRING_AGG(am.name, ', ' ORDER BY am.name) AS amenities
FROM room r
JOIN hotel h ON r.hotel_id = h.hotel_id
LEFT JOIN room_amenity ra ON r.room_id = ra.room_id
LEFT JOIN amenity am ON ra.amenity_id = am.amenity_id
WHERE h.hotel_id = :hotel_id
GROUP BY r.room_id, h.name, r.capacity, r.price, r.view_type, r.status
ORDER BY r.room_id;
