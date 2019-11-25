package com.cryomate.controller;

import org.springframework.stereotype.Controller;
import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
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

	private File gen2DClassTarFile(String outputTarFileName) {
		String filePath = "./warehouse/";
		// String fileName = "2DClass.tar";
		File file = new File(filePath, outputTarFileName);

		if (!file.getParentFile().exists()) {
			file.getParentFile().mkdirs();
		}
		System.out.println("File full path = " + file.getAbsolutePath());

		StringBuilder result = new StringBuilder();
		Process process = null;
		BufferedReader bufrIn = null;
		BufferedReader bufrError = null;
		try {
			String[] command = { "tar", "cvf", outputTarFileName, "2DClass" };
			// 执行命令, 返回一个子进程对象（命令在子进程中执行）
			process = Runtime.getRuntime().exec(command, null, new File(file.getParentFile().getAbsolutePath()));
			// 方法阻塞, 等待命令执行完成（成功会返回0）
			process.waitFor();
			// 获取命令执行结果, 有两个结果: 正常的输出 和 错误的输出（PS: 子进程的输出就是主进程的输入）
			bufrIn = new BufferedReader(new InputStreamReader(process.getInputStream(), "UTF-8"));
			bufrError = new BufferedReader(new InputStreamReader(process.getErrorStream(), "UTF-8"));
			// 读取输出
			String line;
			while ((line = bufrIn.readLine()) != null) {
				result.append(line).append('\n');
			}
			while ((line = bufrError.readLine()) != null) {
				// System.out.println("error message add: " + line + "\n");
				result.append(line).append('\n');
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				bufrIn.close();
				bufrError.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			// 销毁子进程
			if (process != null) {
				process.destroy();
			}

		}

		//System.out.println("result is: " + result);

		return file;
	}

	@RequestMapping("/cDisp_2DClass")
	@ResponseBody
	public String get2DClass(HttpServletRequest request, HttpServletResponse response) {
		String pParaString = request.getParameter("pParaString");
		String pID = request.getParameter("pID");
		String pRound = request.getParameter("pRound");
		String sNormalized = request.getParameter("sNormalized");
		String pSTD = request.getParameter("pSTD");
		String sRaw = request.getParameter("sRaw");

		String outputTarFileName = "2DClass.tar";
		File file = gen2DClassTarFile(outputTarFileName);

		String fileNameToDownload = request.getParameter("filename");
		//System.out.println("===>file name: " + fileNameToDownload);

		if (file.exists()) {
			response.setContentType("application/force-download");// 设置强制下载不打开
			response.addHeader("Content-Disposition", "attachment;fileName=" + outputTarFileName);// 设置文件名
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

		return "data send success";
	}

	@RequestMapping("/download")
	@ResponseBody
	public String downloadFile(HttpServletRequest request, HttpServletResponse response) {
		String fileNameToDownload = request.getParameter("filename");
		System.out.println("===>file name: " + fileNameToDownload);
		String fileName = "Office_Professional_Plus_2016_64Bit_ChnSimp_its.ISO";
		if (fileName != null) {
			String realPath = "E://Software//";
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
					// System.out.println("success");
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
		return "This message is return by download function";
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
