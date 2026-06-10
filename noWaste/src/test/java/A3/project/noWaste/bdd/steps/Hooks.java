package A3.project.noWaste.bdd.steps;

import A3.project.noWaste.infra.BatchRepository;
import A3.project.noWaste.infra.InventoryRepository;
import A3.project.noWaste.infra.ProductRepository;
import A3.project.noWaste.infra.UserRepository;
import io.cucumber.java.Before;
import org.springframework.beans.factory.annotation.Autowired;

public class Hooks {

    @Autowired
    private BatchRepository batchRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private InventoryRepository inventoryRepository;

    @Autowired
    private UserRepository userRepository;

    @Before
    public void clearBank() {
        batchRepository.deleteAll();
        productRepository.deleteAll();
        inventoryRepository.deleteAll();
        userRepository.deleteAll();
    }
}