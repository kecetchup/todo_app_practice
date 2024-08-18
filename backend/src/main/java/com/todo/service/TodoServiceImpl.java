package com.todo.service;

import com.todo.domain.Todo;
import com.todo.domain.User;
import com.todo.repository.TodoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TodoServiceImpl implements TodoService{
    @Autowired
    private TodoRepository todoRepository;

    @Override
    public List<Todo> getTodosByUser(User user) {
        return todoRepository.findByUserId(user.getId());
    }

    @Override
    public Todo addTodo(Todo todo) {
        return todoRepository.save(todo);
    }

    @Override
    public void deleteTodo(Long id) {
        todoRepository.deleteById(id);
    }
}
