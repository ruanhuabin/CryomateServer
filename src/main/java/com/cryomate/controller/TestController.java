package com.cryomate.controller;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.cryomate.entity.Users;
import com.cryomate.repository.UsersRepository;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@RestController
@RequestMapping(value = "/test")
public class TestController {
	
	@Autowired
	private UsersRepository userRepository;

    @GetMapping(value = "/test01")
    public String test01() {
        String info = "测试01";
        System.out.println(info);
        return info;
    }

    @GetMapping(value = "/test02")
    public Users test02() {
       
    	Users user = new Users();
    	user.setUserName("ruanhuabin");
    	user.setPassword("password");
    	user.setDateAdd("today");
        return user;
    }
    @GetMapping(value = "/test03")
    public List<Users> test03()
    {
    	Users user1 = new Users();
    	user1.setUserName("ruanhuabin");
    	user1.setPassword("password");
    	user1.setDateAdd("today");
    	
    	
    	Users user2 = new Users();
    	user2.setUserName("ruanhuabin2");
    	user2.setPassword("password2");
    	user2.setDateAdd("today2");
    	
    	List<Users> allUsers = new ArrayList<Users>();
    	allUsers.add(user1);
    	allUsers.add(user2);
    	
    	return allUsers;
    	
    	
    }
    
    @GetMapping(value = "/test04")
    public List<String> test04(HttpServletRequest request)
    {
    	SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
    	
    	String dir = request.getParameter("dirPath");    	
    	List<String> allFiles = new ArrayList<String>();
    	
    	File file = new File(dir);
    	String[] fileList = file.getAbsoluteFile().list();
    	
    	for(String f:fileList)
    	{
    		File ff = new File(dir + "/" + f);
    		long lastModified = ff.lastModified();
    		
    		String str = f + "@" + sdf.format(lastModified);
    		allFiles.add(str);
    	}
    	
    	
    	return allFiles;
    	
    	
    }
    
    @GetMapping(value = "/test05")
    public String test05(HttpServletRequest request)
    {
    	Users u = userRepository.getOne("xli");
    	
    	return u.getPassword();
    	
    }

    /**
     * 登录逻辑
     * @param name 用户名
     * @param pwd 用户密码
     * @param request
     * @return
     */
    @GetMapping(value = "/login")
    public String login(
            @RequestParam(value = "name") String name,
            @RequestParam(value = "pwd") String pwd,
            HttpServletRequest request
    ) {
        String info = "登录逻辑";
        System.out.println(info);

        // 登录认证，认证成功后将用户信息放到session中
        if (name.equals("fury") && pwd.equals("111111")) {
            request.getSession().setAttribute("userInfo", name + " - " + pwd);
            info = "登录成功";
        } else {
            info = "登录失败";
        }

        System.out.println(info);
        return info;
    }

    /**
     * 登出操作
     * @param request
     * @return
     */
    @GetMapping(value = "/loginout")
    public String loginout(HttpServletRequest request) {
        String info = "登出操作";
        System.out.println(info);
        HttpSession session = request.getSession();

        // 将用户信息从session中删除
        session.removeAttribute("userInfo");

        Object userInfo = session.getAttribute("userInfo");
        if (userInfo == null) {
            info = "登出成功";
        } else {
            info = "登出失败";
        }
        System.out.println(info);

        return info;

    }

}