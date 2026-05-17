package A3.project.noWaste.service.impl;

import A3.project.noWaste.domain.Inventory;
import A3.project.noWaste.domain.User;
import A3.project.noWaste.dto.InventoryDTO;
import A3.project.noWaste.exceptions.DataIntegratyViolationException;
import A3.project.noWaste.exceptions.ObjectNotFoundException;
import A3.project.noWaste.infra.InventoryRepository;
import A3.project.noWaste.service.VerificationService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.modelmapper.ModelMapper;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class InventoryImplTest {

    @Mock
    private InventoryRepository repository;

    @Mock
    private ModelMapper mapper;

    @Mock
    private VerificationService verificationService;

    @InjectMocks
    private InventoryImpl service;

    @Test
    void shouldReturnInventoryByIdForAuthenticatedUser() {
        Inventory inventory = createInventory(10, "Casa", LocalDateTime.now());

        when(verificationService.getUserId()).thenReturn(1);
        when(repository.findByIdAndUserId(10, 1)).thenReturn(Optional.of(inventory));

        Inventory result = service.findById(10);

        assertSame(inventory, result);
    }

    @Test
    void shouldThrowWhenInventoryByIdDoesNotExistForUser() {
        when(verificationService.getUserId()).thenReturn(1);
        when(repository.findByIdAndUserId(10, 1)).thenReturn(Optional.empty());

        assertThrows(ObjectNotFoundException.class, () -> service.findById(10));
    }

    @Test
    void shouldFilterByNameAndSortInventoriesAscending() {
        Inventory later = createInventory(2, "Mercado Bairro", LocalDateTime.now().minusDays(1));
        Inventory earlier = createInventory(1, "Mercado Casa", LocalDateTime.now().minusDays(3));

        when(verificationService.getUserId()).thenReturn(1);
        when(repository.findByUserIdAndNameContainingIgnoreCase(1, "mercado"))
                .thenReturn(new ArrayList<>(List.of(later, earlier)));

        List<Inventory> result = service.findAll("mercado", "asc");

        assertEquals(List.of(earlier, later), result);
    }

    @Test
    void shouldSortInventoriesDescendingWhenSortIsNotAsc() {
        Inventory earlier = createInventory(1, "Cozinha", LocalDateTime.now().minusDays(5));
        Inventory later = createInventory(2, "Despensa", LocalDateTime.now().minusDays(1));

        when(verificationService.getUserId()).thenReturn(1);
        when(repository.findByUserId(1)).thenReturn(new ArrayList<>(List.of(earlier, later)));

        List<Inventory> result = service.findAll(null, null);

        assertEquals(List.of(later, earlier), result);
    }

    @Test
    void shouldCreateInventoryForAuthenticatedUser() {
        User user = new User();
        user.setId(1);

        InventoryDTO dto = new InventoryDTO();
        dto.setName("Despensa");
        dto.setDescription("Itens secos");
        dto.setLocation("Cozinha");

        Inventory mappedInventory = new Inventory();
        mappedInventory.setName(dto.getName());
        mappedInventory.setDescription(dto.getDescription());
        mappedInventory.setLocation(dto.getLocation());

        when(verificationService.verifyUser()).thenReturn(user);
        when(verificationService.getUserId()).thenReturn(1);
        when(repository.findByUserId(1)).thenReturn(List.of());
        when(mapper.map(dto, Inventory.class)).thenReturn(mappedInventory);
        when(repository.save(any(Inventory.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Inventory created = service.create(dto);

        assertEquals("Despensa", created.getName());
        assertSame(user, created.getUser());
        verify(repository).save(created);
    }

    @Test
    void shouldThrowWhenCreatingDuplicatedInventoryNameForUser() {
        InventoryDTO dto = new InventoryDTO();
        dto.setName("Despensa");

        Inventory existing = createInventory(10, "Despensa", LocalDateTime.now());

        User user = new User();
        user.setId(1);

        when(verificationService.verifyUser()).thenReturn(user);
        when(verificationService.getUserId()).thenReturn(1);
        when(repository.findByUserId(1)).thenReturn(List.of(existing));

        assertThrows(DataIntegratyViolationException.class, () -> service.create(dto));
    }

    @Test
    void shouldUpdateInventoryFieldsWhenOwnedByUser() {
        Inventory existing = createInventory(10, "Casa", LocalDateTime.now());
        existing.setDescription("Antiga");
        existing.setLocation("Quintal");

        InventoryDTO dto = new InventoryDTO();
        dto.setName("Casa Nova");
        dto.setDescription("Nova descricao");
        dto.setLocation("Cozinha");

        when(verificationService.getUserId()).thenReturn(1);
        when(repository.findByIdAndUserId(10, 1)).thenReturn(Optional.of(existing));
        when(repository.findByUserId(1)).thenReturn(List.of(existing));
        when(repository.save(existing)).thenReturn(existing);

        Inventory updated = service.update(10, dto);

        assertEquals("Casa Nova", updated.getName());
        assertEquals("Nova descricao", updated.getDescription());
        assertEquals("Cozinha", updated.getLocation());
    }

    @Test
    void shouldDeleteInventoryOwnedByUser() {
        Inventory inventory = createInventory(10, "Casa", LocalDateTime.now());

        when(verificationService.getUserId()).thenReturn(1);
        when(repository.findByIdAndUserId(10, 1)).thenReturn(Optional.of(inventory));

        service.delete(10);

        verify(repository).delete(inventory);
    }

    @Test
    void shouldThrowWhenDeletingInventoryThatDoesNotExistForUser() {
        when(verificationService.getUserId()).thenReturn(1);
        when(repository.findByIdAndUserId(10, 1)).thenReturn(Optional.empty());

        assertThrows(ObjectNotFoundException.class, () -> service.delete(10));
    }

    private Inventory createInventory(Integer id, String name, LocalDateTime createdAt) {
        Inventory inventory = new Inventory();
        inventory.setId(id);
        inventory.setName(name);
        inventory.setCreatedAt(createdAt);
        return inventory;
    }
}
