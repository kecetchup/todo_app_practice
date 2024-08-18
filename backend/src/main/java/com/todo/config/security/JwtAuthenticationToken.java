package com.todo.config.security;

import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;

public class JwtAuthenticationToken extends AbstractAuthenticationToken {

    private final Object principal; //인증된 사용자의 정보
    private Object credentials; //사용자의 인증 정보 JWT등

    public JwtAuthenticationToken(UserDetails principal, Object credentials, Collection<? extends GrantedAuthority> authorities) {
        super(authorities);
        this.principal = principal; //인증된 사용자의 세부 정보
        this.credentials = credentials; //인증 정보(예: JWT 토큰).
        this.setAuthenticated(true); // 인증 완료
    }

    @Override // 인증 정보(예: JWT 토큰)를 반환
    public Object getCredentials() {
        return credentials;
    }

    @Override //인증된 사용자 정보를 반환
    public Object getPrincipal() {
        return principal;
    }
}
