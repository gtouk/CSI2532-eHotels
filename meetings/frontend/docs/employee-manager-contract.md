# Contrat Frontend Employé / Gestionnaire — e-Hotel

## 1. Objet

Ce contrat définit le périmètre fonctionnel et technique du Frontend Employé / Gestionnaire de e-Hotel.

Ce module couvre :
- l’authentification employé
- le dashboard employé
- la gestion des réservations
- la gestion des locations
- la gestion des clients
- la consultation et gestion des chambres
- les pages d’administration gestionnaire
- les rapports

---

## 2. Rôles concernés

### Rôles applicatifs
- EMPLOYE
- GESTIONNAIRE

### Règle d’accès
- les deux rôles passent par /employee/login
- EMPLOYE accède au portail employé standard
- GESTIONNAIRE accède au portail employé standard plus aux pages admin

---

## 3. Pages à réaliser

### Auth employé
- employee-login.html

### Portail employé
- employee/dashboard.html
- employee/reservations.html
- employee/reservation-details.html
- employee/rentals.html
- employee/rental-details.html
- employee/customers.html
- employee/customer-details.html
- employee/rooms.html

### Portail gestionnaire
- employee/admin/hotels.html
- employee/admin/hotel-form.html
- employee/admin/rooms.html
- employee/admin/room-form.html
- employee/admin/employees.html
- employee/admin/employee-form.html
- employee/admin/reports.html

---

## 4. Responsabilités du frontend employé

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

---

## 5. Responsabilités du frontend gestionnaire

Le frontend gestionnaire doit en plus :
- afficher la liste des hôtels
- afficher le formulaire d’ajout/modification d’hôtel
- afficher la liste des chambres admin
- afficher le formulaire d’ajout/modification de chambre
- afficher la liste des employés
- afficher le formulaire d’ajout/modification d’employé
- afficher les rapports

---

## 6. Routes backend consommées

### Auth
- POST /employee/login
- POST /employee/logout recommandé
- GET /employee/me recommandé

### Employé
- GET /employee/dashboard
- GET /employee/reservations
- GET /employee/reservations/{reservationId}
- POST /employee/reservations/{reservationId}/checkin
- GET /employee/rentals
- GET /employee/rentals/{rentalId}
- POST /employee/rentals
- POST /employee/rentals/{rentalId}/checkout
- GET /employee/customers
- GET /employee/customers/{customerId}
- GET /employee/rooms
- POST /employee/rooms/{roomId}/status

### Recommandées pour gestion client complète
- GET /employee/customers/{customerId}/reservations
- GET /employee/customers/{customerId}/rentals

### Gestionnaire
- GET /employee/admin/hotels
- POST /employee/admin/hotels
- POST /employee/admin/hotels/{hotelId}/update
- POST /employee/admin/hotels/{hotelId}/delete
- GET /employee/admin/rooms
- POST /employee/admin/rooms
- POST /employee/admin/rooms/{roomId}/update
- POST /employee/admin/rooms/{roomId}/delete
- GET /employee/admin/employees
- POST /employee/admin/employees
- POST /employee/admin/employees/{employeeId}/update
- POST /employee/admin/employees/{employeeId}/delete
- GET /employee/admin/reports

### Recommandées pour détail admin
- GET /employee/admin/hotels/{hotelId}
- GET /employee/admin/rooms/{roomId}
- GET /employee/admin/employees/{employeeId}

---

## 7. Comportement attendu par page

### EmployeeLoginPage
- formulaire email + password
- afficher erreurs de connexion
- rediriger vers /employee/dashboard après succès

### EmployeeDashboardPage
- afficher résumé opérationnel
- afficher accès rapide vers réservations, locations, clients, chambres
- si rôle = GESTIONNAIRE, afficher accès admin

### EmployeeReservationListPage
- afficher toutes les réservations visibles
- filtres optionnels par statut, date, client, chambre
- lien vers détail réservation

### EmployeeReservationDetailsPage
- afficher client, chambre, dates, statut, prix
- afficher bouton check-in si applicable
- afficher bouton annulation si la politique le permet

### RentalListPage
- afficher les locations
- distinguer active/completed/cancelled si utile
- lien vers détail location

### RentalDetailsPage
- afficher client, chambre, employé, dates, statut
- afficher bouton checkout si location ACTIVE

