package org.acme.service;

import jakarta.ws.rs.core.Response;
import org.acme.model.Todo;

import java.util.List;

public interface TodoService {
    List<Todo> getTodos();

    Todo getTodoById(Integer id);

    String createTodo(Todo todo);

    String updateTodo(Todo todo,Integer id);

    String deleteTodo(Integer id);
}
