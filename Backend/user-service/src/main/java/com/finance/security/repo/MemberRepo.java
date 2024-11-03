package com.finance.security.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.finance.security.model.Members;
@Repository
public interface MemberRepo extends JpaRepository<Members, Long>{

}
