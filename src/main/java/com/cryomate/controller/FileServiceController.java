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
	private static final int SUCCCESS_ACCESS_OK = 0;
	private static final int ERROR_NO_LOGIN_USER_IS_FOUND = 1;
	private static final int ERROR_OWNER_PERMISSION_DENIED = 2;

	@RequestMapping("/test/cList_Dir")
	@ResponseBody
	public List<FileInfo> listDir(HttpServletRequest request, HttpServletResponse response) throws IOException {
		String dirPath = request.getParameter("pDirPath");
		if (dirPath == null || dirPath.equals("")) {
			response.getWriter().write("Error: pDirPath is null or empty");
			return null;
		}

		if (System.getProperty("os.name").toLowerCase().indexOf("linux") >= 0 && dirPath.charAt(0) != '/') {
			response.getWriter().write("Error: pDirPath is not an absolute path");
			return null;
		}

		File fileDir = new File(dirPath);
		if (!fileDir.exists()) {
			System.out.println("Error: pDirPath [ " + dirPath + " ] is not exist");
			response.getWriter().write("Error: pDirPath [ " + dirPath + " ] is not exist");
			return null;
		}
		String[] files = fileDir.list();

		// 若用户没有对该目录的读取权限，list方法会返回null
		if (files == null) {
			response.getWriter().write("Error: Permission denied");
			return null;
		}

		dirPath = dirPath + File.separator;
		System.out.println("dir path = " + dirPath);
		List<FileInfo> fileArray = new ArrayList<FileInfo>();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
		for (String f : files) {
			String fullPath = dirPath + f;
			File currFile = new File(fullPath);
			FileInfo fileInfo = new FileInfo();
			fileInfo.setFileName(f);
			long fileLength = 0;
			if (currFile.isFile()) {
				fileLength = currFile.length();
			}
			fileInfo.setFileLength(fileLength);

			if (currFile.isDirectory()) {
				fileInfo.setFileType("Path");
			} else {
				fileInfo.setFileType("File");
			}

			String lastModifyTime = sdf.format(currFile.lastModified());

			fileInfo.setLastModifyTime(lastModifyTime);
			fileArray.add(fileInfo);
		}

		return fileArray;

	}

	@RequestMapping("/api/pDownload")
	@ResponseBody
	public String downloadFile(HttpServletRequest request, HttpServletResponse response) throws IOException {
		String fileNameFullPath = request.getParameter("pFileFullPath");
		String offsetStr = request.getParameter("pOffset");
		String lengthStr = request.getParameter("pLength");

		int offset = 0;
		if (offsetStr != null && !offsetStr.equals("")) {
			offset = Integer.parseInt(offsetStr);
		}

		int readLength = -1;
		if (lengthStr != null && !lengthStr.equals("")) {
			readLength = Integer.parseInt(lengthStr);
		}

		logger.debug("fileNameFullPath = {}, length = {}, offset = {}", fileNameFullPath, readLength, offset);

		if (fileNameFullPath == null || fileNameFullPath.equals("")) {
			return "Error: pFileFullPath is null or empty";
		}

		int index = fileNameFullPath.lastIndexOf('/');
		String fileName = fileNameFullPath.substring(index + 1);
		String fileDir = fileNameFullPath.substring(0, index + 1);

		logger.debug("fileDir = " + fileDir + ", fileName = " + fileName);

		if (fileName != null) {
			File file = new File(fileDir, fileName);
			if (file.exists()) {
				int checkResult = checkPermission(request, fileNameFullPath);
				if (checkResult == ERROR_NO_LOGIN_USER_IS_FOUND) {
					return "Error: No login user is found";
				} else if (checkResult == ERROR_OWNER_PERMISSION_DENIED) {
					return "Error: Current Login User is Forbidden to Accssess This File [ " + fileNameFullPath + " ]";
				}

				response.setContentType("application/force-download");// 设置强制下载不打开
				response.addHeader("Content-Disposition", "attachment;fileName=" + fileName);// 设置文件名

				FileInputStream fis = null;
				BufferedInputStream bis = null;
				try {
					fis = new FileInputStream(file);

					bis = new BufferedInputStream(fis);
					OutputStream os = response.getOutputStream();
					if (readLength == -1) // read the whole file starting from offset
					{
						logger.debug("Start to read the whole file {} from offset {}", fileNameFullPath, offset);
						fis.skip(offset);
						byte[] buffer = new byte[1024];
						int i = bis.read(buffer);

						while (i != -1) {
							os.write(buffer, 0, i);
							i = bis.read(buffer);
						}
					} else {
						logger.debug("Start to read {} bytes from file {} starting from offset {}", readLength,
								fileNameFullPath, offset);
						byte[] buffer = new byte[readLength];
						fis.skip(offset);
						int i = bis.read(buffer, 0, readLength);

						if (i != -1) {
							os.write(buffer, 0, i);
						}
					}

				} catch (Exception e) {
					e.printStackTrace();
				} finally {
					if (bis != null) {
						try {
							bis.close();
						} catch (IOException e) {
							e.printStackTrace();
						}
					}
					if (fis != null) {
						try {
							fis.close();
						} catch (IOException e) {
							e.printStackTrace();
						}
					}
				}
			} else {
				return "Error: File [ " + fileNameFullPath + " ] is not found ";
			}
		}
		return null;
	}

	private int checkPermission(HttpServletRequest request, String fileNameFullPath) throws IOException {
		Path path = Paths.get(fileNameFullPath);
		// create object of file attribute.
		FileOwnerAttributeView view = Files.getFileAttributeView(path, FileOwnerAttributeView.class);
		// this will get the owner information.
		UserPrincipal userPrincipal = view.getOwner();
		logger.debug("userPrincipal is not null");
		// print information.
		logger.debug("Owner of the file {} is {} ", fileNameFullPath, userPrincipal.getName());
		String fileOwner = userPrincipal.getName();
		Users currUser = (Users) request.getSession().getAttribute("userInfo");
		if (currUser == null) {
			logger.info("Error: no login user is found in current session");
			// return "Error: No login user is found in current session";
			return ERROR_NO_LOGIN_USER_IS_FOUND;
		}
		String currUserName = currUser.getUserName();

		if (!currUserName.equals(fileOwner)) {
			return ERROR_OWNER_PERMISSION_DENIED;
		}

		return SUCCCESS_ACCESS_OK;
	}

	@RequestMapping("/test/pUpload")
	@ResponseBody
	public String uploadSingleFile(@RequestParam("pFile") MultipartFile file, @RequestParam("pTmpDir") String uploadDir,
			HttpServletRequest request, HttpServletResponse response) {

		if (file.isEmpty()) {
			return "Error: File is empty";
		}
		// 获取文件名
		String fileName = file.getOriginalFilename();
		logger.debug("upload file name：" + fileName);

		// 文件上传后的路径
		String filePath = uploadDir + File.separatorChar;
		File dest = new File(filePath + fileName);
		logger.debug("File upload absolute path: {}", dest.getAbsolutePath());
		// 检测是否存在目录
		if (!dest.getParentFile().exists()) {
			dest.getParentFile().mkdirs();
		}
		try {
			file.transferTo(dest);
			return "upload success";
		} catch (IllegalStateException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return "upload failed";
	}

	@RequestMapping("/test/pMergeFiles")
	@ResponseBody
	public String mergeFiles(@RequestParam("pFileDir") String fileDir, @RequestParam("pFileList") String[] fileList,
			HttpServletRequest request, HttpServletResponse response)
	{
		if(fileDir == null || fileDir.length() == 0)
		{
			return "Error: Parameter [ pFileDir ] is null or empty";
		}
		
		if(fileList == null || fileList.length == 0)
		{
			return "Error: Parameter [  pFileList ] is null or empty";
		}
		
		for(int i = 0; i < fileList.length; i ++)
		{
			logger.debug("fileList[{}] = {}", i, fileList[i]);
		}
		
		

		return "Merge Success";
	}

	@RequestMapping(value = "/test/batch/upload", method = RequestMethod.POST)
	public @ResponseBody String uploadMultipleFiles(HttpServletRequest request) {
		List<MultipartFile> files = ((MultipartHttpServletRequest) request).getFiles("file");
		String filePath = "E://tmp//";
		MultipartFile file = null;

		for (int i = 0; i < files.size(); ++i) {
			file = files.get(i);
			// 获取文件名
			String fileName = file.getOriginalFilename();
			System.out.println("上传的文件名为：" + fileName);

			if (!file.isEmpty()) {
				File dest = new File(filePath + fileName);
				// 检测是否存在目录
				if (!dest.getParentFile().exists()) {
					dest.getParentFile().mkdirs();
				}
				try {
					file.transferTo(dest);
				} catch (Exception e) {
					return "上传失败 " + i + " => " + e.getMessage();
				}
			} else {
				return "上传失败 " + i + " 文件为空.";
			}
		}
		return "上传成功";
	}

}
