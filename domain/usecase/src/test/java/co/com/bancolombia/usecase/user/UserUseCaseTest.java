package co.com.bancolombia.usecase.user;

import co.com.bancolombia.model.user.User;
import co.com.bancolombia.model.user.gateways.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserUseCaseTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserUseCase userUseCase;

    @Test
    void saveUser_ShouldReturnSavedUser() {
        // Arrange: Create a sample user
        User user = new User();

        // Stubbing: Mock the repository's behavior
        when(userRepository.save(any(User.class)))
                .thenReturn(Mono.just(user));

        // Act: Call the method you want to test
        Mono<User> result = userUseCase.saveUser(user);

        // Assert: Verify the reactive stream
        StepVerifier.create(result)
                .expectNext(user)
                .verifyComplete();
    }
}