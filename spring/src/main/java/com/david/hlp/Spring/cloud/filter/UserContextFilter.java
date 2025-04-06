package com.david.hlp.Spring.cloud.filter;

import com.david.hlp.Spring.common.util.UserContext;
import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.annotation.WebFilter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * 用户上下文过滤器
 * 确保在请求结束时清理UserContext，防止内存泄漏
 */
@Slf4j
@Component
@WebFilter(urlPatterns = "/*")
@Order(1)
public class UserContextFilter implements Filter {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) 
            throws IOException, ServletException {
        try {
            chain.doFilter(request, response);
        } finally {
            // 无论请求处理是否成功，都清理用户上下文
            UserContext.clear();
            if (log.isDebugEnabled()) {
                log.debug("已清理用户上下文, URI: {}", ((HttpServletRequest) request).getRequestURI());
            }
        }
    }
}