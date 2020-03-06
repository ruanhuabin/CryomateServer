//package com.cryomate.model;
//
//import javax.persistence.Column;
//import javax.persistence.Entity;
//import javax.persistence.EntityListeners;
//import javax.persistence.GeneratedValue;
//import javax.persistence.GenerationType;
//import javax.persistence.Id;
//import javax.persistence.Table;
//
//import org.springframework.data.jpa.domain.support.AuditingEntityListener;
//
//@Entity
//@Table(name = "SYS_Keyword")
//public class SystemKeyword {
//	
//	private String ID;
//	private String keyword;
//	private String dataType;
//	private String defaultValue;
//	private String description;
//	
//	
//	
//	@Id
//	//@GeneratedValue(strategy = GenerationType.IDENTITY)
//	public String getID() {
//		return ID;
//	}
//	public void setID(String iD) {
//		ID = iD;
//	}
//	
//	@Column(name = "keyword", nullable = false)
//	public String getKeyword() {
//		return keyword;
//	}
//	
//	
//	public void setKeyword(String keyword) {
//		this.keyword = keyword;
//	}
//	
//	@Column(name="datatype", nullable=false)
//	public String getDataType() {
//		return dataType;
//	}
//	public void setDataType(String dataType) {
//		this.dataType = dataType;
//	}
//	
//	@Column(name = "defaultvalue", nullable = false)
//	public String getDefaultValue() {
//		return defaultValue;
//	}
//	public void setDefaultValue(String defaultValue) {
//		this.defaultValue = defaultValue;
//	}
//	
//	@Column(name = "description", nullable = true)
//	public String getDescription() {
//		return description;
//	}
//	public void setDescription(String description) {
//		this.description = description;
//	}
//	
//	public SystemKeyword()
//	{
//		
//	}
//	
//	public SystemKeyword(String iD, String keyword, String dataType, String defaultValue, String description) {
//		
//		ID = iD;
//		this.keyword = keyword;
//		this.dataType = dataType;
//		this.defaultValue = defaultValue;
//		this.description = description;
//	}
//	@Override
//	public String toString() {
//		return "SystemKeywords [ID=" + ID + ", keyword=" + keyword + ", dataType=" + dataType + ", defaultValue="
//				+ defaultValue + ", description=" + description + "]";
//	}
//	
//	
//
//}
