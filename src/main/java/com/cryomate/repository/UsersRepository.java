package com.cryomate.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.cryomate.entity.Users;


@Repository
public interface UsersRepository extends JpaRepository<Users, String>{
	
	Users getByUserName(String userName);
	
	void deleteByUserName(String userName);
	
	
	List<Users> findByUserGroup(String workGroup22);
	
	Users findByUserNameAndPassword(String userName, String password);
	
	@Query("select u from Users u where u.userName= :userName and u.password= :password")
	Users withUserNameAndPasswordQuery(@Param("userName")String userName, @Param("password")String password);

	Users withUserNameAndPasswordNamedQuery(String userName, String password);
	
	Users withUserNameAndUserGroupNamedQuery(String userName, String userGroup);
	
	
	
}