//package com.cryomate.model;
//
//import javax.persistence.Column;
//import javax.persistence.Entity;
//import javax.persistence.Id;
//import javax.persistence.Table;
//
//@Entity
//@Table(name = "EM_Basic")
//public class EMBasic {
//	private String ID;
//	private String username;
//	private String usergroup;
//	private String parameterName;
//	private String EMName;
//	private String insertTime;
//	private String cameraName;
//	private String accelerationVoltage;
//	private String SphericalAberrationCoefficient;
//	private String aberration;
//	private String description;
//	
//	@Id
//	public String getID() {
//		return ID;
//	}
//	public void setID(String iD) {
//		ID = iD;
//	}
//	@Column(name = "user_name", nullable = false)
//	public String getUsername() {
//		return username;
//	}
//	public void setUsername(String username) {
//		this.username = username;
//	}
//	
//	@Column(name = "user_group", nullable = false)
//	public String getUsergroup() {
//		return usergroup;
//	}
//	public void setUsergroup(String usergroup) {
//		this.usergroup = usergroup;
//	}
//	
//	@Column(name = "parameter_name", nullable = false)
//	public String getParameterName() {
//		return parameterName;
//	}
//	public void setParameterName(String parameterName) {
//		this.parameterName = parameterName;
//	}
//	
//	@Column(name = "em_name", nullable = false)
//	public String getEMName() {
//		return EMName;
//	}
//	public void setEMName(String eMName) {
//		EMName = eMName;
//	}
//	
//	@Column(name = "insert_time", nullable = false)
//	public String getInsertTime() {
//		return insertTime;
//	}
//	public void setInsertTime(String insertTime) {
//		this.insertTime = insertTime;
//	}
//	
//	@Column(name = "camera_name", nullable = false)
//	public String getCameraName() {
//		return cameraName;
//	}
//	public void setCameraName(String cameraName) {
//		this.cameraName = cameraName;
//	}
//	
//	@Column(name = "acceleration_voltage", nullable = false)
//	public String getAccelerationVoltage() {
//		return accelerationVoltage;
//	}
//	public void setAccelerationVoltage(String accelerationVoltage) {
//		this.accelerationVoltage = accelerationVoltage;
//	}
//	
//	@Column(name = "spherical_berration_coefficient", nullable = false)
//	public String getSphericalAberrationCoefficient() {
//		return SphericalAberrationCoefficient;
//	}
//	public void setSphericalAberrationCoefficient(String sphericalAberrationCoefficient) {
//		SphericalAberrationCoefficient = sphericalAberrationCoefficient;
//	}
//	
//	@Column(name = "aberration", nullable = false)
//	public String getAberration() {
//		return aberration;
//	}
//	public void setAberration(String aberration) {
//		this.aberration = aberration;
//	}
//	
//	@Column(name = "description", nullable = false)
//	public String getDescription() {
//		return description;
//	}
//	public void setDescription(String description) {
//		this.description = description;
//	}
//	
//	
//	public EMBasic() {
//		super();
//	}
//	public EMBasic(String iD, String username, String usergroup, String parameterName, String eMName, String insertTime,
//			String cameraName, String accelerationVoltage, String sphericalAberrationCoefficient, String aberration,
//			String description) {
//		super();
//		ID = iD;
//		this.username = username;
//		this.usergroup = usergroup;
//		this.parameterName = parameterName;
//		EMName = eMName;
//		this.insertTime = insertTime;
//		this.cameraName = cameraName;
//		this.accelerationVoltage = accelerationVoltage;
//		SphericalAberrationCoefficient = sphericalAberrationCoefficient;
//		this.aberration = aberration;
//		this.description = description;
//	}
//	@Override
//	public String toString() {
//		return "EMBasic [ID=" + ID + ", username=" + username + ", usergroup=" + usergroup + ", parameterName="
//				+ parameterName + ", EMName=" + EMName + ", insertTime=" + insertTime + ", cameraName=" + cameraName
//				+ ", accelerationVoltage=" + accelerationVoltage + ", SphericalAberrationCoefficient="
//				+ SphericalAberrationCoefficient + ", aberration=" + aberration + ", description=" + description + "]";
//	}
//	
//	
//
//}
