package pjwstk.s20124.tin.configuration.security.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Component;
import pjwstk.s20124.tin.configuration.properties.ApplicationProperties;

import java.security.Key;
import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Component
public class TokenProvider {
    private static final String AUTHORITIES_KEY = "roles";
    private static final String ID_KEY = "id";
    private final JwtParser jwtParser;
    private final Key key;
    private final Long jwtExpirationMs;

    public TokenProvider(ApplicationProperties applicationProperties) {
        log.debug("Using a Base64-encoded JWT secret key");
        var secret = applicationProperties.getSecurity().getJwtSecret();
        var keyBytes = Decoders.BASE64.decode(secret);
        jwtExpirationMs = applicationProperties.getSecurity().getJwtExpirationMs();
        key = Keys.hmacShaKeyFor(keyBytes);
        jwtParser = Jwts.parserBuilder().setSigningKey(key).build();
    }



    public String generateJwtToken(pjwstk.s20124.tin.model.User user) {
        return generateTokenFromUsername(user.getId(), user.getUsername(), user.getAuthorities());
    }

    public String generateTokenFromUsername(String username) {

        return Jwts.builder()
            .setSubject(username)
            .setIssuedAt(new Date())
            .setExpiration(new Date((new Date()).getTime() + jwtExpirationMs))
            .signWith(key, SignatureAlgorithm.HS512)
            .compact();
    }

    public String generateTokenFromUsername(Long id, String username, Collection<? extends GrantedAuthority> authorities) {
        return Jwts.builder()
            .setSubject(username)
            .claim(AUTHORITIES_KEY, getRolesFromAuthorities(authorities))
            .setId(id.toString())
            .setIssuedAt(new Date())
            .setExpiration(new Date((new Date()).getTime() + jwtExpirationMs))
            .signWith(key, SignatureAlgorithm.HS512)
            .compact();
    }


    private String getRolesFromAuthorities(Collection<? extends GrantedAuthority> authorities) {
        return authorities
            .stream()
            .map(GrantedAuthority::getAuthority)
            .collect(Collectors.joining(","));
    }


    public Authentication getAuthentication(String token) {
        Claims claims = jwtParser.parseClaimsJws(token).getBody();

        Collection<? extends GrantedAuthority> authorities = Arrays
            .stream(claims.get(AUTHORITIES_KEY).toString().split(","))
            .filter(auth -> !auth.trim().isEmpty())
            .map(SimpleGrantedAuthority::new)
            .collect(Collectors.toList());

        User principal = new User(claims.getSubject(), "", authorities);

        return new UsernamePasswordAuthenticationToken(principal, token, authorities);
    }

    public boolean validateToken(String authToken) {
        try {
            var jwt = jwtParser.parseClaimsJws(authToken);
            return jwt.getBody().getExpiration().toInstant().isAfter(Instant.now());
        } catch (JwtException | IllegalArgumentException e) {
            log.info("Invalid JWT token.");
            log.trace("Invalid JWT token trace.", e);
        }
        return false;
    }

    public Optional<Long> getIdFromToken(String authToken) {
        if (Objects.isNull(authToken) || authToken.isEmpty())
            return Optional.empty();
        return Optional.of(Long.valueOf(jwtParser.parseClaimsJws(authToken)
            .getBody()
            .getId()));
    }
}
