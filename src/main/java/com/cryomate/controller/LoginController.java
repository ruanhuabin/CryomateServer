package com.cryomate.controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.cryomate.model.User;
import com.cryomate.repository.UserRepository;
import com.cryomate.utils.CommandRunner;

import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@Controller
@RequestMapping(value = "/user")
public class LoginController {

		@Value("${cryomate.user.homedir.prefix}")
				private String userHomeDirPrefix;
		@Value("${cryomate.user.datadir.prefix}")
				private String dataDirPrefix;
		@Value("${cryomate.user.workdir.prefix}")
				private String workDirPrefix;

		@Autowired
				UserRepository userRepos;

		@RequestMapping(value = "/test01")
				@ResponseBody
				public String test01() {
						String info = "测试01";
						System.out.println(info);
						return info;
				}

		@RequestMapping(value = "/test02")
				@ResponseBody
				public String test02() {
						String info = "test02";
						System.out.println(info);
						return info;
				}

		/**
		 * 登录逻辑
		 * @param name 用户名
		 * @param pwd 用户密码
		 * @param request
		 * @return
		 */
		@RequestMapping(value = "/cLogin")
				@ResponseBody
				public String login(HttpServletRequest request) {

						String name = request.getParameter("pUserName");
						String password = request.getParameter("pPassword");
						String info = "登录逻辑";
						System.out.println(info);

						User user = userRepos.findByUserNameAndPassword(name, password);
						// 登录认证，认证成功后将用户信息放到session中
						if(user != null)
						{
								//request.getSession().setAttribute("userInfo", name + " - " + password);
								request.getSession().setAttribute("userInfo", user);
								System.out.println("user [ " + name + " ] is valid");
								info = "login success";
						}
						else
						{
								info = "login failed";
						}

						System.out.println(info + ", Request Method = " + request.getMethod());
						return info;

						//        // 登录认证，认证成功后将用户信息放到session中
						//        if (name.equals("fury") && password.equals("111111")) {
						//            request.getSession().setAttribute("userInfo", name + " - " + password);
						//            info = "login success";
						//        } else {
						//            info = "login failed";
						//        }
						//
						//        System.out.println(info + ", Request Method = " + request.getMethod());
						//        return info;
				}

		/**
		 * 登出操作
		 * @param request
		 * @return
		 */
		@RequestMapping(value = "/cLogout")
				@ResponseBody
				public String loginout(HttpServletRequest request) {
						String info = "登出操作";
						System.out.println(info);
						HttpSession session = request.getSession();

						// 将用户信息从session中删除
						session.removeAttribute("userInfo");

						Object userInfo = session.getAttribute("userInfo");
						if (userInfo == null) {
								info = "logout success";
						} else {
								info = "logout failed";
						}
						System.out.println(info);

						return info;

				}

		@RequestMapping(value = "/cRegister")
				@ResponseBody
				public String register(HttpServletRequest request)
				{
						String name = request.getParameter("pUserName");
						String password = request.getParameter("pPassword");
						String email = request.getParameter("pEmail");
						String role = request.getParameter("pRole");
						String userGroup = request.getParameter("pUserGroup");


						if(userGroup == null || userGroup.equals(""))
						{
								userGroup = name;
						}

						String userHomeDirPrefix;
						if(this.userHomeDirPrefix.charAt(this.userHomeDirPrefix.length() - 1) != '/')
						{
								userHomeDirPrefix = this.userHomeDirPrefix + "/";
						}
						else
						{
								userHomeDirPrefix = this.userHomeDirPrefix;
						}

						String dataDirPrefix;
						if(this.dataDirPrefix.charAt(this.dataDirPrefix.length() - 1) != '/')
						{
								dataDirPrefix = this.dataDirPrefix + "/";
						}
						else
						{
								dataDirPrefix = this.dataDirPrefix;
						}

						String workDirPrefix;
						if(this.workDirPrefix.charAt(this.workDirPrefix.length() - 1) != '/')
						{
								workDirPrefix = this.workDirPrefix + "/";
						}
						else
						{
								workDirPrefix = this.workDirPrefix;
						}


						String homeDir = userHomeDirPrefix +  name;
						String workDir = workDirPrefix + userGroup + "/" + name;
						String dataDir = dataDirPrefix + userGroup + "/" + name;

						StringBuffer strBuffer = new StringBuffer();
						strBuffer.append("register operation:\n");
						strBuffer.append("\tname:" + name + "\n");
						strBuffer.append("\tpassword:" + password + "\n");
						strBuffer.append("\temail:" + email + "\n");
						strBuffer.append("\trole:" + role + "\n");
						strBuffer.append("\tworkGroup:" + userGroup + "\n");
						strBuffer.append("\thomeDir:" + homeDir + "\n");
						strBuffer.append("\tworkDir:" + workDir + "\n");
						strBuffer.append("\tdataDir:" + dataDir + "\n");
						System.out.println(strBuffer.toString());

						//Check whether user name is already used 
						User user = userRepos.getByUserName(name);
						if(user != null)
						{
								StringBuffer retMsg = new StringBuffer();
								retMsg.append("Register Failed：user [ ");
								retMsg.append(name);
								retMsg.append(" ] is already exist");
								System.out.println(retMsg.toString());
								return retMsg.toString();

						}
						//insert new user into database    	
						User newUser = new User(name, password, email, userGroup, role, homeDir, workDir, dataDir);
						userRepos.save(newUser);
						System.out.println("Register: create new user " + name + " success");

						//invoke script to create system user

						String command[] = new String[7];
						command[0] = "./warehouse/script/register.sh";
						command[1] = userGroup;
						command[2] = name;
						command[3] = password;
						command[4] = this.userHomeDirPrefix;
						command[5] = workDir;
						command[6] = dataDir;

						String result = CommandRunner.runCommand(command);
						System.out.println(command[0] + ": running result: " + result.toString());

						return "register success";
				}    

		@RequestMapping(value = "/cRemove")
				@ResponseBody
				public String remove(HttpServletRequest request)
				{		
						String name = request.getParameter("pUserName");

						if(name == null)
						{
								return "Error: user name is null";
						}

						User user = userRepos.getByUserName(name);
						if(user == null)
						{
								return "Error: user [ " + name + " ] is not exist";
						}

						System.out.println("-------->User [ " + name + " ] is exist");    	   	

						User currUser = (User)(request.getSession().getAttribute("userInfo"));    	

						//We can not remove ourself
						if(currUser != null && currUser.getUserName().equals(name))
						{
								System.out.println("self delete self");
								return "Error: user [ " + currUser.getUserName() + " ] can't not be removed by itself";
						}
						String currUserRole = currUser.getRole(); 

						if(currUserRole != null && currUserRole.equals("admin"))
						{
								User u = new User();
								u.setUserName(name);
								userRepos.delete(u);
								return "remove user success";
						}
						else
						{
								return "Error: permission denied";
						}  	

				}


}
