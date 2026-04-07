# eHotel — Spécification complète mise à jour

Version: 1.1  
Projet: CSI 2532 — e-Hotels  
Technologies visées: PostgreSQL, Java Spring Boot, HTML/CSS/JavaScript, Docker Compose

---

# 1. Objectif du projet

Le système **eHotel** permet de gérer :

- des chaînes d’hôtels
- des hôtels appartenant à ces chaînes
- des chambres
- des clients
- des employés
- des gestionnaires
- des réservations
- des locations

Le projet couvre :

- une base de données PostgreSQL
- un backend Java Spring Boot
- une interface client
- une interface employé / gestionnaire
- l’exécution intégrée via Docker Compose

---

# 2. Rôles applicatifs

Les rôles exacts du système sont :

- `CLIENT`
- `EMPLOYE`
- `GESTIONNAIRE`

## 2.1 Règles d’accès

- il existe **2 portails de connexion** :
  - portail client
  - portail employé
- le rôle `GESTIONNAIRE` passe par le portail employé
- le gestionnaire possède toutes les permissions de l’employé, plus les permissions d’administration

---

# 3. Portée fonctionnelle

## 3.1 Côté client

Le client peut :

- consulter les hôtels
- consulter les chambres
- rechercher des chambres disponibles
- filtrer les résultats
- créer une réservation
- consulter ses réservations
- annuler sa réservation si autorisée
- consulter et modifier son profil selon les champs permis

## 3.2 Côté employé

L’employé peut :

- se connecter au portail employé
- consulter les réservations
- consulter le détail d’une réservation
- créer une location à partir d’une réservation existante ou en direct selon le flux retenu
- consulter les locations
- effectuer un check-out
- consulter les clients
- consulter les chambres
- modifier le statut opérationnel d’une chambre

## 3.3 Côté gestionnaire

Le gestionnaire peut, en plus des permissions employé :

- gérer les hôtels
- gérer les chambres
- gérer les employés
- consulter les rapports
- accéder aux pages et routes d’administration

## 3.4 Côté administration technique

Le système doit permettre :

- l’initialisation automatique de la base de données
- l’exécution du backend et de PostgreSQL via Docker Compose
- la reproductibilité de l’environnement
- la documentation de l’architecture et de l’utilisation

---

# 4. Architecture globale

Le projet est composé de 4 blocs :

## 4.1 Base de données PostgreSQL

Contient :

- schéma relationnel
- contraintes
- triggers
- index
- vues
- données de test

## 4.2 Backend Java Spring Boot

Expose une API REST pour :

- l’authentification client
- l’authentification employé
- la recherche de chambres
- la gestion des réservations
- la gestion des locations
- la gestion des clients
- la gestion des hôtels, chambres, employés
- les rapports

## 4.3 Frontend Client

Application web simple en :

- HTML
- CSS
- JavaScript

Permet :

- consultation publique
- inscription / connexion client
- recherche de chambres
- réservation
- consultation du portail client

## 4.4 Frontend Employé / Gestionnaire

Application web simple en :

- HTML
- CSS
- JavaScript

Permet :

- connexion employé
- opérations hôtelières
- administration gestionnaire

## 4.5 Docker Compose

Permet de lancer :

- PostgreSQL
- backend Java
- éventuellement le frontend statique si vous choisissez de le servir ainsi

---

# 5. Convention générale du projet

## 5.1 Langue

- noms techniques et code : anglais recommandé
- documentation : français accepté
- noms JSON/API : anglais recommandé
- schéma SQL final : anglais recommandé

## 5.2 Format des dates

- SQL : `DATE`
- JSON : `YYYY-MM-DD`

## 5.3 Monnaie

- utiliser un type numérique, par exemple `NUMERIC(10,2)`

## 5.4 Identifiants

- toutes les tables principales utilisent une clé primaire numérique
- les IDs sont générés côté base ou côté application selon votre choix

## 5.5 Convention REST

