package com.cryomate.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.cryomate.model.User;

@Repository
public interface UserRepository extends JpaRepository<User, String>{
	
	User getByUserName(String userName);
	
	void deleteByUserName(String userName);
	
	
	List<User> findByUserGroup(String workGroup22);
	
	User findByUserNameAndPassword(String userName, String password);
	
	@Query("select u from User u where u.userName= :userName and u.password= :password")
	User withUserNameAndPasswordQuery(@Param("userName")String userName, @Param("password")String password);

	User withUserNameAndPasswordNamedQuery(String userName, String password);
	
	User withUserNameAndUserGroupNamedQuery(String userName, String userGroup);
	
	
	
}
