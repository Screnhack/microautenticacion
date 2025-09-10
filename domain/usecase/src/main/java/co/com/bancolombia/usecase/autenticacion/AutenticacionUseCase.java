package co.com.bancolombia.usecase.autenticacion;

import co.com.bancolombia.model.autenticacion.gateways.AutenticacionRepository;
import co.com.bancolombia.model.user.User;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
public class AutenticacionUseCase {
    private final AutenticacionRepository autenticacionRepository;

    public Mono<String> login(String correo, String password) {
        User usuario = new User();
        usuario.setCorreoElectronico(correo);
        usuario.setPassword(password);
        return autenticacionRepository.generateToken(usuario);
    }
}
