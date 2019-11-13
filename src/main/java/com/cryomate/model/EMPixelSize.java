package com.cryomate.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "EM_PixelSize")
public class EMPixelSize {
	
	private String ID;
	private String username;
	private String usergroup;
	private String parameterName;
	private String EMInfoID;
	private String insertTime;
	private String cameraName;
	private String cameraWorkingMode;
	private String genImageMode;
	private String logicMagnifyTimes;
	private String pixelSize;
	private String logicCameraLength;
	private String pixelSizeInMicroEDSpace;
	
	
	@Id
	public String getID() {
		return ID;
	}
	public void setID(String iD) {
		ID = iD;
	}
	
	@Column(name = "user_name", nullable = false)
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	
	@Column(name = "user_group", nullable = false)
	public String getUsergroup() {
		return usergroup;
	}
	public void setUsergroup(String usergroup) {
		this.usergroup = usergroup;
	}
	@Column(name = "parameter_name", nullable = false)
	public String getParameterName() {
		return parameterName;
	}
	public void setParameterName(String parameterName) {
		this.parameterName = parameterName;
	}
	
	@Column(name = "em_info_id", nullable = false)
	public String getEMInfoID() {
		return EMInfoID;
	}
	public void setEMInfoID(String eMInfoID) {
		EMInfoID = eMInfoID;
	}
	
	@Column(name = "insert_time", nullable = false)
	public String getInsertTime() {
		return insertTime;
	}
	public void setInsertTime(String insertTime) {
		this.insertTime = insertTime;
	}
	
	@Column(name = "carema_name", nullable = false)
	public String getCameraName() {
		return cameraName;
	}
	public void setCameraName(String cameraName) {
		this.cameraName = cameraName;
	}
	
	@Column(name = "carema_working_mode", nullable = false)
	public String getCameraWorkingMode() {
		return cameraWorkingMode;
	}
	public void setCameraWorkingMode(String cameraWorkingMode) {
		this.cameraWorkingMode = cameraWorkingMode;
	}
	
	@Column(name = "gen_image_mode", nullable = false)
	public String getGenImageMode() {
		return genImageMode;
	}
	public void setGenImageMode(String genImageMode) {
		this.genImageMode = genImageMode;
	}
	
	@Column(name = "logic_magnify_times", nullable = false)
	public String getLogicMagnifyTimes() {
		return logicMagnifyTimes;
	}
	public void setLogicMagnifyTimes(String logicMagnifyTimes) {
		this.logicMagnifyTimes = logicMagnifyTimes;
	}
	
	@Column(name = "pixel_size", nullable = false)
	public String getPixelSize() {
		return pixelSize;
	}
	public void setPixelSize(String pixelSize) {
		this.pixelSize = pixelSize;
	}
	
	@Column(name = "logic_carema_length", nullable = false)
	public String getLogicCameraLength() {
		return logicCameraLength;
	}
	public void setLogicCameraLength(String logicCameraLength) {
		this.logicCameraLength = logicCameraLength;
	}
	
	@Column(name = "pixel_size_in_microED_space", nullable = false)
	public String getPixelSizeInMicroEDSpace() {
		return pixelSizeInMicroEDSpace;
	}
	public void setPixelSizeInMicroEDSpace(String pixelSizeInMicroEDSpace) {
		this.pixelSizeInMicroEDSpace = pixelSizeInMicroEDSpace;
	}
	@Override
	public String toString() {
		return "EMPixelSize [ID=" + ID + ", username=" + username + ", usergroup=" + usergroup + ", parameterName="
				+ parameterName + ", EMInfoID=" + EMInfoID + ", insertTime=" + insertTime + ", cameraName=" + cameraName
				+ ", cameraWorkingMode=" + cameraWorkingMode + ", genImageMode=" + genImageMode + ", logicMagnifyTimes="
				+ logicMagnifyTimes + ", pixelSize=" + pixelSize + ", logicCameraLength=" + logicCameraLength
				+ ", pixelSizeInMicroEDSpace=" + pixelSizeInMicroEDSpace + "]";
	}
	
	

}
