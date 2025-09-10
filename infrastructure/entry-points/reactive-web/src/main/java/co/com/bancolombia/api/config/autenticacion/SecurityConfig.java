package co.com.bancolombia.api.config.autenticacion;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.security.web.server.context.ServerSecurityContextRepository;
import co.com.bancolombia.api.config.autenticacion.JwtAuthManager;

@Configuration
@EnableWebFluxSecurity
public class SecurityConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityWebFilterChain securityWebFilterChain(
            ServerHttpSecurity http,
            JwtAuthManager jwtAuthManager, // 👈 Inyección por método
            ServerSecurityContextRepository jwtSecurityContextRepository // 👈 Inyección por método
    ) {
        return http
                .csrf(ServerHttpSecurity.CsrfSpec::disable)
                .authenticationManager(jwtAuthManager)
                .securityContextRepository(jwtSecurityContextRepository)
                .authorizeExchange(exchanges -> exchanges
                        .pathMatchers("/api/v1/login").permitAll()
                        .pathMatchers("/swagger-ui.html", "/swagger-ui/**", "/v3/api-docs/**", "/webjars/**").permitAll()
                        .pathMatchers("/api/v1/usuarios").hasRole("ADMINISTRADOR")
                        .pathMatchers( "/api/v1/usuario/correo").hasRole("CLIENTE")
                        .anyExchange().authenticated()
                )
                .build();
    }
}