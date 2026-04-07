# Contrat Backend Client — e-Hotel

## 1. Objet

Ce contrat définit le périmètre, les responsabilités, les routes, les formats de données et les règles métier du module Backend Client du projet e-Hotel.

Ce module couvre :
- l’authentification client
- la consultation des hôtels et chambres
- la recherche de chambres disponibles
- la création de réservations
- la consultation des réservations du client
- l’annulation des réservations du client
- le tableau de bord client

---

## 2. Rôle concerné

### Rôle applicatif
- CLIENT

### Règle d’accès
- le portail client est accessible uniquement aux utilisateurs authentifiés avec le rôle CLIENT
- les routes publiques de consultation peuvent rester accessibles sans authentification selon l’implémentation retenue

---

## 3. Responsabilités du module

Le Backend Client est responsable de :

### Authentification
- inscription client
- connexion client
- déconnexion client
- récupération du profil connecté si nécessaire

### Recherche et consultation
- lister les hôtels
- consulter un hôtel
- rechercher des chambres
- consulter une chambre

### Réservations
- créer une réservation
- afficher la liste des réservations du client connecté
- afficher le détail d’une réservation du client connecté
- annuler une réservation appartenant au client connecté

### Tableau de bord
- fournir les données du dashboard client
- fournir les informations de profil
- fournir l’historique de réservation

---

## 4. Routes prises en charge

### Routes publiques
- GET /
- GET /hotels
- GET /hotels/{hotelId}
- GET /rooms/search
- GET /rooms/{roomId}

### Auth client
- GET /client/login
- POST /client/login
- GET /client/register
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

### Routes additionnelles recommandées
- GET /client/me
- GET /client/search

---

## 5. Contraintes de sécurité

- seul un utilisateur avec rôle CLIENT peut accéder à /client/**
- un client ne peut consulter que ses propres réservations
- un client ne peut annuler que ses propres réservations
- un client ne peut jamais accéder à /employee/**
- un client ne peut jamais accéder à /employee/admin/**

---

## 6. Modèles logiques utilisés

### Client
- clientId
- firstName
- lastName
- email
- phone
- address
- role = CLIENT

### Hotel
- hotelId
- chainId
- name
- category
- address
- contacts
- amenities

### Room
- roomId
- hotelId
- roomNumber
- capacity
- pricePerNight
- viewType
- extendable
- status
- amenities

### Reservation
- reservationId
- clientId
- roomId
- startDate
- endDate
- status
- totalPrice
- createdAt

---

## 7. Format des échanges

### Format général
- JSON
- dates au format YYYY-MM-DD
- réponse standard avec :
  - success
  - message
  - data
  - errors

### Exemple succès
{
  "success": true,
  "message": "Reservation created successfully",
  "data": {
    "reservationId": 101
  }
}

### Exemple erreur
{
  "success": false,
  "message": "Room is not available for the selected dates",
  "errors": [
    {
      "field": "endDate",
      "error": "Invalid date range"
    }
  ]
}

---

## 8. DTOs minimaux attendus

### Requests
- ClientRegisterRequest
- ClientLoginRequest
- UpdateClientProfileRequest
- CreateReservationRequest
- CancelReservationRequest optionnel
- RoomSearchRequest si recherche en POST ou objet filtré

### Responses
- ApiResponse<T>
- ApiError
- ClientAuthData
- ClientSummaryResponse
- HotelSummaryResponse
- HotelDetailsResponse
- RoomSummaryResponse
- RoomDetailsResponse
- ReservationSummaryResponse
- ReservationDetailsResponse
- ClientDashboardResponse

---

## 9. Énumérations à respecter

### Roles
- CLIENT
- EMPLOYE
- GESTIONNAIRE

### RoomStatus
- AVAILABLE
- RESERVED
- OCCUPIED
- MAINTENANCE

### ReservationStatus
- RESERVED
- CANCELLED
- COMPLETED

### ViewType
- SEA
- MOUNTAIN
- CITY
- NONE

### ContactType
- EMAIL
- PHONE

---

## 10. Règles métier

### Recherche
- la recherche peut filtrer par ville, dates, capacité, prix, catégorie et commodités
- seules les chambres compatibles avec les dates demandées doivent être proposées
- une chambre occupée ou déjà réservée sur l’intervalle ne doit pas apparaître comme disponible

### Réservation
- startDate doit être strictement inférieure à endDate
- la chambre doit être disponible sur toute la période
- totalPrice = nombre de nuits x prixPerNight
- une réservation créée reçoit le statut RESERVED

### Annulation
- seul le client propriétaire peut annuler sa réservation
- une réservation déjà transformée en location ne peut plus être annulée comme une simple réservation
- une réservation annulée reçoit le statut CANCELLED

### Profil
- un client ne peut modifier que ses propres informations autorisées
- l’email peut être non modifiable si vous voulez éviter les conflits d’authentification

---

## 11. Responsabilités techniques

Le module doit fournir :
- les controllers client
- les services métier client
- les repositories nécessaires
- les DTOs client
- la validation des entrées
- les erreurs métier standardisées
- l’accès sécurisé aux données par clientId

---

## 12. Structure Java attendue

Sous le package racine backend :
- controller
- service
- repository
- dto.request
- dto.response
- model
- enums
- exception
- security
- util

### Controllers attendus
- ClientAuthController
- ClientHotelController ou PublicHotelController
- ClientRoomController ou PublicRoomController
- ClientReservationController
- ClientProfileController
- ClientDashboardController

### Services attendus
- ClientAuthService
- ClientHotelService
- ClientRoomService
- ClientReservationService
- ClientProfileService
- ClientDashboardService

---

## 13. Critères de validation

Le module est considéré terminé si :
- l’inscription client fonctionne
- la connexion client fonctionne
- le dashboard client retourne les bonnes données
- la recherche de chambres fonctionne
- la création de réservation fonctionne
- la liste des réservations du client fonctionne
- le détail d’une réservation du client fonctionne
- l’annulation d’une réservation autorisée fonctionne
- un client ne peut jamais accéder aux données d’un autre client