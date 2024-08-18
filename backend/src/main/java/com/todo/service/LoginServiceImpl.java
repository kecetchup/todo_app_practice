package com.todo.service;

import com.todo.config.security.JwtTokenProvider;
import com.todo.domain.User;
import com.todo.domain.Role;
import com.todo.dto.LoginRequestDTO;
import com.todo.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class LoginServiceImpl implements LoginService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Override
    public String login(LoginRequestDTO loginRequestDto) {
        User user = userRepository.findByUsername(loginRequestDto.getUsername());
        if (user == null) {
            System.out.println("User not found");
            return null; // 사용자 없음
        }

        // 비밀번호 비교
        if (passwordEncoder.matches(loginRequestDto.getPassword(), user.getPassword())) {
            // 비밀번호가 일치하면 JWT 생성
            System.out.println("Password match");

            // 역할 정보 가져오기
            String role = user.getRoles().stream()
                    .findFirst() // 첫 번째 역할을 가져옴
                    .map(Role::getName) // Role 객체에서 역할 이름을 가져옴
                    .orElse("ROLE_USER"); // 역할이 없을 경우 기본 역할 설정

            return jwtTokenProvider.createToken(user.getUsername(), role); // 역할 정보 추가
        }

        // 인증 실패 처리
        System.out.println("Password does not match");
        return null; // 비밀번호 불일치
    }
}
