package org.acme.service;

import org.acme.model.User;
import jakarta.ws.rs.core.Response;
import org.acme.model.Todo;

import java.util.List;

public interface UserService {
    List<User> getUsers();

    User getUserById(Integer id);

    String createUser(User user);

    String updateUser(User user, Integer id);

    String deleteUser(Integer id);
}
