package com.cryomate.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQuery;

@Entity
@NamedQuery(name="User.withNameAndPasswordNamedQuery", query="select u from User u where u.name=?1 and u.password=?2")
@NamedQuery(name="User.withNameAndWorkGroupNamedQuery", query="select u from User u where u.name=?1 and u.workGroup=?2")
public class User {	
	private String name;
	private String password;
	private String email;
	private String workGroup;
	private String role;
	private String homeDir;	
	private String dataDir;	
	

	public User() {
		super();
	}

	public User( String name, String password, String email, String workGroup, String role, String homeDir) {
		super();		
		this.name = name;
		this.password = password;
		this.email = email;
		this.workGroup = workGroup;
		this.role = role;
		this.homeDir = homeDir;
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
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
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

	public String getWorkGroup() {
		return workGroup;
	}

	public void setWorkGroup(String workGroup) {
		this.workGroup = workGroup;
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

	
	public String getDataDir() {
		return dataDir;
	}

	public void setDataDir(String dataDir) {
		this.dataDir = dataDir;
	}

	@Override
	public String toString() {
		return "User [name=" + name + ", password=" + password + ", email=" + email + ", workGroup=" + workGroup
				+ ", role=" + role + ", homeDir=" + homeDir + ", dataDir=" + dataDir + "]";
	}

	
	
	
	

	
}
