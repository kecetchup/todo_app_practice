package com.todo.service;

import com.todo.config.security.CustomUserDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import com.todo.domain.User;
import com.todo.repository.UserRepository;
import com.todo.repository.RoleRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class UserDetailServiceImpl implements UserDetailsService {

    private static final int MAX_ATTEMPTS = 5; // 최대 시도 횟수
    private static final long LOCK_TIME = 15 * 60 * 1000; // 15분

    private Map<String, Integer> attemptCounts = new ConcurrentHashMap<>(); // 사용자 시도 횟수 저장
    private Map<String, Long> lockTime = new ConcurrentHashMap<>(); // 사용자 잠금 시간 저장

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // 잠금 상태 확인
        if (isAccountLocked(username)) {
            throw new UsernameNotFoundException("계정이 잠금 상태입니다. 잠금 해제 후 다시 시도하세요.");
        }

        // 사용자 정보를 DB에서 조회하는 로직
        User user = userRepository.findByUsername(username);
        if (user != null) {
            // 로그인 성공 시 시도 횟수 리셋
            resetAttemptCount(username);
            List<GrantedAuthority> authorities = new ArrayList<>();

            // 사용자 역할 가져오기
            if (user.getRoles() != null) {
                user.getRoles().forEach(role -> {
                    authorities.add(new SimpleGrantedAuthority(role.getName()));
                });
            }

            // CustomUserDetails 객체 생성하여 반환
            return new CustomUserDetails(
                    user.getId(),
                    user.getUsername(),
                    user.getPassword(),
                    authorities,
                    user // User 객체 추가
            );
        } else {
            // 로그인 실패 시 시도 횟수 증가
            incrementAttemptCount(username);
            throw new UsernameNotFoundException("사용자를 찾을 수 없습니다: " + username);
        }
    }

    private boolean isAccountLocked(String username) {
        if (lockTime.containsKey(username)) {
            long lockDuration = System.currentTimeMillis() - lockTime.get(username);
            if (lockDuration < LOCK_TIME) {
                return true; // 아직 잠금 상태
            } else {
                // 잠금 시간 초과 시 데이터 삭제
                lockTime.remove(username);
                attemptCounts.remove(username);
            }
        }
        return false; // 잠금 상태가 아님
    }

    private void incrementAttemptCount(String username) {
        int attempts = attemptCounts.getOrDefault(username, 0) + 1;
        attemptCounts.put(username, attempts);
        if (attempts >= MAX_ATTEMPTS) {
            lockTime.put(username, System.currentTimeMillis()); // 잠금 시간 기록
        }
    }

    private void resetAttemptCount(String username) {
        attemptCounts.remove(username);
    }
}
