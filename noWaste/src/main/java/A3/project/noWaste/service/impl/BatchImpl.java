package A3.project.noWaste.service.impl;

import A3.project.noWaste.domain.Batch;
import A3.project.noWaste.domain.Product;
import A3.project.noWaste.dto.BatchDTO;
import A3.project.noWaste.exceptions.DataIntegratyViolationException;
import A3.project.noWaste.exceptions.ObjectNotFoundException;
import A3.project.noWaste.infra.BatchRepository;
import A3.project.noWaste.infra.ProductRepository;
import A3.project.noWaste.service.BatchService;
import A3.project.noWaste.service.VerificationService;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class BatchImpl implements BatchService {

    private final BatchRepository repository;
    private final ModelMapper mapper;
    private final ProductRepository productRepository;
    private final VerificationService verificationService;

    public BatchImpl(BatchRepository repository, ModelMapper mapper, ProductRepository productRepository, VerificationService verificationService) {
        this.repository = repository;
        this.mapper = mapper;
        this.productRepository = productRepository;
        this.verificationService = verificationService;
    }


    @Override
    public Batch findById(Integer inventoryId, Integer productId, Integer batchId) {
        Product product = findProductByInventory(inventoryId, productId);
        return repository.findByIdAndProductId(batchId, product.getId())
                .orElseThrow(() -> new ObjectNotFoundException("Lote não encontrado"));
    }

    @Override
    public List<Batch> findAllByProduct(Integer inventoryId, Integer productId, String code,
                                        String status, LocalDate expirationFrom,
                                        LocalDate expirationTo, String sortExpiration) {

        Product product = findProductByInventory(inventoryId, productId);
        List<Batch> batches = repository.findByProductId(product.getId());

        if (code != null && !code.isBlank()) {
            batches = batches.stream()
                    .filter(batch -> batch.getCode() != null
                            && batch.getCode().toLowerCase().contains(code.toLowerCase()))
                    .toList();
        }
        if (status != null && !status.isBlank()) {
            batches = batches.stream()
                    .filter(batch -> batch.getStatus() != null
                            && batch.getStatus().equalsIgnoreCase(status))
                    .toList();
        }
        if (expirationFrom != null) {
            batches = batches.stream()
                    .filter(batch -> batch.getExpirationDate() != null
                            && !batch.getExpirationDate().isBefore(expirationFrom))
                    .toList();
        }
        if (expirationTo != null) {
            batches = batches.stream()
                    .filter(batch -> batch.getExpirationDate() != null
                            && !batch.getExpirationDate().isAfter(expirationTo))
                    .toList();
        }
        if ("desc".equalsIgnoreCase(sortExpiration)) {
            batches = batches.stream()
                    .sorted((b1, b2) -> b2.getExpirationDate().compareTo(b1.getExpirationDate()))
                    .toList();
        } else {
            batches = batches.stream()
                    .sorted((b1, b2) -> b1.getExpirationDate().compareTo(b2.getExpirationDate()))
                    .toList();
        }
        return batches;
    }

    @Override
    public Batch create(Integer inventoryId, Integer productId, BatchDTO obj) {
        Product product = findProductByInventory(inventoryId, productId);

        checkBatchCode(obj.getCode(), product.getId(), null);

        Batch batch = mapper.map(obj, Batch.class);
        batch.setId(null);
        batch.setProduct(product);

        return repository.save(batch);
    }

    @Override
    public Batch update(Integer inventoryId, Integer productId, Integer batchId, BatchDTO obj) {
        Batch batch = findById(inventoryId, productId, batchId);

        checkBatchCode(obj.getCode(), productId, batch.getId());

        batch.setCode(obj.getCode());
        batch.setQuantity(obj.getQuantity());
        batch.setExpirationDate(obj.getExpirationDate());

        return repository.save(batch);
    }

    @Override
    public void delete(Integer inventoryId, Integer productId, Integer batchId) {
        Batch batch = findById(inventoryId, productId, batchId);
        repository.delete(batch);
    }


    private Product findProductByInventory(Integer inventoryId, Integer productId) {
        Integer userId = verificationService.getUserId();

        Product product = productRepository.findByIdAndInventoryId(productId, inventoryId)
                .orElseThrow(() -> new ObjectNotFoundException("Produto não encontrado"));

        if (!product.getInventory().getUser().getId().equals(userId)) {
            throw new DataIntegratyViolationException("Acesso negado");
        }
        return product;
    }


    private void checkBatchCode(String code, Integer productId, Integer batchId) {
        List<Batch> batches = repository.findByProductId(productId);

        boolean exists = batches.stream()
                .anyMatch(batch -> batch.getCode().equalsIgnoreCase(code)
                        && (batchId == null || !batch.getId().equals(batchId)));

        if (exists) {
            throw new DataIntegratyViolationException("Lote com esse código já existe para este produto");
        }
    }

}
