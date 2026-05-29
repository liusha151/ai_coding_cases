package com.works.filter;

import com.works.common.Constants;
import com.works.util.JwtUtil;
import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class AuthFilter implements Filter {
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse res = (HttpServletResponse) response;
        String path = req.getRequestURI();

        if (path.equals("/api/v1/auth/login")) {
            chain.doFilter(request, response);
            return;
        }

        String authHeader = req.getHeader(Constants.TOKEN_HEADER);
        if (authHeader == null || !authHeader.startsWith(Constants.TOKEN_PREFIX)) {
            res.setStatus(401);
            res.setContentType("application/json");
            res.getWriter().write("{\"code\":401,\"message\":\"未授权\"}");
            return;
        }

        try {
            String token = authHeader.substring(Constants.TOKEN_PREFIX.length());
            JwtUtil.verifyToken(token);
            req.setAttribute("username", JwtUtil.getUsername(token));
            req.setAttribute("role", JwtUtil.getRole(token));
            chain.doFilter(request, response);
        } catch (Exception e) {
            res.setStatus(401);
            res.setContentType("application/json");
            res.getWriter().write("{\"code\":401,\"message\":\"无效的令牌\"}");
        }
    }
}
