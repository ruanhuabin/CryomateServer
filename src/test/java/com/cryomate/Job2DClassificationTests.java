package com.cryomate;

import static org.junit.Assert.assertNotNull;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

import com.cryomate.model.Job2DClassification;
import com.cryomate.model.SystemKeyword;
import com.cryomate.repository.Job2DClassificationRepository;
import com.cryomate.repository.SystemKeywordRepository;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class Job2DClassificationTests {
	
	@LocalServerPort
	private int port;

	@Autowired
	private TestRestTemplate restTemplate;
	
	@Autowired
	private Job2DClassificationRepository job2DRepository;
	
	private String getRootUrl() {
		return "http://localhost:" + port + "/api/v1";
	}
	
	
	@Test
	public void testInsertNew2DJob() {
		
		Job2DClassification job2D = new Job2DClassification();
		job2D.setID("ID001");
		job2DRepository.save(job2D);
		
	}
	
	
	@Test
	public void testRunCommand()
	{
		StringBuilder result = new StringBuilder();
		Process process = null;
		BufferedReader bufrIn = null;
		BufferedReader bufrError = null;
		try {
			//String[] command = {"cmd", "-c", "abc"};
			String[] command = {"cmd",  "dir"};
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
	}
}
