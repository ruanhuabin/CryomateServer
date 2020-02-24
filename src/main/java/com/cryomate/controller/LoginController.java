package com.cryomate.controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.cryomate.model.User;
import com.cryomate.repository.UserRepository;

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
	String pwd = request.getParameter("pwd");
        String info = "登录逻辑";
        System.out.println(info);

        // 登录认证，认证成功后将用户信息放到session中
        if (name.equals("fury") && pwd.equals("111111")) {
            request.getSession().setAttribute("userInfo", name + " - " + pwd);
            info = "login success";
        } else {
            info = "login failed";
        }

        System.out.println(info + ", Request Method = " + request.getMethod());
        return info;
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
    	String password = request.getParameter("password");
    	String email = request.getParameter("email");
    	String role = request.getParameter("role");
    	String workGroup = request.getParameter("workGroup");
    	String homeDir = this.userHomeDirPrefix + name;
    	
    	StringBuffer strBuffer = new StringBuffer();
    	strBuffer.append("register operation:\n");
    	strBuffer.append("\tname:" + name + "\n");
    	strBuffer.append("\tpassword:" + password + "\n");
    	strBuffer.append("\temail:" + email + "\n");
    	strBuffer.append("\trole:" + role + "\n");
    	strBuffer.append("\tworkGroup:" + workGroup + "\n");
    	strBuffer.append("\thomeDir:" + homeDir + "\n");
    	System.out.println(strBuffer.toString());
    	
    	//Check whether user name is already used 
    	User user = userRepos.getByName(name);
    	if(user != null)
    	{
    		StringBuffer retMsg = new StringBuffer();
    		retMsg.append("Register Failed：user name [ ");
    		retMsg.append(name);
    		retMsg.append(" ] is already exist");
    		System.out.println(retMsg.toString());
    		return retMsg.toString();
    		
    	}
    	//insert new user into database    	
    	User newUser = new User(name, password, email, workGroup, role, homeDir);
    	userRepos.save(newUser);
    	System.out.println("Register: create new user " + name + " success");
    	
    	//invoke script to create system user
    	
    			
    	return "register success";
    }

}
