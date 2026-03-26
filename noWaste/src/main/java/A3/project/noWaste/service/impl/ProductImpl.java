package A3.project.noWaste.service.impl;

import A3.project.noWaste.domain.Inventory;
import A3.project.noWaste.domain.Product;
import A3.project.noWaste.dto.ProductDTO;
import A3.project.noWaste.exceptions.DataIntegratyViolationException;
import A3.project.noWaste.exceptions.ObjectNotFoundException;
import A3.project.noWaste.infra.InventoryRepository;
import A3.project.noWaste.infra.ProductRepository;
import A3.project.noWaste.service.ProductService;
import A3.project.noWaste.service.VerificationService;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductImpl implements ProductService {

    private final ProductRepository repository;
    private final InventoryRepository inventoryRepository;
    private final VerificationService verificationService;
    private final ModelMapper mapper;

    public ProductImpl(
            ProductRepository repository, InventoryRepository
                    inventoryRepository, VerificationService verificationService, ModelMapper mapper) {
        this.repository = repository;
        this.inventoryRepository = inventoryRepository;
        this.verificationService = verificationService;
        this.mapper = mapper;
    }


    @Override
    public Product findById(Integer inventoryId, Integer productId) {
        Inventory inventory = findInventoryByUser(inventoryId);
        return repository.findByIdAndInventoryId(productId, inventory.getId())
                .orElseThrow(() -> new ObjectNotFoundException("Produto nao encontrado"));
    }

    @Override
    public List<Product> findAllByInventory(Integer inventoryId) {
        Inventory inventory = findInventoryByUser(inventoryId);
        return repository.findByInventoryId(inventory.getId());
    }

    @Override
    public Product create(Integer inventoryId, ProductDTO obj) {
        Inventory inventory = findInventoryByUser(inventoryId);

        checkProductName(obj.getName(), inventory.getId(), null);

        Product product = mapper.map(obj, Product.class);
        product.setId(null);
        product.setInventory(inventory);

        return repository.save(product);
    }

    @Override
    public Product update(Integer inventoryId, Integer productId, ProductDTO obj) {
        Product product = findById(inventoryId, productId);

        checkProductName(obj.getName(), inventoryId, product.getId());

        product.setName(obj.getName());
        product.setWeight(obj.getWeight());
        product.setCategory(obj.getCategory());
        product.setBrand(obj.getBrand());

        return repository.save(product);
    }

    @Override
    public void delete(Integer inventoryId, Integer productId) {
        Product product = findById(inventoryId, productId);
        repository.delete(product);
    }




    private Inventory findInventoryByUser(Integer inventoryId) {
        Integer userId = verificationService.getUserId();

        return inventoryRepository.findByIdAndUserId(inventoryId, userId)
                .orElseThrow(() -> new ObjectNotFoundException("Inventario nao encontrado"));
    }

    private void checkProductName(String name, Integer inventoryId, Integer productId) {
        List<Product> products = repository.findByInventoryId(inventoryId);

        boolean exists = products.stream()
                .anyMatch(product -> product.getName().equalsIgnoreCase(name)
                        && (productId == null || !product.getId().equals(productId)));

        if (exists) {
            throw new DataIntegratyViolationException("Produto com esse nome ja existe no inventario");
        }
    }
}
