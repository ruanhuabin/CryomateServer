package com.cryomate.controller;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.cryomate.pojo.FileInfo;

@Controller
@RequestMapping("/")
public class FileServiceController {
	
	@RequestMapping("/test/cList_Dir")
	@ResponseBody
	public List<FileInfo> listDir(HttpServletRequest request, HttpServletResponse response) throws IOException 
	{
		String dirPath = request.getParameter("pDirPath");
		if(dirPath == null || dirPath.equals(""))
		{
			response.getWriter().write("Error: pDirPath is null or empty");
			return null;
		}
		
		if(System.getProperty("os.name").toLowerCase().indexOf("linux") >= 0 && dirPath.charAt(0) != '/' )
		{
			response.getWriter().write("Error: pDirPath is not an absolute path");
			return null;
		}
		
		
		
		File fileDir = new File(dirPath);			
		if(!fileDir.exists())
		{
			System.out.println("Error: pDirPath [ " + dirPath + " ] is not exist");
			response.getWriter().write("Error: pDirPath [ " + dirPath + " ] is not exist");
			return null;			
		}
		String[] files = fileDir.list();		
		
		//若用户没有对该目录的读取权限，list方法会返回null
		if(files == null)
		{
			response.getWriter().write("Error: Permission denied");
			return null;
		}
		
		dirPath = dirPath + File.separator;
		System.out.println("dir path = " + dirPath);
		List<FileInfo> fileArray = new ArrayList<FileInfo>();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
		for(String f: files)
		{
			String fullPath = dirPath + f;
			File currFile = new File(fullPath);
			FileInfo fileInfo = new FileInfo();
			fileInfo.setFileName(f);
			long fileLength = 0;
			if(currFile.isFile())
			{
				fileLength = currFile.length();				
			}
			fileInfo.setFileLength(fileLength);
			
			if(currFile.isDirectory())
			{
				fileInfo.setFileType("Path");
			}
			else
			{
				fileInfo.setFileType("File");
			}
			
			String lastModifyTime = sdf.format(currFile.lastModified());
			
			fileInfo.setLastModifyTime(lastModifyTime);
			fileArray.add(fileInfo);
		}
		
		return fileArray;
	
		
	}	

}
