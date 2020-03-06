package com.cryomate.controller;

import org.apache.tomcat.jni.OS;
import org.aspectj.weaver.ast.Var;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import java.util.UUID;
import com.fasterxml.uuid.Generators;

//import com.cryomate.model.Job2DClassification;

//import com.cryomate.repository.Job2DClassificationRepository;
import com.cryomate.utils.JDBCUtils;
import com.cryomate.utils.KeyGenerator;

@Controller
@RequestMapping("/")
public class QueryController {
	//@Autowired
	//private Job2DClassificationRepository job2DRepository;



	//@RequestMapping("/api2/cSys_Command")
	@RequestMapping("/api/cSys_Command")
	@ResponseBody
	public String executeSystemCommand(HttpServletRequest request, HttpServletResponse response) {
		String cmdString = request.getParameter("pParaString");
		String command[];

		if (cmdString != null && cmdString != "") {

			command = cmdString.split(" ");
		} else {
			return "pParaString is not valid";
		}

		System.out.print("command = ");
		for (String cmd : command) {
			System.out.print(cmd + " ");
		}
		System.out.println();

		StringBuffer result = new StringBuffer();
		Process process = null;
		BufferedReader bufrIn = null;
		BufferedReader bufrError = null;
		try {
			// String[] command = { command};
			// 执行命令, 返回一个子进程对象（命令在子进程中执行）
			process = Runtime.getRuntime().exec(command, null, new File("./"));
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
			System.out.println("====================>execute failed1");
			return "command executes failed";
		} finally {
			try {
				bufrIn.close();
				bufrError.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				System.out.println("====================>execute failed2");
				return "command executes failed";
			}

			// 销毁子进程
			if (process != null) {
				process.destroy();
			}

		}

		return result.toString();
	}

	private File gen2DClassTarFile(String outputTarFileName) {

		String filePath = "./warehouse/";
		// String fileName = "2DClass.tar";
		File file = new File(filePath, outputTarFileName);

		if (!file.getParentFile().exists()) {
			file.getParentFile().mkdirs();
		}
		System.out.println("File full path = " + file.getAbsolutePath());

		StringBuffer result = new StringBuffer();
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

		// System.out.println("result is: " + result);

		return file;
	}

	@RequestMapping("/api/cDisp_2DClass")
	@ResponseBody
	public String cDisp2DClass(HttpServletRequest request, HttpServletResponse response) {
		String pID = request.getParameter("pID");
		String pRound = request.getParameter("pRound");
		String sNormalized = request.getParameter("sNormalized");
        String pMean = request.getParameter("pMean");
		String pStd = request.getParameter("pStd");
		String sRAW = request.getParameter("sRAW");
        String sMRC = request.getParameter("sMRC");
        String sJPEG = request.getParameter("sJPEG");


        if(sNormalized == null)
        {
            System.out.println("sNormalized is null");
        }
        if(sNormalized != null && sNormalized.length() == 0)
        {
            System.out.println("sNormalized is an empty string");
        }

		System.out.printf("pID = %s, pRound = %s, sNormalized = %s, sRaw = %s, sMRC = %s, sJPEG = %s, pMean = %s, pStd = %s\n", pID, pRound, sNormalized, sRAW, sMRC, sJPEG, pMean, pStd);

		if (pID == "" || pID == null) {
			return "Error: pID is not set";
		}

		if (pRound == "" || pRound == null) {
			pRound = "Final";
		}



        if(sNormalized != null && sNormalized.length() == 0 && sJPEG != null && sJPEG.length() == 0)
        {
            return "Error: Parameter sNormalized and sJPEG should not set simultaneously from client.";
        }

		if(sRAW != null && sMRC != null)
        {
        
            return "Error: Parameter sRAW and sMRC should not set simultaneously from client.";
        }

        if(sJPEG != null && sMRC != null)
        {
        
            return "Error: Parameter sJPEG and sMRC should not set simultaneously from client.";
        }

        if(sJPEG != null && sRAW != null)
        {
        
            return "Error: Parameter sJPEG and sRAW should not set simultaneously from client.";
        }


        String mrcsFileName = "Reference_Round_" + pRound + ".mrcs";
		System.out.printf("2D Class file name: %s\n", mrcsFileName);

        String outputDir = Generators.timeBasedGenerator().generate().toString();
        System.out.printf("output dir: %s\n", outputDir);
        
        String argumentString = "--input ./warehouse/" + mrcsFileName + " --output ./warehouse/imageDir/" + outputDir;

        if(sNormalized != null)
        {
            argumentString = argumentString + " --sNormalized";
        }


        if(sRAW != null)
        {
            argumentString = argumentString + " --sRAW";
        }
        
        if(sMRC != null)
        {
            argumentString = argumentString + " --sMRC";
        }

        if(sJPEG != null)
        {
            argumentString = argumentString + "  --sJPEG";
        }

        if(pMean != null)
        {
            argumentString = argumentString + " --pMean " + pMean;
        }
        else
        {
            argumentString = argumentString + " --pMean 0"; 
        }

        if(pStd != null)
        {
            argumentString = argumentString + " --pStd " + pStd;
        }
        else
        {
            if(sNormalized == null)
            {
                argumentString = argumentString + " --pStd 1";
            }
            else
            {
                argumentString = argumentString + " --pStd 3";
            }
        }

        System.out.printf("Argument String = %s\n", argumentString);

        String paraItems[] = argumentString.split(" ");
        System.out.println("length of paraItems: " + paraItems.length);
        


        String command[] = new String[paraItems.length + 1];
        command[0] = "warehouse/bin/mrcs2jpeg";

        for(int i = 0; i < paraItems.length; i ++)
        {
            command[i + 1] =  paraItems[i].trim();
        }

        System.out.println("-->command to be executed:");
        for(String item: command)
        {
            System.out.print(item + " ");
        }
        System.out.println();


        String result = convertToImage(command);
        if(result.length() != 0)
        {
            return "Error: Failed to generate Image [ detail:  " + result + " ]";
        }

        File tarFile = compressImageDir("./warehouse/imageDir/", outputDir, outputDir + ".tar");

        transferToClient(response, tarFile);

        return null;


		//Job2DClassification job2dClassification = job2DRepository.getOne(pID);

		//String scriptName = job2dClassification.getProgramName();
		//String taskOutputPath = job2dClassification.getTaskOutputPath();
		//String mrcsFileFullName = taskOutputPath + File.separator + mrcsFileName;

		//System.out.println("program name = " + job2dClassification.getProgramName());
		//System.out.println("mrcs file full name " + mrcsFileFullName);

		//String cmdToRun = scriptName + " --input " + mrcsFileFullName;

		//if (pStd != null && pStd != "") {
		//    cmdToRun = cmdToRun + " --pStd " + pStd;
		//}

		//if (sNormalized != null && sNormalized != "") {
		//    cmdToRun = cmdToRun + " --sNormalized" + sNormalized;
		//}

		//if (sRAW != null && sRAW != "") {
		//    cmdToRun = cmdToRun + " --sRaw " + sRAW;
		//}

		//System.out.println("command to run: " + cmdToRun);

		//return null;
	}


