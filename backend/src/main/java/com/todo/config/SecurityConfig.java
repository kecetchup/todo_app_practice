package com.todo.config;

import com.todo.config.security.JwtAccessDeniedHandler;
import com.todo.config.security.JwtAuthenticationEntryPoint;
import com.todo.config.security.JwtAuthenticationFilter;
import com.todo.config.security.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.core.userdetails.UserDetailsService;

@Configuration //설정 클래스 선언
@RequiredArgsConstructor //final 필드에 대한 생성자 자동 생성
@EnableWebSecurity(debug = true) //Spring Security 활성화 및 디버깅 정보 출력
public class SecurityConfig {

    private final JwtTokenProvider jwtTokenProvider; // JWT를 생성하고 검증하는 데 사용되는 컴포넌트
    private final JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint; // 인증 실패 시 처리하는 핸들러
    private final JwtAccessDeniedHandler jwtAccessDeniedHandler; // 권한 부족 시 처리하는 핸들러
    private final UserDetailsService userDetailsService; // 사용자 정보를 로드하는 서비스

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable()) // CSRF 보호 비활성화, REST API에서는 일반적으로 비활성화
                .httpBasic(httpBasic -> httpBasic.disable()) // HTTP Basic 인증 비활성화,
                .formLogin(formLogin -> formLogin.disable()) // 폼 로그인 비활성화
                .sessionManagement(sessionManagement ->
                        sessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS) // 세션을 상태 없이 설정
                )
                .authorizeHttpRequests(authorizeRequests ->
                        authorizeRequests
                                .requestMatchers("/users/mypage/**").authenticated() // 인증된 사용자만 접근 허용
                                .requestMatchers("/admin/**").hasRole("ADMIN") // 관리자 역할을 가진 사용자만 접근 허용
                                .anyRequest().permitAll() // 나머지 요청은 모두 허용
                )
                .exceptionHandling(exceptionHandling -> //인증 및 권한 관련 예외 처리를 설정
                        exceptionHandling
                                .authenticationEntryPoint(jwtAuthenticationEntryPoint) // 인증 실패 시 처리 핸들러
                                .accessDeniedHandler(jwtAccessDeniedHandler) // 권한 부족 시 처리 핸들러.
                )
                .addFilterBefore(new JwtAuthenticationFilter(jwtTokenProvider, userDetailsService), UsernamePasswordAuthenticationFilter.class); // JWT 인증 필터 추가

        return http.build();
    }

    @Bean
    public BCryptPasswordEncoder passwordEncoder() { //비밀번호를 안전하게 해싱 인코더
        return new BCryptPasswordEncoder();
    }
}
