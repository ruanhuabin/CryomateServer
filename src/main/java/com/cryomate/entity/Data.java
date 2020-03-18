package com.cryomate.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "Data")
public class Data
{
    private String ID;
    private String projectID;
    private String parentID;
    private String scriptID;
    private String scriptFile;
    private String settingFile;
    private String name;
    private String dataFormat;
    private String username;
    private String dateAdd;
    private String path;
    private String infoFile;
    private String quality;
    private String EMMethod;
    private String description;

    public Data()
    {

    }
    public Data(String iD, String projectID, String parentID, String scriptID, String scriptFile, String settingFile,
            String name, String dataFormat, String username, String dateAdd, String path, String infoFile,
            String quality, String eMMethod, String description)
    {

        ID               = iD;
        this.projectID   = projectID;
        this.parentID    = parentID;
        this.scriptID    = scriptID;
        this.scriptFile  = scriptFile;
        this.settingFile = settingFile;
        this.name        = name;
        this.dataFormat  = dataFormat;
        this.username    = username;
        this.dateAdd     = dateAdd;
        this.path        = path;
        this.infoFile    = infoFile;
        this.quality     = quality;
        EMMethod         = eMMethod;
        this.description = description;
    }

    @Id
    @Column(name = "ID")
    public String getID()
    {
        return ID;
    }
    public void setID(String iD)
    {
        ID = iD;
    }

    @Column(name = "ProjectID")
    public String getProjectID()
    {
        return projectID;
    }
    public void setProjectID(String projectID)
    {
        this.projectID = projectID;
    }

    @Column(name = "ParentID")
    public String getParentID()
    {
        return parentID;
    }
    public void setParentID(String parentID)
    {
        this.parentID = parentID;
    }

    @Column(name = "ScriptID")
    public String getScriptID()
    {
        return scriptID;
    }
    public void setScriptID(String scriptID)
    {
        this.scriptID = scriptID;
    }

    @Column(length = 2048, name = "ScriptFile")
    public String getScriptFile()
    {
        return scriptFile;
    }
    public void setScriptFile(String scriptFile)
    {
        this.scriptFile = scriptFile;
    }

    @Column(length = 2048, name = "SettingFile")
    public String getSettingFile()
    {
        return settingFile;
    }
    public void setSettingFile(String settingFile)
    {
        this.settingFile = settingFile;
    }

    @Column(name = "Name")
    public String getName()
    {
        return name;
    }
    public void setName(String name)
    {
        this.name = name;
    }

    @Column(name = "DataFormat")
    public String getDataFormat()
    {
        return dataFormat;
    }
    public void setDataFormat(String dataFormat)
    {
        this.dataFormat = dataFormat;
    }

    @Column(name = "Username")
    public String getUsername()
    {
        return username;
    }
    public void setUsername(String username)
    {
        this.username = username;
    }

    @Column(name = "DateAdd")
    public String getDateAdd()
    {
        return dateAdd;
    }
    public void setDateAdd(String dateAdd)
    {
        this.dateAdd = dateAdd;
    }

    @Column(length = 2048, name = "Path")
    public String getPath()
    {
        return path;
    }
    public void setPath(String path)
    {
        this.path = path;
    }

    @Column(length = 2048, name = "InfoFile")
    public String getInfoFile()
    {
        return infoFile;
    }
    public void setInfoFile(String infoFile)
    {
        this.infoFile = infoFile;
    }

    @Column(name = "Quality")
    public String getQuality()
    {
        return quality;
    }
    public void setQuality(String quality)
    {
        this.quality = quality;
    }

    @Column(name = "EMMethod")
    public String getEMMethod()
    {
        return EMMethod;
    }
    public void setEMMethod(String eMMethod)
    {
        EMMethod = eMMethod;
    }

    @Column(length = 4096, name = "Description")
    public String getDescription()
    {
        return description;
    }
    public void setDescription(String description)
    {
        this.description = description;
    }

    @Override
    public String toString()
    {
        return "Data [ID=" + ID + ", projectID=" + projectID + ", parentID=" + parentID + ", scriptID=" + scriptID
                + ", scriptFile=" + scriptFile + ", settingFile=" + settingFile + ", name=" + name + ", dataFormat="
                + dataFormat + ", username=" + username + ", dateAdd=" + dateAdd + ", path=" + path + ", infoFile="
                + infoFile + ", quality=" + quality + ", EMMethod=" + EMMethod + ", description=" + description + "]";
    }

}
