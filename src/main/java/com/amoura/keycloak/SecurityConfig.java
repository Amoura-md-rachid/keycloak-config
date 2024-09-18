//package com.amoura.keycloak;
//
//import lombok.RequiredArgsConstructor;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
//import org.springframework.security.config.annotation.web.builders.HttpSecurity;
//import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
//import org.springframework.security.web.SecurityFilterChain;
//
//import static org.springframework.security.config.http.SessionCreationPolicy.STATELESS;
//
///**
// * Classe de configuration de la sécurité pour l'application Spring Boot.
// *
// * Cette classe configure la sécurité Web en définissant les règles d'autorisation et en intégrant le support
// * des tokens JWT via Keycloak pour l'authentification.
// */
//@Configuration // Indique que cette classe contient des configurations Spring
//@EnableWebSecurity // Active la sécurité Web pour l'application
//@EnableMethodSecurity // Active la sécurité basée sur les annotations au niveau des méthodes
//@RequiredArgsConstructor // Génère automatiquement un constructeur avec les dépendances finales
//public class SecurityConfig {
//
//    // Convertisseur personnalisé pour l'extraction des rôles à partir du JWT (fourni par Keycloak)
//    private final JwtAuthConverter jwtAuthConverter;
//
//    /**
//     * Configuration de la chaîne de filtres de sécurité pour gérer les requêtes HTTP et l'authentification.
//     *
//     * @param http L'objet {@link HttpSecurity} utilisé pour définir les règles de sécurité.
//     * @return Un objet {@link SecurityFilterChain} contenant la configuration de sécurité.
//     * @throws Exception Si une erreur survient lors de la construction de la chaîne de sécurité.
//     */
//    @Bean
//    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
//        http
//                // Désactive la protection CSRF (Cross-Site Request Forgery) car l'application utilise des tokens JWT
//                .csrf(csrf -> csrf.disable())
//                // Définit que toutes les requêtes HTTP doivent être authentifiées
//                .authorizeRequests(authz -> authz
//                        .anyRequest().authenticated()
//                )
//                // Configure l'application pour utiliser Keycloak comme serveur OAuth2 et JWT pour l'authentification
//                .oauth2ResourceServer(oauth2 -> oauth2
//                        .jwt(jwt -> jwt
//                                .jwtAuthenticationConverter(jwtAuthConverter) // Utilise le convertisseur JWT personnalisé
//                        )
//                )
//                // Définit la politique de session comme étant sans état (les sessions ne sont pas utilisées, car JWT est stateless)
//                .sessionManagement(session -> session
//                        .sessionCreationPolicy(STATELESS)
//                );
//
//        // Construit et retourne la chaîne de filtres de sécurité
//        return http.build();
//    }
//}
//
//
