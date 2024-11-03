package com.finance.security.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.finance.security.model.users;
@Repository
public interface CredRepo extends JpaRepository<users, Integer>{

	public users findByUsername(String username);

}
