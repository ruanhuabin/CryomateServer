package com.cryomate.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="Jobs")
public class Jobs {
	private String ID;
	private String projectID;
	private String parentID;
	private String dataID;
	private String scriptID;
	private String scripFile;
	private String EMMethod;
	private String jobType;
	private String settingFile;
	private String username;
	private String dateAdd;
	private String path;
	private String infoFile;
	private String description;
	private String jobStatus;
	private String quality;
	
	
	public Jobs() {
		
	}


	public Jobs(String iD, String projectID, String parentID, String dataID, String scriptID, String scripFile,
			String EMMethod, String jobType, String settingFile, String username, String dateAdd, String path,
			String infoFile, String description, String jobStatus, String quality) {
		
		ID = iD;
		this.projectID = projectID;
		this.parentID = parentID;
		this.dataID = dataID;
		this.scriptID = scriptID;
		this.scripFile = scripFile;
		this.EMMethod = EMMethod;
		this.jobType = jobType;
		this.settingFile = settingFile;
		this.username = username;
		this.dateAdd = dateAdd;
		this.path = path;
		this.infoFile = infoFile;
		this.description = description;
		this.jobStatus = jobStatus;
		this.quality = quality;
	}


	@Id
	@Column(name="ID")
	public String getID() {
		return ID;
	}


	public void setID(String iD) {
		ID = iD;
	}

	@Column(name="ProjectID")
	public String getProjectID() {
		return projectID;
	}


	public void setProjectID(String projectID) {
		this.projectID = projectID;
	}

	@Column(name="ParentID")
	public String getParentID() {
		return parentID;
	}


	public void setParentID(String parentID) {
		this.parentID = parentID;
	}

	@Column(name="DataID")
	public String getDataID() {
		return dataID;
	}


	public void setDataID(String dataID) {
		this.dataID = dataID;
	}

	@Column(name="ScriptID")
	public String getScriptID() {
		return scriptID;
	}


	public void setScriptID(String scriptID) {
		this.scriptID = scriptID;
	}

	@Column(length=2048,name="ScriptFile")
	public String getScripFile() {
		return scripFile;
	}


	public void setScripFile(String scripFile) {
		this.scripFile = scripFile;
	}

	@Column(name="EMMethod")
	public String getEMMethod() {
		return EMMethod;
	}


	public void setEMMethod(String EMMethod) {
		this.EMMethod = EMMethod;
	}

	@Column(name="JobType")
	public String getJobType() {
		return jobType;
	}


	public void setJobType(String jobType) {
		this.jobType = jobType;
	}

	@Column(length=2048,name="SettingFile")
	public String getSettingFile() {
		return settingFile;
	}


	public void setSettingFile(String settingFile) {
		this.settingFile = settingFile;
	}

	@Column(name="Username")
	public String getUsername() {
		return username;
	}


	public void setUsername(String username) {
		this.username = username;
	}

	@Column(name="DataAdd")
	public String getDateAdd() {
		return dateAdd;
	}


	public void setDateAdd(String dateAdd) {
		this.dateAdd = dateAdd;
	}

	@Column(length=2048,name="Path")
	public String getPath() {
		return path;
	}


	public void setPath(String path) {
		this.path = path;
	}

	@Column(length=2048,name="InfoFile")
	public String getInfoFile() {
		return infoFile;
	}


	public void setInfoFile(String infoFile) {
		this.infoFile = infoFile;
	}

	@Column(length=4096, name="Description")
	public String getDescription() {
		return description;
	}


	public void setDescription(String description) {
		this.description = description;
	}

	@Column(name="JobStatus")
	public String getJobStatus() {
		return jobStatus;
	}


	public void setJobStatus(String jobStatus) {
		this.jobStatus = jobStatus;
	}

	@Column(name="Quality")
	public String getQuality() {
		return quality;
	}


	public void setQuality(String quality) {
		this.quality = quality;
	}


	@Override
	public String toString() {
		return "Jobs [ID=" + ID + ", projectID=" + projectID + ", parentID=" + parentID + ", dataID=" + dataID
				+ ", scriptID=" + scriptID + ", scripFile=" + scripFile + ", EMMethod=" + EMMethod + ", jobType="
				+ jobType + ", settingFile=" + settingFile + ", username=" + username + ", dateAdd=" + dateAdd
				+ ", path=" + path + ", infoFile=" + infoFile + ", description=" + description + ", jobStatus="
				+ jobStatus + ", quality=" + quality + "]";
	}
	
	


}
