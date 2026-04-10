package A3.project.noWaste.service;

import A3.project.noWaste.domain.Product;
import A3.project.noWaste.dto.ProductDTO;

import java.util.List;

public interface ProductService {

    Product findById(Integer inventoryId, Integer productId);

    List<Product> findAllByInventory(
            Integer inventoryId,
            String name,
            String category,
            String brand,
            Double minWeight,
            Double maxWeight
    );

    Product create(Integer inventoryId, ProductDTO obj);

    Product update(Integer inventoryId, Integer productId, ProductDTO obj);

    void delete(Integer inventoryId, Integer productId);
}

