package A3.project.noWaste.config;

import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.authentication.BadCredentialsException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class JwtAuthenticationEntryPointTest {

    private JwtAuthenticationEntryPoint authenticationEntryPoint;

    private HttpServletRequest request;

    private MockHttpServletResponse response;

    @BeforeEach
    void setUp() {
        authenticationEntryPoint = new JwtAuthenticationEntryPoint();

        request = mock(HttpServletRequest.class);
        response = new MockHttpServletResponse();
    }

    @Test
    void shouldReturnUnauthorizedResponse() throws Exception {
        when(request.getRequestURI()).thenReturn("/api/products");

        authenticationEntryPoint.commence(
                request,
                response,
                new BadCredentialsException("Invalid token")
        );

        assertAll(
                () -> assertEquals(401, response.getStatus()),
                () -> assertEquals("application/json;charset=UTF-8", response.getContentType()),
                () -> assertEquals("UTF-8", response.getCharacterEncoding())
        );

        String json = response.getContentAsString();

        assertTrue(json.contains("Token ausente, inválido ou expirado"));
        assertTrue(json.contains("/api/products"));
    }

    @Test
    void shouldReturnStandardErrorBody() throws Exception {
        when(request.getRequestURI()).thenReturn("/api/products");

        authenticationEntryPoint.commence(
                request,
                response,
                new BadCredentialsException("Invalid token")
        );

        String json = response.getContentAsString();

        assertAll(
                () -> assertTrue(json.contains("\"status\":401")),
                () -> assertTrue(json.contains("Token ausente, inválido ou expirado")),
                () -> assertTrue(json.contains("/api/products"))
        );
    }
}