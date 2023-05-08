package org.acme.service;

import jakarta.ws.rs.core.Response;
import org.acme.model.Todo;

import java.util.List;

public interface TodoService {
    List<Todo> getTodos();

    Todo getTodoById(Integer id);

    Response createTodo(Todo todo);

    Response updateTodo(Todo todo,Integer id);

    Response deleteTodo(Integer id);
}
