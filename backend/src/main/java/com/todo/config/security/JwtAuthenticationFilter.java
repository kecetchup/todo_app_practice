package com.todo.config.security;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

public class JwtAuthenticationFilter extends OncePerRequestFilter { // 매 요청마다 한 번만 실행되는 필터를 정의

    private final JwtTokenProvider jwtTokenProvider; // JWT의 생성 및 검증을 담당하는 JwtTokenProvider 객체
    private final UserDetailsService userDetailsService; // 사용자 정보를 로드하는 서비스

    public JwtAuthenticationFilter(JwtTokenProvider jwtTokenProvider, UserDetailsService userDetailsService) { // 생성자를 통해 JwtTokenProvider와 UserDetailsService를 주입받아 초기화
        this.jwtTokenProvider = jwtTokenProvider;
        this.userDetailsService = userDetailsService;
    }

    @Override // 요청을 필터링하는 핵심 로직을 포함
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws ServletException, IOException {
        String token = getJwtFromRequest(request); // 요청 헤더에서 JWT를 추출

        if (token != null && jwtTokenProvider.validateToken(token)) { // 추출한 토큰이 유효한지 검증
            String username = jwtTokenProvider.getUsernameFromToken(token); // 유효시 JWT에서 사용자 이름을 추출
            UserDetails userDetails = userDetailsService.loadUserByUsername(username); // 사용자 정보를 로드
            String role = jwtTokenProvider.getRoleFromToken(token); // JWT에서 역할을 추출

            // JwtAuthenticationToken 객체 생성
            JwtAuthenticationToken authentication = new JwtAuthenticationToken(userDetails, token, role, userDetails.getAuthorities());
            SecurityContextHolder.getContext().setAuthentication(authentication); // 요청에서 인증된 사용자로 인식
        }

        chain.doFilter(request, response); // 다음 필터로 요청 전달
    }

    private String getJwtFromRequest(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7); // "Bearer " 부분을 제거하고 토큰 반환
        }
        return null;
    }
}
