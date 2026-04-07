# eHotel — Spécification complète

Version: 1.0  
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
- effectuer un check-in
- créer une location directe si le flux retenu l’autorise
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
- `Room`
- `Customer`
- `Employee`
- `Booking`
- `Rental`

Relations principales :

- une chaîne possède plusieurs hôtels
- un hôtel possède plusieurs chambres
- un hôtel emploie plusieurs employés
- un client peut faire plusieurs réservations
- une réservation concerne une chambre
- une location concerne une chambre
- une réservation peut être convertie en location

---

# 7. Modèle de données logique minimal

## 7.1 hotel_chain

Champs suggérés :

- `chain_id`
- `name`
- `central_office_address`

## 7.2 hotel_chain_contact

Permet plusieurs contacts par chaîne :

- `contact_id`
- `chain_id`
- `contact_type` (`EMAIL`, `PHONE`)
- `contact_value`

## 7.3 hotel

Champs suggérés :

- `hotel_id`
- `chain_id`
- `name`
- `category`
- `street`
- `city`
- `province`
- `country`
- `postal_code`
- `number_of_rooms`

## 7.4 hotel_contact

Permet plusieurs contacts par hôtel :

- `contact_id`
- `hotel_id`
- `contact_type` (`EMAIL`, `PHONE`)
- `contact_value`

## 7.5 customer

Champs suggérés :

- `customer_id`
- `first_name`
- `last_name`
- `email`
- `phone`
- `password_hash` ou `password`
- `address`
- `id_type`
- `id_number`
- `registration_date`
- `active`

## 7.6 employee

Champs suggérés :

- `employee_id`
- `hotel_id`
- `first_name`
- `last_name`
- `email`
- `phone`
- `password_hash` ou `password`
- `role` (`EMPLOYE`, `GESTIONNAIRE`)
- `active`

## 7.7 room

Champs suggérés :

- `room_id`
- `hotel_id`
- `room_number`
- `capacity`
- `price_per_night`
- `view_type`
- `extendable`
- `status`
- `description`

## 7.8 room_amenity

Permet plusieurs commodités par chambre :

- `room_amenity_id`
- `room_id`
- `amenity_name`

## 7.9 booking

Champs suggérés :

- `booking_id`
- `customer_id`
- `room_id`
- `start_date`
- `end_date`
- `status`
- `total_price`
- `created_at`

## 7.10 rental

Champs suggérés :

- `rental_id`
- `booking_id` nullable si location directe
- `customer_id`
- `room_id`
- `employee_id`
- `check_in_date`
- `check_out_date`
- `status`
- `created_at`

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
- `GET /client/login`
- `POST /client/login`
- `GET /client/register`
- `POST /client/register`
- `GET /employee/login`
- `POST /employee/login`
- `POST /employee/logout`
- `GET /employee/me`

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

- `GET /employee/dashboard`
- `GET /employee/reservations`
- `GET /employee/reservations/{reservationId}`
- `POST /employee/reservations/{reservationId}/checkin`
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
- `GET /employee/admin/hotels/{hotelId}` recommandé
- `POST /employee/admin/hotels`
- `POST /employee/admin/hotels/{hotelId}/update`
- `POST /employee/admin/hotels/{hotelId}/delete`
- `GET /employee/admin/rooms`
- `GET /employee/admin/rooms/{roomId}` recommandé
- `POST /employee/admin/rooms`
- `POST /employee/admin/rooms/{roomId}/update`
- `POST /employee/admin/rooms/{roomId}/delete`
- `GET /employee/admin/employees`
- `GET /employee/admin/employees/{employeeId}` recommandé
- `POST /employee/admin/employees`
- `POST /employee/admin/employees/{employeeId}/update`
- `POST /employee/admin/employees/{employeeId}/delete`
- `GET /employee/admin/reports`

---

# 11. Sécurité et permissions

## 11.1 Accès généraux

- `/client/**` nécessite le rôle `CLIENT`
- `/employee/**` nécessite `EMPLOYE` ou `GESTIONNAIRE`
- `/employee/admin/**` nécessite `GESTIONNAIRE`

## 11.2 Règles client

- un client ne peut consulter que ses propres réservations
- un client ne peut annuler que ses propres réservations
- un client n’accède jamais à `/employee/**`

## 11.3 Règles employé

- un employé standard n’accède jamais à `/employee/admin/**`
- un employé peut effectuer les opérations hôtelières autorisées

## 11.4 Règles gestionnaire

- un gestionnaire peut gérer hôtels, chambres, employés et rapports
- la suppression d’un employé est de préférence logique (`active = false`)

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

- `GET /client/login`
- `POST /client/login`
- `GET /client/register`
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
- `ClientAuthData`
- `ClientSummaryResponse`
- `HotelSummaryResponse`
- `HotelDetailsResponse`
- `RoomSummaryResponse`
- `RoomDetailsResponse`
- `ReservationSummaryResponse`
- `ReservationDetailsResponse`
- `ClientDashboardResponse`

## 12.4 Règles métier client

### Recherche

