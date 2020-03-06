//package com.cryomate.model;
//
//import javax.persistence.*;
//
//@Entity
//@Table(name="JOB_ParticlePicking")
//public class JobParticlePicking {
//	private String ID;
//	private String userName;
//	private String userGroup;
//	private String tagID;
//	private String parentTagIDs;
//	private String jobShortDescription;
//	private String importTime;
//	private String programName;
//	private String taskOutputPath;
//	private String taskInputParameterFileName;
//	private String  imageNum;
//	private String processQuality;
//	private String description;
//	
//	@Id
//	public String getID() {
//		return ID;
//	}
//	public void setID(String iD) {
//		ID = iD;
//	}
//	public String getUserName() {
//		return userName;
//	}
//	public void setUserName(String userName) {
//		this.userName = userName;
//	}
//	public String getUserGroup() {
//		return userGroup;
//	}
//	public void setUserGroup(String userGroup) {
//		this.userGroup = userGroup;
//	}
//	public String getTagID() {
//		return tagID;
//	}
//	public void setTagID(String tagID) {
//		this.tagID = tagID;
//	}
//	
//	@Column(length=4096)
//	public String getParentTagIDs() {
//		return parentTagIDs;
//	}
//	
//	
//	public void setParentTagIDs(String parentTagIDs) {
//		this.parentTagIDs = parentTagIDs;
//	}
//	public String getJobShortDescription() {
//		return jobShortDescription;
//	}
//	public void setJobShortDescription(String jobShortDescription) {
//		this.jobShortDescription = jobShortDescription;
//	}
//	public String getImportTime() {
//		return importTime;
//	}
//	public void setImportTime(String importTime) {
//		this.importTime = importTime;
//	}
//	public String getProgramName() {
//		return programName;
//	}
//	public void setProgramName(String programName) {
//		this.programName = programName;
//	}
//	public String getTaskOutputPath() {
//		return taskOutputPath;
//	}
//	public void setTaskOutputPath(String taskOutputPath) {
//		this.taskOutputPath = taskOutputPath;
//	}
//	public String getTaskInputParameterFileName() {
//		return taskInputParameterFileName;
//	}
//	public void setTaskInputParameterFileName(String taskInputParameterFileName) {
//		this.taskInputParameterFileName = taskInputParameterFileName;
//	}
//	public String getImageNum() {
//		return imageNum;
//	}
//	public void setImageNum(String imageNum) {
//		this.imageNum = imageNum;
//	}
//	public String getProcessQuality() {
//		return processQuality;
//	}
//	public void setProcessQuality(String processQuality) {
//		this.processQuality = processQuality;
//	}
//	public String getDescription() {
//		return description;
//	}
//	public void setDescription(String description) {
//		this.description = description;
//	}
//	@Override
//	public String toString() {
//		return "JobParticlePicking [ID=" + ID + ", userName=" + userName + ", userGroup=" + userGroup + ", tagID="
//				+ tagID + ", parentTagIDs=" + parentTagIDs + ", jobShortDescription=" + jobShortDescription
//				+ ", importTime=" + importTime + ", programName=" + programName + ", taskOutputPath=" + taskOutputPath
//				+ ", taskInputParameterFileName=" + taskInputParameterFileName + ", imageNum=" + imageNum
//				+ ", processQuality=" + processQuality + ", description=" + description + "]";
//	}
//	
//	
//
//}
