package A3.project.noWaste.service.impl;

import A3.project.noWaste.domain.Batch;
import A3.project.noWaste.dto.ExpirationAlertDTO;
import A3.project.noWaste.infra.BatchRepository;
import A3.project.noWaste.service.ExpirationAlertService;
import A3.project.noWaste.service.VerificationService;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ExpirationAlertImpl implements ExpirationAlertService {

    private final BatchRepository batchRepository;
    private final VerificationService verificationService;

    public ExpirationAlertImpl(BatchRepository batchRepository, VerificationService verificationService) {
        this.batchRepository = batchRepository;
        this.verificationService = verificationService;
    }

    // lotes do usuario que vencem no mes atual
    @Override
    public List<ExpirationAlertDTO> findBatchesExpiringThisMonth() {
        Integer userId = verificationService.getUserId();
        LocalDate today = LocalDate.now();
        int currentMonth = today.getMonthValue();
        int currentYear = today.getYear();

        return batchRepository.findAll().stream()
                .filter(batch -> belongsToUser(batch, userId))
                .filter(batch -> batch.getExpirationDate() != null)
                .filter(batch -> batch.getExpirationDate().getMonthValue() == currentMonth
                        && batch.getExpirationDate().getYear() == currentYear)
                .sorted(Comparator.comparing(Batch::getExpirationDate))
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    // lotes do usuario que ja venceram
    @Override
    public List<ExpirationAlertDTO> findExpiredBatches() {
        Integer userId = verificationService.getUserId();

        return batchRepository.findAll().stream()
                .filter(batch -> belongsToUser(batch, userId))
                .filter(batch -> {
                    Long days = batch.getDaysToExpire();
                    return days != null && days < 0;
                })
                .sorted(Comparator.comparing(Batch::getExpirationDate))
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    // metodo auxiliar
    private boolean belongsToUser(Batch batch, Integer userId) {
        return batch.getProduct() != null
                && batch.getProduct().getInventory() != null
                && batch.getProduct().getInventory().getUser() != null
                && batch.getProduct().getInventory().getUser().getId().equals(userId);
    }

    // metodo para gerar o DTO de alerta
    private ExpirationAlertDTO toDTO(Batch batch) {
        ExpirationAlertDTO dto = new ExpirationAlertDTO();

        dto.setInventoryId(batch.getProduct().getInventory().getId());
        dto.setInventoryName(batch.getProduct().getInventory().getName());

        dto.setProductId(batch.getProduct().getId());
        dto.setProductName(batch.getProduct().getName());

        dto.setBatchId(batch.getId());
        dto.setBatchCode(batch.getCode());

        dto.setQuantity(batch.getQuantity());
        dto.setTotalWeight(batch.getTotalWeight());

        dto.setExpirationDate(batch.getExpirationDate());
        dto.setDaysToExpire(batch.getDaysToExpire());
        dto.setStatus(batch.getStatus());

        return dto;
    }
}
