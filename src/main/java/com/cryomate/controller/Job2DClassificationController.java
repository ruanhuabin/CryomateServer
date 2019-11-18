package com.cryomate.controller;

import org.springframework.stereotype.Controller;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

@Controller
@RequestMapping("/api")
public class Job2DClassificationController {
	
	@RequestMapping("/download")
	//public String downloadFile(org.apache.catalina.servlet4preview.http.HttpServletRequest request,
	public String downloadFile(HttpServletRequest request,	HttpServletResponse response) {
		String fileNameToDownload = request.getParameter("filename");		
		System.out.println("===>file name: " + fileNameToDownload);
		String fileName = "tmp.txt";
		if (fileName != null) {
			String realPath = "E://";
			File file = new File(realPath, fileName);
			if (file.exists()) {
				response.setContentType("application/force-download");// 设置强制下载不打开
				response.addHeader("Content-Disposition", "attachment;fileName=" + fileName);// 设置文件名
				byte[] buffer = new byte[1024];
				FileInputStream fis = null;
				BufferedInputStream bis = null;
				try {
					fis = new FileInputStream(file);
					bis = new BufferedInputStream(fis);
					OutputStream os = response.getOutputStream();
					int i = bis.read(buffer);
					while (i != -1) {
						os.write(buffer, 0, i);
						i = bis.read(buffer);
					}
					System.out.println("success");
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
			}
		}
		return null;
	}
	
	@RequestMapping("/upload")
	@ResponseBody
	public String handleFileUpload(@RequestParam("file") MultipartFile file) {

		if (file.isEmpty()) {
			return "file is null";
		}
		// 获取文件名
		String fileName = file.getOriginalFilename();
		System.out.println("upload file：" + fileName);
		
		// 文件上传后的路径
		String filePath = "E://tmp/";
		File dest = new File(filePath + fileName);
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
	
	@RequestMapping(value = "/batch/upload", method = RequestMethod.POST)
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
