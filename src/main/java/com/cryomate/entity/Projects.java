package com.cryomate.entity;

import javax.persistence.*;

@Entity
@Table(name="Projects")
public class Projects {
	private String ID;
	private String parentID;
	private String name;
	private String username;
	private String dateAdd;
	private String path;
	private String infoFile;
	private String quality;
	private String description;	
	
	public Projects() {
		
	}
	public Projects(String iD, String parentID, String name, String username, String dateAdd, String path,
			String infoFile, String quality, String description) {
		
		ID = iD;
		this.parentID = parentID;
		this.name = name;
		this.username = username;
		this.dateAdd = dateAdd;
		this.path = path;
		this.infoFile = infoFile;
		this.quality = quality;
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
	
	@Column(name="ParentID")
	public String getParentID() {
		return parentID;
	}
	public void setParentID(String parentID) {
		this.parentID = parentID;
	}
	
	@Column(name="Name")
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
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
	
	@Column(length=2048, name="Path")
	public String getPath() {
		return path;
	}
	public void setPath(String path) {
		this.path = path;
	}
	
	@Column(length=2048, name="InfoFile")
	public String getInfoFile() {
		return infoFile;
	}
	public void setInfoFile(String infoFile) {
		this.infoFile = infoFile;
	}
	
	@Column(name="Quality")
	public String getQuality() {
		return quality;
	}
	public void setQuality(String quality) {
		this.quality = quality;
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
		return "Projects [ID=" + ID + ", parentID=" + parentID + ", name=" + name + ", username=" + username
				+ ", dateAdd=" + dateAdd + ", path=" + path + ", infoFile=" + infoFile + ", quality=" + quality
				+ ", description=" + description + "]";
	}
	
	
	
}