- `GET` pour lecture
- `POST` pour création ou action métier
- `PUT` pour mise à jour complète si vous l’utilisez
- `PATCH` pour mise à jour partielle si vous l’utilisez
- `DELETE` possible, mais suppression logique recommandée pour certains objets

---

# 6. Modèle fonctionnel principal

Les entités principales sont :

- `HotelChain`
- `Hotel`
- `Address`
- `Room`
- `Amenity`
- `Customer`
- `Employee`
- `Reservation`
- `Rental`

Relations principales :

- une chaîne possède plusieurs hôtels
- une chaîne, un hôtel, un client et un employé sont rattachés à une adresse
- un hôtel possède plusieurs chambres
- un hôtel emploie plusieurs employés
- un client peut faire plusieurs réservations
- une réservation concerne une chambre
- une location concerne une chambre
- une réservation peut être convertie en location

---

# 7. Modèle de données logique recommandé

## 7.1 Tables principales

### `address`

- `address_id`
- `street_number`
- `street_name`
- `city`
- `province`
- `postal_code`
- `country`

### `hotel_chain`

- `chain_id`
- `name`
- `hotel_count`
- `address_id`

### `hotel_chain_email`

- `chain_email_id`
- `chain_id`
- `email`

### `hotel_chain_phone`

- `chain_phone_id`
- `chain_id`
- `phone`

### `hotel`

- `hotel_id`
- `chain_id`
- `manager_id` nullable
- `name`
- `category`
- `room_count`
- `address_id`

### `hotel_email`

- `hotel_email_id`
- `hotel_id`
- `email`

### `hotel_phone`

- `hotel_phone_id`
- `hotel_id`
- `phone`

### `customer`

- `customer_id`
- `ssn`
- `first_name`
- `last_name`
- `registration_date`
- `address_id`
- `email`
- `phone`
- `address` (champ texte pratique si vous le gardez côté backend)
- `id_type`
- `id_number`

### `employee`

- `employee_id`
- `hotel_id`
- `first_name`
- `last_name`
- `ssn`
- `role`
- `address_id`
- `email`
- `phone`
- `password`
- `active`

### `room`

- `room_id`
- `hotel_id`
- `price`
- `capacity`
- `view_type`
- `is_extendable`
- `status`
- `room_number`

### `amenity`

- `amenity_id`
- `name`

### `room_amenity`

- `room_id`
- `amenity_id`

### `reservation`

- `reservation_id`
- `room_id`
- `customer_id`
- `start_date`
- `end_date`
- `status`
- `total_price`
- `created_at`

### `rental`

- `rental_id`
- `reservation_id` nullable si location directe
- `customer_id`
- `room_id`
- `employee_id`
- `start_date`
- `end_date`
- `status`

## 7.2 Remarques d’alignement backend

- côté API Java, il est acceptable d’exposer `checkInDate` / `checkOutDate` dans les DTOs tout en mappant la table `rental` sur `start_date` / `end_date`
- côté API Java, `pricePerNight` peut mapper la colonne SQL `price`
- côté API Java, `extendable` peut mapper la colonne SQL `is_extendable`
- la capacité des chambres est actuellement un **texte métier** (`single`, `double`, `triple`, `suite`, `family` ou équivalent), pas un entier

---

# 8. Énumérations communes

## 8.1 Roles

- `CLIENT`
- `EMPLOYE`
- `GESTIONNAIRE`

## 8.2 RoomStatus

- `AVAILABLE`
- `RESERVED`
- `OCCUPIED`
- `MAINTENANCE`

## 8.3 ReservationStatus

- `RESERVED`
- `CANCELLED`
- `COMPLETED`

## 8.4 RentalStatus

- `ACTIVE`
- `COMPLETED`
- `CANCELLED`

## 8.5 ContactType

- `EMAIL`
- `PHONE`

## 8.6 ViewType

- `SEA`
- `MOUNTAIN`
- `CITY`
- `NONE`

---

# 9. Format standard des échanges API

## 9.1 Format général

