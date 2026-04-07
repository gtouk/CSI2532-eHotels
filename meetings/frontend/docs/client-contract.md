# Contrat Frontend Client — e-Hotel

## 1. Objet

Ce contrat définit le périmètre fonctionnel et technique du Frontend Client de e-Hotel.

Ce module couvre :
- les pages publiques
- l’authentification client
- la navigation client
- le dashboard client
- le profil client
- la recherche de chambres
- la création et consultation de réservations

---

## 2. Rôle concerné

### Rôle applicatif
- CLIENT

### Accès
- certaines pages sont publiques
- les pages du portail client nécessitent une session client active

---

## 3. Pages à réaliser

### Pages publiques
- index.html
- hotels.html
- hotel-details.html
- room-search.html
- room-details.html

### Auth client
- client-login.html
- client-register.html

### Portail client
- client/dashboard.html
- client/profile.html
- client/reservations.html
- client/reservation-details.html
- client/new-reservation.html

---

## 4. Responsabilités du frontend client

Le frontend client doit :
- afficher les pages publiques
- afficher les formulaires de connexion et inscription
- permettre la recherche de chambres
- afficher les détails d’hôtel et de chambre
- soumettre les demandes de réservation
- afficher les réservations du client
- permettre l’annulation d’une réservation si autorisée
- afficher le profil client
- consommer les réponses JSON du backend

---

## 5. Routes backend consommées

### Publiques
- GET /
- GET /hotels
- GET /hotels/{hotelId}
- GET /rooms/search
- GET /rooms/{roomId}

### Auth client
- POST /client/login
- POST /client/register
- POST /client/logout

### Portail client
- GET /client/dashboard
- GET /client/profile
- POST /client/profile/update
- GET /client/reservations
- GET /client/reservations/{reservationId}
- POST /client/reservations
- POST /client/reservations/{reservationId}/cancel

### Optionnelles recommandées
- GET /client/me

---

## 6. Comportement attendu par page

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
- formulaire avec :
  - ville
  - startDate
  - endDate
  - capacity
  - minPrice
  - maxPrice
  - category
  - amenity
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
- redirection vers /client/dashboard après succès

### ClientRegisterPage
- formulaire :
  - firstName
  - lastName
  - email
  - phone
  - password
  - address
- validation côté client
- soumission au backend
- redirection après succès

### ClientDashboardPage
- afficher informations essentielles du client
- afficher réservations récentes
- liens vers profil et réservations

### ClientProfilePage
- afficher les informations du compte
- permettre modification des champs autorisés

### ClientReservationListPage
- afficher la liste des réservations du client connecté
- distinguer réservations à venir, passées, annulées si utile

### ClientReservationDetailsPage
- afficher détail complet de la réservation
- afficher statut
- afficher totalPrice
- afficher bouton annuler si applicable

### NewReservationPage
- préremplir roomId si la chambre est choisie
- afficher startDate et endDate
- afficher récapitulatif
- afficher prix estimé
- soumettre la réservation

---

## 7. Contrat de données frontend

### Réponse API standard
- success
- message
- data
- errors

### Gestion des erreurs
- si success = false, afficher message
- si errors existe, afficher les erreurs par champ
- ne jamais supposer qu’un champ non documenté existe

### Dates
- toujours afficher et envoyer au format YYYY-MM-DD

---

## 8. État UI minimal

Le frontend doit gérer :
- utilisateur non connecté
- utilisateur client connecté
- chargement
- succès
- erreur API
- formulaire invalide
- liste vide

---

## 9. Validation côté client

### Login
- email requis
- password requis

### Register
- firstName requis
- lastName requis
- email requis
- phone requis selon règle choisie
- password requis
- address requise si décidée

### Réservation
- startDate requise
- endDate requise
- startDate < endDate

### Profil
- validation des champs modifiables
- empêcher l’envoi de valeurs vides si non autorisées

---

## 10. Navigation et garde d’accès

### Non connecté
- accès autorisé aux pages publiques
- accès autorisé aux pages login/register
- accès refusé aux pages /client/*

### Connecté comme CLIENT
- accès autorisé aux pages client
- accès refusé aux pages employee
- accès refusé aux pages admin

---

## 11. Fichiers frontend attendus

### HTML
- index.html
- hotels.html
- hotel-details.html
- room-search.html
- room-details.html
- client-login.html
- client-register.html
- client/dashboard.html
- client/profile.html
- client/reservations.html
- client/reservation-details.html
- client/new-reservation.html

### JS
- js/main.js
- js/client.js

### CSS
- css/style.css

---

## 12. Responsabilités techniques

Le module doit :
- appeler les bonnes routes backend
- sérialiser les requêtes JSON correctement
- lire les réponses JSON correctement
- afficher les messages d’erreur du backend
- maintenir une navigation cohérente
- respecter les noms de pages définis dans la spec

---

## 13. Critères de validation

Le module est terminé si :
- un client peut s’inscrire
- un client peut se connecter
- les pages publiques s’affichent
- la recherche de chambres fonctionne
- les détails hôtel et chambre s’affichent
- une réservation peut être créée
- les réservations du client s’affichent
- le détail d’une réservation s’affiche
- une réservation annulable peut être annulée
- un client non connecté ne peut pas ouvrir le portail client