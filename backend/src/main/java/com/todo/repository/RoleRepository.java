package com.todo.repository;

import com.todo.domain.Role;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<Role, Long> {
    Role findByName(String name); // 역할 이름으로 조회하는 메서드
}