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
import org.acme.service.TodoService;
import org.jboss.logging.Logger;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

@Path("/todos/")
@ApplicationScoped
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class TodoRestController {
    private static final Logger LOGGER = Logger.getLogger(TodoRestController.class.getName());

    @Inject
    TodoService  todoService;

    @GET
    @Path("get")
    public List<Todo> get() {
        return todoService.getTodos();
    }

    @GET
    @Path("{id}")
    public Todo getSingle(Integer id) {
        return todoService.getTodoById(id);
    }

    @POST
    @Transactional
    public Response create(Todo todo) {
        return todoService.createTodo(todo);
    }

    @PUT
    @Path("{id}")
    @Transactional
    public Response update(Integer id, Todo todo) {
        return todoService.updateTodo(todo,id);
    }

    @DELETE
    @Path("{id}")
    @Transactional
    public Response delete(Integer id) {
        return todoService.deleteTodo(id);
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
