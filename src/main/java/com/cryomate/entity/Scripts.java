package com.cryomate.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="Scripts")
public class Scripts {
	
	private String ID;
	private String name;
	private String version;
	private String scriptFile;
	private String dateAdd;
	private String EMMethod;
	private String jobType;
	private String description;
	
	public Scripts() {
		// TODO Auto-generated constructor stub
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

	@Column(length=2048, name="ScriptFile")
	public String getScriptFile() {
		return scriptFile;
	}

	public void setScriptFile(String scriptFile) {
		this.scriptFile = scriptFile;
	}

	@Column(name="DateAdd")
	public String getDateAdd() {
		return dateAdd;
	}

	public void setDateAdd(String dateAdd) {
		this.dateAdd = dateAdd;
	}

	@Column(name="EMMethod")
	public String getEMMethod() {
		return EMMethod;
	}

	public void setEMMethod(String eMMethod) {
		EMMethod = eMMethod;
	}

	@Column(name="JobType")
	public String getJobType() {
		return jobType;
	}

	public void setJobType(String jobType) {
		this.jobType = jobType;
	}

	@Column(length=4096, name="Description")
	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
	
	
	
	

}
