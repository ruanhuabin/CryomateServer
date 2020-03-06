package com.cryomate.entity;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name="Users")
public class Users {	
	private String userName;
	private String password;
	private String group;
	private String authority;
	private String dateAdd;
	private String dateExp;
	private String homeDir;
	private String workDir;
	private String dataDir;
	
	
	public Users() {
		
	}
	public Users(String userName, String password, String group, String authority, String dateAdd, String dateExp,
			String homeDir, String workDir, String dataDir) {
		super();
		this.userName = userName;
		this.password = password;
		this.group = group;
		this.authority = authority;
		this.dateAdd = dateAdd;
		this.dateExp = dateExp;
		this.homeDir = homeDir;
		this.workDir = workDir;
		this.dataDir = dataDir;
	}
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
	public String getGroup() {
		return group;
	}
	public void setGroup(String group) {
		this.group = group;
	}
	public String getAuthority() {
		return authority;
	}
	public void setAuthority(String authority) {
		this.authority = authority;
	}
	public String getDateAdd() {
		return dateAdd;
	}
	public void setDateAdd(String dateAdd) {
		this.dateAdd = dateAdd;
	}
	public String getDateExp() {
		return dateExp;
	}
	public void setDateExp(String dateExp) {
		this.dateExp = dateExp;
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
		return "Users [userName=" + userName + ", password=" + password + ", group=" + group + ", authority="
				+ authority + ", dateAdd=" + dateAdd + ", dateExp=" + dateExp + ", homeDir=" + homeDir + ", workDir="
				+ workDir + ", dataDir=" + dataDir + "]";
	}
	
	

}
