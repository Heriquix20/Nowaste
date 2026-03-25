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


    @Override
    public Inventory findById(Integer id) {
        Integer userId = verificationService.getUserId();
        return repository.findByIdAndUserId(id, userId)
                .orElseThrow(() -> new ObjectNotFoundException("Inventario nao encontrado"));
    }

    @Override
    public List<Inventory> findAll() {
        Integer userId = verificationService.getUserId();
        return repository.findByUserId(userId);
    }

    @Override
    public Inventory create(InventoryDTO obj) {
        User user = verificationService.verifyUser();
        checkInventoryName(obj);

        Inventory inventory = mapper.map(obj, Inventory.class);
        inventory.setId(null);
        inventory.setUser(user);

        return repository.save(inventory);
    }

    @Override
    public Inventory update(Integer id, InventoryDTO obj) {
        Integer userId = verificationService.getUserId();

        Inventory upInventory = repository.findByIdAndUserId(obj.getId(), userId)
                .orElseThrow(() -> new ObjectNotFoundException("Inventario nao encontrado"));

        checkInventoryName(obj);
        upInventory.setName(obj.getName());

        return repository.save(upInventory);
    }

    @Override
    public void delete(Integer id) {
        Integer userId = verificationService.getUserId();

        Inventory inventory = repository.findByIdAndUserId(id, userId)
                .orElseThrow(() -> new ObjectNotFoundException("Inventario nao encontrado"));

        repository.delete(inventory);
    }


    private void checkInventoryName(InventoryDTO obj) {
        Integer userId = verificationService.getUserId();
        Optional<Inventory> inv = repository.findByUserId(userId).stream()
                .filter(i -> i.getName().equalsIgnoreCase(obj.getName())
                        && !i.getId().equals(obj.getId()))
                .findFirst();
        if (inv.isPresent()) {
            throw new DataIntegratyViolationException("Inventario com esse nome ja existe para o usuario");
        }
    }
}
