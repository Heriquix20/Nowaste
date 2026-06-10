package A3.project.noWaste.ui;

import A3.project.noWaste.config.TokenConfig;
import A3.project.noWaste.domain.Product;
import A3.project.noWaste.dto.ProductDTO;
import A3.project.noWaste.exceptions.ExceptionHandlerService;
import A3.project.noWaste.service.ProductService;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import org.modelmapper.ModelMapper;
import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest({ProductController.class, ExceptionHandlerService.class})
class ProductControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private ProductService service;

    @MockitoBean
    private TokenConfig tokenConfig;

    @MockitoBean
    private ModelMapper mapper;

    private ObjectMapper objectMapper;

    private Product product;
    private ProductDTO productDTO;

    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper();

        product = new Product();
        product.setId(1);
        product.setName("Arroz");
        product.setWeightInGrams(1000.0);
        product.setCategory("Grãos");
        product.setBrand("Tio João");

        productDTO = new ProductDTO();
        productDTO.setId(1);
        productDTO.setName("Arroz");
        productDTO.setWeight(1000.0);
        productDTO.setCategory("Grãos");
        productDTO.setBrand("Tio João");
        productDTO.setWeightUnit("g");
    }

    @AfterEach
    void tearDown() {
        SecurityContextHolder.clearContext();
    }

    // ===================== GET /products =====================

    @Test
    @WithMockUser
    void shouldReturnAllProductsByUser() throws Exception {
        when(service.findAllByUser()).thenReturn(List.of(product));

        mockMvc.perform(get("/products"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].name").value("Arroz"));
    }

    @Test
    @WithMockUser
    void shouldReturnEmptyListWhenNoProductsForUser() throws Exception {
        when(service.findAllByUser()).thenReturn(List.of());

        mockMvc.perform(get("/products"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isEmpty());
    }

    // ===================== GET /inventories/{inventoryId}/products =====================

    @Test
    @WithMockUser
    void shouldReturnAllProductsByInventory() throws Exception {
        when(service.findAllByInventory(1, null, null, null, null, null, null))
                .thenReturn(List.of(product));

        mockMvc.perform(get("/inventories/1/products"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].name").value("Arroz"));
    }

    @Test
    @WithMockUser
    void shouldReturnFilteredProductsByName() throws Exception {
        when(service.findAllByInventory(1, "Arroz", null, null, null, null, null))
                .thenReturn(List.of(product));

        mockMvc.perform(get("/inventories/1/products").param("name", "Arroz"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("Arroz"));
    }

    @Test
    @WithMockUser
    void shouldReturnFilteredProductsByCategory() throws Exception {
        when(service.findAllByInventory(1, null, "Grãos", null, null, null, null))
                .thenReturn(List.of(product));

        mockMvc.perform(get("/inventories/1/products").param("category", "Grãos"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].category").value("Grãos"));
    }

    @Test
    @WithMockUser
    void shouldReturnEmptyListWhenNoProductsInInventory() throws Exception {
        when(service.findAllByInventory(1, null, null, null, null, null, null))
                .thenReturn(List.of());

        mockMvc.perform(get("/inventories/1/products"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isEmpty());
    }

    // ===================== GET /inventories/{inventoryId}/products/{productId} =====================

    @Test
    @WithMockUser
    void shouldReturnProductById() throws Exception {
        when(service.findById(1, 1)).thenReturn(product);

        mockMvc.perform(get("/inventories/1/products/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Arroz"));
    }

    @Test
    @WithMockUser
    void shouldReturn404WhenProductNotFoundById() throws Exception {
        when(service.findById(1, 99)).thenThrow(new EntityNotFoundException("Produto não encontrado"));

        mockMvc.perform(get("/inventories/1/products/99"))
                .andExpect(status().isNotFound());
    }

    // ===================== POST /inventories/{inventoryId}/products =====================

    @Test
    @WithMockUser
    void shouldCreateProductAndReturn201() throws Exception {
        when(service.create(eq(1), any(ProductDTO.class))).thenReturn(product);

        mockMvc.perform(post("/inventories/1/products")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(productDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Arroz"));
    }

    @Test
    @WithMockUser
    void shouldReturn400WhenNameIsBlankOnCreate() throws Exception {
        productDTO.setName("");

        mockMvc.perform(post("/inventories/1/products")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(productDTO)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser
    void shouldReturn400WhenCategoryIsBlankOnCreate() throws Exception {
        productDTO.setCategory("");

        mockMvc.perform(post("/inventories/1/products")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(productDTO)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser
    void shouldReturn400WhenBrandIsBlankOnCreate() throws Exception {
        productDTO.setBrand("");

        mockMvc.perform(post("/inventories/1/products")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(productDTO)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser
    void shouldReturn400WhenWeightIsNullOnCreate() throws Exception {
        productDTO.setWeight(null);

        mockMvc.perform(post("/inventories/1/products")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(productDTO)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser
    void shouldReturn400WhenWeightIsNegativeOnCreate() throws Exception {
        productDTO.setWeight(-1.0);

        mockMvc.perform(post("/inventories/1/products")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(productDTO)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser
    void shouldReturn400WhenBodyIsMissingOnCreate() throws Exception {
        mockMvc.perform(post("/inventories/1/products")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    // ===================== PUT /inventories/{inventoryId}/products/{productId} =====================

    @Test
    @WithMockUser
    void shouldUpdateProductAndReturn200() throws Exception {
        when(service.update(eq(1), eq(1), any(ProductDTO.class))).thenReturn(product);

        mockMvc.perform(put("/inventories/1/products/1")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(productDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Arroz"));
    }

    @Test
    @WithMockUser
    void shouldReturn404WhenProductNotFoundOnUpdate() throws Exception {
        when(service.update(eq(1), eq(99), any(ProductDTO.class)))
                .thenThrow(new EntityNotFoundException("Produto não encontrado"));

        mockMvc.perform(put("/inventories/1/products/99")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(productDTO)))
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser
    void shouldReturn400WhenBodyIsInvalidOnUpdate() throws Exception {
        productDTO.setName("");

        mockMvc.perform(put("/inventories/1/products/1")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(productDTO)))
                .andExpect(status().isBadRequest());
    }

    // ===================== DELETE /inventories/{inventoryId}/products/{productId} =====================

    @Test
    @WithMockUser
    void shouldDeleteProductAndReturn204() throws Exception {
        doNothing().when(service).delete(1, 1);

        mockMvc.perform(delete("/inventories/1/products/1").with(csrf()))
                .andExpect(status().isNoContent());
    }

    @Test
    @WithMockUser
    void shouldReturn404WhenProductNotFoundOnDelete() throws Exception {
        doThrow(new EntityNotFoundException("Produto não encontrado"))
                .when(service).delete(1, 99);

        mockMvc.perform(delete("/inventories/1/products/99").with(csrf()))
                .andExpect(status().isNotFound());
    }
}