- format : JSON
- dates : `YYYY-MM-DD`
- structure standard :
  - `success`
  - `message`
  - `data`
  - `errors`

## 9.2 Réponse succès type

```json
{
  "success": true,
  "message": "Operation successful",
  "data": {}
}
```

## 9.3 Réponse erreur type

```json
{
  "success": false,
  "message": "Business rule violation",
  "errors": [
    {
      "field": "roomId",
      "error": "Room not available"
    }
  ]
}
```

---

# 10. Routes principales du système

## 10.1 Routes publiques

- `GET /`
- `GET /hotels`
- `GET /hotels/{hotelId}`
- `GET /rooms/search`
- `GET /rooms/{roomId}`
- `POST /client/login`
- `POST /client/register`
- `POST /employee/login`

## 10.2 Routes client

- `GET /client/dashboard`
- `GET /client/profile`
- `POST /client/profile/update`
- `GET /client/reservations`
- `GET /client/reservations/{reservationId}`
- `POST /client/reservations`
- `POST /client/reservations/{reservationId}/cancel`
- `POST /client/logout`
- `GET /client/me` recommandé

## 10.3 Routes employé

- `GET /employee/reservations`
- `GET /employee/reservations/{reservationId}`
- `GET /employee/rentals`
- `GET /employee/rentals/{rentalId}`
- `POST /employee/rentals`
- `POST /employee/rentals/{rentalId}/checkout`
- `GET /employee/customers`
- `GET /employee/customers/{customerId}`
- `GET /employee/customers/{customerId}/reservations` recommandé
- `GET /employee/customers/{customerId}/rentals` recommandé
- `GET /employee/rooms`
- `POST /employee/rooms/{roomId}/status`

## 10.4 Routes gestionnaire

- `GET /employee/admin/hotels`
- `POST /employee/admin/hotels`
- `POST /employee/admin/hotels/{hotelId}/update`
- `POST /employee/admin/hotels/{hotelId}/delete`
- `GET /employee/admin/rooms`
- `POST /employee/admin/rooms`
- `POST /employee/admin/rooms/{roomId}/update`
- `POST /employee/admin/rooms/{roomId}/delete`
- `GET /employee/admin/employees`
- `POST /employee/admin/employees`
- `POST /employee/admin/employees/{employeeId}/update`
- `POST /employee/admin/employees/{employeeId}/delete`
- `GET /employee/admin/reports`

## 10.5 Routes optionnelles / recommandées

Ces routes peuvent être ajoutées plus tard si vous voulez une API plus riche :

- `GET /employee/dashboard`
- `GET /employee/me`
- `POST /employee/logout`
- `POST /employee/reservations/{reservationId}/checkin` si vous souhaitez un endpoint dédié au lieu d’utiliser `POST /employee/rentals` avec `reservationId`
- `GET /employee/admin/hotels/{hotelId}`
- `GET /employee/admin/rooms/{roomId}`
- `GET /employee/admin/employees/{employeeId}`

---

# 11. Sécurité et permissions

## 11.1 Cible fonctionnelle

- `/client/**` nécessite le rôle `CLIENT`
- `/employee/**` nécessite `EMPLOYE` ou `GESTIONNAIRE`
- `/employee/admin/**` nécessite `GESTIONNAIRE`

## 11.2 État actuel du backend

- une configuration de sécurité minimale est suffisante pour le développement
- les restrictions fines par rôle peuvent être renforcées dans une étape ultérieure
- la suppression d’un employé doit rester logique (`active = false`)

---

# 12. Contrat complet — Backend Client

## 12.1 Objet

Le module Backend Client couvre :

- l’authentification client
- la consultation des hôtels et chambres
- la recherche des chambres disponibles
- la création de réservations
- la consultation des réservations du client
- l’annulation des réservations du client
- le tableau de bord client

## 12.2 Routes prises en charge

### Routes publiques

- `GET /`
- `GET /hotels`
- `GET /hotels/{hotelId}`
- `GET /rooms/search`
- `GET /rooms/{roomId}`

