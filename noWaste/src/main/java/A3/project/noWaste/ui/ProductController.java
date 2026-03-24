package A3.project.noWaste.ui;

import A3.project.noWaste.domain.Product;
import A3.project.noWaste.dto.ProductDTO;
import A3.project.noWaste.service.ProductService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@CrossOrigin("*")
@RequestMapping("/products")
public class ProductController {

    @Autowired
    private ProductService service;

    @Autowired
    private ModelMapper mapper;


    // Buscar produto por ID
    @GetMapping("/{id}")
    public ResponseEntity<ProductDTO> findById(@PathVariable Integer id) {
        Product product = service.findById(id);
        return ResponseEntity.ok().body(mapper.map(product, ProductDTO.class));
    }

    // Listar todos os produtos
    @GetMapping
    public ResponseEntity<List<ProductDTO>> findAll() {
        List<Product> list = service.findAll();
        List<ProductDTO> listDTO = list.stream()
                .map(p -> mapper.map(p, ProductDTO.class))
                .collect(Collectors.toList());
        return ResponseEntity.ok().body(listDTO);
    }

    // Criar produto
    @PostMapping
    public ResponseEntity<ProductDTO> create(@RequestBody ProductDTO obj) {
        Product newObj = service.create(obj);
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}").buildAndExpand(newObj.getId()).toUri();
        return ResponseEntity.created(uri).build();
    }

    // Atualizar produto
    @PutMapping("/{id}")
    public ResponseEntity<ProductDTO> update(@PathVariable Integer id, @RequestBody ProductDTO obj) {
        obj.setId(id);
        Product updated = service.update(obj);
        return ResponseEntity.ok().body(mapper.map(updated, ProductDTO.class));
    }

    // Deletar produto
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}