### CustomerListPage
- afficher les clients
- recherche par nom, email ou téléphone si possible

### CustomerDetailsPage
- afficher les informations du client
- afficher ses réservations
- afficher ses locations

### RoomStatusPage
- afficher les chambres
- afficher leur statut
- permettre mise à jour autorisée du statut

### AdminHotelListPage
- afficher les hôtels
- bouton ajouter
- bouton modifier
- bouton supprimer

### AdminHotelFormPage
- formulaire :
  - chainId
  - name
  - category
  - street
  - city
  - province
  - country
  - postalCode

### AdminRoomListPage
- afficher les chambres
- bouton ajouter
- bouton modifier
- bouton supprimer

### AdminRoomFormPage
- formulaire :
  - hotelId
  - roomNumber
  - capacity
  - pricePerNight
  - viewType
  - extendable
  - status

### AdminEmployeeListPage
- afficher les employés
- bouton ajouter
- bouton modifier
- bouton désactiver/supprimer logique

### AdminEmployeeFormPage
- formulaire :
  - firstName
  - lastName
  - email
  - phone
  - password lors de création
  - role
  - hotelId
  - active pour modification

### AdminReportsPage
- afficher les rapports disponibles
- tableaux lisibles
- possibilité de rafraîchir

---

## 8. Contrat de données frontend

### Réponse standard
- success
- message
- data
- errors

### Dates
- format YYYY-MM-DD

### Énumérations à respecter
- Roles : CLIENT, EMPLOYE, GESTIONNAIRE
- RoomStatus : AVAILABLE, RESERVED, OCCUPIED, MAINTENANCE
- ReservationStatus : RESERVED, CANCELLED, COMPLETED
- RentalStatus : ACTIVE, COMPLETED, CANCELLED
- ViewType : SEA, MOUNTAIN, CITY, NONE

---

## 9. Garde d’accès et permissions UI

### Non connecté
- accès autorisé uniquement à employee-login.html
- accès refusé aux autres pages employee

### Connecté comme EMPLOYE
- accès autorisé à :
  - dashboard
  - reservations
  - rentals
  - customers
  - rooms
- accès refusé à employee/admin/*

### Connecté comme GESTIONNAIRE
- accès autorisé à toutes les pages employee/*
- accès autorisé à toutes les pages employee/admin/*

---

## 10. Validation côté client

### Login
- email requis
- password requis

### Check-in
- n’afficher le bouton que si réservation compatible

### Checkout
- n’afficher le bouton que si location ACTIVE

### Changement statut chambre
- n’envoyer que des valeurs de RoomStatus valides

### Formulaires admin
- champs requis validés avant envoi
- role limité à EMPLOYE ou GESTIONNAIRE pour création employé admin
- category numérique
- pricePerNight numérique positif
- capacity entier positif

---

## 11. Fichiers frontend attendus

### HTML
- employee-login.html
- employee/dashboard.html
- employee/reservations.html
- employee/reservation-details.html
- employee/rentals.html
- employee/rental-details.html
- employee/customers.html
- employee/customer-details.html
- employee/rooms.html
- employee/admin/hotels.html
- employee/admin/hotel-form.html
- employee/admin/rooms.html
- employee/admin/room-form.html
- employee/admin/employees.html
- employee/admin/employee-form.html
- employee/admin/reports.html

### JS
- js/employee.js
- js/admin.js

### CSS
- css/style.css

---

## 12. Responsabilités techniques

Le module doit :
- consommer les routes /employee/**
- consommer les routes /employee/admin/** selon le rôle
- afficher correctement les erreurs backend
- masquer l’UI non autorisée selon le rôle
- maintenir une navigation cohérente employé/gestionnaire

---

## 13. Critères de validation

Le module est terminé si :
- un employé peut se connecter
- un employé peut voir les réservations
- un employé peut faire un check-in autorisé
- un employé peut voir les locations
- un employé peut faire un checkout autorisé
- un employé peut voir les clients
- un employé peut voir les chambres et changer un statut autorisé
- un gestionnaire peut accéder aux pages admin
- un gestionnaire peut gérer hôtels, chambres, employés
- un gestionnaire peut consulter les rapports
- un employé standard ne voit jamais l’interface admin