### Auth client

- `POST /client/login`
- `POST /client/register`
- `POST /client/logout`

### Portail client

- `GET /client/dashboard`
- `GET /client/profile`
- `POST /client/profile/update`
- `GET /client/reservations`
- `GET /client/reservations/{reservationId}`
- `POST /client/reservations`
- `POST /client/reservations/{reservationId}/cancel`
- `GET /client/me` recommandé

## 12.3 DTOs minimaux attendus

### Requests

- `ClientRegisterRequest`
- `ClientLoginRequest`
- `UpdateClientProfileRequest`
- `CreateReservationRequest`
- `CancelReservationRequest` optionnel
- `RoomSearchRequest` si recherche en POST

### Responses

- `ApiResponse<T>`
- `ApiError`
- `ClientAuthResponse`
- `ClientSummaryResponse`
- `HotelSummaryResponse`
- `HotelDetailsResponse`
- `RoomSummaryResponse`
- `RoomDetailsResponse`
- `ReservationSummaryResponse`
- `ReservationDetailsResponse`
- `ClientDashboardResponse`

## 12.4 Règles métier client

- `startDate < endDate`
- la chambre doit être disponible sur toute la période
- `totalPrice = nombre de nuits x price`
- une réservation créée reçoit le statut `RESERVED`
- seul le client propriétaire peut annuler sa réservation
- une réservation déjà transformée en location ne peut plus être annulée

---

# 13. Contrat complet — Backend Employé / Gestionnaire

## 13.1 Rôles gérés

- `EMPLOYE`
- `GESTIONNAIRE`

## 13.2 Permissions employé

L’employé peut :

- se connecter
- voir les réservations
- voir une réservation
- créer une location
- faire le check-out
- voir les clients
- voir les chambres
- modifier le statut opérationnel d’une chambre

## 13.3 Permissions gestionnaire

Le gestionnaire a toutes les permissions employé, plus :

- créer, modifier, supprimer des hôtels
- créer, modifier, supprimer des chambres
- créer, modifier, désactiver des employés
- consulter les rapports

## 13.4 Routes backend employé / gestionnaire

### Auth employé

- `POST /employee/login`
- `POST /employee/logout` recommandé
- `GET /employee/me` recommandé

### Réservations

- `GET /employee/reservations`
- `GET /employee/reservations/{reservationId}`

### Locations

- `GET /employee/rentals`
- `GET /employee/rentals/{rentalId}`
- `POST /employee/rentals`
- `POST /employee/rentals/{rentalId}/checkout`

### Clients

- `GET /employee/customers`
- `GET /employee/customers/{customerId}`
- `GET /employee/customers/{customerId}/reservations` recommandé
- `GET /employee/customers/{customerId}/rentals` recommandé

### Chambres

- `GET /employee/rooms`
- `POST /employee/rooms/{roomId}/status`

### Admin hôtels

- `GET /employee/admin/hotels`
- `POST /employee/admin/hotels`
- `POST /employee/admin/hotels/{hotelId}/update`
- `POST /employee/admin/hotels/{hotelId}/delete`

### Admin chambres

- `GET /employee/admin/rooms`
- `POST /employee/admin/rooms`
- `POST /employee/admin/rooms/{roomId}/update`
- `POST /employee/admin/rooms/{roomId}/delete`

### Admin employés

- `GET /employee/admin/employees`
- `POST /employee/admin/employees`
- `POST /employee/admin/employees/{employeeId}/update`
- `POST /employee/admin/employees/{employeeId}/delete`

### Rapports

- `GET /employee/admin/reports`

## 13.5 DTOs minimaux attendus

### Requests

- `EmployeeLoginRequest`
- `CreateRentalRequest`
- `UpdateRoomStatusRequest`
- `AdminHotelRequest`
- `AdminRoomRequest`
- `AdminEmployeeRequest`

### Responses

