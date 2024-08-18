package com.todo.service;

import com.todo.domain.Role;
import com.todo.domain.User;
import com.todo.dto.RegisterRequestDTO;
import com.todo.repository.RoleRepository;
import com.todo.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

@Service
public class RegisterServiceImpl implements RegisterService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Override
    public boolean register(RegisterRequestDTO registerRequestDTO) {
        // 받은 정보 출력
        System.out.println("Received registration request:");
        System.out.println("Username: " + registerRequestDTO.getUsername());
        System.out.println("Email: " + registerRequestDTO.getEmail());

        // 사용자명으로 이미 존재하는지 확인
        User existingUser = userRepository.findByUsername(registerRequestDTO.getUsername());
        if (existingUser != null) {
            System.out.println("User already exists");
            return false;
        }

        // 새로운 사용자 생성
        User newUser = new User();
        newUser.setUsername(registerRequestDTO.getUsername());
        newUser.setPassword(passwordEncoder.encode(registerRequestDTO.getPassword()));
        newUser.setEmail(registerRequestDTO.getEmail());

        // 기본 역할 할당 (예: ROLE_USER)
        Role userRole = roleRepository.findByName("ROLE_USER");
        if (userRole == null) {
            // 현재 데이터베이스에 저장된 모든 역할 출력
            List<Role> roles = roleRepository.findAll();
            roles.forEach(role -> System.out.println(role.getName()));

            System.out.println("Role ROLE_USER not found");
            return false; // 역할이 없으면 등록 실패
        }
        newUser.setRoles(Set.of(userRole));


        // 사용자 저장
        userRepository.save(newUser);
        System.out.println("User registered successfully");

        return true;
    }
}
