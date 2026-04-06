DROP TABLE IF EXISTS chambre_commodite CASCADE;
DROP TABLE IF EXISTS commodite CASCADE;
DROP TABLE IF EXISTS hotel_telephone CASCADE;
DROP TABLE IF EXISTS hotel_email CASCADE;
DROP TABLE IF EXISTS chaine_telephone CASCADE;
DROP TABLE IF EXISTS chaine_email CASCADE;
DROP TABLE IF EXISTS location CASCADE;
DROP TABLE IF EXISTS reservation CASCADE;
DROP TABLE IF EXISTS employe CASCADE;
DROP TABLE IF EXISTS client CASCADE;
DROP TABLE IF EXISTS chambre CASCADE;
DROP TABLE IF EXISTS hotel CASCADE;
DROP TABLE IF EXISTS chaine_hoteliere CASCADE;
DROP TABLE IF EXISTS adresse CASCADE;

CREATE TABLE adresse (
    id_adresse SERIAL PRIMARY KEY,
    numero_civique INTEGER NOT NULL,
    nom_rue VARCHAR(150) NOT NULL,
    ville VARCHAR(100) NOT NULL,
    province VARCHAR(100) NOT NULL,
    postal_code VARCHAR(20) NOT NULL,
    pays VARCHAR(100) NOT NULL
);

CREATE TABLE chaine_hoteliere (
    id_chaine SERIAL PRIMARY KEY,
    nom VARCHAR(150) NOT NULL,
    nb_hotel INTEGER NOT NULL CHECK (nb_hotel >= 0),
    id_adresse INTEGER NOT NULL,
    CONSTRAINT fk_chaine_adresse
        FOREIGN KEY (id_adresse) REFERENCES adresse(id_adresse)
);

CREATE TABLE chaine_email (
    id_chaine INTEGER NOT NULL,
    email VARCHAR(150) NOT NULL,
    PRIMARY KEY (id_chaine, email),
    CONSTRAINT fk_chaine_email_chaine
        FOREIGN KEY (id_chaine) REFERENCES chaine_hoteliere(id_chaine)
            ON DELETE CASCADE
);

CREATE TABLE chaine_telephone (
    id_chaine INTEGER NOT NULL,
    telephone VARCHAR(30) NOT NULL,
    PRIMARY KEY (id_chaine, telephone),
    CONSTRAINT fk_chaine_telephone_chaine
        FOREIGN KEY (id_chaine) REFERENCES chaine_hoteliere(id_chaine)
            ON DELETE CASCADE
);

CREATE TABLE hotel (
    id_hotel SERIAL PRIMARY KEY,
    id_chaine INTEGER NOT NULL,
    id_gestionnaire INTEGER,
    nom VARCHAR(150) NOT NULL,
    categorie INTEGER NOT NULL CHECK (categorie BETWEEN 1 AND 5),
    nb_chambre INTEGER NOT NULL CHECK (nb_chambre >= 0),
    id_adresse INTEGER NOT NULL,
    CONSTRAINT fk_hotel_chaine
        FOREIGN KEY (id_chaine) REFERENCES chaine_hoteliere(id_chaine),
    CONSTRAINT fk_hotel_adresse
        FOREIGN KEY (id_adresse) REFERENCES adresse(id_adresse)
);

CREATE TABLE hotel_email (
    id_hotel INTEGER NOT NULL,
    email VARCHAR(150) NOT NULL,
    PRIMARY KEY (id_hotel, email),
    CONSTRAINT fk_hotel_email_hotel
        FOREIGN KEY (id_hotel) REFERENCES hotel(id_hotel)
            ON DELETE CASCADE
);

CREATE TABLE hotel_telephone (
    id_hotel INTEGER NOT NULL,
    telephone VARCHAR(30) NOT NULL,
    PRIMARY KEY (id_hotel, telephone),
    CONSTRAINT fk_hotel_telephone_hotel
        FOREIGN KEY (id_hotel) REFERENCES hotel(id_hotel)
            ON DELETE CASCADE
);

