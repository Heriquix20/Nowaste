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


    // lote especifico
    @Override
    public Batch findById(Integer inventoryId, Integer productId, Integer batchId) {
        Product product = findProductByInventory(inventoryId, productId);
        return repository.findByIdAndProductId(batchId, product.getId())
                .orElseThrow(() -> new ObjectNotFoundException("Lote não encontrado"));
    }

    // listar lotes e filtrar por codigo, status, data de validade, quantidade e ordenação por data de validade
    @Override
    public List<Batch> findAllByProduct(Integer inventoryId, Integer productId, String code, String status,
                                        LocalDate expirationFrom, LocalDate expirationTo, Integer minQuantity,
                                        Integer maxQuantity, String sortExpiration) {

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
        if (minQuantity != null) {
            batches = batches.stream()
                    .filter(batch -> batch.getQuantity() != null
                            && batch.getQuantity() >= minQuantity)
                    .toList();
        }
        if (maxQuantity != null) {
            batches = batches.stream()
                    .filter(batch -> batch.getQuantity() != null
                            && batch.getQuantity() <= maxQuantity)
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

    // criar um lote
    @Override
    public Batch create(Integer inventoryId, Integer productId, BatchDTO obj) {
        Product product = findProductByInventory(inventoryId, productId);

        Batch batch = new Batch();
        batch.setId(null);
        batch.setCode(generateBatchCode(product));
        batch.setQuantity(obj.getQuantity());
        batch.setExpirationDate(obj.getExpirationDate());
        batch.setProduct(product);

        return repository.save(batch);
    }

    // atualizar um lote
    @Override
    public Batch update(Integer inventoryId, Integer productId, Integer batchId, BatchDTO obj) {
        Batch batch = findById(inventoryId, productId, batchId);

        batch.setQuantity(obj.getQuantity());
        batch.setExpirationDate(obj.getExpirationDate());

        return repository.save(batch);
    }

    // deletar um lote
    @Override
    public void delete(Integer inventoryId, Integer productId, Integer batchId) {
        Batch batch = findById(inventoryId, productId, batchId);
        repository.delete(batch);
    }

    // metodos auxiliares
    // encontrar produto pelo inventario
    private Product findProductByInventory(Integer inventoryId, Integer productId) {
        Integer userId = verificationService.getUserId();

        Product product = productRepository.findByIdAndInventoryId(productId, inventoryId)
                .orElseThrow(() -> new ObjectNotFoundException("Produto não encontrado"));

        if (!product.getInventory().getUser().getId().equals(userId)) {
            throw new DataIntegratyViolationException("Acesso negado");
        }
        return product;
    }

    // metodos auxiliares
    // gerar codigo de lote
    private String generateBatchCode(Product product) {
        List<Batch> batches = repository.findByProductId(product.getId());

        int nextSequence = batches.stream()
                .map(Batch::getCode)
                .map(this::extractSequenceNumber)
                .max(Integer::compareTo)
                .orElse(0) + 1;

        String normalizedProductName = normalizeProductName(product.getName());

        return "LT-" + normalizedProductName + "-" + String.format("%03d", nextSequence);
    }

    // tratar o nome do produto para virar codigo
    private String normalizeProductName(String productName) {
        return productName == null
                ? "PRODUTO"
                : productName.trim()
                  .toUpperCase()
                  .replaceAll("[^A-Z0-9]+", "_")
                  .replaceAll("^_+|_+$", "");
    }

    // extrair o ultimo numero da sequencia de codigos
    private Integer extractSequenceNumber(String code) {
        if (code == null || code.isBlank()) {
            return 0;
        }
        String[] parts = code.split("-");
        String lastPart = parts[parts.length - 1];
        try {
            return Integer.parseInt(lastPart);
        } catch (NumberFormatException ex) {
            return 0;
        }
    }
}
