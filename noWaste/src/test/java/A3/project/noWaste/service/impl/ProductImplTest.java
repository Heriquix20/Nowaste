package A3.project.noWaste.service.impl;

import A3.project.noWaste.domain.Inventory;
import A3.project.noWaste.domain.Product;
import A3.project.noWaste.dto.ProductDTO;
import A3.project.noWaste.exceptions.DataIntegratyViolationException;
import A3.project.noWaste.exceptions.ObjectNotFoundException;
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
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
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

        assertThrows(
                DataIntegratyViolationException.class,
                () -> service.findAllByInventory(inventoryId, null, null, null, 3000.0, 500.0, null)
        );
    }

    @Test
    void shouldReturnProductByIdWhenInventoryBelongsToUser() {
        Integer userId = 1;
        Integer inventoryId = 10;
        Product product = createProduct(100, "Arroz", "Graos", "Tio Joao", 1000.0);
        Inventory inventory = new Inventory();
        inventory.setId(inventoryId);

        when(verificationService.getUserId()).thenReturn(userId);
        when(inventoryRepository.findByIdAndUserId(inventoryId, userId)).thenReturn(Optional.of(inventory));
        when(repository.findByIdAndInventoryId(100, inventoryId)).thenReturn(Optional.of(product));

        Product result = service.findById(inventoryId, 100);

        assertSame(product, result);
    }

    @Test
    void shouldThrowWhenProductIsNotFoundById() {
        Integer userId = 1;
        Integer inventoryId = 10;
        Inventory inventory = new Inventory();
        inventory.setId(inventoryId);

        when(verificationService.getUserId()).thenReturn(userId);
        when(inventoryRepository.findByIdAndUserId(inventoryId, userId)).thenReturn(Optional.of(inventory));
        when(repository.findByIdAndInventoryId(100, inventoryId)).thenReturn(Optional.empty());

        assertThrows(ObjectNotFoundException.class, () -> service.findById(inventoryId, 100));
    }

    @Test
    void shouldFilterProductsByTextFieldsAndWeightRange() {
        Integer userId = 1;
        Integer inventoryId = 10;
        Inventory inventory = new Inventory();
        inventory.setId(inventoryId);

        Product arroz = createProduct(1, "Arroz Branco", "Graos", "Tio Joao", 1000.0);
        Product feijao = createProduct(2, "Feijao", "Graos", "Kicaldo", 500.0);
        Product macarrao = createProduct(3, "Macarrao", "Massas", "Renata", 1500.0);

        when(verificationService.getUserId()).thenReturn(userId);
        when(inventoryRepository.findByIdAndUserId(inventoryId, userId)).thenReturn(Optional.of(inventory));
        when(repository.findByInventoryId(inventoryId)).thenReturn(List.of(arroz, feijao, macarrao));

        List<Product> result = service.findAllByInventory(inventoryId, "arroz", "gra", "joao", 900.0, 1100.0, "asc");

        assertEquals(1, result.size());
        assertSame(arroz, result.get(0));
    }

    @Test
    void shouldSortProductsByWeightDescendingWhenRequested() {
        Integer userId = 1;
        Integer inventoryId = 10;
        Inventory inventory = new Inventory();
        inventory.setId(inventoryId);

        Product arroz = createProduct(1, "Arroz", "Graos", "Marca 1", 1000.0);
        Product feijao = createProduct(2, "Feijao", "Graos", "Marca 2", 500.0);
        Product macarrao = createProduct(3, "Macarrao", "Massas", "Marca 3", 1500.0);

        when(verificationService.getUserId()).thenReturn(userId);
        when(inventoryRepository.findByIdAndUserId(inventoryId, userId)).thenReturn(Optional.of(inventory));
        when(repository.findByInventoryId(inventoryId)).thenReturn(List.of(arroz, feijao, macarrao));

        List<Product> result = service.findAllByInventory(inventoryId, null, null, null, null, null, "desc");

        assertEquals(List.of(macarrao, arroz, feijao), result);
    }

    @Test
    void shouldThrowWhenCreatingProductWithDuplicatedName() {
        Integer userId = 1;
        Integer inventoryId = 10;
        Inventory inventory = new Inventory();
        inventory.setId(inventoryId);

        Product existing = createProduct(1, "Arroz", "Graos", "Marca", 1000.0);

        ProductDTO dto = new ProductDTO();
        dto.setName("Arroz");
        dto.setWeight(1.0);
        dto.setWeightUnit("kg");
        dto.setCategory("Graos");
        dto.setBrand("Marca");

        when(verificationService.getUserId()).thenReturn(userId);
        when(inventoryRepository.findByIdAndUserId(inventoryId, userId)).thenReturn(Optional.of(inventory));
        when(repository.findByInventoryId(inventoryId)).thenReturn(List.of(existing));

        assertThrows(DataIntegratyViolationException.class, () -> service.create(inventoryId, dto));
    }

    @Test
    void shouldUpdateProductAndConvertWeightToGrams() {
        Integer userId = 1;
        Integer inventoryId = 10;
        Product existing = createProduct(100, "Arroz", "Graos", "Marca", 1000.0);
        Inventory inventory = new Inventory();
        inventory.setId(inventoryId);

        ProductDTO dto = new ProductDTO();
        dto.setName("Arroz Integral");
        dto.setWeight(2.0);
        dto.setWeightUnit("kg");
        dto.setCategory("Graos");
        dto.setBrand("Nova Marca");

        when(verificationService.getUserId()).thenReturn(userId);
        when(inventoryRepository.findByIdAndUserId(inventoryId, userId)).thenReturn(Optional.of(inventory));
        when(repository.findByIdAndInventoryId(100, inventoryId)).thenReturn(Optional.of(existing));
        when(repository.findByInventoryId(inventoryId)).thenReturn(List.of(existing));
        when(repository.save(existing)).thenReturn(existing);

        Product updated = service.update(inventoryId, 100, dto);

        assertEquals("Arroz Integral", updated.getName());
        assertEquals("Nova Marca", updated.getBrand());
        assertEquals(2000.0, updated.getWeightInGrams());
    }

    @Test
    void shouldDeleteProductFromInventory() {
        Integer userId = 1;
        Integer inventoryId = 10;
        Product existing = createProduct(100, "Arroz", "Graos", "Marca", 1000.0);
        Inventory inventory = new Inventory();
        inventory.setId(inventoryId);

        when(verificationService.getUserId()).thenReturn(userId);
        when(inventoryRepository.findByIdAndUserId(inventoryId, userId)).thenReturn(Optional.of(inventory));
        when(repository.findByIdAndInventoryId(100, inventoryId)).thenReturn(Optional.of(existing));

        service.delete(inventoryId, 100);

        verify(repository).delete(existing);
    }

    private Product createProduct(Integer id, String name, String category, String brand, Double weightInGrams) {
        Product product = new Product();
        product.setId(id);
        product.setName(name);
        product.setCategory(category);
        product.setBrand(brand);
        product.setWeightInGrams(weightInGrams);
        return product;
    }

    @Test
    void shouldReturnAllProductsFromAuthenticatedUser() {
        Integer userId = 1;

        Product product = createProduct(
                1,
                "Arroz",
                "Graos",
                "Marca",
                1000.0
        );

        when(verificationService.getUserId()).thenReturn(userId);
        when(repository.findByInventoryUserId(userId))
                .thenReturn(List.of(product));

        List<Product> result = service.findAllByUser();

        assertEquals(1, result.size());
        assertEquals("Arroz", result.get(0).getName());
    }

    @Test
    void shouldSortProductsByWeightAscending() {
        Integer userId = 1;
        Integer inventoryId = 10;

        Inventory inventory = new Inventory();
        inventory.setId(inventoryId);

        Product p1 = createProduct(1, "A", "Cat", "Marca", 1000.0);
        Product p2 = createProduct(2, "B", "Cat", "Marca", 500.0);
        Product p3 = createProduct(3, "C", "Cat", "Marca", 1500.0);

        when(verificationService.getUserId()).thenReturn(userId);
        when(inventoryRepository.findByIdAndUserId(inventoryId, userId))
                .thenReturn(Optional.of(inventory));
        when(repository.findByInventoryId(inventoryId))
                .thenReturn(List.of(p1, p2, p3));

        List<Product> result =
                service.findAllByInventory(inventoryId, null, null, null, null, null, "asc");

        assertEquals(List.of(p2, p1, p3), result);
    }

    @Test
    void shouldUpdateProductKeepingSameName() {
        Integer userId = 1;
        Integer inventoryId = 10;

        Product existing = createProduct(
                100,
                "Arroz",
                "Graos",
                "Marca",
                1000.0
        );

        Inventory inventory = new Inventory();
        inventory.setId(inventoryId);

        ProductDTO dto = new ProductDTO();
        dto.setName("Arroz");
        dto.setWeight(1.0);
        dto.setWeightUnit("kg");
        dto.setCategory("Graos");
        dto.setBrand("Marca");

        when(verificationService.getUserId()).thenReturn(userId);
        when(inventoryRepository.findByIdAndUserId(inventoryId, userId))
                .thenReturn(Optional.of(inventory));
        when(repository.findByIdAndInventoryId(100, inventoryId))
                .thenReturn(Optional.of(existing));
        when(repository.findByInventoryId(inventoryId))
                .thenReturn(List.of(existing));
        when(repository.save(existing))
                .thenReturn(existing);

        Product result = service.update(inventoryId, 100, dto);

        assertEquals("Arroz", result.getName());
    }

    @Test
    void shouldThrowWhenInventoryDoesNotBelongToUser() {
        Integer userId = 1;
        Integer inventoryId = 10;

        when(verificationService.getUserId()).thenReturn(userId);
        when(inventoryRepository.findByIdAndUserId(inventoryId, userId))
                .thenReturn(Optional.empty());

        assertThrows(
                ObjectNotFoundException.class,
                () -> service.findById(inventoryId, 1)
        );
    }

    @Test
    void shouldThrowWhenWeightUnitIsNull() {
        Integer userId = 1;
        Integer inventoryId = 10;

        Inventory inventory = new Inventory();
        inventory.setId(inventoryId);

        ProductDTO dto = new ProductDTO();
        dto.setName("Arroz");
        dto.setWeight(1.0);
        dto.setWeightUnit(null);
        dto.setCategory("Graos");
        dto.setBrand("Marca");

        when(verificationService.getUserId()).thenReturn(userId);
        when(inventoryRepository.findByIdAndUserId(inventoryId, userId))
                .thenReturn(Optional.of(inventory));
        when(repository.findByInventoryId(inventoryId))
                .thenReturn(Collections.emptyList());

        assertThrows(
                DataIntegratyViolationException.class,
                () -> service.create(inventoryId, dto)
        );
    }

    @Test
    void shouldThrowWhenWeightUnitIsInvalid() {
        Integer userId = 1;
        Integer inventoryId = 10;

        Inventory inventory = new Inventory();
        inventory.setId(inventoryId);

        ProductDTO dto = new ProductDTO();
        dto.setName("Arroz");
        dto.setWeight(1.0);
        dto.setWeightUnit("lb");
        dto.setCategory("Graos");
        dto.setBrand("Marca");

        when(verificationService.getUserId()).thenReturn(userId);
        when(inventoryRepository.findByIdAndUserId(inventoryId, userId))
                .thenReturn(Optional.of(inventory));
        when(repository.findByInventoryId(inventoryId))
                .thenReturn(Collections.emptyList());

        assertThrows(
                DataIntegratyViolationException.class,
                () -> service.create(inventoryId, dto)
        );
    }
}
