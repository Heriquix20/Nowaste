package A3.project.noWaste.ui;

import A3.project.noWaste.config.TokenConfig;
import A3.project.noWaste.domain.User;
import A3.project.noWaste.dto.UserDTO;
import A3.project.noWaste.exceptions.ExceptionHandlerService;
import A3.project.noWaste.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest({UserController.class, ExceptionHandlerService.class})
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private UserService service;

    @MockitoBean
    private ModelMapper mapper;

    @MockitoBean
    private TokenConfig tokenConfig;

    private ObjectMapper objectMapper;

    private User user;
    private UserDTO userDTO;

    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper();

        user = new User();
        user.setId(1);
        user.setName("João Silva");
        user.setEmail("joao@email.com");
        user.setPassword("senha123");

        userDTO = new UserDTO();
        userDTO.setId(1);
        userDTO.setName("João Silva");
        userDTO.setEmail("joao@email.com");
        userDTO.setPassword("senha123");
    }

    @AfterEach
    void tearDown() {
        SecurityContextHolder.clearContext();
    }

    // ===================== GET /users =====================

    @Test
    @WithMockUser
    void shouldReturnAllUsers() throws Exception {
        when(service.findAll()).thenReturn(List.of(user));
        when(mapper.map(user, UserDTO.class)).thenReturn(userDTO);

        mockMvc.perform(get("/users"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].name").value("João Silva"))
                .andExpect(jsonPath("$[0].email").value("joao@email.com"));
    }

    @Test
    @WithMockUser
    void shouldReturnEmptyListWhenNoUsersExist() throws Exception {
        when(service.findAll()).thenReturn(List.of());

        mockMvc.perform(get("/users"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isEmpty());
    }

    // ===================== POST /users =====================

    @Test
    void shouldCreateUserAndReturn201() throws Exception {
        when(service.create(any(UserDTO.class))).thenReturn(user);

        mockMvc.perform(post("/users")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"name":"João Silva","email":"joao@email.com","password":"senha123"}
                                """))
                .andExpect(status().isCreated());
    }

    @Test
    void shouldReturn400WhenNameIsBlankOnCreate() throws Exception {
        userDTO.setName("");

        mockMvc.perform(post("/users")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userDTO)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void shouldReturn400WhenEmailIsInvalidOnCreate() throws Exception {
        userDTO.setEmail("email-invalido");

        mockMvc.perform(post("/users")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userDTO)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void shouldReturn400WhenEmailIsBlankOnCreate() throws Exception {
        userDTO.setEmail("");

        mockMvc.perform(post("/users")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userDTO)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void shouldReturn400WhenPasswordIsBlankOnCreate() throws Exception {
        userDTO.setPassword("");

        mockMvc.perform(post("/users")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userDTO)))
                .andExpect(status().isBadRequest());
    }

    // ===================== PUT /users/{id} =====================

    @Test
    @WithMockUser
    void shouldUpdateUserAndReturn200() throws Exception {
        when(service.update(any(UserDTO.class))).thenReturn(user);
        when(mapper.map(user, UserDTO.class)).thenReturn(userDTO);

        mockMvc.perform(put("/users/1")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                            {"name":"João Silva","email":"joao@email.com","password":"senha123"}
                            """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("João Silva"));
    }

    @Test
    @WithMockUser
    void shouldReturn404WhenUserNotFoundOnUpdate() throws Exception {
        when(service.update(any(UserDTO.class)))
                .thenThrow(new EntityNotFoundException("Usuário não encontrado"));

        mockMvc.perform(put("/users/99")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                            {"name":"João Silva","email":"joao@email.com","password":"senha123"}
                            """))
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser
    void shouldReturn400WhenBodyIsInvalidOnUpdate() throws Exception {
        userDTO.setName("");

        mockMvc.perform(put("/users/1")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userDTO)))
                .andExpect(status().isBadRequest());
    }

    // ===================== DELETE /users/{id} =====================

    @Test
    @WithMockUser
    void shouldDeleteUserAndReturn204() throws Exception {
        doNothing().when(service).delete(1);

        mockMvc.perform(delete("/users/1").with(csrf()))
                .andExpect(status().isNoContent());
    }

    @Test
    @WithMockUser
    void shouldReturn404WhenUserNotFoundOnDelete() throws Exception {
        doThrow(new EntityNotFoundException("Usuário não encontrado"))
                .when(service).delete(99);

        mockMvc.perform(delete("/users/99").with(csrf()))
                .andExpect(status().isNotFound());
    }
}