package org.acme.service;

import org.acme.model.User;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.Response;
import org.acme.model.Todo;

import java.util.List;

@ApplicationScoped
public class UserServiceImp implements UserService{

    @Inject
    EntityManager entityManager;

    @Override
    public List<User> getUsers() {
        return entityManager.createNamedQuery("Users.findAll", User.class).getResultList();
    }

    @Override
    public User getUserById(Integer id) {
        User entity = entityManager.find(User.class, id);
        if (entity == null) {
            throw new WebApplicationException("User with id of " + id + " does not exist.", 404);
        }
        return entity;
    }

    @Override
    public String createUser(User user) {
        if (user.getUserId() != null) {
            throw new WebApplicationException("Id was invalidly set on request.", 422);
        }

        entityManager.persist(user);
        return "User created.";
    }

    @Override
    public String updateUser(User user, Integer id) {
        if (user.getUserName() == null) {
            throw new WebApplicationException("Fruit Name was not set on request.", 422);
        }

        User entity = entityManager.find(User.class, id);

        if (entity == null) {
            throw new WebApplicationException("Fruit with id of " + id + " does not exist.", 404);
        }

        entity.setUserName(user.getUserName());

        return entity + "updated";
    }

    @Override
    public String deleteUser(Integer id) {
        User entity = entityManager.getReference(User.class, id);
        if (entity == null) {
            throw new WebApplicationException("Todo with id of " + id + " does not exist.", 404);
        }
        entityManager.remove(entity);
        return entity + "removed";
    }
}
