package com.todo.controller;

import com.todo.dto.LoginRequestDTO;
import com.todo.dto.RegisterRequestDTO;
import com.todo.service.LoginService;
import com.todo.service.RegisterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class LoginController {

    @Autowired
    private LoginService loginService;

    @Autowired
    private RegisterService registerService;

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
    @GetMapping("/protected")
    @PreAuthorize("hasAuthority('ROLE_USER')")
    public ResponseEntity<Map<String, String>> getProtectedData() {
        Map<String, String> response = new HashMap<>();
        response.put("message", "This is protected data!");
        return ResponseEntity.ok(response);
    }
}
