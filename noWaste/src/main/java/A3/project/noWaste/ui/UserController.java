package A3.project.noWaste.ui;

import A3.project.noWaste.domain.User;
import A3.project.noWaste.domain.dto.UserDTO;
import A3.project.noWaste.service.exceptions.UserNotFoundException;
import A3.project.noWaste.service.UserService;
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
@RequestMapping("/users")
public class UserController {

    @Autowired
    UserService service;

    @Autowired
    private ModelMapper mapper;


    // find a user
    @GetMapping(value = "/{Id}")
    public ResponseEntity<UserDTO> findById(@PathVariable Integer Id){
        return ResponseEntity.ok().body(mapper.map(service.findById(Id), UserDTO.class));
    }

    // get users
    @GetMapping
    public ResponseEntity<List<UserDTO>> findAll() {
        List<User> list = service.findAll();
        List<UserDTO> listDTO = list.stream().map(x -> mapper.map(x, UserDTO.class))
                .collect(Collectors.toList());
        return ResponseEntity.ok().body(listDTO);
    }

    // add user
    @PostMapping
    public ResponseEntity<UserDTO> create(@RequestBody UserDTO obj) {
        User newObj = service.create(obj);
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
                .buildAndExpand(newObj.getId()).toUri();
        return ResponseEntity.created(uri).build();
    }

    // update user
    @PutMapping(value = "/{Id}")
    public ResponseEntity<UserDTO> update(@PathVariable Integer Id, @RequestBody UserDTO obj) {
        obj.setId(Id);
        User newObj = service.update(obj);
        return ResponseEntity.ok().body(mapper.map(newObj, UserDTO.class));
    }


    // delete user
    @DeleteMapping(value = "/{Id}")
    public ResponseEntity delete(@PathVariable Integer Id) {
        service.delete(Id);
        return ResponseEntity.noContent().build();
    }
}
