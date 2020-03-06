package com.cryomate.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="Pipelines")
public class Pipelines {
	private String ID;
	private String Name;
	private String Version;
	private String ScriptID;
	private String ScriptFile;
	private String SettingFile;
	private String Username;
	private String DateAdd;
	private String Description;
	
	public Pipelines() {
		super();
	}
	public Pipelines(String iD, String name, String version, String scriptID, String scriptFile, String settingFile,
			String username, String dateAdd, String description) {
		super();
		ID = iD;
		Name = name;
		Version = version;
		ScriptID = scriptID;
		ScriptFile = scriptFile;
		SettingFile = settingFile;
		Username = username;
		DateAdd = dateAdd;
		Description = description;
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
		return Name;
	}
	public void setName(String name) {
		Name = name;
	}
	
	@Column(name="Version")
	public String getVersion() {
		return Version;
	}
	public void setVersion(String version) {
		Version = version;
	}
	
	@Column(name="ScriptID")
	public String getScriptID() {
		return ScriptID;
	}
	public void setScriptID(String scriptID) {
		ScriptID = scriptID;
	}
	
	@Column(length=4096, name="ScriptFile")
	public String getScriptFile() {
		return ScriptFile;
	}
	public void setScriptFile(String scriptFile) {
		ScriptFile = scriptFile;
	}
	
	@Column(length=4096, name="SettingFile")
	public String getSettingFile() {
		return SettingFile;
	}
	public void setSettingFile(String settingFile) {
		SettingFile = settingFile;
	}
	
	@Column(name="UserName")
	public String getUsername() {
		return Username;
	}
	public void setUsername(String username) {
		Username = username;
	}
	
	@Column(name="DateAdd")
	public String getDateAdd() {
		return DateAdd;
	}
	public void setDateAdd(String dateAdd) {
		DateAdd = dateAdd;
	}
	
	@Column(length=4096, name="Description")
	public String getDescription() {
		return Description;
	}
	public void setDescription(String description) {
		Description = description;
	}
	@Override
	public String toString() {
		return "Pipelines [ID=" + ID + ", Name=" + Name + ", Version=" + Version + ", ScriptID=" + ScriptID
				+ ", ScriptFile=" + ScriptFile + ", SettingFile=" + SettingFile + ", Username=" + Username
				+ ", DateAdd=" + DateAdd + ", Description=" + Description + "]";
	}
	
	
	
	
	
	

}
