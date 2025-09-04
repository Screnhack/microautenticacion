// api/config/autenticacion/JwtAuthManager.java
package co.com.bancolombia.api.config.autenticacion;

import co.com.bancolombia.model.user.gateways.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.Collection;
import java.util.Collections;

@Component
@RequiredArgsConstructor
public class JwtAuthManager implements ReactiveAuthenticationManager {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public Mono<Authentication> authenticate(Authentication authentication) {
        String correo = authentication.getPrincipal().toString();
        String password = authentication.getCredentials().toString();

        return userRepository.findByCorreoElectronico(correo)
                .switchIfEmpty(Mono.error(new BadCredentialsException("Usuario no encontrado")))
                .flatMap(user -> {
                    if (passwordEncoder.matches(password, user.getPassword())) {
                        Collection<GrantedAuthority> authorities = Collections.singletonList(
                                new SimpleGrantedAuthority("ROLE_" + user.getNombreRol())
                        );
                        return Mono.just(new UsernamePasswordAuthenticationToken(user, null, authorities));
                    } else {
                        return Mono.error(new BadCredentialsException("Contraseña incorrecta"));
                    }
                });
    }
}