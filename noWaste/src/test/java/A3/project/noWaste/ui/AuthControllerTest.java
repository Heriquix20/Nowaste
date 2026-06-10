package A3.project.noWaste.ui;

import A3.project.noWaste.config.TokenConfig;
import A3.project.noWaste.domain.User;
import A3.project.noWaste.exceptions.ExceptionHandlerService;
import A3.project.noWaste.infra.UserRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest({AuthController.class, ExceptionHandlerService.class})
class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private UserRepository repository;

    @MockitoBean
    private AuthenticationManager manager;

    @MockitoBean
    private PasswordEncoder encoder;

    @MockitoBean
    private TokenConfig tokenConfig;

    private User user;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setId(1);
        user.setName("João Silva");
        user.setEmail("joao@email.com");
        user.setPassword("senha123");
    }

    @AfterEach
    void tearDown() {
        SecurityContextHolder.clearContext();
    }

    // ===================== POST /auth/login =====================

    @Test
    void shouldLoginAndReturnToken() throws Exception {
        UsernamePasswordAuthenticationToken authToken =
                new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());

        when(manager.authenticate(any())).thenReturn(authToken);
        when(tokenConfig.generateToken(user)).thenReturn("mocked-jwt-token");

        mockMvc.perform(post("/auth/login")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"email":"joao@email.com","password":"senha123"}
                                """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").value("mocked-jwt-token"));
    }

    @Test
    void shouldReturn400WhenEmailIsBlankOnLogin() throws Exception {
        mockMvc.perform(post("/auth/login")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"email":"","password":"senha123"}
                                """))
                .andExpect(status().isBadRequest());
    }

    @Test
    void shouldReturn400WhenPasswordIsBlankOnLogin() throws Exception {
        mockMvc.perform(post("/auth/login")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"email":"joao@email.com","password":""}
                                """))
                .andExpect(status().isBadRequest());
    }

    @Test
    void shouldReturn400WhenBodyIsMissingOnLogin() throws Exception {
        mockMvc.perform(post("/auth/login")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    void shouldReturn401WhenCredentialsAreInvalid() throws Exception {
        when(manager.authenticate(any()))
                .thenThrow(new BadCredentialsException("Credenciais inválidas"));

        mockMvc.perform(post("/auth/login")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"email":"joao@email.com","password":"senha_errada"}
                                """))
                .andExpect(status().isUnauthorized());
    }
}