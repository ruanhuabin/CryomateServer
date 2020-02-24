package com.cryomate.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.cryomate.model.User;
import com.cryomate.model.UserExample;

@Repository
public interface UserExampleRepository  extends JpaRepository<UserExample, Long>{
	
	

}