- `ApiResponse<T>`
- `EmployeeAuthResponse`
- `ReservationSummaryResponse`
- `RentalSummaryResponse`
- `CustomerSummaryResponse`
- `CustomerDetailsResponse`
- `RoomSummaryResponse`
- `HotelSummaryResponse`
- `EmployeeSummaryResponse`
- `AdminReportResponse`

## 13.6 Règles métier backend employé

- un flux de check-in peut être implémenté par `POST /employee/rentals` avec `reservationId`
- une location créée depuis une réservation doit utiliser les dates de la réservation
- un check-out n’est possible que pour une location `ACTIVE`
- un check-out met la location en `COMPLETED`
- un check-out remet la chambre en `AVAILABLE`
- un employé standard ne peut pas accéder à `/employee/admin/**`
- seul un gestionnaire peut gérer hôtels, chambres, employés et rapports

## 13.7 Structure Java recommandée

Controllers :

- `EmployeeAuthController`
- `EmployeeReservationController`
- `EmployeeRentalController`
- `EmployeeCustomerController`
- `EmployeeRoomController`
- `AdminHotelController`
- `AdminRoomController`
- `AdminEmployeeController`
- `AdminReportController`

Services :

- `EmployeeAuthService`
- `EmployeeReservationService`
- `EmployeeRentalService`
- `EmployeeCustomerService`
- `EmployeeRoomService`
- `AdminHotelService`
- `AdminRoomService`
- `AdminEmployeeService`
- `AdminReportService`

---

# 14. Contrat complet — Frontend Client

## 14.1 Pages à réaliser

### Pages publiques

- `index.html`
- `hotels.html`
- `hotel-details.html`
- `room-search.html`
- `room-details.html`

### Auth client

- `client-login.html`
- `client-register.html`

### Portail client

- `client/dashboard.html`
- `client/profile.html`
- `client/reservations.html`
- `client/reservation-details.html`
- `client/new-reservation.html`

## 14.2 Responsabilités du frontend client

Le frontend client doit :

- afficher les pages publiques
- afficher les formulaires de connexion et d’inscription
- permettre la recherche de chambres
- afficher les détails d’hôtel et de chambre
- soumettre les demandes de réservation
- afficher les réservations du client
- permettre l’annulation d’une réservation si autorisée
- afficher le profil client
- consommer les réponses JSON du backend

## 14.3 Routes backend consommées

### Publiques

- `GET /`
- `GET /hotels`
- `GET /hotels/{hotelId}`
- `GET /rooms/search`
- `GET /rooms/{roomId}`

### Auth client

- `POST /client/login`
- `POST /client/register`
- `POST /client/logout`

### Portail client

- `GET /client/dashboard`
- `GET /client/profile`
- `POST /client/profile/update`
- `GET /client/reservations`
- `GET /client/reservations/{reservationId}`
- `POST /client/reservations`
- `POST /client/reservations/{reservationId}/cancel`
- `GET /client/me` recommandé

---

# 15. Contrat complet — Frontend Employé / Gestionnaire

## 15.1 Pages à réaliser

### Auth employé

- `employee-login.html`

### Portail employé

- `employee/dashboard.html`
- `employee/reservations.html`
- `employee/reservation-details.html`
- `employee/rentals.html`
- `employee/rental-details.html`
- `employee/customers.html`
- `employee/customer-details.html`
- `employee/rooms.html`

### Portail gestionnaire

- `employee/admin/hotels.html`
- `employee/admin/hotel-form.html`
- `employee/admin/rooms.html`
- `employee/admin/room-form.html`
- `employee/admin/employees.html`
- `employee/admin/employee-form.html`
- `employee/admin/reports.html`

## 15.2 Routes backend consommées

### Auth

- `POST /employee/login`
- `POST /employee/logout` recommandé
- `GET /employee/me` recommandé

### Employé

- `GET /employee/reservations`
- `GET /employee/reservations/{reservationId}`
- `GET /employee/rentals`
- `GET /employee/rentals/{rentalId}`
- `POST /employee/rentals`
- `POST /employee/rentals/{rentalId}/checkout`
- `GET /employee/customers`
- `GET /employee/customers/{customerId}`
- `GET /employee/rooms`
- `POST /employee/rooms/{roomId}/status`

