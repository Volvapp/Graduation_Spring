package com.nbu.graduation.spring.repository;

import com.nbu.graduation.spring.model.User;
import com.nbu.graduation.spring.model.enums.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    User findByUsername(String username);

    User findByEmail(String email);

    List<User> findAllByRole(Role role);
}
