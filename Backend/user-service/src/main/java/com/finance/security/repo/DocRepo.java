package com.finance.security.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.finance.security.model.Document;
@Repository
public interface DocRepo extends JpaRepository<Document, Long>{
//	public Document findByMemId(Long id);
}
