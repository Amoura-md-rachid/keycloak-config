# Keycloak Integration with Spring Boot 3

Ce projet montre comment sécuriser une application Spring Boot en utilisant Keycloak, un puissant outil open-source de gestion des identités et des accès.

## Fonctionnalités

- Single Sign-On (SSO) avec OpenID Connect
- Contrôle d'accès basé sur les rôles (Role-Based Access Control - RBAC)
- Autorisation fine-grainée (Fine-Grained Authorization)

## Prérequis

- Java 17+
- Maven
- Keycloak Server 21+
- Docker

## Démarrage rapide

Ces instructions vous permettront de configurer et d'exécuter le projet sur votre machine locale pour le développement et les tests.

### Installation

1. Clonez le dépôt :

    ```bash
    git clone <votre-repository-git>
    ```

2. Naviguez dans le répertoire du projet :

    ```bash
    cd votre-repository-name
    ```

3. Utilisez Maven pour construire le projet :

    ```bash
    mvn clean install
    ```

4. Lancez l'application Spring Boot :

    ```bash
    mvn spring-boot:run
    ```

### Configuration Keycloak

1. Exécutez le conteneur Keycloak en utilisant Docker Compose :

    ```bash
    docker-compose up -d
    ```

   Assurez-vous que le fichier Docker Compose est correctement configuré avec les variables d'environnement nécessaires dans un fichier `.env` (ex. : `KEYCLOAK_ADMIN`, `KEYCLOAK_ADMIN_PASSWORD`).

2. Accédez à l'interface d'administration de Keycloak :

   Par défaut, Keycloak est accessible sur [http://localhost:9090](http://localhost:9090).
   Connectez-vous avec les identifiants administratifs définis dans votre fichier `.env`.

3. Créez un nouveau Realm :

   Créez un nouveau Realm dans Keycloak et nommez-le `Amoura`. Si vous choisissez un autre nom, assurez-vous de mettre à jour votre fichier `application.yml` pour spécifier le nom du Realm.

4. Créez des rôles :

   Créez les rôles nécessaires dans l'onglet `Roles` de Keycloak. Par exemple, `client_user` et `client_admin`.

5. Créez des utilisateurs :

   Ajoutez des utilisateurs dans Keycloak et assignez-leur des rôles.

### Gestion des rôles dans Spring Boot

Dans l'application Spring Boot, les rôles sont gérés via les annotations telles que `@PreAuthorize` pour sécuriser les points d'accès API. Par exemple :

```java
@PreAuthorize("hasRole('client_user')")
