package com.cryomate.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQuery;

@Entity
@NamedQuery(name="User.withUserNameAndPasswordNamedQuery", query="select u from User u where u.userName=?1 and u.password=?2")
@NamedQuery(name="User.withUserNameAndUserGroupNamedQuery", query="select u from User u where u.userName=?1 and u.userGroup=?2")
public class User {	
	private String userName;
	private String password;
	private String email;
	private String userGroup;
	private String role;
	private String homeDir;	
	private String workDir;
	private String dataDir;		

	public User() {
		super();
	}

	public User(String name, String password, String email, String workGroup, String role, String homeDir, String workDir, String dataDir) {
		super();		
		this.userName = name;
		this.password = password;
		this.email = email;
		this.userGroup = workGroup;
		this.role = role;
		this.homeDir = homeDir;
		this.workDir = workDir;
		this.dataDir = dataDir;
	}

//	@Id
//	@GeneratedValue(strategy = GenerationType.SEQUENCE)
//	public int getId() {
//		return id;
//	}
//
//	public void setId(int id) {
//		this.id = id;
//	}

	@Id
	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getUserGroup() {
		return userGroup;
	}

	public void setUserGroup(String userGroup) {
		this.userGroup = userGroup;
	}

	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}
	
	

	public String getHomeDir() {
		return homeDir;
	}

	public void setHomeDir(String homeDir) {
		this.homeDir = homeDir;
	}

	
	
	public String getWorkDir() {
		return workDir;
	}

	public void setWorkDir(String workDir) {
		this.workDir = workDir;
	}

	public String getDataDir() {
		return dataDir;
	}

	public void setDataDir(String dataDir) {
		this.dataDir = dataDir;
	}

	@Override
	public String toString() {
		return "User [userName=" + userName + ", password=" + password + ", email=" + email + ", userGroup=" + userGroup
				+ ", role=" + role + ", homeDir=" + homeDir + ", workDir=" + workDir + ", dataDir=" + dataDir + "]";
	}

	
	
	
	
	

	
}