- filtrage possible par ville, dates, capacité, prix, catégorie et commodités
- seules les chambres compatibles avec l’intervalle demandé doivent être proposées

### Réservation

- `startDate < endDate`
- la chambre doit être disponible sur toute la période
- `totalPrice = nombre de nuits x pricePerNight`
- une réservation créée reçoit le statut `RESERVED`

### Annulation

- seul le client propriétaire peut annuler sa réservation
- une réservation déjà transformée en location ne peut plus être annulée comme une simple réservation
- une réservation annulée reçoit le statut `CANCELLED`

## 12.5 Responsabilités techniques

Le module doit fournir :

- controllers client
- services métier client
- repositories nécessaires
- DTOs client
- validations
- gestion d’erreurs standardisée

## 12.6 Structure Java recommandée

Controllers :

- `ClientAuthController`
- `ClientHotelController` ou `PublicHotelController`
- `ClientRoomController` ou `PublicRoomController`
- `ClientReservationController`
- `ClientProfileController`
- `ClientDashboardController`

Services :

- `ClientAuthService`
- `ClientHotelService`
- `ClientRoomService`
- `ClientReservationService`
- `ClientProfileService`
- `ClientDashboardService`

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
- faire le check-in
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
- `POST /employee/reservations/{reservationId}/checkin`

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

## 13.5 Règles métier backend employé

- un check-in n’est possible que pour une réservation en statut `RESERVED`
- un check-in crée une location `ACTIVE`
- un check-in met la chambre en statut `OCCUPIED`
- un check-out n’est possible que pour une location `ACTIVE`
- un check-out met la location en `COMPLETED`
- un check-out remet la chambre en `AVAILABLE`
- un employé standard ne peut pas accéder à `/employee/admin/**`
- seul un gestionnaire peut gérer hôtels, chambres, employés et rapports

## 13.6 DTOs minimaux attendus

### Requests

- `EmployeeLoginRequest`
- `CreateRentalRequest`
- `UpdateRoomStatusRequest`
- `CreateHotelRequest`
- `UpdateHotelRequest`
- `CreateRoomRequest`
- `UpdateRoomRequest`
- `CreateEmployeeRequest`
- `UpdateEmployeeRequest`

### Responses

- `ApiResponse<T>`
- `EmployeeAuthData`
- `ReservationSummaryResponse`
- `ReservationDetailsResponse`
- `RentalSummaryResponse`
- `RentalDetailsResponse`
- `CustomerSummaryResponse`
- `CustomerDetailsResponse`
- `RoomSummaryResponse`
- `HotelSummaryResponse`
- `EmployeeSummaryResponse`
- `ReportResponse`

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

## 14.4 Comportement attendu par page

### HomePage

- présenter le projet
- rediriger vers recherche, hôtels, login client, login employé

### HotelListPage

- afficher la liste des hôtels
- permettre clic vers la fiche hôtel

### HotelDetailsPage

- afficher nom, catégorie, adresse, contacts, commodités
- lister les chambres ou rediriger vers la recherche

### RoomSearchPage

- formulaire avec ville, dates, capacité, prix, catégorie, commodité
- affichage des résultats
- lien vers détails chambre
- lien vers réservation

### RoomDetailsPage

- afficher les détails complets de la chambre
- afficher son hôtel
- afficher le prix
- proposer réserver

### ClientLoginPage

- formulaire email + password
- gestion des erreurs d’authentification
- redirection vers `/client/dashboard` après succès

### ClientRegisterPage

- formulaire avec `firstName`, `lastName`, `email`, `phone`, `password`, `address`
- validation côté client
- soumission au backend
- redirection après succès

### ClientDashboardPage

- afficher les informations essentielles du client
- afficher les réservations récentes
- liens vers profil et réservations

### ClientProfilePage

- afficher les informations du compte
- permettre modification des champs autorisés

### ClientReservationListPage

- afficher la liste des réservations du client connecté
- distinguer à venir, passées, annulées si utile

### ClientReservationDetailsPage

- afficher détail complet de la réservation
- afficher le statut
- afficher `totalPrice`
- afficher bouton annuler si applicable

### NewReservationPage

- préremplir `roomId` si la chambre a été choisie
- afficher `startDate` et `endDate`
- afficher récapitulatif et prix estimé
- soumettre la réservation

## 14.5 Validation côté client

### Login

- email requis
- password requis

### Register

- `firstName` requis
- `lastName` requis
- `email` requis
- `password` requis

### Réservation

- `startDate` requis
- `endDate` requis
- `startDate < endDate`

## 14.6 Fichiers frontend attendus

### HTML

- `index.html`
- `hotels.html`
- `hotel-details.html`
- `room-search.html`
- `room-details.html`
- `client-login.html`
- `client-register.html`
- `client/dashboard.html`
- `client/profile.html`
- `client/reservations.html`
- `client/reservation-details.html`
- `client/new-reservation.html`

### JS

- `js/main.js`
- `js/client.js`

### CSS

- `css/style.css`

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

## 15.2 Responsabilités du frontend employé

