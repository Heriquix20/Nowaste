package A3.project.noWaste.service.impl;

import A3.project.noWaste.domain.Batch;
import A3.project.noWaste.domain.Inventory;
import A3.project.noWaste.domain.Product;
import A3.project.noWaste.domain.User;
import A3.project.noWaste.dto.BatchDTO;
import A3.project.noWaste.exceptions.DataIntegratyViolationException;
import A3.project.noWaste.infra.BatchRepository;
import A3.project.noWaste.infra.ProductRepository;
import A3.project.noWaste.service.VerificationService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.modelmapper.ModelMapper;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class BatchImplTest {

    @Mock
    private BatchRepository repository;

    @Mock
    private ModelMapper mapper;

    @Mock
    private ProductRepository productRepository;

    @Mock
    private VerificationService verificationService;

    @InjectMocks
    private BatchImpl service;

    @Test
    void shouldGenerateInitialBatchCodeInExpectedFormat() {
        Integer userId = 1;
        Integer inventoryId = 10;
        Integer productId = 100;

        Product product = createProduct(productId, "Arroz", userId);
        BatchDTO dto = createBatchDTO();

        when(verificationService.getUserId()).thenReturn(userId);
        when(productRepository.findByIdAndInventoryId(productId, inventoryId)).thenReturn(Optional.of(product));
        when(repository.findByProductId(productId)).thenReturn(List.of());
        when(repository.save(any(Batch.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Batch savedBatch = service.create(inventoryId, productId, dto);

        assertEquals("LT-ARROZ-001", savedBatch.getCode());
    }

    @Test
    void shouldGenerateSequentialCodeForSameProduct() {
        Integer userId = 1;
        Integer inventoryId = 10;
        Integer productId = 100;

        Product product = createProduct(productId, "Arroz", userId);
        BatchDTO dto = createBatchDTO();

        Batch existingBatch = new Batch();
        existingBatch.setCode("LT-ARROZ-001");

        when(verificationService.getUserId()).thenReturn(userId);
        when(productRepository.findByIdAndInventoryId(productId, inventoryId)).thenReturn(Optional.of(product));
        when(repository.findByProductId(productId)).thenReturn(List.of(existingBatch));
        when(repository.save(any(Batch.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Batch savedBatch = service.create(inventoryId, productId, dto);

        assertEquals("LT-ARROZ-002", savedBatch.getCode());
    }

    @Test
    void shouldStartOwnSequenceForDifferentProducts() {
        Integer userId = 1;
        Integer inventoryId = 10;
        Integer productId = 200;

        Product product = createProduct(productId, "Feijao Preto", userId);
        BatchDTO dto = createBatchDTO();

        when(verificationService.getUserId()).thenReturn(userId);
        when(productRepository.findByIdAndInventoryId(productId, inventoryId)).thenReturn(Optional.of(product));
        when(repository.findByProductId(productId)).thenReturn(List.of());
        when(repository.save(any(Batch.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Batch savedBatch = service.create(inventoryId, productId, dto);

        assertEquals("LT-FEIJAO_PRETO-001", savedBatch.getCode());
    }

    @Test
    void shouldReturnBatchByIdWhenProductBelongsToAuthenticatedUser() {
        Integer userId = 1;
        Integer inventoryId = 10;
        Integer productId = 100;
        Product product = createProduct(productId, "Arroz", userId);
        Batch batch = new Batch();
        batch.setId(50);
        batch.setProduct(product);

        when(verificationService.getUserId()).thenReturn(userId);
        when(productRepository.findByIdAndInventoryId(productId, inventoryId)).thenReturn(Optional.of(product));
        when(repository.findByIdAndProductId(50, productId)).thenReturn(Optional.of(batch));

        Batch result = service.findById(inventoryId, productId, 50);

        assertSame(batch, result);
    }

    @Test
    void shouldFilterBatchesByMultipleCriteria() {
        Integer userId = 1;
        Integer inventoryId = 10;
        Integer productId = 100;
        Product product = createProduct(productId, "Arroz", userId);
        LocalDate today = LocalDate.now();

        Batch matching = createBatch(1, "LT-ARROZ-001", 10, today.plusDays(5), product);
        Batch wrongStatus = createBatch(2, "LT-ARROZ-002", 10, today.plusDays(40), product);
        Batch wrongQuantity = createBatch(3, "LT-ARROZ-003", 2, today.plusDays(5), product);

        when(verificationService.getUserId()).thenReturn(userId);
        when(productRepository.findByIdAndInventoryId(productId, inventoryId)).thenReturn(Optional.of(product));
        when(repository.findByProductId(productId)).thenReturn(List.of(matching, wrongStatus, wrongQuantity));

        List<Batch> result = service.findAllByProduct(
                inventoryId, productId, "001", "WARNING",
                today.plusDays(1), today.plusDays(10), 5, 20, "asc"
        );

        assertEquals(1, result.size());
        assertSame(matching, result.get(0));
    }

    @Test
    void shouldSortBatchesByExpirationDateDescendingWhenRequested() {
        Integer userId = 1;
        Integer inventoryId = 10;
        Integer productId = 100;
        Product product = createProduct(productId, "Arroz", userId);
        LocalDate today = LocalDate.now();

        Batch first = createBatch(1, "LT-ARROZ-001", 10, today.plusDays(3), product);
        Batch second = createBatch(2, "LT-ARROZ-002", 10, today.plusDays(10), product);

        when(verificationService.getUserId()).thenReturn(userId);
        when(productRepository.findByIdAndInventoryId(productId, inventoryId)).thenReturn(Optional.of(product));
        when(repository.findByProductId(productId)).thenReturn(List.of(first, second));

        List<Batch> result = service.findAllByProduct(
                inventoryId, productId, null, null, null, null, null, null, "desc"
        );

        assertEquals(List.of(second, first), result);
    }

    @Test
    void shouldUpdateBatchFields() {
        Integer userId = 1;
        Integer inventoryId = 10;
        Integer productId = 100;
        Product product = createProduct(productId, "Arroz", userId);
        Batch existing = createBatch(50, "LT-ARROZ-001", 10, LocalDate.now().plusDays(5), product);

        BatchDTO dto = new BatchDTO();
        dto.setQuantity(20);
        dto.setExpirationDate(LocalDate.now().plusDays(15));

        when(verificationService.getUserId()).thenReturn(userId);
        when(productRepository.findByIdAndInventoryId(productId, inventoryId)).thenReturn(Optional.of(product));
        when(repository.findByIdAndProductId(50, productId)).thenReturn(Optional.of(existing));
        when(repository.save(existing)).thenReturn(existing);

        Batch updated = service.update(inventoryId, productId, 50, dto);

        assertEquals(20, updated.getQuantity());
        assertEquals(dto.getExpirationDate(), updated.getExpirationDate());
    }

    @Test
    void shouldDeleteBatchFromProduct() {
        Integer userId = 1;
        Integer inventoryId = 10;
        Integer productId = 100;
        Product product = createProduct(productId, "Arroz", userId);
        Batch existing = createBatch(50, "LT-ARROZ-001", 10, LocalDate.now().plusDays(5), product);

        when(verificationService.getUserId()).thenReturn(userId);
        when(productRepository.findByIdAndInventoryId(productId, inventoryId)).thenReturn(Optional.of(product));
        when(repository.findByIdAndProductId(50, productId)).thenReturn(Optional.of(existing));

        service.delete(inventoryId, productId, 50);

        verify(repository).delete(existing);
    }

    @Test
    void shouldThrowAccessDeniedWhenProductBelongsToAnotherUser() {
        Integer inventoryId = 10;
        Integer productId = 100;
        Product product = createProduct(productId, "Arroz", 99);

        when(verificationService.getUserId()).thenReturn(1);
        when(productRepository.findByIdAndInventoryId(productId, inventoryId)).thenReturn(Optional.of(product));

        assertThrows(DataIntegratyViolationException.class,
                () -> service.create(inventoryId, productId, createBatchDTO()));
    }

    @Test
    void shouldIgnoreInvalidExistingCodeAndStartFromOne() {
        Integer userId = 1;
        Integer inventoryId = 10;
        Integer productId = 100;

        Product product = createProduct(productId, "Arroz", userId);
        BatchDTO dto = createBatchDTO();

        Batch existingBatch = new Batch();
        existingBatch.setCode("LT-ARROZ-ABC");

        when(verificationService.getUserId()).thenReturn(userId);
        when(productRepository.findByIdAndInventoryId(productId, inventoryId)).thenReturn(Optional.of(product));
        when(repository.findByProductId(productId)).thenReturn(List.of(existingBatch));
        when(repository.save(any(Batch.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Batch savedBatch = service.create(inventoryId, productId, dto);

        assertEquals("LT-ARROZ-001", savedBatch.getCode());
    }

    private Product createProduct(Integer productId, String productName, Integer userId) {
        User user = new User();
        user.setId(userId);

        Inventory inventory = new Inventory();
        inventory.setId(10);
        inventory.setUser(user);

        Product product = new Product();
        product.setId(productId);
        product.setName(productName);
        product.setInventory(inventory);

        return product;
    }

    private BatchDTO createBatchDTO() {
        BatchDTO dto = new BatchDTO();
        dto.setQuantity(10);
        dto.setExpirationDate(LocalDate.now().plusDays(15));
        return dto;
    }

    private Batch createBatch(Integer id, String code, Integer quantity, LocalDate expirationDate, Product product) {
        Batch batch = new Batch();
        batch.setId(id);
        batch.setCode(code);
        batch.setQuantity(quantity);
        batch.setExpirationDate(expirationDate);
        batch.setProduct(product);
        return batch;
    }

    @Test
    void shouldSortBatchesByExpirationDateAscendingWhenRequested() {
        Integer userId = 1;
        Integer inventoryId = 10;
        Integer productId = 100;

        Product product = createProduct(productId, "Arroz", userId);
        LocalDate today = LocalDate.now();

        Batch later = createBatch(2, "LT-ARROZ-002", 10, today.plusDays(10), product);
        Batch earlier = createBatch(1, "LT-ARROZ-001", 10, today.plusDays(3), product);

        when(verificationService.getUserId()).thenReturn(userId);
        when(productRepository.findByIdAndInventoryId(productId, inventoryId))
                .thenReturn(Optional.of(product));
        when(repository.findByProductId(productId))
                .thenReturn(List.of(later, earlier));

        List<Batch> result = service.findAllByProduct(
                inventoryId, productId,
                null, null, null, null, null, null, "asc"
        );

        assertEquals(List.of(earlier, later), result);
    }

    @Test
    void shouldThrowWhenProductDoesNotExistInInventory() {
        when(verificationService.getUserId()).thenReturn(1);

        when(productRepository.findByIdAndInventoryId(100, 10))
                .thenReturn(Optional.empty());

        assertThrows(
                RuntimeException.class,
                () -> service.findAllByProduct(
                        10, 100,
                        null, null, null, null, null, null, null
                )
        );
    }

    @Test
    void shouldThrowWhenBatchNotFound() {
        Integer userId = 1;
        Integer inventoryId = 10;
        Integer productId = 100;

        Product product = createProduct(productId, "Arroz", userId);

        when(verificationService.getUserId()).thenReturn(userId);
        when(productRepository.findByIdAndInventoryId(productId, inventoryId))
                .thenReturn(Optional.of(product));

        when(repository.findByIdAndProductId(50, productId))
                .thenReturn(Optional.empty());

        assertThrows(
                RuntimeException.class,
                () -> service.findById(inventoryId, productId, 50)
        );
    }
}
