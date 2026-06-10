package A3.project.noWaste.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class InventoryTest {

    private Inventory inventory;

    @BeforeEach
    void setUp() {
        inventory = new Inventory();

        inventory.setId(1);
        inventory.setName("Main Inventory");
        inventory.setDescription("Main storage");
        inventory.setLocation("Warehouse A");
        inventory.setProducts(new ArrayList<>());
    }

    @Test
    void shouldSetAndGetInventoryProperties() {
        assertAll(
                () -> assertEquals(1, inventory.getId()),
                () -> assertEquals("Main Inventory", inventory.getName()),
                () -> assertEquals("Main storage", inventory.getDescription()),
                () -> assertEquals("Warehouse A", inventory.getLocation()),
                () -> assertTrue(inventory.getProducts().isEmpty())
        );
    }

    @Test
    void shouldCreateInventoryUsingAllArgsConstructor() {
        User user = new User();
        LocalDateTime createdAt = LocalDateTime.now();

        Inventory newInventory = new Inventory(
                2,
                "Secondary Inventory",
                "Backup storage",
                "Warehouse B",
                createdAt,
                user,
                new ArrayList<>()
        );

        assertAll(
                () -> assertEquals(2, newInventory.getId()),
                () -> assertEquals("Secondary Inventory", newInventory.getName()),
                () -> assertEquals("Backup storage", newInventory.getDescription()),
                () -> assertEquals("Warehouse B", newInventory.getLocation()),
                () -> assertEquals(createdAt, newInventory.getCreatedAt()),
                () -> assertEquals(user, newInventory.getUser()),
                () -> assertTrue(newInventory.getProducts().isEmpty())
        );
    }

    @Test
    void shouldAssociateUser() {
        User user = new User();

        inventory.setUser(user);

        assertEquals(user, inventory.getUser());
    }

    @Test
    void shouldInitializeProductsList() {
        Inventory newInventory = new Inventory();

        assertNotNull(newInventory.getProducts());
        assertTrue(newInventory.getProducts().isEmpty());
    }

    @Test
    void shouldSetCreatedAtWhenPrePersistIsCalled() {
        assertNull(inventory.getCreatedAt());

        inventory.prePersist();

        assertNotNull(inventory.getCreatedAt());
    }
}