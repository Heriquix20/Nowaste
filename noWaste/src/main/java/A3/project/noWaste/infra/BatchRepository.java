package A3.project.noWaste.infra;

import A3.project.noWaste.domain.Batch;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;


public interface BatchRepository extends JpaRepository<Batch, Integer> {

    List<Batch> findByProductId(Integer productId);

    Optional<Batch> findByIdAndProductId(Integer batchId, Integer productId);

    boolean existsByCodeIgnoreCaseAndProductId(String code, Integer productId);
}
