package A3.project.noWaste.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class ProductTest {

    private Product product;

    @BeforeEach
    void setUp() {
        product = new Product();

        product.setId(1);
        product.setName("Rice");
        product.setWeightInGrams(1000.0);
        product.setCategory("Food");
        product.setBrand("Brand A");
        product.setBatches(new ArrayList<>());
    }

    @Test
    void shouldSetAndGetProductProperties() {
        assertAll(
                () -> assertEquals(1, product.getId()),
                () -> assertEquals("Rice", product.getName()),
                () -> assertEquals(1000.0, product.getWeightInGrams()),
                () -> assertEquals("Food", product.getCategory()),
                () -> assertEquals("Brand A", product.getBrand()),
                () -> assertTrue(product.getBatches().isEmpty())
        );
    }

    @Test
    void shouldCreateProductUsingAllArgsConstructor() {
        Inventory inventory = new Inventory();

        Product newProduct = new Product(
                2,
                "Beans",
                500.0,
                "Food",
                "Brand B",
                inventory,
                new ArrayList<>()
        );

        assertAll(
                () -> assertEquals(2, newProduct.getId()),
                () -> assertEquals("Beans", newProduct.getName()),
                () -> assertEquals(500.0, newProduct.getWeightInGrams()),
                () -> assertEquals("Food", newProduct.getCategory()),
                () -> assertEquals("Brand B", newProduct.getBrand()),
                () -> assertEquals(inventory, newProduct.getInventory()),
                () -> assertTrue(newProduct.getBatches().isEmpty())
        );
    }

    @Test
    void shouldAssociateInventory() {
        Inventory inventory = new Inventory();

        product.setInventory(inventory);

        assertEquals(inventory, product.getInventory());
    }

    @Test
    void shouldInitializeBatchesList() {
        Product newProduct = new Product();

        assertNotNull(newProduct.getBatches());
        assertTrue(newProduct.getBatches().isEmpty());
    }
}