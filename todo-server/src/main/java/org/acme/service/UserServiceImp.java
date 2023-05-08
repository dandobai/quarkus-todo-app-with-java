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
        return entityManager.createNamedQuery("User.findAll", User.class)
                .getResultList();
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
    public Response createUser(User user) {
        if (user.getUserId() != null) {
            throw new WebApplicationException("Id was invalidly set on request.", 422);
        }

        entityManager.persist(user);
        return Response.ok(user).status(201).build();
    }

    @Override
    public Response updateUser(User user, Integer id) {
        if (user.getUserName() == null) {
            throw new WebApplicationException("Fruit Name was not set on request.", 422);
        }

        User entity = entityManager.find(User.class, id);

        if (entity == null) {
            throw new WebApplicationException("Fruit with id of " + id + " does not exist.", 404);
        }

        entity.setUserName(user.getUserName());

        return Response.ok(entity).status(201).build();
    }

    @Override
    public Response deleteUser(Integer id) {
        User entity = entityManager.getReference(User.class, id);
        if (entity == null) {
            throw new WebApplicationException("Todo with id of " + id + " does not exist.", 404);
        }
        entityManager.remove(entity);
        return Response.status(204).build();
    }
}
