package A3.project.noWaste.exceptions;

import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ExceptionHandlerServiceTest {

    private ExceptionHandlerService exceptionHandler;

    private HttpServletRequest request;

    @BeforeEach
    void setUp() {
        exceptionHandler = new ExceptionHandlerService();
        request = mock(HttpServletRequest.class);

        when(request.getRequestURI()).thenReturn("/api/test");
    }

    @Test
    void shouldHandleAuthenticationException() {
        ResponseEntity<StandardError> response =
                exceptionHandler.authenticationException(
                        new BadCredentialsException("Invalid credentials"),
                        request
                );

        assertAll(
                () -> assertEquals(401, response.getStatusCode().value()),
                () -> assertEquals("Email ou senha invalidos", response.getBody().getError()),
                () -> assertEquals("/api/test", response.getBody().getPath())
        );
    }

    @Test
    void shouldHandleUserNotFoundException() {
        ResponseEntity<StandardError> response =
                exceptionHandler.userNotFoundException(
                        new UserNotFoundException("Usuário não encontrado"),
                        request
                );

        assertAll(
                () -> assertEquals(404, response.getStatusCode().value()),
                () -> assertEquals("Usuário não encontrado", response.getBody().getError())
        );
    }

    @Test
    void shouldHandleObjectNotFoundException() {
        ResponseEntity<StandardError> response =
                exceptionHandler.objectNotFoundException(
                        new ObjectNotFoundException("Objeto não encontrado"),
                        request
                );

        assertAll(
                () -> assertEquals(404, response.getStatusCode().value()),
                () -> assertEquals("Objeto não encontrado", response.getBody().getError())
        );
    }

    @Test
    void shouldHandleDataIntegratyViolationException() {
        ResponseEntity<StandardError> response =
                exceptionHandler.dataIntegratyViolationException(
                        new DataIntegratyViolationException("Erro de integridade"),
                        request
                );

        assertAll(
                () -> assertEquals(400, response.getStatusCode().value()),
                () -> assertEquals("Erro de integridade", response.getBody().getError())
        );
    }

    @Test
    void shouldHandleEntityNotFoundException() {
        ResponseEntity<StandardError> response =
                exceptionHandler.entityNotFoundException(
                        new jakarta.persistence.EntityNotFoundException("Entidade não encontrada"),
                        request
                );

        assertAll(
                () -> assertEquals(404, response.getStatusCode().value()),
                () -> assertEquals("Entidade não encontrada", response.getBody().getError())
        );
    }
}