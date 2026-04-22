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


    // produto especifio
    @Override
    public Product findById(Integer inventoryId, Integer productId) {
        Inventory inventory = findInventoryByUser(inventoryId);
        return repository.findByIdAndInventoryId(productId, inventory.getId())
                .orElseThrow(() -> new ObjectNotFoundException("Produto nao encontrado"));
    }

    // listar produtos e filtrar por nome, categoria, marca, peso e ordenacao por peso
    @Override
    public List<Product> findAllByInventory(Integer inventoryId, String name, String category,
                                            String brand, Double minWeight,
                                            Double maxWeight, String sortWeight) {

        Inventory inventory = findInventoryByUser(inventoryId);
        List<Product> products = repository.findByInventoryId(inventory.getId());

        if (name != null && !name.isBlank()) {
            products = products.stream()
                    .filter(product -> product.getName() != null
                            && product.getName().toLowerCase().contains(name.toLowerCase()))
                    .toList();
        }
        if (category != null && !category.isBlank()) {
            products = products.stream()
                    .filter(product -> product.getCategory() != null
                            && product.getCategory().toLowerCase().contains(category.toLowerCase()))
                    .toList();
        }
        if (brand != null && !brand.isBlank()) {
            products = products.stream()
                    .filter(product -> product.getBrand() != null
                            && product.getBrand().toLowerCase().contains(brand.toLowerCase()))
                    .toList();
        }
        if (minWeight != null && maxWeight != null && minWeight > maxWeight) {
            throw new DataIntegratyViolationException("O peso mínimo nao pode ser maior que o peso máximo");
        }
        if (minWeight != null) {
            products = products.stream()
                    .filter(product -> product.getWeightInGrams() != null
                            && product.getWeightInGrams() >= minWeight)
                    .toList();
        }
        if (maxWeight != null) {
            products = products.stream()
                    .filter(product -> product.getWeightInGrams() != null
                            && product.getWeightInGrams() <= maxWeight)
                    .toList();
        }
        if ("desc".equalsIgnoreCase(sortWeight)) {
            products = products.stream()
                    .sorted((p1, p2) -> p2.getWeightInGrams().compareTo(p1.getWeightInGrams()))
                    .toList();
        } else if ("asc".equalsIgnoreCase(sortWeight)) {
            products = products.stream()
                    .sorted((p1, p2) -> p1.getWeightInGrams().compareTo(p2.getWeightInGrams()))
                    .toList();
        }
        return products;
    }

    @Override
    public Product create(Integer inventoryId, ProductDTO obj) {
        Inventory inventory = findInventoryByUser(inventoryId);

        checkProductName(obj.getName(), inventory.getId(), null);

        Product product = new Product();
        product.setId(null);
        product.setName(obj.getName());
        product.setCategory(obj.getCategory());
        product.setBrand(obj.getBrand());
        product.setWeightInGrams(convertToGrams(obj.getWeight(), obj.getWeightUnit()));
        product.setInventory(inventory);

        return repository.save(product);
    }

    @Override
    public Product update(Integer inventoryId, Integer productId, ProductDTO obj) {
        Product product = findById(inventoryId, productId);

        checkProductName(obj.getName(), inventoryId, product.getId());

        product.setName(obj.getName());
        product.setCategory(obj.getCategory());
        product.setBrand(obj.getBrand());
        product.setWeightInGrams(convertToGrams(obj.getWeight(), obj.getWeightUnit()));

        return repository.save(product);
    }

    // deletar produto
    @Override
    public void delete(Integer inventoryId, Integer productId) {
        Product product = findById(inventoryId, productId);
        repository.delete(product);
    }


    // metodos de verificacao
    // encontrar inventario a partir de um usuario
    private Inventory findInventoryByUser(Integer inventoryId) {
        Integer userId = verificationService.getUserId();

        return inventoryRepository.findByIdAndUserId(inventoryId, userId)
                .orElseThrow(() -> new ObjectNotFoundException("Inventario nao encontrado"));
    }

    // checar nomes repetidos
    private void checkProductName(String name, Integer inventoryId, Integer productId) {
        List<Product> products = repository.findByInventoryId(inventoryId);

        boolean exists = products.stream()
                .anyMatch(product -> product.getName().equalsIgnoreCase(name)
                        && (productId == null || !product.getId().equals(productId)));

        if (exists) {
            throw new DataIntegratyViolationException("Produto com esse nome ja existe no inventario");
        }
    }

    private Double convertToGrams(Double weight, String unit) {
        if ("kg".equalsIgnoreCase(unit)) {
            return weight * 1000;
        }
        return weight;
    }
}
