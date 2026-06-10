package A3.project.noWaste.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class UserTest {

    private User user;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setId(1);
        user.setName("Henrique");
        user.setEmail("henrique@email.com");
        user.setPassword("123456");
        user.setInventories(List.of());
    }

    @Test
    void shouldReturnEmailAsUsername() {
        assertEquals("henrique@email.com", user.getUsername());
    }

    @Test
    void shouldReturnEmptyAuthoritiesList() {
        assertTrue(user.getAuthorities().isEmpty());
    }

    @Test
    void shouldReturnTrueWhenAccountIsNonExpired() {
        assertTrue(user.isAccountNonExpired());
    }

    @Test
    void shouldReturnTrueWhenAccountIsNonLocked() {
        assertTrue(user.isAccountNonLocked());
    }

    @Test
    void shouldReturnTrueWhenCredentialsAreNonExpired() {
        assertTrue(user.isCredentialsNonExpired());
    }

    @Test
    void shouldReturnTrueWhenUserIsEnabled() {
        assertTrue(user.isEnabled());
    }

    @Test
    void shouldSetAndGetUserProperties() {
        assertAll(
                () -> assertEquals(1, user.getId()),
                () -> assertEquals("Henrique", user.getName()),
                () -> assertEquals("henrique@email.com", user.getEmail()),
                () -> assertEquals("123456", user.getPassword()),
                () -> assertTrue(user.getInventories().isEmpty())
        );
    }

    @Test
    void shouldCreateUserUsingAllArgsConstructor() {
        User newUser = new User(
                2,
                "Maria",
                "maria@email.com",
                "senha123",
                List.of()
        );

        assertAll(
                () -> assertEquals(2, newUser.getId()),
                () -> assertEquals("Maria", newUser.getName()),
                () -> assertEquals("maria@email.com", newUser.getEmail()),
                () -> assertEquals("senha123", newUser.getPassword()),
                () -> assertTrue(newUser.getInventories().isEmpty())
        );
    }

    @Test
    void shouldConsiderUsersEqualWhenAllFieldsMatch() {
        User otherUser = new User(
                1,
                "Henrique",
                "henrique@email.com",
                "123456",
                List.of()
        );

        assertAll(
                () -> assertEquals(user, otherUser),
                () -> assertEquals(user.hashCode(), otherUser.hashCode())
        );
    }
}