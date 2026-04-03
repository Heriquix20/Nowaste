package A3.project.noWaste.service;

import A3.project.noWaste.dto.ExpirationAlertDTO;

import java.util.List;

public interface ExpirationAlertService {

    List<ExpirationAlertDTO> findExpiringBatches();
}
