package org.acme.service;

import org.acme.model.User;
import jakarta.ws.rs.core.Response;
import org.acme.model.Todo;

import java.util.List;

public interface UserService {
    Response getUsers();

    Response getUserById(Integer id);

    Response createUser(User user);

    Response updateUser(User user, Integer id);

    Response deleteUser(Integer id);
}
