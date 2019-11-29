package com.cryomate.controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@RestController
@RequestMapping(value = "/test")
public class TestController {

    @GetMapping(value = "/test01")
    public String test01() {
        String info = "测试01";
        System.out.println(info);
        return info;
    }

    @GetMapping(value = "/test02")
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