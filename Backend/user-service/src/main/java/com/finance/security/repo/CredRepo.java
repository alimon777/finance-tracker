package com.finance.security.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.finance.security.model.User;
@Repository
public interface CredRepo extends JpaRepository<User, Long>{

	public User findByUsername(String username);

}
