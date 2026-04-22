package A3.project.noWaste.service.impl;

import A3.project.noWaste.domain.Batch;
import A3.project.noWaste.domain.Inventory;
import A3.project.noWaste.domain.Product;
import A3.project.noWaste.domain.User;
import A3.project.noWaste.dto.BatchDTO;
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
import static org.mockito.ArgumentMatchers.any;
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
}
