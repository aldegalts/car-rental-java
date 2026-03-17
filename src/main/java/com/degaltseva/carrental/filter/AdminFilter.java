package com.degaltseva.carrental.filter;

import com.degaltseva.carrental.model.User;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

@WebFilter
public class AdminFilter implements Filter {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse resp = (HttpServletResponse) response;

        HttpSession session = req.getSession(false);
        if (session != null) {
            User user = (User) session.getAttribute("user");
            if (user != null && "admin".equals(user.getRoleName())) {
                chain.doFilter(request, response); // пропустить запрос дальше по цепочке
                return;
            }
        }

        resp.sendError(HttpServletResponse.SC_FORBIDDEN, "Доступ запрещён");
    }
}
