package com.cryomate.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.cryomate.model.User;

@Repository
public interface UserRepository extends JpaRepository<User, String>{
	
	User getByName(String name);
	
	void deleteByName(String name);
	
	
	List<User> findByWorkGroup(String workGroup22);
	
	User findByNameAndPassword(String name, String password);
	
	@Query("select u from User u where u.name= :name and u.password= :password")
	User withNameAndPasswordQuery(@Param("name")String name, @Param("password")String password);

	User withNameAndPasswordNamedQuery(String name, String password);
	
	User withNameAndWorkGroupNamedQuery(String name, String workGroup);
	
	
	
}
