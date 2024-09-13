package com.amoura.keycloak;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.convert.converter.Converter;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtClaimNames;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Classe permettant de convertir un token JWT en un token d'authentification Spring Security.
 * Cette classe extrait les rôles et les autorités à partir du token JWT, notamment les rôles définis dans la section
 * 'resource_access' du token Keycloak.
 */
@Component // Indique que cette classe est un composant Spring
public class JwtAuthConverter implements Converter<Jwt, AbstractAuthenticationToken> {

    // Convertisseur par défaut pour les autorités à partir du JWT
    private final JwtGrantedAuthoritiesConverter jwtGrantedAuthoritiesConverter = new JwtGrantedAuthoritiesConverter();

    // Attribut customisable pour identifier l'utilisateur (souvent "sub", mais peut être modifié via une propriété)
    @Value("${jwt.auth.converter.principle-attribute}")
    private String principleAttribute;

    // Identifiant de la ressource (application) utilisé dans Keycloak pour extraire les rôles associés
    @Value("${jwt.auth.converter.resource-id}")
    private String resourceId;

    /**
     * Méthode de conversion du token JWT en un token d'authentification spécifique à Spring Security.
     *
     * @param jwt Le token JWT contenant les informations de l'utilisateur et ses rôles.
     * @return Un token d'authentification contenant les autorités et informations d'identification de l'utilisateur.
     */
    @Override
    public AbstractAuthenticationToken convert(@NonNull Jwt jwt) {
        // Combine les autorités converties à partir du JWT avec les rôles extraits des ressources
        Collection<GrantedAuthority> authorities = Stream.concat(
                jwtGrantedAuthoritiesConverter.convert(jwt).stream(),
                extractResourceRoles(jwt).stream()
        ).collect(Collectors.toSet());

        // Crée un nouveau token d'authentification avec les autorités et l'attribut principal (ex: username)
        return new JwtAuthenticationToken(
                jwt,
                authorities,
                getPrincipleClaimName(jwt)
        );
    }

    /**
     * Méthode pour obtenir le nom de l'attribut principal (par défaut "sub", ou personnalisé via configuration).
     *
     * @param jwt Le token JWT contenant les informations utilisateur.
     * @return Le nom de l'attribut principal du JWT (ex: "sub" ou un autre attribut configuré).
     */
    private String getPrincipleClaimName(Jwt jwt) {
        String claimName = JwtClaimNames.SUB; // Par défaut, l'attribut principal est "sub"
        if (principleAttribute != null) {
            claimName = principleAttribute; // Peut être personnalisé avec une autre valeur via configuration
        }
        return jwt.getClaim(claimName); // Récupère la valeur de l'attribut principal du JWT
    }

    /**
     * Extraction des rôles spécifiques à une ressource définie dans Keycloak (situés dans la section "resource_access").
     *
     * @param jwt Le token JWT contenant les rôles utilisateur.
     * @return Une collection des autorités sous forme de rôles extraits de la ressource.
     */
    private Collection<? extends GrantedAuthority> extractResourceRoles(Jwt jwt) {
        // Vérifie si la section "resource_access" existe dans le JWT
        if (jwt.getClaim("resource_access") == null) {
            return Set.of(); // Si non présente, renvoie un ensemble vide
        }

        // Récupère la section "resource_access" du JWT
        Map<String, Object> resourceAccess = jwt.getClaim("resource_access");

        // Vérifie si l'accès à la ressource définie (resourceId) existe
        if (resourceAccess.get(resourceId) == null) {
            return Set.of(); // Si non présent, renvoie un ensemble vide
        }

        // Récupère les rôles associés à la ressource spécifiée
        Map<String, Object> resource = (Map<String, Object>) resourceAccess.get(resourceId);
        Collection<String> resourceRoles = (Collection<String>) resource.get("roles");

        // Convertit chaque rôle en autorité Spring Security avec le préfixe "ROLE_"
        return resourceRoles
                .stream()
                .map(role -> new SimpleGrantedAuthority("ROLE_" + role))
                .collect(Collectors.toSet());
    }
}

