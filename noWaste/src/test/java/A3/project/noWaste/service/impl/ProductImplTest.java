package A3.project.noWaste.service.impl;

import A3.project.noWaste.domain.Inventory;
import A3.project.noWaste.domain.Product;
import A3.project.noWaste.dto.ProductDTO;
import A3.project.noWaste.exceptions.DataIntegratyViolationException;
import A3.project.noWaste.infra.InventoryRepository;
import A3.project.noWaste.infra.ProductRepository;
import A3.project.noWaste.service.VerificationService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.modelmapper.ModelMapper;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ProductImplTest {

    @Mock
    private ProductRepository repository;

    @Mock
    private InventoryRepository inventoryRepository;

    @Mock
    private VerificationService verificationService;

    @Mock
    private ModelMapper mapper;

    @InjectMocks
    private ProductImpl service;

    @Test
    void shouldConvertWeightFromKgToGramsWhenCreatingProduct() {
        Integer userId = 1;
        Integer inventoryId = 10;

        Inventory inventory = new Inventory();
        inventory.setId(inventoryId);

        ProductDTO dto = new ProductDTO();
        dto.setName("Arroz");
        dto.setWeight(5.0);
        dto.setWeightUnit("kg");
        dto.setCategory("Graos");
        dto.setBrand("Tio Joao");

        when(verificationService.getUserId()).thenReturn(userId);
        when(inventoryRepository.findByIdAndUserId(inventoryId, userId)).thenReturn(Optional.of(inventory));
        when(repository.findByInventoryId(inventoryId)).thenReturn(Collections.emptyList());
        when(repository.save(any(Product.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Product savedProduct = service.create(inventoryId, dto);

        assertEquals(5000.0, savedProduct.getWeightInGrams());
    }

    @Test
    void shouldKeepWeightValueWhenUnitIsAlreadyGrams() {
        Integer userId = 1;
        Integer inventoryId = 10;

        Inventory inventory = new Inventory();
        inventory.setId(inventoryId);

        ProductDTO dto = new ProductDTO();
        dto.setName("Feijao");
        dto.setWeight(500.0);
        dto.setWeightUnit("g");
        dto.setCategory("Graos");
        dto.setBrand("Kicaldo");

        when(verificationService.getUserId()).thenReturn(userId);
        when(inventoryRepository.findByIdAndUserId(inventoryId, userId)).thenReturn(Optional.of(inventory));
        when(repository.findByInventoryId(inventoryId)).thenReturn(Collections.emptyList());
        when(repository.save(any(Product.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Product savedProduct = service.create(inventoryId, dto);

        assertEquals(500.0, savedProduct.getWeightInGrams());
    }

    @Test
    void shouldThrowExceptionWhenMinWeightIsGreaterThanMaxWeight() {
        Integer userId = 1;
        Integer inventoryId = 10;

        Inventory inventory = new Inventory();
        inventory.setId(inventoryId);

        when(verificationService.getUserId()).thenReturn(userId);
        when(inventoryRepository.findByIdAndUserId(inventoryId, userId)).thenReturn(Optional.of(inventory));
        when(repository.findByInventoryId(inventoryId)).thenReturn(Collections.emptyList());

        DataIntegratyViolationException exception = assertThrows(
                DataIntegratyViolationException.class,
                () -> service.findAllByInventory(inventoryId, null, null, null, 3000.0, 500.0, null)
        );

        assertEquals("O peso mínimo nao pode ser maior que o peso máximo", exception.getMessage());
    }
}
