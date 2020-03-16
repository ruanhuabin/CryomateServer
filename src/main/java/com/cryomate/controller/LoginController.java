package com.cryomate.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.cryomate.entity.Users;
import com.cryomate.model.User;
import com.cryomate.pojo.Constant;
//import com.cryomate.repository.UserRepository;
import com.cryomate.repository.UsersRepository;
import com.cryomate.utils.CommandRunner;

import org.springframework.web.bind.annotation.ResponseBody;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@Controller
@RequestMapping(value = "/user")
public class LoginController {
	private static final Logger logger = LoggerFactory
            .getLogger(LoginController.class);

	
	@Value("${cryomate.user.homedir.prefix}")
	private String userHomeDirPrefix;
	@Value("${cryomate.user.datadir.prefix}")
	private String dataDirPrefix;
	@Value("${cryomate.user.workdir.prefix}")
	private String workDirPrefix;

	@Autowired
	UsersRepository userRepos;

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
	 * 
	 * @param name    用户名
	 * @param pwd     用户密码
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/cLogin")
	@ResponseBody
	public String login(HttpServletRequest request) {

		String name = request.getParameter("pUserName");
		String password = request.getParameter("pPassword");
		String info = "登录逻辑";		
		logger.info(info + ", Request Method = " + request.getMethod());
		
		StringBuffer retMessage = new StringBuffer();

		Users user = userRepos.findByUserNameAndPassword(name, password);
		// 登录认证，认证成功后将用户信息放到session中
		if (user != null)
		{
			request.getSession().setAttribute("userInfo", user);
			logger.info("user [ " + name + " ] is valid");
			
			retMessage.append("pUserName:" + user.getUserName() + "\n");
			retMessage.append("pUserGroup:" + user.getUserGroup() + "\n");
			retMessage.append("pAuthority:" + user.getAuthority() + "\n");
			retMessage.append("pDataAdd:" + user.getDateAdd() + "\n");
			retMessage.append("pDateExp:" + user.getDateExp() + "\n");
			retMessage.append("pHomeDir:" + user.getHomeDir() + "\n");
			retMessage.append("pWorkDir:" + user.getWorkDir() + "\n");
			retMessage.append("pDataDir:" + user.getDataDir() + "\n");
			retMessage.append("pEmail:" + user.getEmail() + "\n");
			retMessage.append("pPhone:" + user.getPhone() + "\n");
			retMessage.append("pStatus:login success");		
			
			return retMessage.toString();		
			
		} else
		{
			return "pStatus:login failed";
		}

		
		

		
	}

	/**
	 * 登出操作
	 * 
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
	public String register(HttpServletRequest request) {
		String userName = request.getParameter("pUserName");
		String password = request.getParameter("pPassword");
		//String email = request.getParameter("pEmail");
		//String role = request.getParameter("pRole");
		String authority = request.getParameter("pAuthority");
		String userGroup = request.getParameter("pUserGroup");
		String dateAdd = request.getParameter("PDateAdd");
		String dateExp = request.getParameter("pDateExp");

		if (userGroup == null || userGroup.equals("")) {
			userGroup = userName;
		}

		String userHomeDirPrefix;
		if (this.userHomeDirPrefix.charAt(this.userHomeDirPrefix.length() - 1) != '/') {
			userHomeDirPrefix = this.userHomeDirPrefix + "/";
		} else {
			userHomeDirPrefix = this.userHomeDirPrefix;
		}

		String dataDirPrefix;
		if (this.dataDirPrefix.charAt(this.dataDirPrefix.length() - 1) != '/') {
			dataDirPrefix = this.dataDirPrefix + "/";
		} else {
			dataDirPrefix = this.dataDirPrefix;
		}

		String workDirPrefix;
		if (this.workDirPrefix.charAt(this.workDirPrefix.length() - 1) != '/') {
			workDirPrefix = this.workDirPrefix + "/";
		} else {
			workDirPrefix = this.workDirPrefix;
		}

		String homeDir = userHomeDirPrefix + userName;
		String workDir = workDirPrefix + userGroup + "/" + userName;
		String dataDir = dataDirPrefix + userGroup + "/" + userName;

		StringBuffer strBuffer = new StringBuffer();
		strBuffer.append("register operation:\n");
		strBuffer.append("\tname:" + userName + "\n");
		strBuffer.append("\tpassword:" + password + "\n");		
		strBuffer.append("\tauthority:" + authority + "\n");
		strBuffer.append("\tworkGroup:" + userGroup + "\n");
		strBuffer.append("\thomeDir:" + homeDir + "\n");
		strBuffer.append("\tworkDir:" + workDir + "\n");
		strBuffer.append("\tdataDir:" + dataDir + "\n");
		System.out.println(strBuffer.toString());

		// Check whether user name is already used
		Users user = userRepos.getByUserName(userName);
		if (user != null) {
			StringBuffer retMsg = new StringBuffer();
			retMsg.append("pStatus:register failed:user [ ");
			retMsg.append(userName);
			retMsg.append(" ] is already exist");
			System.out.println(retMsg.toString());
			return retMsg.toString();

		}
		// insert new user into database
		//User newUser = new User(name, password, email, userGroup, role, homeDir, workDir, dataDir);
		Users newUser = new Users();
		newUser.setAuthority(authority);		
		newUser.setDateAdd(dateAdd);
		newUser.setDateExp(dateExp);		
		newUser.setPassword(password);
		newUser.setUserGroup(userGroup);
		newUser.setUserName(userName);
		newUser.setHomeDir(homeDir);
		newUser.setDataDir(dataDir);
		newUser.setWorkDir(workDir);
		
		userRepos.save(newUser);
		System.out.println("Register: create new user " + userName + " success");

		// invoke script to create system user

		String command[] = new String[7];
		command[0] = "./warehouse/script/register.sh";
		command[1] = userGroup;
		command[2] = userName;
		command[3] = password;
		command[4] = this.userHomeDirPrefix;
		command[5] = workDir;
		command[6] = dataDir;

		String result = CommandRunner.runCommand(command);
		System.out.println(command[0] + ": running result: " + result.toString());

		return "pStatus:register success";
	}

	@RequestMapping(value = "/cRemove")
	@ResponseBody
	public String remove(HttpServletRequest request) {
		String name = request.getParameter("pUserName");

		if (name == null) {
			return "pStatus:Error: user name is null";
		}

		Users user = userRepos.getByUserName(name);
		if (user == null) {
			return "pStatus:Error: user [ " + name + " ] is not exist";
		}

		System.out.println("-------->User [ " + name + " ] is exist");

		Users currUser = (Users) (request.getSession().getAttribute("userInfo"));

		// We can not remove ourself
		if (currUser != null && currUser.getUserName().equals(name)) {
			System.out.println("self delete self");
			return "pStatus:Error: user [ " + currUser.getUserName() + " ] can't not be removed by himself";
		}
		String currUserAuthority = currUser.getAuthority();

		if (currUserAuthority != null && currUserAuthority.equals(Constant.AUTHORITY_SYSTEM_ROOT)) {
			Users u = new Users();
			u.setUserName(name);
			userRepos.delete(u);
			return "pStatus:remove success";
		} else {
			return "pStatus:Error: permission denied";
		}

	}

}
