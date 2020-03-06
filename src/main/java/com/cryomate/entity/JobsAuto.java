package com.cryomate.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="JobsAuto")
public class JobsAuto {
	private String ID;
	private String projectID;
	private String dataID;
	private String pipelineID;
	private String pipelineFile;
	private String settingFile;
	private String username;
	private String dateAdd;
	private String path;
	private String infoFile;
	private String EMMethod;
	private String description;
	private String jobStatus;
	private String quality;
	
	
	public JobsAuto() {
		
	}


	public JobsAuto(String iD, String projectID, String dataID, String pipelineID, String pipelineFile,
			String settingFile, String username, String dateAdd, String path, String infoFile, String eMMethod,
			String description, String jobStatus, String quality) {
		
		ID = iD;
		this.projectID = projectID;
		this.dataID = dataID;
		this.pipelineID = pipelineID;
		this.pipelineFile = pipelineFile;
		this.settingFile = settingFile;
		this.username = username;
		this.dateAdd = dateAdd;
		this.path = path;
		this.infoFile = infoFile;
		EMMethod = eMMethod;
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

	@Column(name="DataID")
	public String getDataID() {
		return dataID;
	}


	public void setDataID(String dataID) {
		this.dataID = dataID;
	}

	@Column(name="PipelineID")
	public String getPipelineID() {
		return pipelineID;
	}


	public void setPipelineID(String pipelineID) {
		this.pipelineID = pipelineID;
	}

	@Column(length=4096,name="PipelineFile")
	public String getPipelineFile() {
		return pipelineFile;
	}


	public void setPipelineFile(String pipelineFile) {
		this.pipelineFile = pipelineFile;
	}

	@Column(length=4096,name="Settingfile")
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

	@Column(name="DateAdd")
	public String getDateAdd() {
		return dateAdd;
	}


	public void setDateAdd(String dateAdd) {
		this.dateAdd = dateAdd;
	}

	@Column(length=4096,name="Path")
	public String getPath() {
		return path;
	}


	public void setPath(String path) {
		this.path = path;
	}

	@Column(length=4096, name="InfoFile")
	public String getInfoFile() {
		return infoFile;
	}


	public void setInfoFile(String infoFile) {
		this.infoFile = infoFile;
	}

	@Column(name="EMMethod")
	public String getEMMethod() {
		return EMMethod;
	}


	public void setEMMethod(String eMMethod) {
		EMMethod = eMMethod;
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
		return "JobsAuto [ID=" + ID + ", projectID=" + projectID + ", dataID=" + dataID + ", pipelineID=" + pipelineID
				+ ", pipelineFile=" + pipelineFile + ", settingFile=" + settingFile + ", username=" + username
				+ ", dateAdd=" + dateAdd + ", path=" + path + ", infoFile=" + infoFile + ", EMMethod=" + EMMethod
				+ ", description=" + description + ", jobStatus=" + jobStatus + ", quality=" + quality + "]";
	}
	
	

}
