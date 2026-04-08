# CSI2532 - e-Hotels

Projet de cours pour **CSI2532 : Bases de données I**.

## Membres de l'équipe
- Gilly Dyvan Toukam Mengaptche
- Frank Martial Penka Nghoko
- Naomi Kesley Tekeu
- Antoine Missoup

---

## Description
**e-Hotels** est une application de gestion hôtelière permettant :
- la consultation de la disponibilité des chambres
- la réservation de chambres
- la location directe de chambres
- la conversion d’une réservation en location
- la gestion des clients, employés et gestionnaires
- l’administration des hôtels, chambres, employés et rapports

Le projet couvre :
- une base de données PostgreSQL
- un backend Java Spring Boot
- une interface frontend HTML / CSS / JavaScript
- une exécution reproductible via Docker Compose

---

## Structure du projet

```text
CSI2532-eHotels/
├── backend/               # Backend Spring Boot
│   ├── Dockerfile
│   ├── pom.xml
│   └── src/
├── database/              # Scripts SQL et initialisation
│   ├── init/              # Scripts réellement utilisés au démarrage Docker
│   ├── schema/
│   ├── seeds/
│   ├── triggers/
│   ├── indexes/
│   ├── views/
│   └── queries/
├── frontend/              # Frontend HTML / CSS / JS
├── docs/                  # Documentation et livrables
├── meetings/              # Décisions d’équipe et suivi
├── docker-compose.yml
├── README.md
├── ehotel_spec_updated.md
└── backend_employee_manager_handoff.md
```

---

## Stack technique retenu
- **Base de données** : PostgreSQL
- **Backend** : Java Spring Boot
- **ORM / Persistance** : Spring Data JPA / Hibernate
- **Conteneurisation** : Docker / Docker Compose
- **Gestion de version** : GitHub
- **Frontend** : HTML / CSS / JavaScript

## Justification du choix
Ce stack a été retenu car il est cohérent avec les technologies suggérées dans l’énoncé du projet et permet une séparation claire entre :
- la base de données
- la logique applicative
- l’interface utilisateur

**PostgreSQL** facilite la gestion des contraintes, vues, index et triggers.  
**Spring Boot** permet de structurer le backend proprement avec controllers, services, repositories et DTOs.  
**Docker Compose** permet à tous les membres de lancer le même environnement localement.  
**HTML/CSS/JavaScript** permet de développer rapidement une interface simple et fonctionnelle.

---

## Règles de travail
- Ne pas pousser directement sur `main`
- Travailler avec des branches
- Faire des pull requests
- Nommer les commits clairement
- Garder la base Docker comme environnement de référence

---

## Fonctionnalités principales

### Côté client
- consulter les hôtels
- consulter les chambres
- rechercher des chambres disponibles
- créer une réservation
- consulter ses réservations
- annuler une réservation si autorisée
- consulter et modifier son profil

### Côté employé
- se connecter au portail employé
- consulter les réservations
- consulter le détail d’une réservation
- créer une location à partir d’une réservation
- effectuer un check-out
- consulter les locations
- consulter les clients
- consulter les chambres
- modifier le statut opérationnel d’une chambre

### Côté gestionnaire
En plus des permissions employé :
- gérer les hôtels
- gérer les chambres
- gérer les employés
- consulter les rapports

---

## Backend employé / gestionnaire actuellement livré

Le backend employé / gestionnaire est déjà **fonctionnellement livré** pour :

### Authentification employé
- `POST /employee/login`

### Réservations et locations
- `GET /employee/reservations`
- `GET /employee/reservations/{reservationId}`
- `GET /employee/rentals`
- `GET /employee/rentals/{rentalId}`
- `POST /employee/rentals`
- `POST /employee/rentals/{rentalId}/checkout`

### Chambres
- `GET /employee/rooms`
- `POST /employee/rooms/{roomId}/status`

### Clients
- `GET /employee/customers`
- `GET /employee/customers/{customerId}`

### Administration gestionnaire
#### Hôtels
- `GET /employee/admin/hotels`
- `POST /employee/admin/hotels`
- `POST /employee/admin/hotels/{hotelId}/update`
- `POST /employee/admin/hotels/{hotelId}/delete`

#### Chambres
- `GET /employee/admin/rooms`
- `POST /employee/admin/rooms`
- `POST /employee/admin/rooms/{roomId}/update`
- `POST /employee/admin/rooms/{roomId}/delete`

#### Employés
- `GET /employee/admin/employees`
- `POST /employee/admin/employees`
- `POST /employee/admin/employees/{employeeId}/update`
- `POST /employee/admin/employees/{employeeId}/delete`

#### Rapports
- `GET /employee/admin/reports`

---

## Format standard des réponses API

