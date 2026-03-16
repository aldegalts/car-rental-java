package com.degaltseva.carrental.servlet.admin;

import com.degaltseva.carrental.model.ViolationType;
import com.degaltseva.carrental.service.ViolationTypeService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@WebServlet("/admin/violation-types/*")
public class AdminViolationTypesServlet extends HttpServlet {

    private final ViolationTypeService service = new ViolationTypeService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String pathInfo = req.getPathInfo();

        if (pathInfo == null || pathInfo.equals("/")) {
            showList(req, resp);
        } else if (pathInfo.equals("/create")) {
            showCreateForm(req, resp);
        } else if (pathInfo.matches("/\\d+/edit")) {
            showEditForm(req, resp, pathInfo);
        } else {
            resp.sendError(HttpServletResponse.SC_NOT_FOUND);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String pathInfo = req.getPathInfo();

        if (pathInfo == null) {
            resp.sendError(HttpServletResponse.SC_NOT_FOUND);
            return;
        }

        if (pathInfo.equals("/create")) {
            doCreate(req, resp);
        } else if (pathInfo.matches("/\\d+/edit")) {
            doUpdate(req, resp, pathInfo);
        } else if (pathInfo.matches("/\\d+/delete")) {
            doDelete(req, resp, pathInfo);
        } else {
            resp.sendError(HttpServletResponse.SC_NOT_FOUND);
        }
    }

    private void showList(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        List<ViolationType> violationTypes = service.findAll();
        req.setAttribute("violationTypes", violationTypes);
        req.setAttribute("pageTitle", "Типы нарушений — Админ");
        req.getRequestDispatcher("/WEB-INF/jsp/admin/violation-types-list.jsp").forward(req, resp);
    }

    private void showCreateForm(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setAttribute("pageTitle", "Новый тип нарушения");
        req.getRequestDispatcher("/WEB-INF/jsp/admin/violation-type-form.jsp").forward(req, resp);
    }

    private void showEditForm(HttpServletRequest req, HttpServletResponse resp, String pathInfo)
            throws ServletException, IOException {
        Long id = extractId(pathInfo);
        if (id == null) {
            resp.sendError(HttpServletResponse.SC_NOT_FOUND);
            return;
        }

        Optional<ViolationType> vt = service.findById(id);
        if (vt.isEmpty()) {
            resp.sendError(HttpServletResponse.SC_NOT_FOUND);
            return;
        }

        req.setAttribute("violationType", vt.get());
        req.setAttribute("pageTitle", "Редактирование типа нарушения");
        req.getRequestDispatcher("/WEB-INF/jsp/admin/violation-type-form.jsp").forward(req, resp);
    }

    private void doCreate(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String typeName = req.getParameter("typeName");
        String defaultFineStr = req.getParameter("defaultFine");
        String description = req.getParameter("description");

        String error = validate(typeName, defaultFineStr);
        if (error != null) {
            req.setAttribute("error", error);
            req.setAttribute("typeName", typeName);
            req.setAttribute("defaultFine", defaultFineStr);
            req.setAttribute("description", description);
            req.setAttribute("pageTitle", "Новый тип нарушения");
            req.getRequestDispatcher("/WEB-INF/jsp/admin/violation-type-form.jsp").forward(req, resp);
            return;
        }

        ViolationType vt = new ViolationType();
        vt.setTypeName(typeName.trim());
        vt.setDefaultFine(new BigDecimal(defaultFineStr.trim()));
        vt.setDescription(description != null ? description.trim() : null);
        service.save(vt);

        resp.sendRedirect(req.getContextPath() + "/admin/violation-types?message=created");
    }

    private void doUpdate(HttpServletRequest req, HttpServletResponse resp, String pathInfo)
            throws ServletException, IOException {
        Long id = extractId(pathInfo);
        if (id == null) {
            resp.sendError(HttpServletResponse.SC_NOT_FOUND);
            return;
        }

        Optional<ViolationType> existing = service.findById(id);
        if (existing.isEmpty()) {
            resp.sendError(HttpServletResponse.SC_NOT_FOUND);
            return;
        }

        String typeName = req.getParameter("typeName");
        String defaultFineStr = req.getParameter("defaultFine");
        String description = req.getParameter("description");

        String error = validate(typeName, defaultFineStr);
        if (error != null) {
            req.setAttribute("error", error);
            ViolationType vt = existing.get();
            vt.setTypeName(typeName);
            vt.setDescription(description);
            req.setAttribute("violationType", vt);
            req.setAttribute("defaultFine", defaultFineStr);
            req.setAttribute("pageTitle", "Редактирование типа нарушения");
            req.getRequestDispatcher("/WEB-INF/jsp/admin/violation-type-form.jsp").forward(req, resp);
            return;
        }

        ViolationType vt = existing.get();
        vt.setTypeName(typeName.trim());
        vt.setDefaultFine(new BigDecimal(defaultFineStr.trim()));
        vt.setDescription(description != null ? description.trim() : null);
        service.update(vt);

        resp.sendRedirect(req.getContextPath() + "/admin/violation-types?message=updated");
    }

    private void doDelete(HttpServletRequest req, HttpServletResponse resp, String pathInfo) throws IOException {
        Long id = extractId(pathInfo);
        if (id == null) {
            resp.sendError(HttpServletResponse.SC_NOT_FOUND);
            return;
        }

        service.delete(id);
        resp.sendRedirect(req.getContextPath() + "/admin/violation-types?message=deleted");
    }

    private String validate(String typeName, String defaultFineStr) {
        if (typeName == null || typeName.isBlank()) {
            return "Название типа нарушения обязательно";
        }
        if (defaultFineStr == null || defaultFineStr.isBlank()) {
            return "Сумма штрафа обязательна";
        }
        try {
            BigDecimal fine = new BigDecimal(defaultFineStr.trim());
            if (fine.compareTo(BigDecimal.ZERO) < 0) {
                return "Сумма штрафа не может быть отрицательной";
            }
        } catch (NumberFormatException e) {
            return "Некорректная сумма штрафа";
        }
        return null;
    }

    private Long extractId(String pathInfo) {
        try {
            String[] parts = pathInfo.split("/");
            return Long.parseLong(parts[1]);
        } catch (Exception e) {
            return null;
        }
    }
}
