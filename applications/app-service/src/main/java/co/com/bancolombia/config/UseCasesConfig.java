package co.com.bancolombia.config;

import co.com.bancolombia.model.autenticacion.gateways.AutenticacionRepository;
import co.com.bancolombia.model.rol.gateways.RolRepository;
import co.com.bancolombia.model.user.gateways.UserRepository;
import co.com.bancolombia.usecase.autenticacion.AutenticacionUseCase;
import co.com.bancolombia.usecase.user.UserUseCase;
import co.com.bancolombia.usecase.user.UserUseCaseValidateEmail;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.FilterType;

@Configuration
@ComponentScan(basePackages = "co.com.bancolombia.usecase",
        includeFilters = {
                @ComponentScan.Filter(type = FilterType.REGEX, pattern = "^.+UseCase$")
        },
        useDefaultFilters = false)
public class UseCasesConfig {

    @Bean
    public UserUseCase userUseCase(UserRepository userRepository, RolRepository rolRepository) {
        return new UserUseCase(userRepository, rolRepository);
    }

    @Bean
    UserUseCaseValidateEmail userUseCaseValidateEmail(UserRepository userRepository) {
        return new UserUseCaseValidateEmail(userRepository);
    }

    @Bean
    AutenticacionUseCase autenticacionUseCase(AutenticacionRepository autenticacionRepository) {
        return new AutenticacionUseCase(autenticacionRepository);
    }


}
