package A3.project.noWaste.infra;

import A3.project.noWaste.domain.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<Product, Integer> {

    List<Product> findByInventoryId(Integer inventoryId);

    Optional<Product> findByIdAndInventoryId(Integer productId, Integer inventoryId);

    List<Product> findByInventoryUserId(Integer userId);
}
