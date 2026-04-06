-- =====================================================
-- 06_views.sql
-- Vues SQL pour e-Hotels
-- =====================================================

-- -----------------------------------------------------
-- Vue 1
-- Nombre de chambres disponibles par zone
-- Ici, la zone est representee par la ville
-- -----------------------------------------------------
CREATE OR REPLACE VIEW vue_chambres_disponibles_par_zone AS
SELECT
    a.ville AS zone,
    COUNT(ch.id_chambre) AS nombre_chambres_disponibles
FROM chambre ch
JOIN hotel h ON ch.id_hotel = h.id_hotel
JOIN adresse a ON h.id_adresse = a.id_adresse
WHERE ch.id_chambre NOT IN (
    SELECT r.id_chambre
    FROM reservation r
    WHERE CURRENT_DATE < r.date_fin
      AND CURRENT_DATE >= r.date_debut
)
AND ch.id_chambre NOT IN (
    SELECT l.id_chambre
    FROM location l
    WHERE CURRENT_DATE < l.date_fin
      AND CURRENT_DATE >= l.date_debut
)
GROUP BY a.ville
ORDER BY a.ville;

-- -----------------------------------------------------
-- Vue 2
-- Capacite totale des chambres d’un hotel
-- Conversion textuelle de la capacite en valeur numerique
-- -----------------------------------------------------
CREATE OR REPLACE VIEW vue_capacite_totale_hotel AS
SELECT
    h.id_hotel,
    h.nom AS nom_hotel,
    SUM(
        CASE ch.capacite
            WHEN 'simple' THEN 1
            WHEN 'double' THEN 2
            WHEN 'triple' THEN 3
            WHEN 'suite' THEN 4
            WHEN 'familiale' THEN 5
            ELSE 0
        END
    ) AS capacite_totale
FROM hotel h
JOIN chambre ch ON h.id_hotel = ch.id_hotel
GROUP BY h.id_hotel, h.nom
ORDER BY h.id_hotel;