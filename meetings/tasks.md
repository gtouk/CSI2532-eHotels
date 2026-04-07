## 8. Répartition du travail

```mermaid
flowchart LR
    A["Martial"] --> A1["Backend Client"]
    A1 --> A2["Auth client"]
    A1 --> A3["Recherche"]
    A1 --> A4["Reservations"]

    B["Gilly"] --> B1["Backend Employe Gestionnaire"]
    B1 --> B2["Auth employe"]
    B1 --> B3["Locations"]
    B1 --> B4["Gestion admin"]

    C["Antoine"] --> C1["Frontend HTML CSS JS"]
    C1 --> C2["Pages publiques"]
    C1 --> C3["Pages client"]
    C1 --> C4["Pages employe admin"]

    D["Naomi"] --> D1["Integration Docker Rapport"]
    D1 --> D2["Docker Compose"]
    D1 --> D3["Tests integration"]
    D1 --> D4["README et rapport"]
```

### Répartition détaillée

#### Martial — Backend client
- authentification client
- recherche d’hôtels et chambres
- création, consultation et annulation de réservations
- tableau de bord client

#### Antoine — Backend employé / gestionnaire
- authentification employé
- gestion des réservations et locations
- gestion des clients
- routes admin pour hôtels, chambres, employés et rapports

#### Antoine — Frontend
- pages publiques
- pages client
- pages employé
- pages gestionnaire
- validation HTML/CSS/JS

#### Naomi — Intégration / Docker / rapport
- `Dockerfile`
- `docker-compose.yml`
- initialisation PostgreSQL
- tests d’intégration
- README
- rapport final
