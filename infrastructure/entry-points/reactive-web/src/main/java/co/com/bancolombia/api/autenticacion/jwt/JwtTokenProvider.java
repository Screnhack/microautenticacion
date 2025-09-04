package co.com.bancolombia.api.autenticacion.jwt;

import co.com.bancolombia.model.user.User;
import co.com.bancolombia.model.autenticacion.gateways.AutenticacionRepository;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import javax.crypto.SecretKey;
import java.util.Date;

@Component
public class JwtTokenProvider implements AutenticacionRepository {

    @Value("${jwt.secret}")
    private String jwtSecret;

    @Value("${jwt.expiration}")
    private int jwtExpirationInMs;

    private SecretKey getSigningKey() {
        byte[] keyBytes = this.jwtSecret.getBytes();
        return Keys.hmacShaKeyFor(keyBytes);
    }

    @Override
    public Mono<String> generateToken(User user) {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + jwtExpirationInMs);

        // Los métodos .subject() y .signWith(SignatureAlgorithm) son válidos aquí
        String token = Jwts.builder()
                .setSubject(user.getCorreoElectronico()) // Usa setSubject() para versiones antiguas
                .claim("roles", user.getNombreRol())
                .setIssuedAt(now) // Usa setIssuedAt()
                .setExpiration(expiryDate) // Usa setExpiration()
                .signWith(getSigningKey(), SignatureAlgorithm.HS256) // Usa SignatureAlgorithm
                .compact();

        return Mono.just(token);
    }

    public boolean validateToken(String authToken) {
        try {
            Jwts.parserBuilder() // Usa parserBuilder()
                    .setSigningKey(getSigningKey()) // Usa setSigningKey()
                    .build()
                    .parseClaimsJws(authToken); // Usa parseClaimsJws()
            return true;
        } catch (Exception ex) {
            // Manejar excepciones de validación
        }
        return false;
    }

    public String getUsernameFromJWT(String token) {
        Claims claims = Jwts.parserBuilder() // Usa parserBuilder()
                .setSigningKey(getSigningKey()) // Usa setSigningKey()
                .build()
                .parseClaimsJws(token) // Usa parseClaimsJws()
                .getBody();

        return claims.getSubject();
    }
}