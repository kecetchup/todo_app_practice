package com.todo.config.security;

import com.todo.domain.User; // User 클래스 임포트
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;

public class CustomUserDetails implements UserDetails {
    private Long userId; // 사용자 ID
    private String username;
    private String password;
    private Collection<? extends GrantedAuthority> authorities;
    private User user; // User 객체 추가

    public CustomUserDetails(Long userId, String username, String password, Collection<? extends GrantedAuthority> authorities, User user) {
        this.userId = userId;
        this.username = username;
        this.password = password;
        this.authorities = authorities;
        this.user = user; // User 객체 초기화
    }

    public Long getUserId() {
        return userId;
    }

    public User getUser() { // User 객체를 반환하는 메소드 추가
        return user;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true; // 계정 만료 여부
    }

    @Override
    public boolean isAccountNonLocked() {
        return true; // 계정 잠금 여부
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true; // 자격 증명 만료 여부
    }

    @Override
    public boolean isEnabled() {
        return true; // 계정 활성화 여부
    }
}
