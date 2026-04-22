package A3.project.noWaste.service.impl;

import A3.project.noWaste.domain.User;
import A3.project.noWaste.dto.UserDTO;
import A3.project.noWaste.exceptions.ObjectNotFoundException;
import A3.project.noWaste.infra.UserRepository;
import A3.project.noWaste.service.UserService;
import A3.project.noWaste.exceptions.DataIntegratyViolationException;
import A3.project.noWaste.service.VerificationService;
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

    @Autowired
    private VerificationService verificationService;


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
        newUser.setName(obj.getName());
        newUser.setEmail(obj.getEmail());
        newUser.setId(obj.getId());

        return repository.save(newUser);
    }

    // update user
    @Override
    public User update(UserDTO obj) {
        Integer loggedUserId = verificationService.getUserId();

        if (!loggedUserId.equals(obj.getId())) {
            throw new DataIntegratyViolationException("Acesso negado");
        }

        findByEmail(obj);

        User existingUser = repository.findById(obj.getId())
                .orElseThrow(() -> new ObjectNotFoundException("Usuario nao existe"));

        existingUser.setName(obj.getName());
        existingUser.setEmail(obj.getEmail());
        existingUser.setPassword(passwordEncoder.encode(obj.getPassword()));

        return repository.save(existingUser);
    }

    // delete User
    @Override
    public void delete(Integer id) {
        Integer loggedUserId = verificationService.getUserId();
        if (!loggedUserId.equals(id)) {
            throw new DataIntegratyViolationException("Acesso negado");
        }
        User user = repository.findById(id)
                .orElseThrow(() -> new ObjectNotFoundException("Usuario nao existe"));
        repository.delete(user);
    }

    // email verification
    public void findByEmail(UserDTO obj) {
        Optional<User> user = repository.findByEmail(obj.getEmail());
        if (user.isPresent() && !user.get().getId().equals(obj.getId())) {
            throw new DataIntegratyViolationException("Email já cadastrado no sistema");
        }
    }

}