### Gestionnaire

- `GET /employee/admin/hotels`
- `POST /employee/admin/hotels`
- `POST /employee/admin/hotels/{hotelId}/update`
- `POST /employee/admin/hotels/{hotelId}/delete`
- `GET /employee/admin/rooms`
- `POST /employee/admin/rooms`
- `POST /employee/admin/rooms/{roomId}/update`
- `POST /employee/admin/rooms/{roomId}/delete`
- `GET /employee/admin/employees`
- `POST /employee/admin/employees`
- `POST /employee/admin/employees/{employeeId}/update`
- `POST /employee/admin/employees/{employeeId}/delete`
- `GET /employee/admin/reports`

## 15.3 Validation côté client

### Login

- email requis
- password requis

### Checkout

- n’afficher le bouton que si la location est `ACTIVE`

### Changement statut chambre

- n’envoyer que des valeurs `RoomStatus` valides

### Formulaires admin

- champs requis validés avant envoi
- `role` limité à `EMPLOYE` ou `GESTIONNAIRE`
- `category` numérique
- `pricePerNight` numérique positif
- `capacity` texte métier valide (`single`, `double`, `triple`, `suite`, `family` ou convention retenue)
- création employé admin : inclure `ssn` et les champs d’adresse
- création hôtel admin : inclure `address`, `city`, `province`, `country`, `postalCode`

---

# 16. Structure projet recommandée

## 16.1 Backend

```text
backend/src/main/java/com/ehotel/
├── controller/
├── service/
├── repository/
├── dto/
│   ├── request/
│   └── response/
├── model/
├── enums/
├── exception/
├── security/
└── util/
```

## 16.2 Frontend

```text
frontend/
├── index.html
├── hotels.html
├── hotel-details.html
├── room-search.html
├── room-details.html
├── client-login.html
├── client-register.html
├── employee-login.html
├── client/
│   ├── dashboard.html
│   ├── profile.html
│   ├── reservations.html
│   ├── reservation-details.html
│   └── new-reservation.html
├── employee/
│   ├── dashboard.html
│   ├── reservations.html
│   ├── reservation-details.html
│   ├── rentals.html
│   ├── rental-details.html
│   ├── customers.html
│   ├── customer-details.html
│   └── rooms.html
├── employee/admin/
│   ├── hotels.html
│   ├── hotel-form.html
│   ├── rooms.html
│   ├── room-form.html
│   ├── employees.html
│   ├── employee-form.html
│   └── reports.html
├── css/
│   ├── base.css
│   ├── components.css
│   ├── client.css
│   ├── employee.css
│   └── admin.css
└── js/
    ├── main.js
    ├── client.js
    ├── employee.js
    └── admin.js
```

### Rôle des fichiers CSS

- `base.css` : contient les styles globaux communs à tout le site, comme le `body`, la typographie, les espacements, les formulaires, les boutons et les tableaux de base.

- `components.css` : contient les composants réutilisables dans plusieurs pages, comme la navbar, les cards, les alertes, les badges, les sections et les blocs visuels communs.

- `client.css` : contient les styles spécifiques au portail client et à ses pages.

- `employee.css` : contient les styles spécifiques au portail employé et à ses pages opérationnelles.

- `admin.css` : contient les styles spécifiques au portail gestionnaire / administration, notamment les pages CRUD, formulaires admin et rapports.

## 16.3 SQL et init Docker

```text
database/
├── schema/
├── seeds/
├── queries/
├── triggers/
├── indexes/
├── views/
└── init/
    ├── 01_schema.sql
    ├── 02_data.sql
    ├── 04_triggers.sql
    ├── 05_indexes.sql
    ├── 06_views.sql
    ├── 07_backend_alignment_patch.sql
    ├── 08_dev_seed_employee.sql
    └── 09_dev_seed_business_data.sql
```

