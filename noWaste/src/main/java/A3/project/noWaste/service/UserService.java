package A3.project.noWaste.service;

import A3.project.noWaste.domain.User;

import java.util.List;

public interface UserService {

    User findById(Integer Id);
    List<User> findAll();
    User create(User user);
    boolean delete(Integer id);

}