    private void transferToClient(HttpServletResponse response, File tarFile) {
		//String outputTarFileName = "2DClass.tar";
		//File file = gen2DClassTarFile(outputTarFileName);

		//String fileNameToDownload = request.getParameter("filename");
		// System.out.println("===>file name: " + fileNameToDownload);

		if (tarFile.exists()) {
			response.setContentType("application/force-download");// 设置强制下载不打开
			response.addHeader("Content-Disposition", "attachment;fileName=" + tarFile.getName());// 设置文件名
			byte[] buffer = new byte[1024];
			FileInputStream fis = null;
			BufferedInputStream bis = null;
			try {
				fis = new FileInputStream(tarFile);
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

		//return "data send success";
	}




    private File compressImageDir(String resultDir, String dirToBeCompressed, String outputTarFileName)
    {
        //String dirName = "./warehouse/";
		File file = new File(resultDir, outputTarFileName);

        if (!file.getParentFile().exists())
        {
           file.getParentFile().mkdirs();
        }
		System.out.println("File full path = " + file.getAbsolutePath());

		StringBuffer result = new StringBuffer();
		Process process = null;
		BufferedReader bufrIn = null;
		BufferedReader bufrError = null;
		try {
			String[] command = { "tar", "cvf", outputTarFileName, dirToBeCompressed};
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

		// System.out.println("result is: " + result);

		return file;

    }

    private String convertToImage(String[] command)
    {

		StringBuffer result = new StringBuffer();
		Process process = null;
		BufferedReader bufrIn = null;
		BufferedReader bufrError = null;
		try {
			// 执行命令, 返回一个子进程对象（命令在子进程中执行）
			process = Runtime.getRuntime().exec(command, null, null);
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

		System.out.println("result is: " + result);

        return result.toString();


    }

	@RequestMapping("/api/orig/cDisp_2DClass")
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
		// System.out.println("===>file name: " + fileNameToDownload);

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

//	@RequestMapping("/api/execSQL")
//	@ResponseBody
//	public String execSQL(HttpServletRequest request, HttpServletResponse response) {
//		UserExample employee = null;
//		List<UserExample> list = new ArrayList();
//		Connection conn = null;
//		PreparedStatement ps = null;
//		ResultSet result = null;
//		String sql = "select * from users";
//		try {
//			// 创建连接
//			conn = JDBCUtils.getConnetions();
//			// 创建prepareStatement对象，用于执行SQL
//			ps = conn.prepareStatement(sql);
//			// 获取查询结果集
//			result = ps.executeQuery();
//			while (result.next()) {
//				// employee = new
//				// User(result.getInt(1),result.getString(2),result.getString(3));
//				employee = new UserExample();
//				employee.setId(result.getInt(1));
//				employee.setFirstName(result.getString(2));
//				employee.setLastName(result.getString(3));
//				list.add(employee);
//
//				System.out.printf("user id = %d, firstname = %s, lastname = %s\n", employee.getId(),
//						employee.getFirstName(), employee.getLastName());
//			}
//			System.out.println("list = " + list);
//		} catch (Exception e) {
//			e.printStackTrace();
//		} finally {
//			JDBCUtils.release(result, ps, conn);
//		}
//
//		return null;
//	}

	@RequestMapping("/api/cSQL_Command")
	@ResponseBody
	public String execSQLCommand(HttpServletRequest request, HttpServletResponse response) 
	{
		String sql = request.getParameter("pParaString");
		
		if(sql == null || sql == "")
		{
			return "Error: sql statement is empty";
		}
		
		String command[] = new String[2];
		command[0] = "./warehouse/script/execSQL.sh";
		command[1] = sql;	

		StringBuffer result = new StringBuffer();
		Process process = null;
		BufferedReader bufrIn = null;
		BufferedReader bufrError = null;
		try {
			// String[] command = { command};
			// 执行命令, 返回一个子进程对象（命令在子进程中执行）
			process = Runtime.getRuntime().exec(command, null, new File("./"));
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
			System.out.println("====================>execute failed1");
			return "command executes failed";
		} finally {
			try {
				bufrIn.close();
				bufrError.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				System.out.println("====================>execute failed2");
				return "command executes failed";
			}

			// 销毁子进程
			if (process != null) {
				process.destroy();
			}

		}
		
		//return success message for modify operation like insert, update, delete
		if(result.length() == 0)
		{
			result.append("execute success").append('\n');
		}
		return result.toString();		
	}
	
	@RequestMapping("/api/cSQL_Tables")
	@ResponseBody
	public String getDatabaseTables(HttpServletRequest request, HttpServletResponse response) 
	{		
		
		String command[] = new String[1];
		command[0] = "./warehouse/script/getTables.sh";
		

		StringBuffer result = new StringBuffer();
		Process process = null;
		BufferedReader bufrIn = null;
		BufferedReader bufrError = null;
		try {
			// String[] command = { command};
			// 执行命令, 返回一个子进程对象（命令在子进程中执行）
			process = Runtime.getRuntime().exec(command, null, new File("./"));
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
			System.out.println("====================>execute failed1");
			return "command executes failed";
		} finally {
			try {
				bufrIn.close();
				bufrError.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				System.out.println("====================>execute failed2");
				return "command executes failed";
			}

			// 销毁子进程
			if (process != null) {
				process.destroy();
			}

		}
		
		//return success message for modify operation like insert, update, delete
		if(result.length() == 0)
		{
			result.append("execute success").append('\n');
		}
		return result.toString();		
	}
	
	@RequestMapping("/api/cSQL_Columns")
	@ResponseBody
	public String getTableColumns(HttpServletRequest request, HttpServletResponse response) 
	{		
		String tableName = request.getParameter("pDBTable");
		if(tableName == null || tableName == "")
		{
			return "Error: Table name is not specified by key pDBTable";
		}
		String command[] = new String[2];
		command[0] = "./warehouse/script/getTableColumns.sh";
		command[1] = tableName;
		

		StringBuffer result = new StringBuffer();
		Process process = null;
		BufferedReader bufrIn = null;
		BufferedReader bufrError = null;
		try {
			// String[] command = { command};
			// 执行命令, 返回一个子进程对象（命令在子进程中执行）
			process = Runtime.getRuntime().exec(command, null, new File("./"));
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
			System.out.println("====================>execute failed1");
			return "command executes failed";
		} finally {
			try {
				bufrIn.close();
				bufrError.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				System.out.println("====================>execute failed2");
				return "command executes failed";
			}

			// 销毁子进程
			if (process != null) {
				process.destroy();
			}

		}
		
		//return success message for modify operation like insert, update, delete
		if(result.length() == 0)
		{
			result.append("execute success").append('\n');
		}
		return result.toString();		
	}


	@RequestMapping("/api/cGen_ID")
	@ResponseBody
	public String genID(HttpServletRequest request, HttpServletResponse response) 
    {
        String ID = KeyGenerator.getNextID();

        return ID;
    }


}
