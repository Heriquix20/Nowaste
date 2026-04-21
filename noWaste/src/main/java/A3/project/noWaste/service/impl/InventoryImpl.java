package A3.project.noWaste.service.impl;

import A3.project.noWaste.domain.Inventory;
import A3.project.noWaste.domain.User;
import A3.project.noWaste.dto.InventoryDTO;
import A3.project.noWaste.exceptions.DataIntegratyViolationException;
import A3.project.noWaste.exceptions.ObjectNotFoundException;
import A3.project.noWaste.infra.InventoryRepository;
import A3.project.noWaste.service.InventoryService;
import A3.project.noWaste.service.VerificationService;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class InventoryImpl implements InventoryService {

    private final InventoryRepository repository;
    private final ModelMapper mapper;
    private final VerificationService verificationService;

    public InventoryImpl(InventoryRepository repository, ModelMapper mapper, VerificationService verificationService) {
        this.repository = repository;
        this.mapper = mapper;
        this.verificationService = verificationService;
    }


    // inventario especifico
    @Override
    public Inventory findById(Integer id) {
        Integer userId = verificationService.getUserId();
        return repository.findByIdAndUserId(id, userId)
                .orElseThrow(() -> new ObjectNotFoundException("Inventário não encontrado"));
    }

    // listar inventarios e filtrar por nome e ordenar por data de criacao
    @Override
    public List<Inventory> findAll(String name, String sort) {
        Integer userId = verificationService.getUserId();
        List<Inventory> inventories;

        if (name != null && !name.isBlank()) {
            inventories = repository.findByUserIdAndNameContainingIgnoreCase(userId, name);
        } else {
            inventories = repository.findByUserId(userId);
        }

        if ("asc".equalsIgnoreCase(sort)) {
            inventories.sort((i1, i2) -> i1.getCreatedAt().compareTo(i2.getCreatedAt()));
        } else {
            inventories.sort((i1, i2) -> i2.getCreatedAt().compareTo(i1.getCreatedAt()));
        }
        return inventories;
    }

    // criar inventario
    @Override
    public Inventory create(InventoryDTO obj) {
        User user = verificationService.verifyUser();
        checkInventoryName(obj);

        Inventory inventory = mapper.map(obj, Inventory.class);
        inventory.setId(null);
        inventory.setUser(user);

        return repository.save(inventory);
    }

    // atualizar inventario
    @Override
    public Inventory update(Integer id, InventoryDTO obj) {
        Integer userId = verificationService.getUserId();
        obj.setId(id);

        Inventory upInventory = repository.findByIdAndUserId(id, userId)
                .orElseThrow(() -> new ObjectNotFoundException("Inventario nao encontrado"));

        checkInventoryName(obj);
        upInventory.setName(obj.getName());
        upInventory.setDescription(obj.getDescription());
        upInventory.setLocation(obj.getLocation());

        return repository.save(upInventory);
    }

    // deletar inventario
    @Override
    public void delete(Integer id) {
        Integer userId = verificationService.getUserId();

        Inventory inventory = repository.findByIdAndUserId(id, userId)
                .orElseThrow(() -> new ObjectNotFoundException("Inventario nao encontrado"));

        repository.delete(inventory);
    }

    // metodo auxiliar
    // checar nome de inventario
    private void checkInventoryName(InventoryDTO obj) {
        Integer userId = verificationService.getUserId();
        Optional<Inventory> inv = repository.findByUserId(userId).stream()
                .filter(i -> i.getName().equalsIgnoreCase(obj.getName())
                        && !i.getId().equals(obj.getId()))
                .findFirst();
        if (inv.isPresent()) {
            throw new DataIntegratyViolationException("Inventário com esse nome já existe para este usuário");
        }
    }
}
