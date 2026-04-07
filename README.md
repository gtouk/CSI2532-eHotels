# CSI2532 - e-Hotels

Projet de cours pour CSI2532 : Bases de données I.

## Membres de l'équipe
- Gilly Dyvan Toukam Mengaptche
- Frank Martial Penka Nghoko
- Naomi Kesley Tekeu
- Antoine Missoup

## Description
Application de gestion hôtelière permettant :
- la consultation de la disponibilité des chambres
- la réservation de chambres
- la location directe de chambres
- la conversion d’une réservation en location

## Structure du projet
- `docs/` : documentation et livrables
- `database/` : scripts SQL
- `backend/` : logique applicative
- `frontend/` : interface utilisateur
- `meetings/` : décisions d’équipe et suivi

## Stack technique retenu

- **Base de données** : PostgreSQL
- **Backend** : Java
- **Serveur d’application** : Apache Tomcat
- **Conteneurisation** : Docker Compose
- **Gestion de version** : GitHub
- **Frontend** : HTML / CSS / JavaScript

## Justification du choix
Ce stack a été retenu car il est cohérent avec les technologies suggérées dans l’énoncé du projet et permet une séparation claire entre la base de données, la logique applicative et l’interface utilisateur.
**PostgreSQL** facilite la gestion des contraintes, vues, index et triggers.
**Java** avec **Tomcat** permet de développer une application web structurée.
**Docker** Compose permet à tous les membres de lancer le même environnement localement.
**HTML/CSS/JavaScript** permet de développer rapidement une interface simple et fonctionnelle.

## Règles de travail
- Ne pas pousser directement sur `main`
- Travailler avec des branches
- Faire des pull requests
- Nommer les commits clairement

## Prochaines étapes
1. Finaliser le stack technique
2. Écrire le schéma SQL
3. Ajouter les données de test
4. Développer l’application
