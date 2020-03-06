//package com.cryomate.model;
//
//import javax.persistence.*;
//
//@Entity
//@Table(name="data_particles")
//public class DataParticles {
//	
//	private String ID;
//	private String userName;
//	private String userGroup;
//	private String tagID;
//	private String parentTagID;
//	private String dataName;
//	private String dataDescription;
//	private String collectTime;
//	private String imagePath;
//	private String imageParameterFileName;
//	private String genImageEMID;
//	private String dataCollectType;
//	private String imageSaveType;	
//	private String pixelSize;
//	private String isCTFRefine;
//	private String isIrradiationRefine;
//    private String isShiftRefine;
//    private String totalIrradiationDos;
//	private String imageNum;
//	private String imageQuality;
//	private String description;
//	
//	
//    
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
//	public String getParentTagID() {
//		return parentTagID;
//	}
//	public void setParentTagID(String parentTagID) {
//		this.parentTagID = parentTagID;
//	}
//	public String getDataName() {
//		return dataName;
//	}
//	public void setDataName(String dataName) {
//		this.dataName = dataName;
//	}
//	public String getDataDescription() {
//		return dataDescription;
//	}
//	public void setDataDescription(String dataDescription) {
//		this.dataDescription = dataDescription;
//	}
//	public String getCollectTime() {
//		return collectTime;
//	}
//	public void setCollectTime(String collectTime) {
//		this.collectTime = collectTime;
//	}
//	public String getImagePath() {
//		return imagePath;
//	}
//	public void setImagePath(String imagePath) {
//		this.imagePath = imagePath;
//	}
//	public String getImageParameterFileName() {
//		return imageParameterFileName;
//	}
//	public void setImageParameterFileName(String imageParameterFileName) {
//		this.imageParameterFileName = imageParameterFileName;
//	}
//	public String getGenImageEMID() {
//		return genImageEMID;
//	}
//	public void setGenImageEMID(String genImageEMID) {
//		this.genImageEMID = genImageEMID;
//	}
//	public String getDataCollectType() {
//		return dataCollectType;
//	}
//	public void setDataCollectType(String dataCollectType) {
//		this.dataCollectType = dataCollectType;
//	}
//	public String getImageSaveType() {
//		return imageSaveType;
//	}
//	public void setImageSaveType(String imageSaveType) {
//		this.imageSaveType = imageSaveType;
//	}
//	
//	public String getImageNum() {
//		return imageNum;
//	}
//	public void setImageNum(String imageNum) {
//		this.imageNum = imageNum;
//	}
//	public String getImageQuality() {
//		return imageQuality;
//	}
//	public void setImageQuality(String imageQuality) {
//		this.imageQuality = imageQuality;
//	}
//	public String getDescription() {
//		return description;
//	}
//	public void setDescription(String description) {
//		this.description = description;
//	}
//	@Override
//	public String toString() {
//		return "DataRawMicrograph [ID=" + ID + ", userName=" + userName + ", userGroup=" + userGroup + ", tagID="
//				+ tagID + ", parentTagID=" + parentTagID + ", dataName=" + dataName + ", dataDescription="
//				+ dataDescription + ", collectTime=" + collectTime + ", imagePath=" + imagePath
//				+ ", imageParameterFileName=" + imageParameterFileName + ", genImageEMID=" + genImageEMID
//				+ ", dataCollectType=" + dataCollectType + ", imageSaveType=" + imageSaveType + ", isCompressed="
//				+ ", imageNum=" + imageNum + ", imageQuality=" + imageQuality + ", description="
//				+ description + "]";
//	}
//	
//	
//
//}
