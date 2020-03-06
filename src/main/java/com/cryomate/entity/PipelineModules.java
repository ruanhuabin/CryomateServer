package com.cryomate.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Id;

@Entity
@Table(name="PipelineModules")
public class PipelineModules {
	private String ID;
	private String name;
	private String version;
	private String scriptID;
	private String scriptFile;
	private String iconFile;
	private String jsonFile;
	private String dateAdd;
	private String username;
	private String description;

	
	public PipelineModules() {
	}


	public PipelineModules(String iD, String name, String version, String scriptID, String scriptFile, String iconFile,
			String jsonFile, String dateAdd, String username, String description) {
		ID = iD;
		this.name = name;
		this.version = version;
		this.scriptID = scriptID;
		this.scriptFile = scriptFile;
		this.iconFile = iconFile;
		this.jsonFile = jsonFile;
		this.dateAdd = dateAdd;
		this.username = username;
		this.description = description;
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

	@Column(name="Version")
	public String getVersion() {
		return version;
	}


	public void setVersion(String version) {
		this.version = version;
	}

	@Column(name="ScriptID")
	public String getScriptID() {
		return scriptID;
	}


	public void setScriptID(String scriptID) {
		this.scriptID = scriptID;
	}

	@Column(length=2048, name="ScriptFile")
	public String getScriptFile() {
		return scriptFile;
	}


	public void setScriptFile(String scriptFile) {
		this.scriptFile = scriptFile;
	}

	@Column(length=2048, name="IconFile")
	public String getIconFile() {
		return iconFile;
	}


	public void setIconFile(String iconFile) {
		this.iconFile = iconFile;
	}

	@Column(length=2048, name="JsonFile")
	public String getJsonFile() {
		return jsonFile;
	}


	public void setJsonFile(String jsonFile) {
		this.jsonFile = jsonFile;
	}

	@Column(length=2048, name="DateAdd")
	public String getDateAdd() {
		return dateAdd;
	}


	public void setDateAdd(String dateAdd) {
		this.dateAdd = dateAdd;
	}

	@Column(name="Username")
	public String getUsername() {
		return username;
	}


	public void setUsername(String username) {
		this.username = username;
	}

	@Column(length=4096, name="Description")
	public String getDescription() {
		return description;
	}


	public void setDescription(String description) {
		this.description = description;
	}


	@Override
	public String toString() {
		return "PipelineModules [ID=" + ID + ", name=" + name + ", version=" + version + ", scriptID=" + scriptID
				+ ", scriptFile=" + scriptFile + ", iconFile=" + iconFile + ", jsonFile=" + jsonFile + ", dateAdd="
				+ dateAdd + ", username=" + username + ", description=" + description + "]";
	}

	
	

}
