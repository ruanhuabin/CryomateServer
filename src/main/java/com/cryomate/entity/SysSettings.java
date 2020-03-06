package com.cryomate.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="SysSettings")
public class SysSettings {
	private String ID;
	private String name;
	private String settingFile;
	private String sKey;
	private String sValue;
	private String description;
	private String username;
	private String dateAdd;

	
	
	public SysSettings() {
	}



	public SysSettings(String iD, String name, String settingFile, String sKey, String sValue, String description,
			String username, String dateAdd) {
		ID = iD;
		this.name = name;
		this.settingFile = settingFile;
		this.sKey = sKey;
		this.sValue = sValue;
		this.description = description;
		this.username = username;
		this.dateAdd = dateAdd;
	}



	@Id
	@Column(name="ID")
	public String getID() {
		return ID;
	}



	public void setID(String iD) {
		ID = iD;
	}


	@Column(name="Name")
	public String getName() {
		return name;
	}



	public void setName(String name) {
		this.name = name;
	}


	@Column(length=2048, name="SettingFile")
	public String getSettingFile() {
		return settingFile;
	}



	public void setSettingFile(String settingFile) {
		this.settingFile = settingFile;
	}


	@Column(length=2048, name="SKey")
	public String getsKey() {
		return sKey;
	}



	public void setsKey(String sKey) {
		this.sKey = sKey;
	}


	@Column(name="SValue")
	public String getsValue() {
		return sValue;
	}



	public void setsValue(String sValue) {
		this.sValue = sValue;
	}


	@Column(length=4096, name="Description")
	public String getDescription() {
		return description;
	}



	public void setDescription(String description) {
		this.description = description;
	}


	@Column(name="Username")
	public String getUsername() {
		return username;
	}



	public void setUsername(String username) {
		this.username = username;
	}


	@Column(name="DateAdd")
	public String getDateAdd() {
		return dateAdd;
	}



	public void setDateAdd(String dateAdd) {
		this.dateAdd = dateAdd;
	}



	@Override
	public String toString() {
		return "SysSettings [ID=" + ID + ", name=" + name + ", settingFile=" + settingFile + ", sKey=" + sKey
				+ ", sValue=" + sValue + ", description=" + description + ", username=" + username + ", dateAdd="
				+ dateAdd + "]";
	}

	

}
