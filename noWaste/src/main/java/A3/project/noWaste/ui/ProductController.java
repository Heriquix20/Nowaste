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

    @GetMapping("/inventories/{inventoryId}/products")
    public ResponseEntity<List<ProductDTO>> findAllByInventory(@PathVariable Integer inventoryId) {
        List<Product> list = service.findAllByInventory(inventoryId);
        List<ProductDTO> listDTO = list.stream()
                .map(product -> mapper.map(product, ProductDTO.class))
                .collect(Collectors.toList());

        return ResponseEntity.ok(listDTO);
    }

    @GetMapping("/products/{id}")
    public ResponseEntity<ProductDTO> findById(@PathVariable Integer id) {
        Product product = service.findById(id);
        return ResponseEntity.ok(mapper.map(product, ProductDTO.class));
    }

    @PostMapping("/inventories/{inventoryId}/products")
    public ResponseEntity<ProductDTO> create(
            @PathVariable Integer inventoryId,
            @Valid @RequestBody ProductDTO obj
    ) {
        Product newProduct = service.create(inventoryId, obj);

        URI uri = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(newProduct.getId())
                .toUri();

        return ResponseEntity.created(uri).body(mapper.map(newProduct, ProductDTO.class));
    }

    @PutMapping("/products/{id}")
    public ResponseEntity<ProductDTO> update(@PathVariable Integer id, @Valid @RequestBody ProductDTO obj) {
        Product updated = service.update(id, obj);
        return ResponseEntity.ok(mapper.map(updated, ProductDTO.class));
    }

    @DeleteMapping("/products/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
