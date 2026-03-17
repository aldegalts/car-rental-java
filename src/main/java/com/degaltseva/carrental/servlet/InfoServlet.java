package com.degaltseva.carrental.servlet;

import com.degaltseva.carrental.service.ViolationTypeService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet(urlPatterns = {"/about", "/info/violation-types"})
public class InfoServlet extends HttpServlet {

    private final ViolationTypeService violationTypeService = new ViolationTypeService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String path = req.getServletPath();

        if ("/about".equals(path)) {
            req.setAttribute("pageTitle", "О разработчике");
            req.getRequestDispatcher("/WEB-INF/jsp/info/about.jsp").forward(req, resp);
        } else if ("/info/violation-types".equals(path)) {
            req.setAttribute("violationTypes", violationTypeService.findAll());
            req.setAttribute("pageTitle", "Типы нарушений");
            req.getRequestDispatcher("/WEB-INF/jsp/info/violation-types.jsp").forward(req, resp);
        } else {
            resp.sendError(HttpServletResponse.SC_NOT_FOUND);
        }
    }
}
