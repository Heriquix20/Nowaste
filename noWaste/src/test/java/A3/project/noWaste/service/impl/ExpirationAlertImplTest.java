package A3.project.noWaste.service.impl;

import A3.project.noWaste.domain.Batch;
import A3.project.noWaste.domain.Inventory;
import A3.project.noWaste.domain.Product;
import A3.project.noWaste.domain.User;
import A3.project.noWaste.dto.ExpirationAlertDTO;
import A3.project.noWaste.infra.BatchRepository;
import A3.project.noWaste.service.VerificationService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ExpirationAlertImplTest {

    @Mock
    private BatchRepository batchRepository;

    @Mock
    private VerificationService verificationService;

    @InjectMocks
    private ExpirationAlertImpl service;

    @Test
    void shouldReturnCurrentMonthBatchesForAuthenticatedUserSortedAndMapped() {
        Integer userId = 1;
        LocalDate today = LocalDate.now();
        LocalDate firstDate = today.withDayOfMonth(1);
        LocalDate secondDate = today.withDayOfMonth(Math.min(2, today.lengthOfMonth()));

        Batch laterBatch = createBatch(2, "LT-ARROZ-002", 12, secondDate, 1000.0, 10, 100, "Arroz", userId);
        Batch earlierBatch = createBatch(1, "LT-ARROZ-001", 8, firstDate, 1000.0, 10, 100, "Arroz", userId);
        Batch otherUserBatch = createBatch(3, "LT-ARROZ-003", 5, secondDate, 1000.0, 10, 100, "Arroz", 99);
        Batch nullDateBatch = createBatch(4, "LT-ARROZ-004", 7, null, 1000.0, 10, 100, "Arroz", userId);
        Batch otherMonthBatch = createBatch(5, "LT-ARROZ-005", 6, today.plusMonths(1), 1000.0, 10, 100, "Arroz", userId);

        when(verificationService.getUserId()).thenReturn(userId);
        when(batchRepository.findAll()).thenReturn(List.of(laterBatch, earlierBatch, otherUserBatch, nullDateBatch, otherMonthBatch));

        List<ExpirationAlertDTO> alerts = service.findBatchesExpiringThisMonth();

        assertEquals(2, alerts.size());
        assertEquals(1, alerts.get(0).getBatchId());
        assertEquals(2, alerts.get(1).getBatchId());
        assertEquals("LT-ARROZ-001", alerts.get(0).getBatchCode());
        assertEquals(8000.0, alerts.get(0).getTotalWeight());
        assertEquals(10, alerts.get(0).getInventoryId());
        assertEquals(100, alerts.get(0).getProductId());
        assertEquals(earlierBatch.getStatus(), alerts.get(0).getStatus());
        assertEquals(earlierBatch.getDaysToExpire(), alerts.get(0).getDaysToExpire());
    }

    @Test
    void shouldReturnOnlyExpiredBatchesForAuthenticatedUserSortedByExpirationDate() {
        Integer userId = 1;
        LocalDate today = LocalDate.now();

        Batch olderExpired = createBatch(1, "LT-FEIJAO-001", 4, today.minusDays(10), 500.0, 10, 200, "Feijao", userId);
        Batch recentExpired = createBatch(2, "LT-FEIJAO-002", 6, today.minusDays(2), 500.0, 10, 200, "Feijao", userId);
        Batch warningBatch = createBatch(3, "LT-FEIJAO-003", 7, today.plusDays(3), 500.0, 10, 200, "Feijao", userId);
        Batch otherUserExpired = createBatch(4, "LT-FEIJAO-004", 8, today.minusDays(5), 500.0, 10, 200, "Feijao", 99);

        when(verificationService.getUserId()).thenReturn(userId);
        when(batchRepository.findAll()).thenReturn(List.of(recentExpired, warningBatch, olderExpired, otherUserExpired));

        List<ExpirationAlertDTO> alerts = service.findExpiredBatches();

        assertEquals(2, alerts.size());
        assertEquals(1, alerts.get(0).getBatchId());
        assertEquals(2, alerts.get(1).getBatchId());
        assertEquals("EXPIRED", alerts.get(0).getStatus());
        assertEquals(-10, alerts.get(0).getDaysToExpire());
    }

    @Test
    void shouldReturnEmptyAlertListsWhenNoBatchMatches() {
        when(verificationService.getUserId()).thenReturn(1);
        when(batchRepository.findAll()).thenReturn(List.of());

        assertEquals(List.of(), service.findBatchesExpiringThisMonth());
        assertEquals(List.of(), service.findExpiredBatches());
    }

    private Batch createBatch(Integer batchId, String code, Integer quantity, LocalDate expirationDate,
                              Double weightInGrams, Integer inventoryId, Integer productId,
                              String productName, Integer userId) {
        User user = new User();
        user.setId(userId);

        Inventory inventory = new Inventory();
        inventory.setId(inventoryId);
        inventory.setName("Despensa");
        inventory.setUser(user);

        Product product = new Product();
        product.setId(productId);
        product.setName(productName);
        product.setWeightInGrams(weightInGrams);
        product.setInventory(inventory);

        Batch batch = new Batch();
        batch.setId(batchId);
        batch.setCode(code);
        batch.setQuantity(quantity);
        batch.setExpirationDate(expirationDate);
        batch.setProduct(product);
        return batch;
    }

    @Test
    void shouldReturnBatchesExpiringIn7DaysForAuthenticatedUser() {
        Integer userId = 1;
        LocalDate today = LocalDate.now();

        Batch batch1 = createBatch(
                1, "LT-001", 10,
                today.plusDays(3),
                1000.0, 10, 100,
                "Arroz", userId
        );

        Batch batch2 = createBatch(
                2, "LT-002", 5,
                today.plusDays(7),
                1000.0, 10, 100,
                "Arroz", userId
        );

        Batch expired = createBatch(
                3, "LT-003", 5,
                today.minusDays(1),
                1000.0, 10, 100,
                "Arroz", userId
        );

        Batch farFuture = createBatch(
                4, "LT-004", 5,
                today.plusDays(15),
                1000.0, 10, 100,
                "Arroz", userId
        );

        Batch otherUser = createBatch(
                5, "LT-005", 5,
                today.plusDays(3),
                1000.0, 10, 100,
                "Arroz", 99
        );

        when(verificationService.getUserId()).thenReturn(userId);
        when(batchRepository.findAll())
                .thenReturn(List.of(batch1, batch2, expired, farFuture, otherUser));

        List<ExpirationAlertDTO> alerts =
                service.findBatchesExpiringIn7Days();

        assertEquals(2, alerts.size());

        assertEquals(1, alerts.get(0).getBatchId());
        assertEquals(2, alerts.get(1).getBatchId());
    }

    @Test
    void shouldReturnEmptyListWhenNoBatchExpiresIn7Days() {
        Integer userId = 1;
        LocalDate today = LocalDate.now();

        Batch batch = createBatch(
                1, "LT-001", 10,
                today.plusDays(20),
                1000.0, 10, 100,
                "Arroz", userId
        );

        when(verificationService.getUserId()).thenReturn(userId);
        when(batchRepository.findAll()).thenReturn(List.of(batch));

        List<ExpirationAlertDTO> alerts =
                service.findBatchesExpiringIn7Days();

        assertEquals(0, alerts.size());
    }
}
