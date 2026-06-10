package A3.project.noWaste.ui;

import A3.project.noWaste.domain.Inventory;
import A3.project.noWaste.dto.InventoryDTO;
import A3.project.noWaste.exceptions.ExceptionHandlerService;
import A3.project.noWaste.service.InventoryService;
import A3.project.noWaste.config.TokenConfig;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.http.MediaType;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest({InventoryController.class, ExceptionHandlerService.class})
class InventoryControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private InventoryService service;

    @MockitoBean
    private ModelMapper mapper;

    private ObjectMapper objectMapper;

    @MockitoBean
    private TokenConfig tokenConfig;

    private Inventory inventory;
    private InventoryDTO inventoryDTO;

    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper();

        inventory = new Inventory();
        inventory.setId(1);
        inventory.setName("Estoque Principal");
        inventory.setDescription("Estoque do galpão central");
        inventory.setLocation("Galpão A");

        inventoryDTO = new InventoryDTO();
        inventoryDTO.setId(1);
        inventoryDTO.setName("Estoque Principal");
        inventoryDTO.setDescription("Estoque do galpão central");
        inventoryDTO.setLocation("Galpão A");
    }

    @AfterEach
    void tearDown() {
        SecurityContextHolder.clearContext();
    }

    // ===================== GET /inventories/{id} =====================

    @Test
    @WithMockUser
    void shouldReturnInventoryById() throws Exception {
        when(service.findById(1)).thenReturn(inventory);
        when(mapper.map(inventory, InventoryDTO.class)).thenReturn(inventoryDTO);

        mockMvc.perform(get("/inventories/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Estoque Principal"))
                .andExpect(jsonPath("$.location").value("Galpão A"));
    }

    @Test
    @WithMockUser
    void shouldReturn404WhenInventoryNotFoundById() throws Exception {
        when(service.findById(99)).thenThrow(new jakarta.persistence.EntityNotFoundException("Inventário não encontrado"));

        mockMvc.perform(get("/inventories/99"))
                .andExpect(status().isNotFound());
    }

    // ===================== GET /inventories =====================

    @Test
    @WithMockUser
    void shouldReturnAllInventories() throws Exception {
        when(service.findAll(null, "desc")).thenReturn(List.of(inventory));
        when(mapper.map(inventory, InventoryDTO.class)).thenReturn(inventoryDTO);

        mockMvc.perform(get("/inventories"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].name").value("Estoque Principal"));
    }

    @Test
    @WithMockUser
    void shouldReturnFilteredInventoriesByName() throws Exception {
        when(service.findAll("Estoque Principal", "desc")).thenReturn(List.of(inventory));
        when(mapper.map(inventory, InventoryDTO.class)).thenReturn(inventoryDTO);

        mockMvc.perform(get("/inventories").param("name", "Estoque Principal"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("Estoque Principal"));
    }

    @Test
    @WithMockUser
    void shouldReturnEmptyListWhenNoInventoriesExist() throws Exception {
        when(service.findAll(null, "desc")).thenReturn(List.of());

        mockMvc.perform(get("/inventories"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isEmpty());
    }

    @Test
    @WithMockUser
    void shouldReturnInventoriesSortedAsc() throws Exception {
        when(service.findAll(null, "asc")).thenReturn(List.of(inventory));
        when(mapper.map(inventory, InventoryDTO.class)).thenReturn(inventoryDTO);

        mockMvc.perform(get("/inventories").param("sort", "asc"))
                .andExpect(status().isOk());
    }

    // ===================== POST /inventories =====================

    @Test
    @WithMockUser
    void shouldCreateInventoryAndReturn201() throws Exception {
        when(service.create(any(InventoryDTO.class))).thenReturn(inventory);
        when(mapper.map(inventory, InventoryDTO.class)).thenReturn(inventoryDTO);

        mockMvc.perform(post("/inventories")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(inventoryDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Estoque Principal"));
    }

    @Test
    @WithMockUser
    void shouldReturn400WhenNameIsBlankOnCreate() throws Exception {
        inventoryDTO.setName("");

        mockMvc.perform(post("/inventories")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(inventoryDTO)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser
    void shouldReturn400WhenLocationIsBlankOnCreate() throws Exception {
        inventoryDTO.setLocation("");

        mockMvc.perform(post("/inventories")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(inventoryDTO)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser
    void shouldReturn400WhenBodyIsMissingOnCreate() throws Exception {
        mockMvc.perform(post("/inventories")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    // ===================== PUT /inventories/{id} =====================

    @Test
    @WithMockUser
    void shouldUpdateInventoryAndReturn200() throws Exception {
        when(service.update(eq(1), any(InventoryDTO.class))).thenReturn(inventory);
        when(mapper.map(inventory, InventoryDTO.class)).thenReturn(inventoryDTO);

        mockMvc.perform(put("/inventories/1")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(inventoryDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Estoque Principal"));
    }

    @Test
    @WithMockUser
    void shouldReturn404WhenInventoryNotFoundOnUpdate() throws Exception {
        when(service.update(eq(99), any(InventoryDTO.class)))
                .thenThrow(new jakarta.persistence.EntityNotFoundException("Inventário não encontrado"));

        mockMvc.perform(put("/inventories/99")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(inventoryDTO)))
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser
    void shouldReturn400WhenBodyIsInvalidOnUpdate() throws Exception {
        inventoryDTO.setName("");

        mockMvc.perform(put("/inventories/1")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(inventoryDTO)))
                .andExpect(status().isBadRequest());
    }

    // ===================== DELETE /inventories/{id} =====================

    @Test
    @WithMockUser
    void shouldDeleteInventoryAndReturn204() throws Exception {
        doNothing().when(service).delete(1);

        mockMvc.perform(delete("/inventories/1").with(csrf()))
                .andExpect(status().isNoContent());
    }

    @Test
    @WithMockUser
    void shouldReturn404WhenInventoryNotFoundOnDelete() throws Exception {
        doThrow(new jakarta.persistence.EntityNotFoundException("Inventário não encontrado"))
                .when(service).delete(99);

        mockMvc.perform(delete("/inventories/99").with(csrf()))
                .andExpect(status().isNotFound());
    }
}