package A3.project.noWaste.service;


import A3.project.noWaste.domain.Batch;
import A3.project.noWaste.dto.BatchDTO;

import java.time.LocalDate;
import java.util.List;

public interface BatchService {

    Batch findById(Integer inventoryId, Integer productId,Integer batchId);

    List<Batch> findAllByProduct(
            Integer inventoryId,
            Integer productId,
            String code,
            String status,
            LocalDate expirationFrom,
            LocalDate expirationTo,
            Integer minQuantity,
            Integer maxQuantity,
            String sortExpiration
    );

    Batch create(Integer inventoryId, Integer productId, BatchDTO obj);

    Batch update(Integer inventoryId, Integer productId, Integer batchId, BatchDTO obj);

    void delete(Integer inventoryId, Integer productId, Integer batchId);
}
