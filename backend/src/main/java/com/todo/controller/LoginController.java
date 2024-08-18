package com.todo.controller;

import com.todo.config.security.CustomUserDetails;
import com.todo.domain.Todo;
import com.todo.domain.User;
import com.todo.dto.LoginRequestDTO;
import com.todo.dto.RegisterRequestDTO;
import com.todo.dto.TodoRequestDTO;
import com.todo.service.LoginService;
import com.todo.service.RegisterService;
import com.todo.service.TodoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class LoginController {

    @Autowired
    private LoginService loginService;

    @Autowired
    private RegisterService registerService;

    @Autowired
    private TodoService todoService;

    @CrossOrigin(origins = "http://localhost:3000")
    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody LoginRequestDTO loginRequestDto) {
        // 디버깅을 위해 받은 데이터를 출력
        System.out.println("Received login request: " + loginRequestDto.getUsername());
        System.out.println("Received login request: " + loginRequestDto.getPassword());

        String jwtToken = loginService.login(loginRequestDto);

        if (jwtToken != null) {
            return ResponseEntity.ok(jwtToken); // JWT를 반환
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid username or password");
        }
    }

    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody RegisterRequestDTO registerRequestDTO) {
        boolean isRegistered = registerService.register(registerRequestDTO);

        if (isRegistered) {
            return ResponseEntity.status(HttpStatus.CREATED).body("Account created successfully");
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("User already exists");
        }
    }

    @GetMapping("/home")
    @PreAuthorize("hasAuthority('ROLE_USER')")
    public ResponseEntity<Map<String, String>> getProtectedData(@AuthenticationPrincipal CustomUserDetails userDetails) {
        if (userDetails == null) {
            System.out.println("인증 실패: userDetails가 null입니다.");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("message", "사용자가 인증되지 않았습니다."));
        }

        Long userId = userDetails.getUserId(); // userId 가져오기
        String username = userDetails.getUsername(); // 사용자 이름 가져오기
        System.out.println("Authenticated userId: " + userId); // userId 출력
        System.out.println("Authenticated username: " + username); // 사용자 이름 출력

        Map<String, String> response = new HashMap<>();
        response.put("message", username + "님 반갑습니다!");

        return ResponseEntity.ok(response);
    }
    @GetMapping("/todos")
    @PreAuthorize("hasAuthority('ROLE_USER')")
    public List<Todo> getTodos(@AuthenticationPrincipal CustomUserDetails userDetails) {
        User user = userDetails.getUser(); // 인증된 사용자
        return todoService.getTodosByUser(user);
    }

    @PostMapping("/todos")
    @PreAuthorize("hasAuthority('ROLE_USER')")
    public Todo createTodo(@RequestBody TodoRequestDTO todoRequestDTO, @AuthenticationPrincipal CustomUserDetails userDetails) {
        Todo todo = new Todo();
        todo.setContent(todoRequestDTO.getTodo());
        todo.setUser(userDetails.getUser()); // 현재 사용자와 연결

        return todoService.addTodo(todo);
    }

    @DeleteMapping("/todos/{id}")
    @PreAuthorize("hasAuthority('ROLE_USER')")
    public void deleteTodo(@PathVariable Long id) {
        todoService.deleteTodo(id);
    }
    @PutMapping("/todos/{id}")
    @PreAuthorize("hasAuthority('ROLE_USER')")
    public Todo updateTodo(@PathVariable Long id, @RequestBody TodoRequestDTO todoRequestDTO, @AuthenticationPrincipal CustomUserDetails userDetails) {
        Todo existingTodo = todoService.getTodoById(id); // ID로 기존 Todo 가져오기

        if (existingTodo == null || !existingTodo.getUser().equals(userDetails.getUser())) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Todo 항목을 찾을 수 없습니다.");
        }

        existingTodo.setContent(todoRequestDTO.getTodo()); // 내용 수정
        return todoService.updateTodo(existingTodo); // 업데이트된 Todo 반환
    }
}