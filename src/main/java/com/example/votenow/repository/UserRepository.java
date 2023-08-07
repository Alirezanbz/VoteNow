package com.example.votenow.repository;

import com.example.votenow.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
    User findByEmail(String email);
    User save (User user);

    boolean existsByEmail(String email);

    User findByName(String username);

}
