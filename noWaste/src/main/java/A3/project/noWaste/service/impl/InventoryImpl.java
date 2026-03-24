package A3.project.noWaste.service.impl;

import A3.project.noWaste.domain.Inventory;
import A3.project.noWaste.dto.InventoryDTO;
import A3.project.noWaste.infra.InventoryRepository;
import A3.project.noWaste.infra.UserRepository;
import A3.project.noWaste.service.InventoryService;
import A3.project.noWaste.exceptions.DataIntegratyViolationException;
import A3.project.noWaste.exceptions.ObjectNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.modelmapper.ModelMapper;

import java.util.List;
import java.util.Optional;

@Service
public class InventoryImpl implements InventoryService {

    @Autowired
    private InventoryRepository repository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ModelMapper mapper;


    // Buscar inventário por ID
    @Override
    public Inventory findById(Integer id) {
        Optional<Inventory> obj = repository.findById(id);
        return obj.orElseThrow(() -> new ObjectNotFoundException("Inventário não encontrado"));
    }

    // Listar todos os inventários
    @Override
    public List<Inventory> findAll() {
        return repository.findAll();
    }

    // Criar inventário
    @Override
    public Inventory create(InventoryDTO obj) {
        checkUserExists(obj.getUserId());
        checkInventoryName(obj);
        Inventory inventory = mapper.map(obj, Inventory.class);
        // Setar o usuário completo
        inventory.setUser(userRepository.findById(obj.getUserId()).get());
        return repository.save(inventory);
    }

    // Atualizar inventário
    @Override
    public Inventory update(InventoryDTO obj) {
        checkUserExists(obj.getUserId());
        checkInventoryName(obj);
        Inventory inventory = mapper.map(obj, Inventory.class);
        inventory.setUser(userRepository.findById(obj.getUserId()).get());
        return repository.save(inventory);
    }

    // Deletar inventário
    @Override
    public void delete(Integer id) {
        Inventory inventory = findById(id);
        repository.delete(inventory);
    }

    // Verifica se o usuário existe
    private void checkUserExists(Integer userId) {
        if (!userRepository.existsById(userId)) {
            throw new ObjectNotFoundException("Usuário não encontrado");
        }
    }

    // Verifica duplicidade de nome do inventário para o mesmo usuário
    private void checkInventoryName(InventoryDTO obj) {
        Optional<Inventory> inv = repository.findAll().stream()
                .filter(i -> i.getName().equalsIgnoreCase(obj.getName())
                        && i.getUser().getId().equals(obj.getUserId())
                        && !i.getId().equals(obj.getId()))
                .findFirst();
        if (inv.isPresent()) {
            throw new DataIntegratyViolationException("Inventário com esse nome já existe para o usuário");
        }
    }
}
