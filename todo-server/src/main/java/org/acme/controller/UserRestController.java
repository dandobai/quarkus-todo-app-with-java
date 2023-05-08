package org.acme.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.acme.model.User;
import com.fasterxml.jackson.databind.node.ObjectNode;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;
import org.acme.model.Todo;
import org.acme.service.UserService;
import org.jboss.logging.Logger;

import java.util.List;

@Path("/todos/")
public class UserRestController {
    private static final Logger LOGGER = Logger.getLogger(TodoRestController.class.getName());

    @Inject
    UserService userService;

    @GET
    @Path("get")
    public List<User> get() {
        return userService.getUsers();
    }

    @GET
    @Path("{id}")
    public User getSingle(Integer id) {
        return userService.getUserById(id);
    }

    @POST
    @Transactional
    public Response create(User user) {
        return userService.createUser(user);
    }

    @PUT
    @Path("{id}")
    @Transactional
    public Response update(Integer id, User user) {
        return userService.updateUser(user,id);
    }

    @DELETE
    @Path("{id}")
    @Transactional
    public Response delete(Integer id) {
        return userService.deleteUser(id);
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
