package com.david.hlp.Spring.cloud.aspect;

import com.david.hlp.Spring.common.service.JwtCommonService;
import com.david.hlp.Spring.cloud.module.entity.LoginRequest;
import com.david.hlp.Spring.common.enums.ResultCode;
import com.david.hlp.Spring.common.result.Result;
import com.david.hlp.Spring.common.util.UserContext;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import com.david.hlp.Spring.cloud.module.entity.CloudToken;
import com.david.hlp.Spring.cloud.service.impl.AuthServiceImpl;
/**
 * Token检查切面
 * 用于在处理请求前验证token的有效性
 */
@Slf4j
@Aspect
@Component
@RequiredArgsConstructor
public class TokenCheckAspect {

    private final JwtCommonService<LoginRequest> jwtService;
    private final AuthServiceImpl authService;
    /**
     * 定义切点：排除登录和登出相关的API
     */
    @Pointcut("execution(* com.david.hlp.Spring.cloud.controller.CloudController.*(..)) && " +
              "!execution(* com.david.hlp.Spring.cloud.controller.CloudController.login(..))"+
              "!execution(* com.david.hlp.Spring.cloud.controller.CloudController.logout(..))"
              )
    public void tokenCheckPointcut() {}
    /**
     * 环绕通知：在方法执行前检查token
     */
    @Around("tokenCheckPointcut()")
    public Object checkToken(ProceedingJoinPoint joinPoint) throws Throwable {
            // 获取当前请求
            ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
            if (attributes == null) {
                log.warn("无法获取当前请求信息");
                return joinPoint.proceed();
            }
            HttpServletRequest request = attributes.getRequest();
            // 检查Authorization头
            final String authHeader = request.getHeader("Authorization");
            // 如果没有Authorization头或格式不正确，返回401
            if (authHeader == null || !authHeader.startsWith("Bearer ")) {
                log.warn("请求无效token: {}", request.getRequestURI());
                return Result.error(ResultCode.UNAUTHORIZED, "未提供有效的认证令牌");
            } else {
                // 提取并验证token
                final String jwt = authHeader.substring(7);
                final String userEmail = jwtService.extractEmail(jwt);
                final CloudToken cloudToken = CloudToken.builder()
                        .email(userEmail)
                        .token(jwt)
                        .build();
                if (!authService.checkToken(cloudToken)) {
                    log.warn("无效的token: {}", jwt);
                    return Result.error(ResultCode.UNAUTHORIZED, "无效或已过期的认证令牌");
                }
                // 将用户邮箱存入上下文
                UserContext.setCurrentEmail(userEmail);
                try {
                    // 如果token有效，继续执行原始方法
                    return joinPoint.proceed();
                } finally {
                    // 请求结束后清除上下文，防止内存泄漏
                    UserContext.clear();
                }
        }
    }
}