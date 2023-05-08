package org.acme.service;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.Response;
import org.acme.model.Todo;

import java.util.List;

@ApplicationScoped
public class TodoServiceImp implements TodoService {

    @Inject
    EntityManager entityManager;

    @Override
    public Response getTodos() {
        return Response.ok(entityManager.createNamedQuery("Todos.findAll", Todo.class)
                .getResultList()).build();
    }

    @Override
    public Response getTodoById(Integer id) {
        Todo entity = entityManager.find(Todo.class, id);
        if (entity == null) {
            throw new WebApplicationException("Todo with id of " + id + " does not exist.", 404);
        }
        return Response.ok(entity).build();
    }

    @Override
    public Response createTodo(Todo todo) {
        if (todo.getId() != null) {
            throw new WebApplicationException("Id was invalidly set on request.", 422);
        }

        entityManager.persist(todo);
        return Response.ok(todo).status(201).build();
    }

    @Override
    public Response updateTodo(Todo todo, Integer id) {
        if (todo.getName() == null) {
            throw new WebApplicationException("Fruit Name was not set on request.", 422);
        }

        Todo entity = entityManager.find(Todo.class, id);

        if (entity == null) {
            throw new WebApplicationException("Fruit with id of " + id + " does not exist.", 404);
        }

        entity.setName(todo.getName());

        return Response.ok(entity).status(201).build();
    }

    @Override
    public Response deleteTodo(Integer id) {
        Todo entity = entityManager.getReference(Todo.class, id);
        if (entity == null) {
            throw new WebApplicationException("Todo with id of " + id + " does not exist.", 404);
        }
        entityManager.remove(entity);
        return Response.status(204).build();
    }
}
