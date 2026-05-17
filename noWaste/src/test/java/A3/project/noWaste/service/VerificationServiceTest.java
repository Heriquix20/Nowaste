package A3.project.noWaste.service;

import A3.project.noWaste.config.JWTUserData;
import A3.project.noWaste.config.TokenConfig;
import A3.project.noWaste.domain.User;
import A3.project.noWaste.exceptions.UserNotFoundException;
import A3.project.noWaste.infra.UserRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class VerificationServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private TokenConfig tokenConfig;

    @InjectMocks
    private VerificationService service;

    @AfterEach
    void tearDown() {
        SecurityContextHolder.clearContext();
    }

    @Test
    void shouldReturnAuthenticatedUserId() {
        authenticate(1, "ana@email.com");

        assertEquals(1, service.getUserId());
    }

    @Test
    void shouldThrowWhenThereIsNoAuthenticatedUser() {
        SecurityContextHolder.clearContext();

        assertThrows(UserNotFoundException.class, service::getUserId);
    }

    @Test
    void shouldReturnVerifiedUserFromRepository() {
        User user = new User();
        user.setId(1);
        authenticate(1, "ana@email.com");
        when(userRepository.findById(1)).thenReturn(Optional.of(user));

        User result = service.verifyUser();

        assertSame(user, result);
    }

    @Test
    void shouldThrowWhenAuthenticatedUserDoesNotExistInRepository() {
        authenticate(1, "ana@email.com");
        when(userRepository.findById(1)).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, service::verifyUser);
    }

    private void authenticate(Integer userId, String email) {
        JWTUserData userData = JWTUserData.builder()
                .userId(userId)
                .email(email)
                .build();
        UsernamePasswordAuthenticationToken authentication =
                new UsernamePasswordAuthenticationToken(userData, null, null);
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }
}