---

# 17. Docker et intégration

## 17.1 Fichiers attendus

- `Dockerfile` pour le backend
- `docker-compose.yml`
- scripts SQL d’initialisation dans `database/init/`
- `README.md`

## 17.2 Services minimums

### PostgreSQL

- base de données `ehotel`
- chargement automatique des scripts SQL depuis `database/init/`
- port hôte recommandé : `5433`

### Backend

- application Spring Boot connectée à PostgreSQL
- port exposé pour l’API : `8080`

## 17.3 Commandes de démarrage recommandées

```bash
docker compose down -v
docker compose up -d
cd backend
mvn spring-boot:run
```

## 17.4 Paramètres de connexion recommandés

```properties
spring.datasource.url=${DB_URL:jdbc:postgresql://localhost:5433/ehotel}
spring.datasource.username=${DB_USERNAME:postgres}
spring.datasource.password=${DB_PASSWORD:postgres}
```

---

# 18. Répartition du travail recommandée

## 18.1 Backend Client

Responsable de :

- auth client
- recherche
- réservations client
- dashboard client

## 18.2 Backend Employé / Gestionnaire

Responsable de :

- auth employé
- locations et checkout
- clients
- gestion admin
- rapports

## 18.3 Frontend

Responsable de :

- pages publiques
- pages client
- pages employé
- pages admin

## 18.4 Docker / Intégration / Rapport

Responsable de :

- Dockerfile
- docker-compose
- initialisation BD
- README
- rapport
- tests d’intégration

---

# 19. Règles métier globales

## 19.1 Réservation

- une chambre ne peut pas être réservée si elle n’est pas disponible sur l’intervalle demandé
- `startDate` doit être strictement inférieure à `endDate`
- le prix total est calculé à partir du nombre de nuits
- les triggers doivent vérifier les chevauchements en tenant compte du statut métier

## 19.2 Annulation

- seul le propriétaire de la réservation peut l’annuler côté client
- une réservation déjà convertie en location ne peut plus être annulée comme une simple réservation

## 19.3 Conversion réservation → location

- un flux de check-in peut être représenté par la création d’une location depuis une réservation
- la réservation source doit être `RESERVED`
- la réservation source doit passer à `COMPLETED`
- la chambre doit passer à `OCCUPIED`

## 19.4 Check-out

- possible uniquement pour une location `ACTIVE`
- met la location en `COMPLETED`
- remet la chambre en `AVAILABLE`
- la date de fin doit respecter les contraintes SQL (`end_date > start_date`)

## 19.5 Gestion des employés

- un employé standard ne gère pas les ressources admin
- la suppression logique est préférée à la suppression physique

---

# 20. Critères de validation finaux

Le projet est considéré complet si :

## 20.1 Base de données

- le schéma fonctionne
- les données de test se chargent
- les requêtes fonctionnent
- les triggers, index et vues sont présents
- les séquences sont correctement réinitialisées après les inserts manuels si nécessaire

## 20.2 Backend

- auth client fonctionne
- auth employé fonctionne
- recherche de chambres fonctionne
- création réservation fonctionne
- annulation réservation fonctionne
- création location depuis réservation fonctionne
- checkout fonctionne
- gestion admin fonctionne selon le rôle
- rapports fonctionnent

## 20.3 Frontend

- pages publiques fonctionnelles
- portail client fonctionnel
- portail employé fonctionnel
- portail gestionnaire fonctionnel
- erreurs API affichées correctement

## 20.4 Intégration

- le projet démarre avec Docker Compose
- le backend accède à PostgreSQL
- les interfaces consomment correctement l’API

---

# 21. Décision finale d’équipe

Le présent document sert de **référence principale** pour :

- les rôles
- les pages
- les routes
- les permissions
- les formats JSON
- les règles métier
- la structure du projet
- l’initialisation Docker
- l’alignement entre schéma SQL et backend Java

En cas de conflit entre modules, cette spec consolidée fait foi jusqu’à décision d’équipe contraire.
