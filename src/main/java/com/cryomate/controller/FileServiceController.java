package com.cryomate.controller;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.cryomate.entity.Users;
import com.cryomate.pojo.Constant;
import com.cryomate.pojo.CryomateFileAttribute;
import com.cryomate.pojo.FileInfo;
import com.cryomate.utils.FileUtils;

import java.nio.file.*;
import java.nio.file.attribute.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.io.IOException;
import java.nio.file.FileSystem;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
@Controller
@RequestMapping("/")
public class FileServiceController
{

	private static final Logger logger = LoggerFactory
	                .getLogger(FileServiceController.class);
	private static final int SUCCCESS_ACCESS_OK = 0;
	private static final int ERROR_NO_LOGIN_USER_IS_FOUND = 1;
	private static final int ERROR_OWNER_PERMISSION_DENIED = 2;
	private static final int SUCCESS = 0;
	private static final int FAILED = 1;

	@RequestMapping("/api/cList_Dir")
	@ResponseBody
	public List<FileInfo> listDir2(HttpServletRequest request,	
	                HttpServletResponse response) throws IOException, URISyntaxException
	{
		//Check whether the user is login
		Users loginUser = (Users)request.getSession().getAttribute("userInfo");
		if(loginUser == null)
		{
			response.getWriter().write(Constant.HTTP_RTN_TEXT_RESULT_PREFIX + "Error: No login user is found");
			return null;
		}		
		
		String dirPath = request.getParameter("pParaString");
		if (dirPath == null || dirPath.equals(""))
		{
			response.getWriter().write(Constant.HTTP_RTN_TEXT_RESULT_PREFIX + 
			                "Error: pParaString is null or empty");
			return null;
		}
		
		File fileDir = new File(dirPath);		
		logger.info("dir path: {}, absolute path: {}", dirPath, fileDir.getAbsolutePath());
		if(!fileDir.exists())
		{
			response.getWriter().write(Constant.HTTP_RTN_TEXT_RESULT_PREFIX + " Error: Directory does not exist");
			return null;
			
		}
		
		//Check the owner of targeted dir is owned by the current login user
		String userName = loginUser.getUserName();
		FileUtils fu = new FileUtils();
		String dirOwner = fu.getFileOwner(dirPath);
		//CryomateFileAttribute cfa = fu.getFileAttr(dirPath);
		//String dirOwner = cfa.getOwner();
		if(!userName.equals(dirOwner))
		{
			logger.info("login user:{}, dir owner is: {}", userName, dirOwner);
			response.getWriter().write(Constant.HTTP_RTN_TEXT_RESULT_PREFIX + "Error: Perssion denied");
			return null;
		}
		String[] files = fileDir.list();
		dirPath = dirPath + File.separator;		
		List<FileInfo> fileArray = new ArrayList<FileInfo>();
		SimpleDateFormat sdf = new SimpleDateFormat(
		                "yyyy/MM/dd HH:mm:ss");
		for (String f : files)
		{
			String fullPath = dirPath + f;
			File currFile = new File(fullPath);
			FileInfo fileInfo = new FileInfo();
			fileInfo.setFileName(f);
			long fileLength = 0;
			if (currFile.isFile())
			{
				fileLength = currFile.length();
			}
			fileInfo.setFileLength(fileLength);

			if (currFile.isDirectory())
			{
				fileInfo.setFileType("Path");
			} else
			{
				fileInfo.setFileType("File");
			}

			String lastModifyTime = sdf.format(currFile.lastModified());
			fileInfo.setLastModifyTime(lastModifyTime);
			fileArray.add(fileInfo);
		}

		//response.getWriter().write(Constant.HTTP_RTN_TEXT_RESULT_PREFIX + "\n");
		return fileArray;
	}	

