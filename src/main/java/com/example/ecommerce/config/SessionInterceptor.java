package com.example.ecommerce.config;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import java.util.List;

@Component
public class SessionInterceptor implements HandlerInterceptor {

    private static final List<String> PUBLIC_ROUTES = List.of(
            "/", "/login", "/register", "/saveUser", "/shop", "/products",
            "/static", "/css", "/images", "/admin/login"
    );

    @Override
    public boolean preHandle(HttpServletRequest request,
                             HttpServletResponse response,
                             Object handler) throws Exception {
        String path = request.getRequestURI();

        boolean isPublic = PUBLIC_ROUTES.stream().anyMatch(path::startsWith);
        if (isPublic) return true;

        if (request.getSession().getAttribute("user") == null) {
            if (path.startsWith("/admin")) {
                response.sendRedirect("/admin/login");
            } else {
                response.sendRedirect("/");
            }
            return false;
        }
        return true;
    }
}
