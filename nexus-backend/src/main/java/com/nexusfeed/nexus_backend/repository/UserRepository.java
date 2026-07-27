package com.nexusfeed.nexus_backend.repository;

import java.util.Optional;

import com.nexusfeed.nexus_backend.model.User;

import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
}
