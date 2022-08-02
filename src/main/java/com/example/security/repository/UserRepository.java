package com.example.security.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.example.security.model.User;

public interface UserRepository extends JpaRepository<User, Long> {
    @Query("SELECT u FROM User u WHERE u.email = ?1")
    public User findByEmail(String email);

    @Query("UPDATE User u SET u.failedAttempts = ?1 WHERE u.email = ?2")
    @Modifying
    public void updateFailedAttempts(int failedAttempts, String email);
}
