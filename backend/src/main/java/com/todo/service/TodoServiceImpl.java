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
    public Todo getTodoById(Long id) {
        return todoRepository.findById(id).orElse(null); // ID로 Todo 검색, 없으면 null 반환
    }

    @Override
    public Todo addTodo(Todo todo) {
        return todoRepository.save(todo);
    }

    @Override
    public void deleteTodo(Long id) {
        todoRepository.deleteById(id);
    }
    @Override
    public Todo updateTodo(Todo todo) { // 수정 기능 구현
        if (!todoRepository.existsById(todo.getId())) {
            throw new IllegalArgumentException("Todo 항목이 존재하지 않습니다.");
        }
        return todoRepository.save(todo); // 수정된 Todo 반환
    }
}