	@RequestMapping("/api/cDownload")
	@ResponseBody
	public String downloadFile(HttpServletRequest request,
	                HttpServletResponse response) throws IOException
	{
		String fileNameFullPath = request.getParameter("pFileFullPath");
		String offsetStr = request.getParameter("pOffset");
		String lengthStr = request.getParameter("pLength");

		int offset = 0;
		if (offsetStr != null && !offsetStr.equals(""))
		{
			offset = Integer.parseInt(offsetStr);
		}

		int readLength = -1;
		if (lengthStr != null && !lengthStr.equals(""))
		{
			readLength = Integer.parseInt(lengthStr);
		}

		logger.debug("fileNameFullPath = {}, length = {}, offset = {}",
		                fileNameFullPath, readLength, offset);

		if (fileNameFullPath == null || fileNameFullPath.equals(""))
		{
			return "Error: pFileFullPath is null or empty";
		}

		int index = fileNameFullPath.lastIndexOf('/');
		String fileName = fileNameFullPath.substring(index + 1);
		String fileDir = fileNameFullPath.substring(0, index + 1);

		logger.debug("fileDir = " + fileDir + ", fileName = "
		                + fileName);

		if (fileName != null)
		{
			File file = new File(fileDir, fileName);
			if (file.exists())
			{
				int checkResult = checkPermission(request,
				                fileNameFullPath);
				if (checkResult == ERROR_NO_LOGIN_USER_IS_FOUND)
				{
					return "Error: No login user is found";
				} else if (checkResult == ERROR_OWNER_PERMISSION_DENIED)
				{
					return "Error: Current Login User is Forbidden to Accssess This File [ "
					                + fileNameFullPath
					                + " ]";
				}

				response.setContentType(
				                "application/force-download");// 设置强制下载不打开
				response.addHeader("Content-Disposition",
				                "attachment;fileName="
				                                + fileName);// 设置文件名

				FileInputStream fis = null;
				BufferedInputStream bis = null;
				try
				{
					fis = new FileInputStream(file);

					bis = new BufferedInputStream(fis);
					OutputStream os = response
					                .getOutputStream();
					if (readLength == -1) // read the whole
					                      // file starting
					                      // from offset
					{
						logger.debug("Start to read the whole file {} from offset {}",
						                fileNameFullPath,
						                offset);
						fis.skip(offset);
						byte[] buffer = new byte[1024];
						int i = bis.read(buffer);

						while (i != -1)
						{
							os.write(buffer, 0, i);
							i = bis.read(buffer);
						}
					} else
					{
						logger.debug("Start to read {} bytes from file {} starting from offset {}",
						                readLength,
						                fileNameFullPath,
						                offset);
						byte[] buffer = new byte[readLength];
						fis.skip(offset);
						int i = bis.read(buffer, 0,
						                readLength);

						if (i != -1)
						{
							os.write(buffer, 0, i);
						}
					}

				} catch (Exception e)
				{
					e.printStackTrace();
				} finally
				{
					if (bis != null)
					{
						try
						{
							bis.close();
						} catch (IOException e)
						{
							e.printStackTrace();
						}
					}
					if (fis != null)
					{
						try
						{
							fis.close();
						} catch (IOException e)
						{
							e.printStackTrace();
						}
					}
				}
			} else
			{
				return "Error: File [ " + fileNameFullPath
				                + " ] is not found ";
			}
		}
		return null;
	}

	private int checkPermission(HttpServletRequest request,
	                String fileNameFullPath) throws IOException
	{
		Path path = Paths.get(fileNameFullPath);
		// create object of file attribute.
		FileOwnerAttributeView view = Files.getFileAttributeView(path,
		                FileOwnerAttributeView.class);
		// this will get the owner information.
		UserPrincipal userPrincipal = view.getOwner();
		logger.debug("userPrincipal is not null");
		// print information.
		logger.debug("Owner of the file {} is {} ", fileNameFullPath,
		                userPrincipal.getName());
		String fileOwner = userPrincipal.getName();
		Users currUser = (Users) request.getSession()
		                .getAttribute("userInfo");
		if (currUser == null)
		{
			logger.info("Error: no login user is found in current session");
			// return "Error: No login user is found in current
			// session";
			return ERROR_NO_LOGIN_USER_IS_FOUND;
		}
		String currUserName = currUser.getUserName();

		if (!currUserName.equals(fileOwner))
		{
			return ERROR_OWNER_PERMISSION_DENIED;
		}

		return SUCCCESS_ACCESS_OK;
	}

	@RequestMapping("/api/cUpload")
	@ResponseBody
	public String uploadSingleFile(
	                @RequestParam("pFile") MultipartFile file,
	                @RequestParam("pTmpDir") String uploadDir,
	                HttpServletRequest request,
	                HttpServletResponse response)
	{
		Users currUser = (Users)request.getSession().getAttribute("userInfo");
		if(currUser == null)
		{
			logger.info("Error: No login user is found");
			return "Error: No login user is found";
		}
		
		//Using user home dir as the base dir for the uploaded file
		String uploadBaseDir = currUser.getHomeDir() + File.separatorChar;

		if (file.isEmpty())
		{
			return "Error: File is empty";
		}
		// 获取文件名
		String fileName = file.getOriginalFilename();
		logger.debug("upload file name：" + fileName);

		// 文件上传后的路径
		String filePath = uploadBaseDir + uploadDir + File.separatorChar;
		String fileFullName = filePath + fileName;
		File dest = new File(fileFullName);
		logger.debug("File upload absolute path: {}",
		                dest.getAbsolutePath());
		// 检测是否存在目录
		if (!dest.getParentFile().exists())
		{
			dest.getParentFile().mkdirs();
		}
		try
		{
			file.transferTo(dest);
			//Change the owner of uploaded file to the login user
			
			String currLoginUserName = currUser.getUserName();
			String currLoginUserGroup = currUser.getUserGroup();
			FileUtils fu = new FileUtils();
			int ret = fu.changeFileOwnerGroup(fileFullName, currLoginUserName, currLoginUserGroup);
			if(ret == SUCCESS)
			{
				logger.info("Success: Change owner of file {} to {} success", fileFullName, currLoginUserName);
			}
			else
			{
				logger.info("Failed: Change owner of file {} to {} success", fileFullName, currLoginUserName);
			}
			return "upload success";
		} catch (IllegalStateException e)
		{
			e.printStackTrace();
		} catch (IOException e)
		{
			e.printStackTrace();
		}
		return "upload failed";
	}

	

