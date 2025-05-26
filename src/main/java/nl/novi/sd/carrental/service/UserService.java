package nl.novi.sd.carrental.service;

import nl.novi.sd.carrental.model.User;

import java.util.List;

public interface UserService {

    List<User> getAllUsers();

    User getUser(Long id);

    User getUserByUsername(String username);

    User createUser(User user);

    User updateUser(User updatedUser);

    void deleteUser(Long id);
}
