package A3.project.noWaste.service.impl;

import A3.project.noWaste.domain.Batch;
import A3.project.noWaste.dto.ExpirationAlertDTO;
import A3.project.noWaste.infra.BatchRepository;
import A3.project.noWaste.service.ExpirationAlertService;
import A3.project.noWaste.service.VerificationService;
import org.springframework.stereotype.Service;

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

    // listar todos os lotes do usuario que estão para expirar em até 7 dias,
    // Ordenados por data de validade(mais proximo ao mais distante de vencer)
    @Override
    public List<ExpirationAlertDTO> findExpiringBatches() {
        Integer userId = verificationService.getUserId();

        return batchRepository.findAll().stream()
                .filter(batch -> batch.getProduct() != null)
                .filter(batch -> batch.getProduct().getInventory() != null)
                .filter(batch -> batch.getProduct().getInventory().getUser() != null)
                .filter(batch -> batch.getProduct().getInventory().getUser().getId().equals(userId))
                .filter(batch -> {
                    Long days = batch.getDaysToExpire();
                    return days != null && days <= 7;
                })
                .sorted(Comparator
                        .comparing(Batch::getDaysToExpire)
                        .thenComparing(Batch::getExpirationDate))
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    // criar o DTO para retornar ao usuario
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