Le frontend employé doit :

- afficher le formulaire de connexion employé
- afficher le dashboard employé
- afficher la liste des réservations
- afficher le détail d’une réservation
- permettre le check-in
- afficher la liste des locations
- afficher le détail d’une location
- permettre le check-out
- afficher la liste des clients
- afficher le détail d’un client
- afficher les chambres
- permettre la mise à jour autorisée du statut d’une chambre

## 15.3 Responsabilités du frontend gestionnaire

Le frontend gestionnaire doit en plus :

- afficher la liste des hôtels
- afficher le formulaire d’ajout/modification d’hôtel
- afficher la liste des chambres admin
- afficher le formulaire d’ajout/modification de chambre
- afficher la liste des employés
- afficher le formulaire d’ajout/modification d’employé
- afficher les rapports

## 15.4 Routes backend consommées

### Auth

- `POST /employee/login`
- `POST /employee/logout` recommandé
- `GET /employee/me` recommandé

### Employé

- `GET /employee/dashboard`
- `GET /employee/reservations`
- `GET /employee/reservations/{reservationId}`
- `POST /employee/reservations/{reservationId}/checkin`
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

## 15.5 Garde d’accès UI

### Non connecté

- accès autorisé uniquement à `employee-login.html`
- accès refusé aux autres pages employee

### Connecté comme EMPLOYE

- accès autorisé à :
  - dashboard
  - reservations
  - rentals
  - customers
  - rooms
- accès refusé à `employee/admin/*`

### Connecté comme GESTIONNAIRE

- accès autorisé à toutes les pages `employee/*`
- accès autorisé à toutes les pages `employee/admin/*`

## 15.6 Validation côté client

### Login

- email requis
- password requis

### Check-in

- n’afficher le bouton que si la réservation est compatible

### Checkout

- n’afficher le bouton que si la location est `ACTIVE`

### Changement statut chambre

- n’envoyer que des valeurs `RoomStatus` valides

### Formulaires admin

- champs requis validés avant envoi
- `role` limité à `EMPLOYE` ou `GESTIONNAIRE`
- `category` numérique
- `pricePerNight` numérique positif
- `capacity` entier positif

## 15.7 Fichiers frontend attendus

### HTML

- `employee-login.html`
- `employee/dashboard.html`
- `employee/reservations.html`
- `employee/reservation-details.html`
- `employee/rentals.html`
- `employee/rental-details.html`
- `employee/customers.html`
- `employee/customer-details.html`
- `employee/rooms.html`
- `employee/admin/hotels.html`
- `employee/admin/hotel-form.html`
- `employee/admin/rooms.html`
- `employee/admin/room-form.html`
- `employee/admin/employees.html`
- `employee/admin/employee-form.html`
- `employee/admin/reports.html`

### JS

- `js/employee.js`
- `js/admin.js`

### CSS

- `css/style.css`

---

# 16. Structure projet recommandée

## 16.1 Backend

```text
src/main/java/com/ehotel/
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
│   └── style.css
└── js/
    ├── main.js
    ├── client.js
    ├── employee.js
    └── admin.js
```

## 16.3 SQL

```text
sql/
├── 01_schema.sql
├── 02_data.sql
├── 03_queries.sql
├── 04_triggers.sql
├── 05_indexes.sql
└── 06_views.sql
```

---

# 17. Docker et intégration

## 17.1 Fichiers attendus

- `Dockerfile` pour le backend
- `docker-compose.yml`
- scripts SQL d’initialisation
- `README.md`

## 17.2 Services minimums

### PostgreSQL

- base de données `ehotel`
- chargement automatique des scripts SQL

### Backend

- application Spring Boot connectée à PostgreSQL
- port exposé pour l’API

### Frontend

- servi statiquement ou ouvert directement selon votre choix

## 17.3 Objectif Docker

Le projet doit pouvoir être lancé de façon reproductible avec une commande simple du type :

```bash
docker compose up --build
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
- check-in / check-out
- locations
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

## 19.2 Annulation

- seul le propriétaire de la réservation peut l’annuler côté client
- une réservation déjà convertie en location ne peut plus être annulée comme une simple réservation

## 19.3 Check-in

- possible uniquement pour une réservation `RESERVED`
- crée une location `ACTIVE`
- met la chambre en `OCCUPIED`

## 19.4 Check-out

- possible uniquement pour une location `ACTIVE`
- met la location en `COMPLETED`
- remet la chambre en `AVAILABLE`

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

## 20.2 Backend

- auth client fonctionne
- auth employé fonctionne
- recherche de chambres fonctionne
- création réservation fonctionne
- annulation réservation fonctionne
- check-in fonctionne
- check-out fonctionne
- gestion admin fonctionne selon le rôle

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

## 20.5 Sécurité

- un client n’accède jamais aux routes employé
- un employé standard n’accède jamais aux routes admin
- un gestionnaire peut accéder à toutes les routes prévues pour lui

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

En cas de conflit entre modules, cette spec consolidée fait foi jusqu’à décision d’équipe contraire.