CREATE TABLE chambre (
    id_chambre SERIAL PRIMARY KEY,
    id_hotel INTEGER NOT NULL,
    prix DECIMAL(10,2) NOT NULL CHECK (prix > 0),
    capacite VARCHAR(50) NOT NULL,
    vue VARCHAR(50),
    est_extensible BOOLEAN NOT NULL DEFAULT FALSE,
    etat VARCHAR(100) NOT NULL,
    CONSTRAINT fk_chambre_hotel
        FOREIGN KEY (id_hotel) REFERENCES hotel(id_hotel)
);

CREATE TABLE commodite (
    id_commodite SERIAL PRIMARY KEY,
    nom_commodite VARCHAR(100) NOT NULL UNIQUE
);

CREATE TABLE chambre_commodite (
    id_chambre INTEGER NOT NULL,
    id_commodite INTEGER NOT NULL,
    PRIMARY KEY (id_chambre, id_commodite),
    CONSTRAINT fk_chambre_commodite_chambre
        FOREIGN KEY (id_chambre) REFERENCES chambre(id_chambre)
            ON DELETE CASCADE,
    CONSTRAINT fk_chambre_commodite_commodite
        FOREIGN KEY (id_commodite) REFERENCES commodite(id_commodite)
            ON DELETE CASCADE
);

CREATE TABLE client (
    id_client SERIAL PRIMARY KEY,
    nas VARCHAR(20) NOT NULL UNIQUE,
    prenom VARCHAR(100) NOT NULL,
    nom VARCHAR(100) NOT NULL,
    date_inscription DATE NOT NULL,
    id_adresse INTEGER NOT NULL,
    CONSTRAINT fk_client_adresse
        FOREIGN KEY (id_adresse) REFERENCES adresse(id_adresse)
);

CREATE TABLE employe (
    id_employe SERIAL PRIMARY KEY,
    id_hotel INTEGER NOT NULL,
    prenom VARCHAR(100) NOT NULL,
    nom VARCHAR(100) NOT NULL,
    nas VARCHAR(20) NOT NULL UNIQUE,
    role VARCHAR(100) NOT NULL,
    id_adresse INTEGER NOT NULL,
    CONSTRAINT fk_employe_hotel
        FOREIGN KEY (id_hotel) REFERENCES hotel(id_hotel),
    CONSTRAINT fk_employe_adresse
        FOREIGN KEY (id_adresse) REFERENCES adresse(id_adresse)
);

CREATE TABLE reservation (
    id_reservation SERIAL PRIMARY KEY,
    id_chambre INTEGER NOT NULL,
    id_client INTEGER NOT NULL,
    date_debut DATE NOT NULL,
    date_fin DATE NOT NULL,
    CONSTRAINT fk_reservation_chambre
        FOREIGN KEY (id_chambre) REFERENCES chambre(id_chambre),
    CONSTRAINT fk_reservation_client
        FOREIGN KEY (id_client) REFERENCES client(id_client),
    CONSTRAINT chk_reservation_dates
        CHECK (date_debut < date_fin)
);

CREATE TABLE location (
    id_location SERIAL PRIMARY KEY,
    id_client INTEGER NOT NULL,
    id_employe INTEGER NOT NULL,
    id_reservation INTEGER NULL,
    id_chambre INTEGER NOT NULL,
    date_debut DATE NOT NULL,
    date_fin DATE NOT NULL,
    CONSTRAINT fk_location_client
        FOREIGN KEY (id_client) REFERENCES client(id_client),
    CONSTRAINT fk_location_employe
        FOREIGN KEY (id_employe) REFERENCES employe(id_employe),
    CONSTRAINT fk_location_reservation
        FOREIGN KEY (id_reservation) REFERENCES reservation(id_reservation),
    CONSTRAINT fk_location_chambre
        FOREIGN KEY (id_chambre) REFERENCES chambre(id_chambre),
    CONSTRAINT chk_location_dates
        CHECK (date_debut < date_fin)
);

ALTER TABLE hotel
ADD CONSTRAINT fk_hotel_gestionnaire
FOREIGN KEY (id_gestionnaire) REFERENCES employe(id_employe);