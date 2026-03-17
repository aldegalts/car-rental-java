package com.degaltseva.carrental.servlet.account;

import com.degaltseva.carrental.model.User;
import com.degaltseva.carrental.service.ViolationService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet("/account/violations")
public class MyViolationsServlet extends HttpServlet {

    private final ViolationService violationService = new ViolationService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        User user = (User) req.getSession().getAttribute("user");

        req.setAttribute("violations", violationService.findByUserId(user.getId()));
        req.setAttribute("pageTitle", "Мои нарушения");
        req.getRequestDispatcher("/WEB-INF/jsp/account/my-violations.jsp").forward(req, resp);
    }
}
