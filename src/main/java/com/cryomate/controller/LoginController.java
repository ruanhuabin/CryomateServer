package com.cryomate.controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.stereotype.Controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@Controller
@RequestMapping(value = "/user")
public class LoginController {

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

}
