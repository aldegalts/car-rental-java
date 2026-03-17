package com.degaltseva.carrental.servlet.admin;

import com.degaltseva.carrental.model.Violation;
import com.degaltseva.carrental.service.ViolationService;
import com.degaltseva.carrental.service.ViolationTypeService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;

@WebServlet("/admin/rentals/*/violations/*")
public class AdminViolationsServlet extends HttpServlet {

    private final ViolationService violationService = new ViolationService();
    private final ViolationTypeService violationTypeService = new ViolationTypeService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        PathInfo path = parsePath(req);
        if (path == null) {
            resp.sendError(HttpServletResponse.SC_NOT_FOUND);
            return;
        }

        if (path.violationId != null && path.action != null && path.action.equals("edit")) {
            showEditForm(req, resp, path);
        } else {
            resp.sendError(HttpServletResponse.SC_NOT_FOUND);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        PathInfo path = parsePath(req);
        if (path == null) {
            resp.sendError(HttpServletResponse.SC_NOT_FOUND);
            return;
        }

        if (path.violationId == null) {
            // POST /admin/rentals/{rentalId}/violations — create
            doCreate(req, resp, path.rentalId);
        } else if ("edit".equals(path.action)) {
            doUpdate(req, resp, path);
        } else if ("delete".equals(path.action)) {
            doDelete(req, resp, path);
        } else {
            resp.sendError(HttpServletResponse.SC_NOT_FOUND);
        }
    }

    private void showEditForm(HttpServletRequest req, HttpServletResponse resp, PathInfo path)
            throws ServletException, IOException {
        Optional<Violation> violation = violationService.findById(path.violationId);
        if (violation.isEmpty()) {
            resp.sendError(HttpServletResponse.SC_NOT_FOUND);
            return;
        }

        req.setAttribute("violation", violation.get());
        req.setAttribute("rentalId", path.rentalId);
        req.setAttribute("violationTypes", violationTypeService.findAll());
        req.setAttribute("pageTitle", "Редактирование нарушения");
        req.getRequestDispatcher("/WEB-INF/jsp/admin/violation-edit.jsp").forward(req, resp);
    }

    private void doCreate(HttpServletRequest req, HttpServletResponse resp, Long rentalId)
            throws ServletException, IOException {
        Violation v = parseForm(req);
        v.setRentalId(rentalId);

        String error = validate(v);
        if (error != null) {
            resp.sendRedirect(req.getContextPath() + "/admin/rentals/" + rentalId + "?error=" + error);
            return;
        }

        violationService.save(v);
        resp.sendRedirect(req.getContextPath() + "/admin/rentals/" + rentalId + "?message=violation_created");
    }

    private void doUpdate(HttpServletRequest req, HttpServletResponse resp, PathInfo path)
            throws ServletException, IOException {
        Optional<Violation> existing = violationService.findById(path.violationId);
        if (existing.isEmpty()) {
            resp.sendError(HttpServletResponse.SC_NOT_FOUND);
            return;
        }

        Violation v = parseForm(req);
        v.setId(path.violationId);
        v.setRentalId(path.rentalId);

        String error = validate(v);
        if (error != null) {
            req.setAttribute("error", error);
            req.setAttribute("violation", v);
            req.setAttribute("rentalId", path.rentalId);
            req.setAttribute("violationTypes", violationTypeService.findAll());
            req.setAttribute("pageTitle", "Редактирование нарушения");
            req.getRequestDispatcher("/WEB-INF/jsp/admin/violation-edit.jsp").forward(req, resp);
            return;
        }

        violationService.update(v);
        resp.sendRedirect(req.getContextPath() + "/admin/rentals/" + path.rentalId + "?message=violation_updated");
    }

    private void doDelete(HttpServletRequest req, HttpServletResponse resp, PathInfo path) throws IOException {
        violationService.delete(path.violationId);
        resp.sendRedirect(req.getContextPath() + "/admin/rentals/" + path.rentalId + "?message=violation_deleted");
    }

    private Violation parseForm(HttpServletRequest req) {
        Violation v = new Violation();

        String typeId = req.getParameter("violationTypeId");
        if (typeId != null && !typeId.isBlank()) {
            try { v.setViolationTypeId(Long.parseLong(typeId.trim())); }
            catch (NumberFormatException ignored) {}
        }

        v.setDescription(req.getParameter("description"));

        String fineAmount = req.getParameter("fineAmount");
        if (fineAmount != null && !fineAmount.isBlank()) {
            try { v.setFineAmount(new BigDecimal(fineAmount.trim())); }
            catch (NumberFormatException ignored) {}
        }

        String violationDate = req.getParameter("violationDate");
        if (violationDate != null && !violationDate.isBlank()) {
            try { v.setViolationDate(LocalDateTime.parse(violationDate)); }
            catch (Exception ignored) {}
        }

        v.setPaid("on".equals(req.getParameter("paid")) || "true".equals(req.getParameter("paid")));

        return v;
    }

    private String validate(Violation v) {
        if (v.getViolationTypeId() == null) return "Тип нарушения обязателен";
        if (v.getDescription() == null || v.getDescription().isBlank()) return "Описание обязательно";
        if (v.getFineAmount() == null || v.getFineAmount().compareTo(BigDecimal.ZERO) <= 0)
            return "Сумма штрафа должна быть положительной";
        if (v.getViolationDate() == null) return "Дата нарушения обязательна";
        return null;
    }

    // Parses: /admin/rentals/{rentalId}/violations[/{violationId}[/action]]
    private PathInfo parsePath(HttpServletRequest req) {
        String uri = req.getRequestURI();
        String ctx = req.getContextPath();
        String path = uri.substring(ctx.length()); // e.g. /admin/rentals/5/violations/3/edit

        String prefix = "/admin/rentals/";
        if (!path.startsWith(prefix)) return null;

        String rest = path.substring(prefix.length()); // e.g. 5/violations/3/edit
        String[] parts = rest.split("/");

        // parts[0] = rentalId, parts[1] = "violations", parts[2]? = violationId, parts[3]? = action
        if (parts.length < 2 || !"violations".equals(parts[1])) return null;

        try {
            PathInfo info = new PathInfo();
            info.rentalId = Long.parseLong(parts[0]);

            if (parts.length >= 3 && !parts[2].isEmpty()) {
                info.violationId = Long.parseLong(parts[2]);
            }
            if (parts.length >= 4) {
                info.action = parts[3];
            }

            return info;
        } catch (NumberFormatException e) {
            return null;
        }
    }

    private static class PathInfo {
        Long rentalId;
        Long violationId;
        String action;
    }
}