	@RequestMapping("/api/cMergeFiles")
	@ResponseBody
	public String mergeFiles(@RequestParam("pSrcFileDir") String srcFileDir,
							 @RequestParam("pSrcFileList") String[] srcFileList,
							 @RequestParam(value = "pDstFileDir", required=false) String dstFileDir,
							 @RequestParam("pDstFileName") String dstFileName,							 
							 HttpServletRequest request,
							 HttpServletResponse response) throws IOException
	{
		if (srcFileDir == null || srcFileDir.length() == 0)
		{
			return Constant.HTTP_RTN_STATUS_RESULT_PREFIX + "Error: Parameter [ pSrcFileDir ] is null or empty";
		}

		if (srcFileList == null || srcFileList.length == 0)
		{
			return Constant.HTTP_RTN_STATUS_RESULT_PREFIX + "Error: Parameter [  pSrcFileList ] is null or empty";
		}
		
		if(dstFileName == null || dstFileName.length() == 0)
		{
			return Constant.HTTP_RTN_STATUS_RESULT_PREFIX + "Error: Parameter [ pDstFileName ] is null or empty";
		}

		for (int i = 0; i < srcFileList.length; i++)
		{
			logger.debug("fileList[{}] = {}", i, srcFileList[i]);
		}
		
		srcFileDir = srcFileDir + File.separatorChar;
		
		if(dstFileDir == null || dstFileDir.length() == 0)
		{
			dstFileDir = srcFileDir;
		}
		
		String[] srcFileNamesFullPath = new String[srcFileList.length];
		for(int i = 0; i < srcFileList.length; i ++)
		{
			srcFileNamesFullPath[i] = srcFileDir + srcFileList[i];
			logger.debug("srcFileNamesFullPath[{}] = {}", i, srcFileNamesFullPath[i]);
		}
		
		String outputFileNameFullPath = dstFileDir + File.separatorChar + dstFileName;
		logger.debug("outputFileNameFullPath = " + outputFileNameFullPath);
		
		
		/* ....... */
		//check all files in srcFileList can be found.
		for(String fileName: srcFileNamesFullPath)
		{
			File file = new File(fileName);
			if(!file.exists())
			{
				return Constant.HTTP_RTN_STATUS_RESULT_PREFIX + "Error: File [ " + fileName + " ] does not exist";
			}
		}
		
		//check the owner of all the files belong to current login user
		Users user = (Users)request.getSession().getAttribute("userInfo");
		if(user == null)
		{	
			logger.info("Error: No login user is found");
			return Constant.HTTP_RTN_STATUS_RESULT_PREFIX + "Error: No login user is found";
		}
		String currOwner = user.getUserName();
		String currGroup = user.getUserGroup();
		FileUtils fileUtil = new FileUtils();
		for(String fileName: srcFileNamesFullPath)
		{
			CryomateFileAttribute cfa = fileUtil.getFileAttr(fileName);
			String owner = cfa.getOwner();
			String group = cfa.getGroup();
			if(!currOwner.equals(owner) || !currGroup.equals(group))
			{
				return Constant.HTTP_RTN_STATUS_RESULT_PREFIX + "Error: The owner or group of file [ " + fileName  + " ] is not the current login user [ " + currOwner + " , " + currGroup + " ]";
			}
			
		}
		//merge files
		fileUtil.mergeFiles(outputFileNameFullPath, srcFileNamesFullPath);		
	    //change owner and group of the merged file
		fileUtil.changeFileOwnerGroup(outputFileNameFullPath, currOwner, currGroup);		
		//return the full path of the merged file
		return Constant.HTTP_RTN_STATUS_RESULT_PREFIX + "Merge Success";
	}
	
	

	@RequestMapping(value = "/test/batch/upload", method = RequestMethod.POST)
	public @ResponseBody String uploadMultipleFiles(
	                HttpServletRequest request)
	{
		List<MultipartFile> files = ((MultipartHttpServletRequest) request)
		                .getFiles("file");
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
				} catch (Exception e)
				{
					return "上传失败 " + i + " => "
					                + e.getMessage();
				}
			} else
			{
				return "上传失败 " + i + " 文件为空.";
			}
		}
		return "上传成功";
	}
	
	@RequestMapping("/test/funTest")
	@ResponseBody
	public String funTest( HttpServletRequest request,
						   HttpServletResponse response) throws IOException
	{
		String fileNameFullPath = request.getParameter("pParaString");
		FileUtils fu = new FileUtils();
		CryomateFileAttribute cfa = fu.getFileAttr(fileNameFullPath);
		
		return cfa.toString();
	}
	

}
