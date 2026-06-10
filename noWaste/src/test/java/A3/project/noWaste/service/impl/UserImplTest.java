package A3.project.noWaste.service.impl;

import A3.project.noWaste.domain.User;
import A3.project.noWaste.dto.UserDTO;
import A3.project.noWaste.exceptions.DataIntegratyViolationException;
import A3.project.noWaste.exceptions.ObjectNotFoundException;
import A3.project.noWaste.infra.UserRepository;
import A3.project.noWaste.service.VerificationService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.modelmapper.ModelMapper;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserImplTest {

    @Mock
    private ModelMapper mapper;

    @Mock
    private UserRepository repository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private VerificationService verificationService;

    @InjectMocks
    private UserImpl service;

    @Test
    void shouldReturnAllUsers() {
        List<User> users = List.of(new User(), new User());
        when(repository.findAll()).thenReturn(users);

        assertSame(users, service.findAll());
    }

    @Test
    void shouldCreateUserWithEncodedPassword() {
        UserDTO dto = createUserDTO(1, "Ana", "ana@email.com", "123456");

        when(repository.findByEmail("ana@email.com")).thenReturn(Optional.empty());
        when(passwordEncoder.encode("123456")).thenReturn("encoded-password");
        when(repository.save(any(User.class))).thenAnswer(invocation -> invocation.getArgument(0));

        User created = service.create(dto);

        assertEquals("Ana", created.getName());
        assertEquals("ana@email.com", created.getEmail());
        assertEquals("encoded-password", created.getPassword());
    }

    @Test
    void shouldThrowWhenCreatingUserWithDuplicatedEmail() {
        UserDTO dto = createUserDTO(null, "Ana", "ana@email.com", "123456");

        User existing = new User();
        existing.setId(99);
        existing.setEmail("ana@email.com");

        when(repository.findByEmail("ana@email.com")).thenReturn(Optional.of(existing));

        assertThrows(DataIntegratyViolationException.class, () -> service.create(dto));
    }

    @Test
    void shouldThrowAccessDeniedWhenUpdatingAnotherUser() {
        UserDTO dto = createUserDTO(2, "Bruno", "bruno@email.com", "senha");

        when(verificationService.getUserId()).thenReturn(1);

        assertThrows(DataIntegratyViolationException.class, () -> service.update(dto));
    }

    @Test
    void shouldUpdateUserWhenAuthorized() {
        UserDTO dto = createUserDTO(1, "Bruna", "bruna@email.com", "nova-senha");
        User existing = new User();
        existing.setId(1);
        existing.setEmail("bruna@email.com");

        when(verificationService.getUserId()).thenReturn(1);
        when(repository.findByEmail("bruna@email.com")).thenReturn(Optional.of(existing));
        when(repository.findById(1)).thenReturn(Optional.of(existing));
        when(passwordEncoder.encode("nova-senha")).thenReturn("encoded-nova-senha");
        when(repository.save(existing)).thenReturn(existing);

        User updated = service.update(dto);

        assertEquals("Bruna", updated.getName());
        assertEquals("bruna@email.com", updated.getEmail());
        assertEquals("encoded-nova-senha", updated.getPassword());
    }

    @Test
    void shouldThrowWhenUpdatingUserThatDoesNotExist() {
        UserDTO dto = createUserDTO(1, "Bruna", "bruna@email.com", "nova-senha");

        when(verificationService.getUserId()).thenReturn(1);
        when(repository.findByEmail("bruna@email.com")).thenReturn(Optional.empty());
        when(repository.findById(1)).thenReturn(Optional.empty());

        assertThrows(ObjectNotFoundException.class, () -> service.update(dto));
    }

    @Test
    void shouldDeleteAuthenticatedUser() {
        User existing = new User();
        existing.setId(1);

        when(verificationService.getUserId()).thenReturn(1);
        when(repository.findById(1)).thenReturn(Optional.of(existing));

        service.delete(1);

        verify(repository).delete(existing);
    }

    @Test
    void shouldThrowAccessDeniedWhenDeletingAnotherUser() {
        when(verificationService.getUserId()).thenReturn(1);

        assertThrows(DataIntegratyViolationException.class, () -> service.delete(2));
    }

    private UserDTO createUserDTO(Integer id, String name, String email, String password) {
        UserDTO dto = new UserDTO();
        dto.setId(id);
        dto.setName(name);
        dto.setEmail(email);
        dto.setPassword(password);
        return dto;
    }
}
