package A3.project.noWaste.service.impl;

import A3.project.noWaste.domain.User;
import A3.project.noWaste.dto.UserDTO;
import A3.project.noWaste.exceptions.ObjectNotFoundException;
import A3.project.noWaste.infra.UserRepository;
import A3.project.noWaste.service.UserService;
import A3.project.noWaste.exceptions.DataIntegratyViolationException;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;


@Service
public class UserImpl implements UserService {

    @Autowired
    private ModelMapper mapper;

    @Autowired
    private UserRepository repository;

    @Autowired
    private PasswordEncoder passwordEncoder;


    // find all Users
    @Override
    public List<User> findAll() {
        return repository.findAll();
    }


    // create User
    @Override
    public User create(UserDTO obj) {
        findByEmail(obj);

        User newUser = new User();
        newUser.setPassword(passwordEncoder.encode(obj.getPassword()));
        newUser.setUsername(obj.getUsername());
        newUser.setEmail(obj.getEmail());
        newUser.setId(obj.getId());

        return repository.save(newUser);
    }

    // update user
    @Override
    public User update(UserDTO obj) {
        findByEmail(obj);

        User newUser = new User();
        newUser.setPassword(passwordEncoder.encode(obj.getPassword()));
        newUser.setUsername(obj.getUsername());
        newUser.setEmail(obj.getEmail());
        newUser.setId(obj.getId());

        return repository.save(mapper.map(obj, User.class));
    }

    // delete User
    @Override
    public void delete(Integer id) {
        if (!repository.findById(id).isPresent()) {
            throw new ObjectNotFoundException("Usuário não existe");
        }
        repository.deleteById(id);
    }

    // email verification
    public void findByEmail(UserDTO obj) {
        Optional<User> user = repository.findByEmail(obj.getEmail());
        if (user.isPresent() && !user.get().getId().equals(obj.getId())) {
            throw new DataIntegratyViolationException("Email já cadastrado no sistema");
        }
    }

}
