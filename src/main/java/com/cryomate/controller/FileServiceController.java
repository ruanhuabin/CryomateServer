package com.cryomate.controller;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.cryomate.entity.Users;
import com.cryomate.pojo.FileInfo;

import java.nio.file.*;
import java.nio.file.attribute.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Controller
@RequestMapping("/")
public class FileServiceController {
	
	private static final Logger logger = LoggerFactory.getLogger(FileServiceController.class);  
	
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
	
	@RequestMapping("/test/download")
	@ResponseBody
	public String downloadFile(HttpServletRequest request, HttpServletResponse response) throws IOException
	{
		String fileNameFullPath = request.getParameter("pFileFullPath");
		String offsetStr = request.getParameter("pOffset");
		String lengthStr = request.getParameter("pLength");
		
		long offset = 0;
		if(offsetStr != null && !offsetStr.equals(""))
		{
			offset = Long.parseLong(offsetStr);
		}
		
		long length = -1;
		if(lengthStr != null && !lengthStr.equals(""))
		{
			length = Long.parseLong(lengthStr);
		}
		
		logger.debug("fileNameFullPath = {}, length = {}, offset = {}", fileNameFullPath, length, offset);
		
		if(fileNameFullPath == null || fileNameFullPath.equals(""))
		{
			return "Error: pFileFullPath is null or empty";
		}
		
		int index = fileNameFullPath.lastIndexOf('/');
		String fileName = fileNameFullPath.substring(index + 1);
		String fileDir = fileNameFullPath.substring(0, index + 1);		
		
		logger.debug("fileDir = " + fileDir + ", fileName = " + fileName);	
		
		
		
		
		if (fileName != null)
		{			
			File file = new File(fileDir, fileName);						
			if (file.exists())
			{
				Path path = Paths.get(fileNameFullPath);       
			    // create object of file attribute.
		    FileOwnerAttributeView view = Files.getFileAttributeView(path,
		    FileOwnerAttributeView.class);    
		    // this will get the owner information.
		    UserPrincipal userPrincipal = view.getOwner();   
		    logger.debug("userPrincipal is not null");
		    // print information.
		    logger.debug("Owner of the file {} is {} ", fileNameFullPath, userPrincipal.getName());
		    String fileOwner = userPrincipal.getName();
		    Users currUser = (Users)request.getSession().getAttribute("userInfo");
		    if(currUser == null)
		        {
		        	logger.info("Error: no login user is found in current session");
		        	return "Error: No login user is found in current session";
		        }
		    String currUserName = currUser.getUserName();
		    
		    if(!currUserName.equals(fileOwner))
		    	{
		    		return "Error: Permission denied!";
		    	}	
				response.setContentType("application/force-download");// 设置强制下载不打开
				response.addHeader("Content-Disposition", "attachment;fileName=" + fileName);// 设置文件名
				byte[] buffer = new byte[1024];
				FileInputStream fis = null;
				BufferedInputStream bis = null;
				try
				{
					fis = new FileInputStream(file);
					bis = new BufferedInputStream(fis);
					OutputStream os = response.getOutputStream();
					int i = bis.read(buffer);
					while (i != -1)
					{
						os.write(buffer, 0, i);
						i = bis.read(buffer);
					}					
				}
				catch (Exception e)
				{
					e.printStackTrace();
				}
				finally
				{
					if (bis != null)
					{
						try
						{
							bis.close();
						}
						catch (IOException e)
						{
							e.printStackTrace();
						}
					}
					if (fis != null)
					{
						try
						{
							fis.close();
						}
						catch (IOException e)
						{
							e.printStackTrace();
						}
					}
				}
			}
			else
			{
				return "Error: File [ " + fileNameFullPath + " ] is not found ";
			}
		}
		return null;
	}

	@RequestMapping("/test/upload")
	@ResponseBody
	public String handleFileUpload(@RequestParam("file") MultipartFile file)
	{

		if (file.isEmpty())
		{
			return "file is null";
		}
		// 获取文件名
		String fileName = file.getOriginalFilename();
		System.out.println("upload file：" + fileName);

		// 文件上传后的路径
		String filePath = "E://tmp/";
		File dest = new File(filePath + fileName);
		// 检测是否存在目录
		if (!dest.getParentFile().exists())
		{
			dest.getParentFile().mkdirs();
		}
		try
		{
			file.transferTo(dest);
			return "upload success";
		}
		catch (IllegalStateException e)
		{
			e.printStackTrace();
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
		return "upload failed";
	}

	@RequestMapping(value = "/test/batch/upload", method = RequestMethod.POST)
	public @ResponseBody String uploadMultipleFiles(HttpServletRequest request)
	{
		List<MultipartFile> files = ((MultipartHttpServletRequest) request).getFiles("file");
		String filePath = "E://tmp//";
		MultipartFile file = null;

		for (int i = 0; i < files.size(); ++i)
		{
			file = files.get(i);
			// 获取文件名
			String fileName = file.getOriginalFilename();
			System.out.println("上传的文件名为：" + fileName);

			if (!file.isEmpty())
			{
				File dest = new File(filePath + fileName);
				// 检测是否存在目录
				if (!dest.getParentFile().exists())
				{
					dest.getParentFile().mkdirs();
				}
				try
				{
					file.transferTo(dest);
				}
				catch (Exception e)
				{
					return "上传失败 " + i + " => " + e.getMessage();
				}
			}
			else
			{
				return "上传失败 " + i + " 文件为空.";
			}
		}
		return "上传成功";
	}
	
	
	

}
