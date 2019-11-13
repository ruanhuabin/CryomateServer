package com.cryomate.model;

import javax.persistence.*;

@Entity
@Table(name="EM_Image")
public class EMImage {
	
	private String ID;
	private String userName;
	private String userGroup;
	private String emInfoID;
	private String insertTime;
	private String caremaName;
	private String caremaWorkingMode;
	private String exposureTimePerFrame;
	private String doseRate;
	private String totalIrradiationDose;
	private String genImageMode;
	private String logicMagnifyTimes;
	private String pixelSize;
	private String logicCameraLength;
	private String pixelSizeInMicroEDSpace;
	private String accelerationVoltage;
	private String sphericalAberrationCoefficient;
	private String aberration;
	private String usePhasePlate;
	private String energyFilteringLength;
	private String condenserDiaphragmSize;
	private String lensApertureSize;
	private String selectedApertureSize;
	private String defocusLowerLimit;
	private String defocusUpperLimit;
	private String amplitudeContrastCoefficient;
	private String description;
	
	

	@Id
	public String getID() {
		return ID;
	}

	public void setID(String iD) {
		ID = iD;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getUserGroup() {
		return userGroup;
	}

	public void setUserGroup(String userGroup) {
		this.userGroup = userGroup;
	}

	public String getEmInfoID() {
		return emInfoID;
	}

	public void setEmInfoID(String emInfoID) {
		this.emInfoID = emInfoID;
	}

	public String getInsertTime() {
		return insertTime;
	}

	public void setInsertTime(String insertTime) {
		this.insertTime = insertTime;
	}

	public String getCaremaName() {
		return caremaName;
	}

	public void setCaremaName(String caremaName) {
		this.caremaName = caremaName;
	}

	public String getCaremaWorkingMode() {
		return caremaWorkingMode;
	}

	public void setCaremaWorkingMode(String caremaWorkingMode) {
		this.caremaWorkingMode = caremaWorkingMode;
	}

	public String getExposureTimePerFrame() {
		return exposureTimePerFrame;
	}

	public void setExposureTimePerFrame(String exposureTimePerFrame) {
		this.exposureTimePerFrame = exposureTimePerFrame;
	}

	public String getDoseRate() {
		return doseRate;
	}

	public void setDoseRate(String doseRate) {
		this.doseRate = doseRate;
	}

	public String getTotalIrradiationDose() {
		return totalIrradiationDose;
	}

	public void setTotalIrradiationDose(String totalIrradiationDose) {
		this.totalIrradiationDose = totalIrradiationDose;
	}

	public String getGenImageMode() {
		return genImageMode;
	}

	public void setGenImageMode(String genImageMode) {
		this.genImageMode = genImageMode;
	}

	public String getLogicMagnifyTimes() {
		return logicMagnifyTimes;
	}

	public void setLogicMagnifyTimes(String logicMagnifyTimes) {
		this.logicMagnifyTimes = logicMagnifyTimes;
	}

	public String getPixelSize() {
		return pixelSize;
	}

	public void setPixelSize(String pixelSize) {
		this.pixelSize = pixelSize;
	}

	public String getLogicCameraLength() {
		return logicCameraLength;
	}

	public void setLogicCameraLength(String logicCameraLength) {
		this.logicCameraLength = logicCameraLength;
	}

	public String getPixelSizeInMicroEDSpace() {
		return pixelSizeInMicroEDSpace;
	}

	public void setPixelSizeInMicroEDSpace(String pixelSizeInMicroEDSpace) {
		this.pixelSizeInMicroEDSpace = pixelSizeInMicroEDSpace;
	}

	public String getAccelerationVoltage() {
		return accelerationVoltage;
	}

	public void setAccelerationVoltage(String accelerationVoltage) {
		this.accelerationVoltage = accelerationVoltage;
	}

	public String getSphericalAberrationCoefficient() {
		return sphericalAberrationCoefficient;
	}

	public void setSphericalAberrationCoefficient(String sphericalAberrationCoefficient) {
		this.sphericalAberrationCoefficient = sphericalAberrationCoefficient;
	}

	public String getAberration() {
		return aberration;
	}

	public void setAberration(String aberration) {
		this.aberration = aberration;
	}

	public String getUsePhasePlate() {
		return usePhasePlate;
	}

	public void setUsePhasePlate(String usePhasePlate) {
		this.usePhasePlate = usePhasePlate;
	}

	public String getEnergyFilteringLength() {
		return energyFilteringLength;
	}

	public void setEnergyFilteringLength(String energyFilteringLength) {
		this.energyFilteringLength = energyFilteringLength;
	}

	public String getCondenserDiaphragmSize() {
		return condenserDiaphragmSize;
	}

	public void setCondenserDiaphragmSize(String condenserDiaphragmSize) {
		this.condenserDiaphragmSize = condenserDiaphragmSize;
	}

	public String getLensApertureSize() {
		return lensApertureSize;
	}

	public void setLensApertureSize(String lensApertureSize) {
		this.lensApertureSize = lensApertureSize;
	}

	public String getSelectedApertureSize() {
		return selectedApertureSize;
	}

	public void setSelectedApertureSize(String selectedApertureSize) {
		this.selectedApertureSize = selectedApertureSize;
	}

	public String getDefocusLowerLimit() {
		return defocusLowerLimit;
	}

	public void setDefocusLowerLimit(String defocusLowerLimit) {
		this.defocusLowerLimit = defocusLowerLimit;
	}

	public String getDefocusUpperLimit() {
		return defocusUpperLimit;
	}

	public void setDefocusUpperLimit(String defocusUpperLimit) {
		this.defocusUpperLimit = defocusUpperLimit;
	}

	public String getAmplitudeContrastCoefficient() {
		return amplitudeContrastCoefficient;
	}

	public void setAmplitudeContrastCoefficient(String amplitudeContrastCoefficient) {
		this.amplitudeContrastCoefficient = amplitudeContrastCoefficient;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
	
	
	

}
