package org.acme.controller;

import java.util.List;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;

import org.acme.model.Todo;
import org.jboss.logging.Logger;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

@Path("/")
public class TodoRestController {
    private static final Logger LOGGER = Logger.getLogger(TodoRestController.class.getName());

    @Inject
    EntityManager entityManager;

    @GET
    public List<Todo> get() {
        return entityManager.createNamedQuery("Todo.findAll", Todo.class)
                .getResultList();
    }

    @GET
    @Path("{id}")
    public Todo getSingle(Integer id) {
        Todo entity = entityManager.find(Todo.class, id);
        if (entity == null) {
            throw new WebApplicationException("Todo with id of " + id + " does not exist.", 404);
        }
        return entity;
    }

    @POST
    @Transactional
    public Response create(Todo todo) {
        if (todo.getId() != null) {
            throw new WebApplicationException("Id was invalidly set on request.", 422);
        }

        entityManager.persist(todo);
        return Response.ok(todo).status(201).build();
    }

    @PUT
    @Path("{id}")
    @Transactional
    public Todo update(Integer id, Todo todo) {
        if (todo.getName() == null) {
            throw new WebApplicationException("Fruit Name was not set on request.", 422);
        }

        Todo entity = entityManager.find(Todo.class, id);

        if (entity == null) {
            throw new WebApplicationException("Fruit with id of " + id + " does not exist.", 404);
        }

        entity.setName(todo.getName());

        return entity;
    }

    @DELETE
    @Path("{id}")
    @Transactional
    public Response delete(Integer id) {
        Todo entity = entityManager.getReference(Todo.class, id);
        if (entity == null) {
            throw new WebApplicationException("Todo with id of " + id + " does not exist.", 404);
        }
        entityManager.remove(entity);
        return Response.status(204).build();
    }
    
    @GET
    @Path("/bello")
    @Produces(MediaType.APPLICATION_JSON)
    public String hello() {
        return "Hello from RESTEasy Reactive";
    }


    @Provider
    public static class ErrorMapper implements ExceptionMapper<Exception> {


        @Inject
        ObjectMapper objectMapper;

        @Override
        public Response toResponse(Exception exception) {
            LOGGER.error("Failed to handle request", exception);

            int code = 500;
            if (exception instanceof WebApplicationException) {
                code = ((WebApplicationException) exception).getResponse().getStatus();
            }

            ObjectNode exceptionJson = objectMapper.createObjectNode();
            exceptionJson.put("exceptionType", exception.getClass().getName());
            exceptionJson.put("code", code);

            if (exception.getMessage() != null) {
                exceptionJson.put("error", exception.getMessage());
            }

            return Response.status(code)
                    .entity(exceptionJson)
                    .build();
        }

    }
}
