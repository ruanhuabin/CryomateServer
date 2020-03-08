package com.cryomate.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

import org.hibernate.annotations.ColumnTransformer;

@Entity
@Table(name="Users")
@NamedQuery(name="Users.withUserNameAndPasswordNamedQuery", query="select u from Users u where u.userName=?1 and u.password=?2")
@NamedQuery(name="Users.withUserNameAndUserGroupNamedQuery", query="select u from Users u where u.userName=?1 and u.userGroup=?2")
public class Users {
	
	
	private String userName;
	
	private String password;
	private String userGroup;
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
		this.userGroup = group;
		this.authority = authority;
		this.dateAdd = dateAdd;
		this.dateExp = dateExp;
		this.homeDir = homeDir;
		this.workDir = workDir;
		this.dataDir = dataDir;
	}
	
	@Id	
	@Column(name="UserName")
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	
	@Column(name="Password")
	//加密密码字段，支持中文密码的加密和解密
	@ColumnTransformer(  
            //read = "AES_DECRYPT(UNHEX(Password), 'ankon')",			
			read = "CAST(AES_DECRYPT(UNHEX(Password), 'ankon') as char(128))",
            write = "HEX(AES_ENCRYPT(?, 'ankon'))"
    )
	
	
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	@Column(name="UserGroup")
	public String getUserGroup() {
		return userGroup;
	}
	public void setUserGroup(String group) {
		this.userGroup = group;
	}
	
	@Column(name="Authority")
	public String getAuthority() {
		return authority;
	}
	
	public void setAuthority(String authority) {
		this.authority = authority;
	}
	
	@Column(name="DateAdd")
	public String getDateAdd() {
		return dateAdd;
	}
	public void setDateAdd(String dateAdd) {
		this.dateAdd = dateAdd;
	}
	
	@Column(name="DateExp")
	public String getDateExp() {
		return dateExp;
	}
	public void setDateExp(String dateExp) {
		this.dateExp = dateExp;
	}
	
	@Column(length=2048, name="HomeDir")
	public String getHomeDir() {
		return homeDir;
	}
	public void setHomeDir(String homeDir) {
		this.homeDir = homeDir;
	}
	
	@Column(length=2048, name="WorkDir")
	public String getWorkDir() {
		return workDir;
	}
	public void setWorkDir(String workDir) {
		this.workDir = workDir;
	}
	
	@Column(length=2048, name="DataDir")
	public String getDataDir() {
		return dataDir;
	}
	public void setDataDir(String dataDir) {
		this.dataDir = dataDir;
	}
	
	
	@Override
	public String toString() {
		return "Users [userName=" + userName + ", password=" + password + ", group=" + userGroup + ", authority="
				+ authority + ", dateAdd=" + dateAdd + ", dateExp=" + dateExp + ", homeDir=" + homeDir + ", workDir="
				+ workDir + ", dataDir=" + dataDir + "]";
	}
	
	

}
