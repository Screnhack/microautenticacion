package co.com.bancolombia.api.config.autenticacion;

import co.com.bancolombia.model.user.User;
import co.com.bancolombia.model.user.gateways.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.ReactiveUserDetailsService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.Collection;
import java.util.Collections;

@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements ReactiveUserDetailsService {

    private final UserRepository userRepository;

    @Override
    public Mono<UserDetails> findByUsername(String username) {
        return userRepository.findByCorreoElectronico(username)
                .switchIfEmpty(Mono.error(new BadCredentialsException("Usuario no encontrado")))
                .map(this::convertUserToUserDetails);
    }

    private UserDetails convertUserToUserDetails(User user) {
        Collection<GrantedAuthority> authorities = Collections.singletonList(
                new SimpleGrantedAuthority("ROLE_" + user.getNombreRol())
        );

        return new org.springframework.security.core.userdetails.User(
                user.getCorreoElectronico(),
                user.getPassword(), // Esta es la contraseña hasheada de la base de datos
                authorities
        );
    }
}