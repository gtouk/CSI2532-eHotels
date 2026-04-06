-- =====================================================
-- 03_queries.sql
-- Requetes principales pour e-Hotels
-- =====================================================

-- -----------------------------------------------------
-- Requete 1
-- Recherche de chambres disponibles avec criteres combines
-- Parametres a remplacer :
-- :date_debut, :date_fin, :capacite, :ville, :categorie, :prix_max
-- -----------------------------------------------------

SELECT
    ch.id_chambre,
    h.nom AS nom_hotel,
    c.nom AS nom_chaine,
    a.ville,
    h.categorie,
    ch.capacite,
    ch.prix,
    ch.vue,
    ch.est_extensible
FROM chambre ch
JOIN hotel h ON ch.id_hotel = h.id_hotel
JOIN chaine_hoteliere c ON h.id_chaine = c.id_chaine
JOIN adresse a ON h.id_adresse = a.id_adresse
WHERE ch.capacite = :capacite
  AND a.ville = :ville
  AND h.categorie = :categorie
  AND ch.prix <= :prix_max
  AND ch.id_chambre NOT IN (
      SELECT r.id_chambre
      FROM reservation r
      WHERE (:date_debut < r.date_fin AND :date_fin > r.date_debut)
  )
  AND ch.id_chambre NOT IN (
      SELECT l.id_chambre
      FROM location l
      WHERE (:date_debut < l.date_fin AND :date_fin > l.date_debut)
  )
ORDER BY ch.prix ASC;

-- -----------------------------------------------------
-- Requete 2
-- Afficher toutes les reservations d’un client
-- Parametre : :id_client
-- -----------------------------------------------------

SELECT
    r.id_reservation,
    cl.prenom || ' ' || cl.nom AS client,
    h.nom AS hotel,
    ch.id_chambre,
    r.date_debut,
    r.date_fin
FROM reservation r
JOIN client cl ON r.id_client = cl.id_client
JOIN chambre ch ON r.id_chambre = ch.id_chambre
JOIN hotel h ON ch.id_hotel = h.id_hotel
WHERE cl.id_client = :id_client
ORDER BY r.date_debut;

-- -----------------------------------------------------
-- Requete 3
-- Afficher les locations gerees par un employe
-- Parametre : :id_employe
-- -----------------------------------------------------

SELECT
    l.id_location,
    e.prenom || ' ' || e.nom AS employe,
    cl.prenom || ' ' || cl.nom AS client,
    h.nom AS hotel,
    l.id_chambre,
    l.date_debut,
    l.date_fin
FROM location l
JOIN employe e ON l.id_employe = e.id_employe
JOIN client cl ON l.id_client = cl.id_client
JOIN chambre ch ON l.id_chambre = ch.id_chambre
JOIN hotel h ON ch.id_hotel = h.id_hotel
WHERE e.id_employe = :id_employe
ORDER BY l.date_debut;

-- -----------------------------------------------------
-- Requete 4
-- Afficher les chambres d’un hotel avec leurs commodites
-- Parametre : :id_hotel
-- -----------------------------------------------------

SELECT
    ch.id_chambre,
    h.nom AS hotel,
    ch.capacite,
    ch.prix,
    ch.vue,
    ch.etat,
    STRING_AGG(co.nom_commodite, ', ' ORDER BY co.nom_commodite) AS commodites
FROM chambre ch
JOIN hotel h ON ch.id_hotel = h.id_hotel
LEFT JOIN chambre_commodite cc ON ch.id_chambre = cc.id_chambre
LEFT JOIN commodite co ON cc.id_commodite = co.id_commodite
WHERE h.id_hotel = :id_hotel
GROUP BY ch.id_chambre, h.nom, ch.capacite, ch.prix, ch.vue, ch.etat
ORDER BY ch.id_chambre;