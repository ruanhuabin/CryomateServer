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
    @RequestMapping(value = "/login")
    @ResponseBody
    public String login(HttpServletRequest request) {
	
	String name = request.getParameter("name");
	String password = request.getParameter("pwd");
    String info = "登录逻辑";
    System.out.println(info);
    
    User user = userRepos.findByNameAndPassword(name, password);
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
    @RequestMapping(value = "/logout")
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
    
    @RequestMapping(value = "/register")
    @ResponseBody
    public String register(HttpServletRequest request)
    {
    	String name = request.getParameter("name");
    	String password = request.getParameter("pwd");
    	String email = request.getParameter("email");
    	String role = request.getParameter("role");
    	String workGroup = request.getParameter("workGroup");
    	String homeDir = this.userHomeDirPrefix + "/" + name;
    	
    	if(workGroup == null || workGroup.equals(""))
    	{
    		workGroup = name;
    	}
    	String workDir = this.workDirPrefix + "/" + workGroup + "/" + name;
    	String dataDir = this.dataDirPrefix + "/" + workGroup + "/" + name;
    	
    	StringBuffer strBuffer = new StringBuffer();
    	strBuffer.append("register operation:\n");
    	strBuffer.append("\tname:" + name + "\n");
    	strBuffer.append("\tpassword:" + password + "\n");
    	strBuffer.append("\temail:" + email + "\n");
    	strBuffer.append("\trole:" + role + "\n");
    	strBuffer.append("\tworkGroup:" + workGroup + "\n");
    	strBuffer.append("\thomeDir:" + homeDir + "\n");
    	strBuffer.append("\tworkDir:" + workDir + "\n");
    	strBuffer.append("\tdataDir:" + dataDir + "\n");
    	System.out.println(strBuffer.toString());
    	
    	//Check whether user name is already used 
    	User user = userRepos.getByName(name);
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
    	User newUser = new User(name, password, email, workGroup, role, homeDir, workDir, dataDir);
    	userRepos.save(newUser);
    	System.out.println("Register: create new user " + name + " success");
    	
    	//invoke script to create system user
    	
    	String command[] = new String[7];
    	command[0] = "./warehouse/script/register.sh";
    	command[1] = workGroup;
    	command[2] = name;
    	command[3] = password;
    	command[4] = this.userHomeDirPrefix;
    	command[5] = workDir;
    	command[6] = dataDir;
    	
    	String result = CommandRunner.runCommand(command);
    	System.out.println(command[0] + "running result: " + result.toString());
        	
    	return "register success";
    }    
    
    @RequestMapping(value = "/remove")
    @ResponseBody
    public String remove(HttpServletRequest request)
    {		
    	String name = request.getParameter("name");
    	
    	if(name == null)
    	{
    		return "Error: user name is null";
    	}
    	
    	User user = userRepos.getByName(name);
    	if(user == null)
    	{
    		return "Error: user [ " + name + " ] is not exist";
    	}
    	
    	System.out.println("-------->User [ " + name + " ] is exist");    	   	
    	 	
    	User currUser = (User)(request.getSession().getAttribute("userInfo"));    	
    	
    	//We can not remove ourself
    	if(currUser != null && currUser.getName().equals(name))
    	{
    		System.out.println("self delete self");
    		return "Error: user [ " + currUser.getName() + " ] can't not be removed by itself";
    	}
    	String currUserRole = currUser.getRole(); 
    	
    	if(currUserRole != null && currUserRole.equals("admin"))
    	{
    		User u = new User();
        	u.setName(name);
        	userRepos.delete(u);
        	return "remove user success";
    	}
    	else
    	{
    		return "Error: permission denied";
    	}  	
    	
    }
    

}
