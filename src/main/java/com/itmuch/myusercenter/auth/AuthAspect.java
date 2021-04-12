package com.itmuch.myusercenter.auth;

import com.itmuch.myusercenter.util.JwtOperator;
import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.Objects;

@Aspect
@Component
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class AuthAspect {

    private final JwtOperator jwtOperator;

    @Around("@annotation(com.itmuch.myusercenter.auth.CheckLogin)")
    public Object checkLogin(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        try {
            checkToken();
        } catch (Throwable throwable) {
            throw new SecurityException("Token不合法!");
        }
        return proceedingJoinPoint.proceed();

    }

    private void checkToken() {
        // 1.从header里面获取token
        HttpServletRequest request = getHttpServletRequest();

        String token = request.getHeader("X-Token");


        // 2.校验token是否合法&是否过期，如果不合法，直接抛异常;如果合法放行
        Boolean isValid = jwtOperator.validateToken(token);
        if (!isValid){
            throw new SecurityException("Token不合法!");
        }
        // 3.如果校验成功，那么就将用户的信息设置到request的attribute里面
        Claims claims = jwtOperator.getClaimsFromToken(token);

        request.setAttribute("id", claims.get("id"));
        request.setAttribute("wxNickName", claims.get("wxNickName"));
        request.setAttribute("role", claims.get("role"));
    }

    private HttpServletRequest getHttpServletRequest() {
        RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
        ServletRequestAttributes attributes = (ServletRequestAttributes) requestAttributes;
        HttpServletRequest request = attributes.getRequest();
        return request;
    }

    @Around("@annotation(com.itmuch.myusercenter.auth.CheckAuthorization)")
    public Object checkAuthorization(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        try {
            // 1.验证token是否合法
            this.checkToken();
            // 2.验证用户角色是否匹配
            HttpServletRequest request = getHttpServletRequest();
            String role = (String)request.getAttribute("role");

            MethodSignature signature = (MethodSignature) proceedingJoinPoint.getSignature();
            Method method = signature.getMethod();
            CheckAuthorization annotation = method.getAnnotation(CheckAuthorization.class);

            String value = annotation.value();

            if(!Objects.equals(role, value)){
                throw new SecurityException("用户无权访问");
            }
        } catch (Throwable throwable) {
            throw new SecurityException("用户无权访问", throwable);
        }

        return proceedingJoinPoint.proceed();
    }
}
