package org.acme.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.enterprise.context.ApplicationScoped;
import org.acme.model.User;
import com.fasterxml.jackson.databind.node.ObjectNode;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;
import org.acme.service.UserService;
import org.jboss.logging.Logger;

import java.util.List;

@Path("/users/")
@ApplicationScoped
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class UserRestController {
    private static final Logger LOGGER = Logger.getLogger(TodoRestController.class.getName());

    @Inject
    UserService userService;

    @GET
    @Path("get")
    public Response get() {
        return Response.ok(userService.getUsers()).build();
    }

    @GET
    @Path("{id}")
    public Response getSingle(Integer id) {
        return Response.ok(userService.getUserById(id)).build();
    }

    @POST
    @Transactional
    public Response create(User user) {
        return Response.ok(userService.createUser(user)).build();
    }

    @PUT
    @Path("{id}")
    @Transactional
    public Response update(Integer id, User user) {
        return Response.ok(userService.updateUser(user,id)).build();
    }

    @DELETE
    @Path("{id}")
    @Transactional
    public Response delete(Integer id) {
        return Response.ok(userService.deleteUser(id)).build();
    }

    @GET
    @Path("/hellobello")
    public Response helloBello() {
        return Response.ok("This is the User controller").build();
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
