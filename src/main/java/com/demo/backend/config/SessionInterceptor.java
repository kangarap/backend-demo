package com.demo.backend.config;

import com.alibaba.fastjson.JSON;
import com.demo.backend.model.AdminUser;
import com.demo.backend.model.User;
import com.demo.backend.utils.Constants;
import com.demo.backend.utils.response.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@Configuration
public class SessionInterceptor implements WebMvcConfigurer {

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/statics/**").addResourceLocations("classpath:/statics/");
        // 解决 SWAGGER 404报错
        registry.addResourceHandler("/swagger-ui.html").addResourceLocations("classpath:/META-INF/resources/");
        registry.addResourceHandler("/webjars/**").addResourceLocations("classpath:/META-INF/resources/webjars/");
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new SecurityInterceptor())
                .addPathPatterns("/**")
                .excludePathPatterns("/**/login")
                .excludePathPatterns("/**/sendvcode")
                .excludePathPatterns("/**/getCaptchaImage")
                .excludePathPatterns("/**/test")
                .excludePathPatterns("/**/logout")
                .excludePathPatterns("/error")
                .excludePathPatterns("/swagger-resources/**", "/webjars/**", "/v2/**", "/swagger-ui.html/**")
        ;
    }

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        //添加映射路径
        registry.addMapping("/**")
                //放行哪些原始域(头部信息)
                .allowedHeaders("*")

                //放行哪些原始域(请求方式)
                .allowedMethods("*")

                //放行哪些原始域
                .allowedOrigins("*")

                //是否发送Cookie信息
                .allowCredentials(true);

    }

    @Configuration
    public class SecurityInterceptor implements HandlerInterceptor {


        @Override
        public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws IOException {

            HttpSession session = request.getSession();

            String uri = request.getRequestURI();
            String method = request.getMethod();

            System.out.println("---------- session start ----------");
            if (null != session.getAttribute(Constants.SESSION_KEY)){

                try
                {
                    //验证当前请求的session是否是已登录的session
                    AdminUser adminUser = (AdminUser)session.getAttribute(Constants.SESSION_KEY);
                    if(null != adminUser)
                    {
                        String loginSessionId = stringRedisTemplate.opsForValue().get("AdminUser" +adminUser.getUserId()+"");
                        System.out.println("loginSessionId = "+loginSessionId);
                        System.out.println("session.getId() = "+session.getId());
                        if (loginSessionId != null && loginSessionId.equals(session.getId()))
                        {
//                            //权限判别
//                            String role_str = session.getAttribute(ADMIN_ROLE)+"";
//                            //解权限
//                            if(null == role_str)
//                            {
//                                response.getWriter().write(JSON.toJSONString(
//                                        new Result("403","no permission")
//                                ));
//
//                            }else if("*".equals(role_str))
//                            {
                            return true;
//                            }//else
//                            {
//                                JSONArray array = JSON.parseArray(role_str);
//
//                                for (short i = 0;i<array.size(); i++)
//                                {
//                                    String id = JSONObject.parseObject(JSONObject.toJSONString(array.get(i))).getString("id");
//                                }
//                                if(HttpMethod.GET.toString().equals(method))
//                                {
//                                    System.out.println("get-==============");
//                                }else if(HttpMethod.POST.toString().equals(method))
//                                {
//                                    System.out.println("POST-==============");
//                                }
//
//                            }
//                            return false;
                        }
                    }

                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }
            }
            response.getWriter().write(JSON.toJSONString(
                    new Result<String>("401","please login first")
            ));
            return false;
        }
    }
}
