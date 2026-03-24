package A3.project.noWaste.service;

import A3.project.noWaste.domain.User;
import A3.project.noWaste.dto.UserDTO;

import java.util.List;

public interface UserService {

    List<User> findAll();

    User create(UserDTO obj);

    User update(UserDTO obj);

    void delete(Integer Id);

}
