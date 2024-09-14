# Étape de construction
FROM eclipse-temurin:21.0.2_13-jdk-jammy AS builder
# Utilise Eclipse Temurin JDK 21 basé sur Ubuntu Jammy pour la construction

WORKDIR /opt/app
# Définit le répertoire de travail

COPY .mvn/ .mvn
COPY mvnw pom.xml ./
# Copie les fichiers nécessaires pour Maven Wrapper et pom.xml

RUN ./mvnw dependency:go-offline
# Télécharge les dépendances Maven et les met en cache

COPY ./src ./src
# Copie le code source de l'application

RUN ./mvnw clean package
# Compile l'application et crée le fichier JAR
# Les tests ne sont pas ignorés par défaut. Ajoutez l'option -DskipTests si nécessaire.

# Étape d'exécution
FROM eclipse-temurin:21.0.2_13-jre-jammy AS final
# Utilise une image JRE plus légère pour l'exécution

WORKDIR /opt/app
# Définit le répertoire de travail pour l'exécution

COPY --from=builder /opt/app/target/*.jar /opt/app/app.jar
# Copie le JAR depuis l'étape de construction

EXPOSE 8080
# Expose le port 8080 pour le serveur

ENTRYPOINT ["java", "-jar", "/opt/app/app.jar"]
# Commande stricte pour démarrer l'application
