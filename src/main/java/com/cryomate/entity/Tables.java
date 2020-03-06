package com.cryomate.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="Tables")
public class Tables {
	
	private String tableIndex;
	private String name;	
	
	public Tables() {
		super();
	}
	@Id
	@Column(name="TableIndex")
	public String getTableIndex() {
		return tableIndex;
	}
	public void setTableIndex(String tableIndex) {
		this.tableIndex = tableIndex;
	}
	
	@Column(name="Name")
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	@Override
	public String toString() {
		return "Tables [tableIndex=" + tableIndex + ", name=" + name + "]";
	}
	public Tables(String tableIndex, String name) {
		super();
		this.tableIndex = tableIndex;
		this.name = name;
	}
	
	

}
