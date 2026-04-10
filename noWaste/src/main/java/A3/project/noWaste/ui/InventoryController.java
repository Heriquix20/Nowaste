package A3.project.noWaste.ui;

import A3.project.noWaste.domain.Inventory;
import A3.project.noWaste.dto.InventoryDTO;
import A3.project.noWaste.service.InventoryService;
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
@RequestMapping("/inventories")
public class InventoryController {

    private final InventoryService service;
    private final ModelMapper mapper;

    public InventoryController(InventoryService service, ModelMapper mapper) {
        this.service = service;
        this.mapper = mapper;
    }

    // inventario especifico
    @GetMapping("/{id}")
    public ResponseEntity<InventoryDTO> findById(@PathVariable Integer id) {
        Inventory inventory = service.findById(id);
        return ResponseEntity.ok(mapper.map(inventory, InventoryDTO.class));
    }

    // listar inventarios e filtrar por nome e ordenar por data de criacao
    @GetMapping
    public ResponseEntity<List<InventoryDTO>> findAll(@RequestParam(required = false) String name,
                                                      @RequestParam(defaultValue = "desc") String sort) {
        List<Inventory> list = service.findAll(name, sort);
        List<InventoryDTO> listDTO = list.stream()
                .map(inv -> mapper.map(inv, InventoryDTO.class))
                .collect(Collectors.toList());

        return ResponseEntity.ok().body(listDTO);
    }

    // criar inventario
    @PostMapping
    public ResponseEntity<InventoryDTO> create(@Valid @RequestBody InventoryDTO obj) {
        Inventory newInv = service.create(obj);
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(newInv.getId())
                .toUri();
        return ResponseEntity.created(uri).body(mapper.map(newInv, InventoryDTO.class));
    }

    // atualizar inventario
    @PutMapping("/{id}")
    public ResponseEntity<InventoryDTO> update(@PathVariable Integer id, @Valid @RequestBody InventoryDTO obj) {
        Inventory updated = service.update(id, obj);
        return ResponseEntity.ok(mapper.map(updated, InventoryDTO.class));
    }

    // deletar inventario
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
