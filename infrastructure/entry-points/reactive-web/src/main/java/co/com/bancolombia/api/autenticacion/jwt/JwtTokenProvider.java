package co.com.bancolombia.api.autenticacion.jwt;

import co.com.bancolombia.model.autenticacion.gateways.AutenticacionRepository;
import co.com.bancolombia.model.user.User;
import co.com.bancolombia.model.user.gateways.UserRepository; // <--- Nueva importación
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

    private final UserRepository userRepository;

    @Value("${jwt.secret}")
    private String jwtSecret;

    @Value("${jwt.expiration}")
    private int jwtExpirationInMs;

    public JwtTokenProvider(UserRepository userRepository) { // <--- Constructor para la inyección
        this.userRepository = userRepository;
    }

    private SecretKey getSigningKey() {
        byte[] keyBytes = this.jwtSecret.getBytes();
        return Keys.hmacShaKeyFor(keyBytes);
    }

    @Override
    public Mono<String> generateToken(User user) {
        return userRepository.findByCorreoElectronico(user.getCorreoElectronico())
                .flatMap(foundUser -> {
                    String userRole = foundUser.getNombreRol();
                    if (userRole == null || userRole.isBlank()) {
                        return Mono.error(new IllegalArgumentException("El rol del usuario está nulo o vacío. No se puede generar el token."));
                    }

                    Date now = new Date();
                    Date expiryDate = new Date(now.getTime() + jwtExpirationInMs);

                    String token = Jwts.builder()
                            .setSubject(foundUser.getCorreoElectronico())
                            .claim("role", userRole)
                            .setIssuedAt(now)
                            .setExpiration(expiryDate)
                            .signWith(getSigningKey(), SignatureAlgorithm.HS512)
                            .compact();

                    return Mono.just(token);
                })
                .switchIfEmpty(Mono.error(new IllegalArgumentException("Usuario no encontrado para generar el token.")));
    }

    public boolean validateToken(String authToken) {
        try {
            Jwts.parserBuilder()
                    .setSigningKey(getSigningKey())
                    .build()
                    .parseClaimsJws(authToken);
            return true;
        } catch (Exception ex) {
            // Manejar excepciones de validación, puedes usar logs para esto
        }
        return false;
    }

    public String getUsernameFromJWT(String token) {
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody();

        return claims.getSubject();
    }
}
