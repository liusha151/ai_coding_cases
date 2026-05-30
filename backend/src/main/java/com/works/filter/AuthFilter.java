package com.works.filter;

import com.works.common.Constants;
import com.works.util.JwtUtil;
import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * JWT 鉴权过滤器：拦截 /api/v1/* 请求，除登录接口外均需携带有效 Token
 * Token 通过 Authorization: Bearer <token> 头传递
 */
public class AuthFilter implements Filter {
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse res = (HttpServletResponse) response;
        String path = req.getRequestURI();

        // 登录接口和 CORS 预检请求放行，无需 Token
        if (path.equals("/api/v1/auth/login") || "OPTIONS".equalsIgnoreCase(req.getMethod())) {
            chain.doFilter(request, response);
            return;
        }

        // 检查 Authorization 头是否以 Bearer 开头
        String authHeader = req.getHeader(Constants.TOKEN_HEADER);
        if (authHeader == null || !authHeader.startsWith(Constants.TOKEN_PREFIX)) {
            res.setStatus(401);
            res.setContentType("application/json");
            res.getWriter().write("{\"code\":401,\"message\":\"未授权\"}");
            return;
        }

        try {
            // 验证 Token 有效性，并将用户名和角色注入请求属性
            String token = authHeader.substring(Constants.TOKEN_PREFIX.length());
            JwtUtil.verifyToken(token);
            req.setAttribute("username", JwtUtil.getUsername(token));
            req.setAttribute("role", JwtUtil.getRole(token));
            chain.doFilter(request, response);
        } catch (Exception e) {
            // Token 过期或无效时返回 401
            res.setStatus(401);
            res.setContentType("application/json");
            res.getWriter().write("{\"code\":401,\"message\":\"无效的令牌\"}");
        }
    }
}
