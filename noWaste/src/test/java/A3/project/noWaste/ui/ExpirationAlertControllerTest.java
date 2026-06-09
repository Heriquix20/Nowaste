package A3.project.noWaste.ui;

import A3.project.noWaste.config.TokenConfig;
import A3.project.noWaste.dto.ExpirationAlertDTO;
import A3.project.noWaste.exceptions.ExceptionHandlerService;
import A3.project.noWaste.service.ExpirationAlertService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest({ExpirationAlertController.class, ExceptionHandlerService.class})
class ExpirationAlertControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private ExpirationAlertService service;

    @MockitoBean
    private TokenConfig tokenConfig;

    private ExpirationAlertDTO alertDTO;

    @BeforeEach
    void setUp() {
        alertDTO = new ExpirationAlertDTO();
        alertDTO.setInventoryId(1);
        alertDTO.setInventoryName("Estoque Principal");
        alertDTO.setProductId(1);
        alertDTO.setProductName("Arroz");
        alertDTO.setBatchId(1);
        alertDTO.setBatchCode("LT-ARROZ-001");
        alertDTO.setQuantity(10);
        alertDTO.setTotalWeight(10000.0);
        alertDTO.setExpirationDate(LocalDate.now().plusDays(5));
        alertDTO.setDaysToExpire(5L);
        alertDTO.setStatus("WARNING");
    }

    @AfterEach
    void tearDown() {
        SecurityContextHolder.clearContext();
    }

    // ===================== GET /alerts/month =====================

    @Test
    @WithMockUser
    void shouldReturnBatchesExpiringThisMonth() throws Exception {
        when(service.findBatchesExpiringThisMonth()).thenReturn(List.of(alertDTO));

        mockMvc.perform(get("/alerts/month"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].batchId").value(1))
                .andExpect(jsonPath("$[0].batchCode").value("LT-ARROZ-001"))
                .andExpect(jsonPath("$[0].status").value("WARNING"));
    }

    @Test
    @WithMockUser
    void shouldReturnEmptyListWhenNoBatchesExpiringThisMonth() throws Exception {
        when(service.findBatchesExpiringThisMonth()).thenReturn(List.of());

        mockMvc.perform(get("/alerts/month"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isEmpty());
    }

    // ===================== GET /alerts/week =====================

    @Test
    @WithMockUser
    void shouldReturnBatchesExpiringIn7Days() throws Exception {
        when(service.findBatchesExpiringIn7Days()).thenReturn(List.of(alertDTO));

        mockMvc.perform(get("/alerts/week"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].batchId").value(1))
                .andExpect(jsonPath("$[0].daysToExpire").value(5))
                .andExpect(jsonPath("$[0].productName").value("Arroz"));
    }

    @Test
    @WithMockUser
    void shouldReturnEmptyListWhenNoBatchesExpiringIn7Days() throws Exception {
        when(service.findBatchesExpiringIn7Days()).thenReturn(List.of());

        mockMvc.perform(get("/alerts/week"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isEmpty());
    }

    // ===================== GET /alerts/expired =====================

    @Test
    @WithMockUser
    void shouldReturnExpiredBatches() throws Exception {
        alertDTO.setDaysToExpire(-3L);
        alertDTO.setStatus("EXPIRED");
        when(service.findExpiredBatches()).thenReturn(List.of(alertDTO));

        mockMvc.perform(get("/alerts/expired"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].batchId").value(1))
                .andExpect(jsonPath("$[0].status").value("EXPIRED"))
                .andExpect(jsonPath("$[0].daysToExpire").value(-3));
    }

    @Test
    @WithMockUser
    void shouldReturnEmptyListWhenNoExpiredBatches() throws Exception {
        when(service.findExpiredBatches()).thenReturn(List.of());

        mockMvc.perform(get("/alerts/expired"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isEmpty());
    }
}