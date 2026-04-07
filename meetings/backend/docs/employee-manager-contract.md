## Rôles gérés par ce module

- EMPLOYE
- GESTIONNAIRE

## Permissions

### EMPLOYE
- se connecter
- voir les réservations
- voir une réservation
- faire le check-in
- créer une location
- faire le check-out
- voir les clients
- voir les chambres
- modifier le statut opérationnel d’une chambre

### GESTIONNAIRE
- toutes les permissions de EMPLOYE
- créer/modifier/supprimer hôtels
- créer/modifier/supprimer chambres
- créer/modifier/désactiver employés
- consulter les rapports

## Routes backend employé / gestionnaire

### Auth employé
- POST /employee/login

### Réservations
- GET /employee/reservations
- GET /employee/reservations/{reservationId}
- POST /employee/reservations/{reservationId}/checkin

### Locations
- GET /employee/rentals
- GET /employee/rentals/{rentalId}
- POST /employee/rentals
- POST /employee/rentals/{rentalId}/checkout

### Clients
- GET /employee/customers
- GET /employee/customers/{customerId}

### Chambres
- GET /employee/rooms
- POST /employee/rooms/{roomId}/status

### Admin hôtels
- GET /employee/admin/hotels
- POST /employee/admin/hotels
- POST /employee/admin/hotels/{hotelId}/update
- POST /employee/admin/hotels/{hotelId}/delete

### Admin chambres
- GET /employee/admin/rooms
- POST /employee/admin/rooms
- POST /employee/admin/rooms/{roomId}/update
- POST /employee/admin/rooms/{roomId}/delete

### Admin employés
- GET /employee/admin/employees
- POST /employee/admin/employees
- POST /employee/admin/employees/{employeeId}/update
- POST /employee/admin/employees/{employeeId}/delete

### Rapports
- GET /employee/admin/reports

## Format de réponse standard

### Succès
{
  "success": true,
  "message": "Operation successful",
  "data": {}
}

### Erreur
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

## Enumerations

### Roles
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

### RentalStatus
- ACTIVE
- COMPLETED
- CANCELLED

## Règles métier backend employé

- un check-in n’est possible que pour une réservation en statut RESERVED
- un check-in crée une location ACTIVE
- un check-in met la chambre en statut OCCUPIED
- un check-out n’est possible que pour une location ACTIVE
- un check-out met la location en COMPLETED
- un check-out remet la chambre en AVAILABLE
- un EMPLOYE ne peut pas accéder à /employee/admin/**
- seul un GESTIONNAIRE peut gérer hôtels, chambres, employés et rapports

