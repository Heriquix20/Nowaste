package A3.project.noWaste.config;

import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class SecurityFilterTest {

    private SecurityFilter securityFilter;

    @Mock
    private TokenConfig tokenConfig;

    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpServletResponse response;

    @Mock
    private FilterChain filterChain;

    private AutoCloseable mocks;

    @BeforeEach
    void setUp() {
        mocks = MockitoAnnotations.openMocks(this);

        securityFilter = new SecurityFilter(tokenConfig);

        SecurityContextHolder.clearContext();
    }

    @AfterEach
    void tearDown() throws Exception {
        SecurityContextHolder.clearContext();
        mocks.close();
    }

    @Test
    void shouldContinueFilterChainWhenAuthorizationHeaderIsMissing() throws Exception {
        when(request.getHeader("Authorization")).thenReturn(null);

        securityFilter.doFilterInternal(request, response, filterChain);

        verify(filterChain).doFilter(request, response);
        assertNull(SecurityContextHolder.getContext().getAuthentication());
    }

    @Test
    void shouldContinueFilterChainWhenAuthorizationHeaderIsInvalid() throws Exception {
        when(request.getHeader("Authorization")).thenReturn("Invalid Header");

        securityFilter.doFilterInternal(request, response, filterChain);

        verify(filterChain).doFilter(request, response);
        assertNull(SecurityContextHolder.getContext().getAuthentication());
    }

    @Test
    void shouldAuthenticateUserWhenTokenIsValid() throws Exception {
        JWTUserData userData = JWTUserData.builder()
                .userId(1)
                .email("user@test.com")
                .build();

        when(request.getHeader("Authorization"))
                .thenReturn("Bearer valid-token");

        when(tokenConfig.validateToken("valid-token"))
                .thenReturn(Optional.of(userData));

        securityFilter.doFilterInternal(request, response, filterChain);

        assertNotNull(SecurityContextHolder.getContext().getAuthentication());

        assertEquals(
                userData,
                SecurityContextHolder.getContext()
                        .getAuthentication()
                        .getPrincipal()
        );

        verify(filterChain).doFilter(request, response);
    }

    @Test
    void shouldNotAuthenticateUserWhenTokenIsInvalid() throws Exception {
        when(request.getHeader("Authorization"))
                .thenReturn("Bearer invalid-token");

        when(tokenConfig.validateToken("invalid-token"))
                .thenReturn(Optional.empty());

        securityFilter.doFilterInternal(request, response, filterChain);

        assertNull(SecurityContextHolder.getContext().getAuthentication());

        verify(filterChain).doFilter(request, response);
    }
}