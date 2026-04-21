package A3.project.noWaste.ui;

import A3.project.noWaste.domain.Product;
import A3.project.noWaste.dto.ProductDTO;
import A3.project.noWaste.service.ProductService;
import jakarta.validation.Valid;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@CrossOrigin("*")
public class ProductController {

    private final ProductService service;
    private final ModelMapper mapper;

    public ProductController(ProductService service, ModelMapper mapper) {
        this.service = service;
        this.mapper = mapper;
    }

    // listar produtos e filtrar por nome, categoria, marca, peso e ordenação por peso
    @GetMapping("/inventories/{inventoryId}/products")
    public ResponseEntity<List<ProductDTO>> findAllByInventory(
            @PathVariable Integer inventoryId,
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String category,
            @RequestParam(required = false) String brand,
            @RequestParam(required = false) Double minWeight,
            @RequestParam(required = false) Double maxWeight,
            @RequestParam(required = false) String sortWeight) {

        List<Product> list = service.findAllByInventory(
                inventoryId,
                name,
                category,
                brand,
                minWeight,
                maxWeight,
                sortWeight
        );
        List<ProductDTO> listDTO = list.stream().map(this::toDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(listDTO);
    }

    // produto especifico
    @GetMapping("/inventories/{inventoryId}/products/{productId}")
    public ResponseEntity<ProductDTO> findById(@PathVariable Integer inventoryId,
                                               @PathVariable Integer productId) {
        Product product = service.findById(inventoryId, productId);
        return ResponseEntity.ok(toDTO(product));
    }

    // criar produto
    @PostMapping("/inventories/{inventoryId}/products")
    public ResponseEntity<ProductDTO> create(@PathVariable Integer inventoryId,
                                             @Valid @RequestBody ProductDTO obj) {
        Product newProduct = service.create(inventoryId, obj);
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(newProduct.getId())
                .toUri();
        return ResponseEntity.created(uri).body(toDTO(newProduct));
    }

    // atualizar produto
    @PutMapping("/inventories/{inventoryId}/products/{productId}")
    public ResponseEntity<ProductDTO> update(@PathVariable Integer inventoryId, @PathVariable Integer productId,
                                             @Valid @RequestBody ProductDTO obj) {
        Product updated = service.update(inventoryId, productId, obj);
        return ResponseEntity.ok(toDTO(updated));
    }

    //deletar produto
    @DeleteMapping("/inventories/{inventoryId}/products/{productId}")
    public ResponseEntity<Void> delete(@PathVariable Integer inventoryId, @PathVariable Integer productId) {
        service.delete(inventoryId, productId);
        return ResponseEntity.noContent().build();
    }

    // to DTO
    private ProductDTO toDTO(Product product) {
        ProductDTO dto = new ProductDTO();
        dto.setId(product.getId());
        dto.setName(product.getName());
        dto.setWeight(product.getWeightInGrams());
        dto.setWeightUnit("g");
        dto.setCategory(product.getCategory());
        dto.setBrand(product.getBrand());
        return dto;
    }
}
