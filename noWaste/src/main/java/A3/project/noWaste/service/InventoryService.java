package A3.project.noWaste.service;

import A3.project.noWaste.domain.Inventory;
import A3.project.noWaste.dto.InventoryDTO;

import java.util.List;

public interface InventoryService {

    Inventory findById(Integer id);

    List<Inventory> findAll();

    Inventory create(InventoryDTO obj);

    Inventory update(InventoryDTO obj);

    void delete(Integer id);

}