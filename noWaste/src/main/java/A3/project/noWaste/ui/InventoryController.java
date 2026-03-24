package A3.project.noWaste.ui;

import A3.project.noWaste.domain.Inventory;
import A3.project.noWaste.dto.InventoryDTO;
import A3.project.noWaste.service.InventoryService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import jakarta.validation.Valid;
import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@CrossOrigin("*")
@RequestMapping("/inventories")
public class InventoryController {

    @Autowired
    private InventoryService service;

    @Autowired
    private ModelMapper mapper;


    // Buscar inventário por ID
    @GetMapping("/{id}")
    public ResponseEntity<InventoryDTO> findById(@PathVariable Integer id) {
        Inventory inventory = service.findById(id);
        return ResponseEntity.ok().body(mapper.map(inventory, InventoryDTO.class));
    }

    // Listar todos os inventários
    @GetMapping
    public ResponseEntity<List<InventoryDTO>> findAll() {
        List<Inventory> list = service.findAll();
        List<InventoryDTO> listDTO = list.stream()
                .map(inv -> mapper.map(inv, InventoryDTO.class))
                .collect(Collectors.toList());
        return ResponseEntity.ok().body(listDTO);
    }

    // Criar inventário
    @PostMapping
    public ResponseEntity<InventoryDTO> create(@Valid @RequestBody InventoryDTO obj) {
        Inventory newInv = service.create(obj);
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}").buildAndExpand(newInv.getId()).toUri();
        return ResponseEntity.created(uri).build();
    }

    // Atualizar inventário
    @PutMapping("/{id}")
    public ResponseEntity<InventoryDTO> update(@PathVariable Integer id, @Valid @RequestBody InventoryDTO obj) {
        obj.setId(id);
        Inventory updated = service.update(obj);
        return ResponseEntity.ok().body(mapper.map(updated, InventoryDTO.class));
    }

    // Deletar inventário
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
