package com.cryomate.config;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class LoginConfig implements WebMvcConfigurer {
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new LoginInterceptor())
                .addPathPatterns("/**")
                .excludePathPatterns("/api/**", "/user/login", "/user/logout");
                //.excludePathPatterns("/api/user/login", "/aip/user/loginout", "/api/**");
                //.excludePathPatterns("/test/login");
        		
                //.excludePathPatterns("/test/login", "/api/user/login", "/api/*");
                //.excludePathPatterns("/test/login", "/api/*");
    }
}
