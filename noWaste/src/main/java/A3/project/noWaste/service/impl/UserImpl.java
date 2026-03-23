package A3.project.noWaste.service.impl;

import A3.project.noWaste.domain.User;
import A3.project.noWaste.domain.dto.UserDTO;
import A3.project.noWaste.infra.UserRepository;
import A3.project.noWaste.service.UserService;
import A3.project.noWaste.service.exceptions.DataIntegratyViolationException;
import A3.project.noWaste.service.exceptions.ObjectNotFoundException;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;


@Service
public class UserImpl implements UserService {

    @Autowired
    private ModelMapper mapper;

    @Autowired
    private UserRepository repository;


    // find a user
    @Override
    public User findById(Integer Id) {
        Optional<User> obj = repository.findById(Id);
        return obj.orElseThrow(() -> new ObjectNotFoundException("Objeto não encontrado"));
    }

    // find all Users
    @Override
    public List<User> findAll() {
        return repository.findAll();
    }

    // create User
    @Override
    public User create(UserDTO obj) {
        findByEmail(obj);
        User user = mapper.map(obj, User.class);
        return repository.save(user);
    }

    // update user
    @Override
    public User update(UserDTO obj) {
        findByEmail(obj);
        return repository.save(mapper.map(obj, User.class));
    }

    // delete User
    @Override
    public void delete(Integer id) {
        findById(id);
        repository.deleteById(id);
    }

    // email verification
    public void findByEmail(UserDTO obj) {
        Optional<User> user = repository.findByEmail(obj.getEmail());
        if (user.isPresent() && user.get().getId().equals(obj.getId())) {
            throw new DataIntegratyViolationException("Email já cadastrado no sistema");
        }
    }

}
