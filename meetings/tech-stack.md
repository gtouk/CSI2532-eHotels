# Décision technique

## Stack retenu
- PostgreSQL
- Java
- Apache Tomcat
- Docker Compose
- GitHub
- HTML / CSS / JavaScript

## Décision d'équipe
L'équipe a choisi ce stack pour le livrable 2 afin de garder une architecture simple, compatible avec l’énoncé, et facile à partager entre les membres.

## Conséquences
- Le schéma SQL sera écrit pour PostgreSQL
- Le backend sera développé en Java
- L’application sera déployée localement avec Tomcat
- L’environnement sera lancé avec Docker Compose
- Le frontend sera développé sans framework, en HTML/CSS/JavaScript

## Décision Docker

L'équipe utilisera Docker Compose avec au minimum deux conteneurs :

1. `db` : conteneur PostgreSQL pour la base de données
2. `app` : conteneur Java/Tomcat pour l’application web

Cette organisation permettra à tous les membres de lancer le même environnement localement.