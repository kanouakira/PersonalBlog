package com.kanouakira.vueblog.common.interceptor;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.kanouakira.vueblog.common.annotation.PassToken;
import com.kanouakira.vueblog.common.annotation.UserLoginToken;
import com.kanouakira.vueblog.entity.User;
import com.kanouakira.vueblog.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;

/**
 * 自定义拦截器，获取token并验证
 */
public class AuthenticationInterceptor implements HandlerInterceptor {
    @Autowired
    UserService userService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // 从请求头中取出token
        String token = request.getHeader("token");
        // 如果不是映射到方法直接通过
        if(!(handler instanceof HandlerMethod)){
            return true;
        }
        HandlerMethod handlerMethod = (HandlerMethod) handler;
        Method method = handlerMethod.getMethod();
        // 检查方法是否有PassToken注解，有则直接跳过认证
        if (method.isAnnotationPresent(PassToken.class)){
            PassToken passToken = method.getAnnotation(PassToken.class);
            if(passToken.required()){
                return true;
            }
        }

        // 检查方法是否有用户权限的注解
        if (method.isAnnotationPresent(UserLoginToken.class)){
            UserLoginToken userLoginToken = method.getAnnotation(UserLoginToken.class);
            if (userLoginToken.required()){
                // 执行认证
                if (token == null){
                    throw new RuntimeException("无Token，请重新登录");
                }
                // 获取token中的用户id
                String userId;
                try {
                    userId = JWT.decode(token).getAudience().get(0);
                }catch (JWTDecodeException j){
                    throw new RuntimeException("401");
                }
                User user = userService.getById(Long.valueOf(userId));
                if (user == null){
                    throw new RuntimeException("用户不存在，请重新登录");
                }
                // 验证token
                JWTVerifier jwtVerifier = JWT.require(Algorithm.HMAC256(user.getPassword())).build();
                try {
                    jwtVerifier.verify(token);
                }catch (JWTVerificationException e){
                    throw new RuntimeException("Token验证不通过");
                }
                //把变量放在request请求域中，仅可以被这次请求，即同一个requerst使用
                request.setAttribute("userId", Long.valueOf(userId));
                return true;
            }
        }
        return true;
    }
}
