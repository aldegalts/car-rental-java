package com.degaltseva.carrental.servlet.account;

import com.degaltseva.carrental.model.User;
import com.degaltseva.carrental.model.Violation;
import com.degaltseva.carrental.service.ViolationService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.Optional;

@WebServlet("/account/violations/*")
public class ViolationDetailServlet extends HttpServlet {

    private final ViolationService violationService = new ViolationService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        User user = (User) req.getSession().getAttribute("user");

        Long violationId = extractId(req.getPathInfo());
        if (violationId == null) {
            resp.sendError(HttpServletResponse.SC_NOT_FOUND);
            return;
        }

        Optional<Violation> violation = violationService.findByUserIdAndViolationId(user.getId(), violationId);
        if (violation.isEmpty()) {
            resp.sendError(HttpServletResponse.SC_NOT_FOUND);
            return;
        }

        req.setAttribute("violation", violation.get());
        req.setAttribute("pageTitle", "Нарушение #" + violationId);
        req.getRequestDispatcher("/WEB-INF/jsp/account/violation-detail.jsp").forward(req, resp);
    }

    private Long extractId(String pathInfo) {
        if (pathInfo == null) return null;
        try {
            return Long.parseLong(pathInfo.substring(1));
        } catch (NumberFormatException e) {
            return null;
        }
    }
}
