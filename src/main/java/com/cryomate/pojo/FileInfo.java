package com.cryomate.pojo;

public class FileInfo {
	private String fileName;
	private String fileType;
	private long fileLength;
	private String lastModifyTime;
	
	public String getFileName() {
		return fileName;
	}
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	public String getFileType() {
		return fileType;
	}
	public void setFileType(String fileType) {
		this.fileType = fileType;
	}
	public long getFileLength() {
		return fileLength;
	}
	public void setFileLength(long fileLength) {
		this.fileLength = fileLength;
	}
	public String getLastModifyTime() {
		return lastModifyTime;
	}
	public void setLastModifyTime(String lastModifyTime) {
		this.lastModifyTime = lastModifyTime;
	}
	@Override
	public String toString() {
		return "FileInfo [fileName=" + fileName + ", fileType=" + fileType + ", fileLength=" + fileLength
				+ ", lastModifyTime=" + lastModifyTime + "]";
	}
	
	
	
	

}
