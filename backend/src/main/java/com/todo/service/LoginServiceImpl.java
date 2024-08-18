package com.todo.service;

import com.todo.config.security.JwtTokenProvider;
import com.todo.domain.User;
import com.todo.dto.LoginRequestDTO;
import com.todo.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

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
            return jwtTokenProvider.createToken(user.getUsername());
        }

        // 인증 실패 처리
        System.out.println("Password does not match");
        return null; // 비밀번호 불일치
    }
}