### Succès
```json
{
  "success": true,
  "message": "Operation successful",
  "data": {}
}
```

### Erreur
```json
{
  "success": false,
  "message": "Unexpected server error",
  "data": null,
  "errors": [
    {
      "field": "server",
      "error": "Detailed message"
    }
  ]
}
```

---

## Configuration Docker

### Services lancés par Docker Compose
- `postgres` : base de données PostgreSQL
- `backend` : application Spring Boot

### Base de données PostgreSQL
Configuration actuelle :
- **Host local** : `localhost`
- **Port local** : `5433`
- **Database** : `ehotel`
- **Username** : `postgres`
- **Password** : `postgres`

### Backend
- **Port local** : `8080`

---

## How to start the project

### Option recommandée : tout lancer avec Docker
Depuis la racine du projet :

```bash
docker compose down -v
docker compose up --build
```

Cela :
- recrée proprement la base
- rejoue les scripts SQL d’initialisation
- construit le backend
- lance PostgreSQL et Spring Boot

### Option de développement : base en Docker, backend avec Maven
Depuis la racine du projet :

```bash
docker compose down -v
docker compose up -d
```

Puis dans `backend/` :

```bash
mvn clean compile
mvn spring-boot:run
```

---

## Initialisation PostgreSQL

L’initialisation automatique de la base repose sur :

```text
database/init/
```

Les scripts SQL de ce dossier sont montés dans PostgreSQL et exécutés lors de la création initiale du volume.

Si vous modifiez les scripts SQL et voulez repartir d’une base propre :

```bash
docker compose down -v
docker compose up -d
```

---

## Configuration backend

Le backend lit la configuration BD depuis `backend/src/main/resources/application.properties`.

Configuration locale actuelle :

```properties
spring.datasource.url=${DB_URL:jdbc:postgresql://localhost:5433/ehotel}
spring.datasource.username=${DB_USERNAME:postgres}
spring.datasource.password=${DB_PASSWORD:postgres}
```

Quand le backend tourne **dans Docker Compose**, il utilise le nom du service Docker :

```text
jdbc:postgresql://postgres:5432/ehotel
```

---

## Données de test actuelles

Le projet contient des données de développement permettant de tester rapidement le backend.

### Compte employé de test
```json
{
  "email": "employee1@ehotel.com",
  "password": "password123"
}
```

### Exemple de test login
```bash
curl -X POST http://localhost:8080/employee/login \
  -H "Content-Type: application/json" \
  -d '{
    "email": "employee1@ehotel.com",
    "password": "password123"
  }'
```

---

## Commandes utiles de test

### Vérifier les rapports
```bash
curl http://localhost:8080/employee/admin/reports
```

### Voir les réservations
```bash
curl http://localhost:8080/employee/reservations
```

### Voir les locations
```bash
curl http://localhost:8080/employee/rentals
```

### Voir les chambres
```bash
curl http://localhost:8080/employee/rooms
```

### Voir les clients
```bash
curl http://localhost:8080/employee/customers
```

### Voir les employés admin
```bash
curl http://localhost:8080/employee/admin/employees
```

---

## Utilisation par les membres de l’équipe

### Pour un membre backend
Un coéquipier peut faire :

```bash
git pull
docker compose down -v
docker compose up -d
cd backend
mvn spring-boot:run
```

### Pour un membre frontend
Le frontend peut déjà consommer les endpoints du backend employé / gestionnaire.  
Il peut donc commencer à brancher :
- login employé
- réservations
- locations
- chambres
- clients
- hôtels admin
- chambres admin
- employés admin
- rapports admin

---

## Limitations connues

Le projet est fonctionnel, mais certains points restent des améliorations possibles :
- sécurité par rôles encore simplifiée
- mots de passe en clair pour le développement
- gestion des adresses encore simplifiée dans certaines parties Java
- pas encore de vraie suite automatisée de tests d’intégration
- certaines parties frontend et backend client restent à finaliser

Ces points ne bloquent pas l’exécution ou la démonstration du projet.

---

## Documents de référence

Les documents suivants doivent être conservés dans le dépôt :
- `ehotel_spec_updated.md`
- `backend_employee_manager_handoff.md`

Ils servent de référence pour :
- la portée fonctionnelle
- les contrats backend
- les attentes frontend
- l’état réel du backend employé / gestionnaire

---

## Prochaines étapes recommandées
- finaliser le frontend
- compléter le backend client si nécessaire
- ajouter des tests d’intégration automatisés
- améliorer la sécurité
- rédiger le rapport final

---

## Résumé final

Le projet est maintenant structuré pour permettre à n’importe quel membre de l’équipe de :
- récupérer le dépôt
- lancer PostgreSQL et le backend
- obtenir la même base de données
- tester les endpoints déjà livrés
- travailler dans un environnement cohérent

