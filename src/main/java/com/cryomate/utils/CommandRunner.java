package com.cryomate.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;

public class CommandRunner {
	
	public static String runCommand(String[] command)
	{	
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
		
		System.out.println("Command Result: " + command[0] + ": "+ result.toString());
		return "command executes success";

	}

}
