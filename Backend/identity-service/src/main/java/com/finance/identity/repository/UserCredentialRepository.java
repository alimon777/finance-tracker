package com.finance.identity.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.finance.identity.entity.User;

import java.util.Optional;

public interface UserCredentialRepository  extends JpaRepository<User,Long> {
    Optional<User> findByUsername(String username);
}