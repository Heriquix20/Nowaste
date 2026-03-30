package A3.project.noWaste.infra;

import A3.project.noWaste.domain.Batch;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface BatchRepository extends JpaRepository<Batch, Integer> {

    Optional<Batch> findBatchById(Integer id);
}
