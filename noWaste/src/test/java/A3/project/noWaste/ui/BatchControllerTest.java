package A3.project.noWaste.ui;

import A3.project.noWaste.config.TokenConfig;
import A3.project.noWaste.domain.Batch;
import A3.project.noWaste.domain.Product;
import A3.project.noWaste.dto.BatchDTO;
import A3.project.noWaste.exceptions.ExceptionHandlerService;
import A3.project.noWaste.service.BatchService;
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

import java.time.LocalDate;
import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest({BatchController.class, ExceptionHandlerService.class})
class BatchControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private BatchService service;

    @MockitoBean
    private ModelMapper mapper;

    @MockitoBean
    private TokenConfig tokenConfig;

    private Batch batch;

    private BatchDTO batchDTO;

    private static final String VALID_BODY = """
            {"quantity":10,"expirationDate":"2026-12-31"}
            """;

    private static final String BODY_NULL_QUANTITY = """
            {"expirationDate":"2026-12-31"}
            """;

    private static final String BODY_ZERO_QUANTITY = """
            {"quantity":0,"expirationDate":"2026-12-31"}
            """;

    private static final String BODY_PAST_DATE = """
            {"quantity":10,"expirationDate":"2020-01-01"}
            """;

    private static final String BODY_INVALID_UPDATE = """
            {"quantity":0,"expirationDate":"2026-12-31"}
            """;

    @BeforeEach
    void setUp() {

        Product product = new Product();
        product.setId(1);
        product.setName("Arroz");
        product.setWeightInGrams(1000.0);

        batch = new Batch();
        batch.setId(1);
        batch.setCode("LT-ARROZ-001");
        batch.setQuantity(10);
        batch.setExpirationDate(LocalDate.now().plusDays(60));
        batch.setProduct(product);

        batchDTO = new BatchDTO();
        batchDTO.setId(1);
        batchDTO.setCode("LT-ARROZ-001");
        batchDTO.setQuantity(10);
        batchDTO.setExpirationDate(LocalDate.now().plusDays(60));
        batchDTO.setTotalWeight(10000.0);
        batchDTO.setDaysToExpire(60L);
        batchDTO.setStatus("OK");
    }

    @AfterEach
    void tearDown() {
        SecurityContextHolder.clearContext();
    }

    // ===================== GET /inventories/{id}/products/{id}/batches =====================

    @Test
    @WithMockUser
    void shouldReturnAllBatchesByProduct() throws Exception {
        when(service.findAllByProduct(1, 1, null, null, null, null, null, null, "asc"))
                .thenReturn(List.of(batch));
        when(mapper.map(batch, BatchDTO.class)).thenReturn(batchDTO);

        mockMvc.perform(get("/inventories/1/products/1/batches"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].code").value("LT-ARROZ-001"));
    }

    @Test
    @WithMockUser
    void shouldReturnFilteredBatchesByCode() throws Exception {
        when(service.findAllByProduct(1, 1, "LT-ARROZ-001", null, null, null, null, null, "asc"))
                .thenReturn(List.of(batch));
        when(mapper.map(batch, BatchDTO.class)).thenReturn(batchDTO);

        mockMvc.perform(get("/inventories/1/products/1/batches").param("code", "LT-ARROZ-001"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].code").value("LT-ARROZ-001"));
    }

    @Test
    @WithMockUser
    void shouldReturnFilteredBatchesByStatus() throws Exception {
        when(service.findAllByProduct(1, 1, null, "OK", null, null, null, null, "asc"))
                .thenReturn(List.of(batch));
        when(mapper.map(batch, BatchDTO.class)).thenReturn(batchDTO);

        mockMvc.perform(get("/inventories/1/products/1/batches").param("status", "OK"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].status").value("OK"));
    }

    @Test
    @WithMockUser
    void shouldReturnEmptyListWhenNoBatchesExist() throws Exception {
        when(service.findAllByProduct(1, 1, null, null, null, null, null, null, "asc"))
                .thenReturn(List.of());

        mockMvc.perform(get("/inventories/1/products/1/batches"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isEmpty());
    }

    // ===================== GET /inventories/{id}/products/{id}/batches/{id} =====================

    @Test
    @WithMockUser
    void shouldReturnBatchById() throws Exception {
        when(service.findById(1, 1, 1)).thenReturn(batch);
        when(mapper.map(batch, BatchDTO.class)).thenReturn(batchDTO);

        mockMvc.perform(get("/inventories/1/products/1/batches/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.code").value("LT-ARROZ-001"));
    }

    @Test
    @WithMockUser
    void shouldReturn404WhenBatchNotFoundById() throws Exception {
        when(service.findById(1, 1, 99)).thenThrow(new EntityNotFoundException("Lote não encontrado"));

        mockMvc.perform(get("/inventories/1/products/1/batches/99"))
                .andExpect(status().isNotFound());
    }

    // ===================== POST /inventories/{id}/products/{id}/batches =====================

    @Test
    @WithMockUser
    void shouldCreateBatchAndReturn201() throws Exception {
        when(service.create(eq(1), eq(1), any(BatchDTO.class))).thenReturn(batch);
        when(mapper.map(batch, BatchDTO.class)).thenReturn(batchDTO);

        mockMvc.perform(post("/inventories/1/products/1/batches")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(VALID_BODY))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.code").value("LT-ARROZ-001"));
    }

    @Test
    @WithMockUser
    void shouldReturn400WhenQuantityIsNullOnCreate() throws Exception {
        mockMvc.perform(post("/inventories/1/products/1/batches")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(BODY_NULL_QUANTITY))
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser
    void shouldReturn400WhenQuantityIsZeroOnCreate() throws Exception {
        mockMvc.perform(post("/inventories/1/products/1/batches")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(BODY_ZERO_QUANTITY))
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser
    void shouldReturn400WhenExpirationDateIsNullOnCreate() throws Exception {
        mockMvc.perform(post("/inventories/1/products/1/batches")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"quantity":10}
                                """))
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser
    void shouldReturn400WhenExpirationDateIsInPastOnCreate() throws Exception {
        mockMvc.perform(post("/inventories/1/products/1/batches")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(BODY_PAST_DATE))
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser
    void shouldReturn400WhenBodyIsMissingOnCreate() throws Exception {
        mockMvc.perform(post("/inventories/1/products/1/batches")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    // ===================== PUT /inventories/{id}/products/{id}/batches/{id} =====================

    @Test
    @WithMockUser
    void shouldUpdateBatchAndReturn200() throws Exception {
        when(service.update(eq(1), eq(1), eq(1), any(BatchDTO.class))).thenReturn(batch);
        when(mapper.map(batch, BatchDTO.class)).thenReturn(batchDTO);

        mockMvc.perform(put("/inventories/1/products/1/batches/1")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(VALID_BODY))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1));
    }

    @Test
    @WithMockUser
    void shouldReturn404WhenBatchNotFoundOnUpdate() throws Exception {
        when(service.update(eq(1), eq(1), eq(99), any(BatchDTO.class)))
                .thenThrow(new EntityNotFoundException("Lote não encontrado"));

        mockMvc.perform(put("/inventories/1/products/1/batches/99")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(VALID_BODY))
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser
    void shouldReturn400WhenBodyIsInvalidOnUpdate() throws Exception {
        mockMvc.perform(put("/inventories/1/products/1/batches/1")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(BODY_INVALID_UPDATE))
                .andExpect(status().isBadRequest());
    }

    // ===================== DELETE /inventories/{id}/products/{id}/batches/{id} =====================

    @Test
    @WithMockUser
    void shouldDeleteBatchAndReturn204() throws Exception {
        doNothing().when(service).delete(1, 1, 1);

        mockMvc.perform(delete("/inventories/1/products/1/batches/1").with(csrf()))
                .andExpect(status().isNoContent());
    }

    @Test
    @WithMockUser
    void shouldReturn404WhenBatchNotFoundOnDelete() throws Exception {
        doThrow(new EntityNotFoundException("Lote não encontrado"))
                .when(service).delete(1, 1, 99);

        mockMvc.perform(delete("/inventories/1/products/1/batches/99").with(csrf()))
                .andExpect(status().isNotFound());
    }
}