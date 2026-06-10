package A3.project.noWaste.config;

import A3.project.noWaste.domain.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class TokenConfigTest {

    private TokenConfig tokenConfig;
    private User user;

    @BeforeEach
    void setUp() {
        tokenConfig = new TokenConfig();

        ReflectionTestUtils.setField(
                tokenConfig,
                "secret",
                "my-secret-key-for-tests"
        );

        user = new User();
        user.setId(1);
        user.setEmail("user@test.com");
    }

    @Test
    void shouldGenerateToken() {
        String token = tokenConfig.generateToken(user);

        assertNotNull(token);
        assertFalse(token.isBlank());
    }

    @Test
    void shouldReturnSubjectFromValidToken() {
        String token = tokenConfig.generateToken(user);

        String subject = tokenConfig.getSubject(token);

        assertEquals("user@test.com", subject);
    }

    @Test
    void shouldThrowExceptionWhenTokenIsInvalid() {
        RuntimeException exception = assertThrows(
                RuntimeException.class,
                () -> tokenConfig.getSubject("invalid-token")
        );

        assertEquals(
                "Token JWT inválido ou expirado",
                exception.getMessage()
        );
    }

    @Test
    void shouldValidateTokenSuccessfully() {
        String token = tokenConfig.generateToken(user);

        Optional<JWTUserData> result =
                tokenConfig.validateToken(token);

        assertTrue(result.isPresent());

        assertAll(
                () -> assertEquals(1, result.get().getUserId()),
                () -> assertEquals(
                        "user@test.com",
                        result.get().getEmail()
                )
        );
    }

    @Test
    void shouldReturnEmptyOptionalWhenTokenIsInvalid() {
        Optional<JWTUserData> result =
                tokenConfig.validateToken("invalid-token");

        assertTrue(result.isEmpty());
    }
}