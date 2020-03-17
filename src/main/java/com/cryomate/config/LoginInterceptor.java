package com.cryomate.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.lang.Nullable;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import com.cryomate.controller.LoginController;
import com.cryomate.entity.Users;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class LoginInterceptor implements HandlerInterceptor
{
	private static final Logger logger = LoggerFactory
            .getLogger(LoginController.class);

	@Override
	public boolean preHandle(HttpServletRequest request,
	        HttpServletResponse response, Object handler) throws Exception
	{
		logger.info("Interception before processing request:登录状态拦截");
		HttpSession session = request.getSession();		
		logger.info("Session ID of current login user is: {}", session.getId() );

		// 获取用户信息，如果没有用户信息直接返回提示信息
		Users userInfo = (Users)session.getAttribute("userInfo");
		if (userInfo == null)
		{
			//System.out.println("没有登录");
			logger.error("No login user is found for processing request");
			response.getWriter().write("Please Login In");
			return false;
		} else
		{
			logger.info("User [ {} ] is already login, user info is: {}", userInfo.getUserName(), userInfo);
			//System.out.println(
			//        "已经登录过啦，用户信息为：" + session.getAttribute("userInfo"));
		}

		return true;
	}

	@Override
	public void postHandle(HttpServletRequest request,
	        HttpServletResponse response, Object handler,
	        @Nullable ModelAndView modelAndView) throws Exception
	{

	}

	@Override
	public void afterCompletion(HttpServletRequest request,
	        HttpServletResponse response, Object handler,
	        @Nullable Exception ex) throws Exception
	{

	}
}