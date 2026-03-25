package A3.project.noWaste.infra;

import A3.project.noWaste.domain.Inventory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface InventoryRepository extends JpaRepository<Inventory, Integer> {

    List<Inventory> findByUserId(Integer userId);

    Optional<Inventory> findByIdAndUserId(Integer inventoryId, Integer userId);


}
