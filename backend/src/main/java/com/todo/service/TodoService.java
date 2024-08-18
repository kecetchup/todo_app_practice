package com.todo.service;

import com.todo.domain.Todo;
import com.todo.domain.User;

import java.util.List;

public interface TodoService {
    List<Todo> getTodosByUser(User user);
    Todo addTodo(Todo todo);
    void deleteTodo(Long id);
}
