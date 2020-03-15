package com.cryomate.pojo;

public class CryomateFileAttribute
{
	private String owner;
	private String group;
	public String getOwner()
	{
		return owner;
	}
	public void setOwner(String owner)
	{
		this.owner = owner;
	}
	public String getGroup()
	{
		return group;
	}
	public void setGroup(String group)
	{
		this.group = group;
	}
	@Override
	public String toString()
	{
		return "CryomateFileAttribute [owner=" + owner + ", group=" + group
		        + "]";
	}

	
}
