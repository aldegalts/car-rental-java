package com.degaltseva.carrental.servlet.admin;

import com.degaltseva.carrental.model.Rental;
import com.degaltseva.carrental.service.*;
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

@WebServlet("/admin/rentals/*")
public class AdminRentalsServlet extends HttpServlet {

    private final RentalService rentalService = new RentalService();
    private final CarService carService = new CarService();
    private final ClientService clientService = new ClientService();
    private final RentalStatusService rentalStatusService = new RentalStatusService();
    private final ViolationService violationService = new ViolationService();
    private final ViolationTypeService violationTypeService = new ViolationTypeService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String pathInfo = req.getPathInfo();

        if (pathInfo == null || pathInfo.equals("/")) {
            showList(req, resp);
        } else if (pathInfo.matches("/\\d+")) {
            showDetail(req, resp, pathInfo);
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

        if (pathInfo.matches("/\\d+/edit")) {
            doUpdate(req, resp, pathInfo);
        } else if (pathInfo.matches("/\\d+/delete")) {
            doDelete(req, resp, pathInfo);
        } else {
            resp.sendError(HttpServletResponse.SC_NOT_FOUND);
        }
    }

    private void showList(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        rentalService.completeExpiredRentals();

        String carIdStr = req.getParameter("carId");
        String clientIdStr = req.getParameter("clientId");

        Long carId = parseLong(carIdStr);
        Long clientId = parseLong(clientIdStr);

        if (carId != null || clientId != null) {
            req.setAttribute("rentals", rentalService.filter(carId, clientId));
        } else {
            req.setAttribute("rentals", rentalService.findAll());
        }

        req.setAttribute("cars", carService.findAll());
        req.setAttribute("clients", clientService.findAll());
        req.setAttribute("selectedCarId", carId);
        req.setAttribute("selectedClientId", clientId);

        String message = req.getParameter("message");
        if ("updated".equals(message)) req.setAttribute("message", "Аренда обновлена");
        if ("deleted".equals(message)) req.setAttribute("message", "Аренда удалена");

        req.setAttribute("pageTitle", "Аренды — Админ");
        req.getRequestDispatcher("/WEB-INF/jsp/admin/rentals-list.jsp").forward(req, resp);
    }

    private void showDetail(HttpServletRequest req, HttpServletResponse resp, String pathInfo)
            throws ServletException, IOException {
        Long id = extractId(pathInfo);
        if (id == null) {
            resp.sendError(HttpServletResponse.SC_NOT_FOUND);
            return;
        }

        Optional<Rental> rental = rentalService.findById(id);
        if (rental.isEmpty()) {
            resp.sendError(HttpServletResponse.SC_NOT_FOUND);
            return;
        }

        String message = req.getParameter("message");
        if ("updated".equals(message)) req.setAttribute("message", "Аренда обновлена");
        if ("violation_created".equals(message)) req.setAttribute("message", "Нарушение зафиксировано");
        if ("violation_updated".equals(message)) req.setAttribute("message", "Нарушение обновлено");
        if ("violation_deleted".equals(message)) req.setAttribute("message", "Нарушение удалено");

        req.setAttribute("rental", rental.get());
        req.setAttribute("violations", violationService.findByRentalId(id));
        req.setAttribute("violationTypes", violationTypeService.findAll());
        req.setAttribute("pageTitle", "Аренда #" + id);
        req.getRequestDispatcher("/WEB-INF/jsp/admin/rental-detail.jsp").forward(req, resp);
    }

    private void showEditForm(HttpServletRequest req, HttpServletResponse resp, String pathInfo)
            throws ServletException, IOException {
        Long id = extractId(pathInfo);
        if (id == null) {
            resp.sendError(HttpServletResponse.SC_NOT_FOUND);
            return;
        }

        Optional<Rental> rental = rentalService.findById(id);
        if (rental.isEmpty()) {
            resp.sendError(HttpServletResponse.SC_NOT_FOUND);
            return;
        }

        req.setAttribute("rental", rental.get());
        setDropdowns(req);
        req.setAttribute("pageTitle", "Редактирование аренды");
        req.getRequestDispatcher("/WEB-INF/jsp/admin/rental-edit.jsp").forward(req, resp);
    }

    private void doUpdate(HttpServletRequest req, HttpServletResponse resp, String pathInfo)
            throws ServletException, IOException {
        Long id = extractId(pathInfo);
        if (id == null) {
            resp.sendError(HttpServletResponse.SC_NOT_FOUND);
            return;
        }

        Optional<Rental> existing = rentalService.findById(id);
        if (existing.isEmpty()) {
            resp.sendError(HttpServletResponse.SC_NOT_FOUND);
            return;
        }

        Rental rental = existing.get();

        String carIdStr = req.getParameter("carId");
        if (carIdStr != null && !carIdStr.isBlank()) {
            try { rental.setCarId(Long.parseLong(carIdStr.trim())); }
            catch (NumberFormatException ignored) {}
        }

        String startDateStr = req.getParameter("startDate");
        if (startDateStr != null && !startDateStr.isBlank()) {
            try { rental.setStartDate(LocalDateTime.parse(startDateStr)); }
            catch (Exception ignored) {}
        }

        String endDateStr = req.getParameter("endDate");
        if (endDateStr != null && !endDateStr.isBlank()) {
            try { rental.setEndDate(LocalDateTime.parse(endDateStr)); }
            catch (Exception ignored) {}
        }

        String totalAmountStr = req.getParameter("totalAmount");
        if (totalAmountStr != null && !totalAmountStr.isBlank()) {
            try { rental.setTotalAmount(new BigDecimal(totalAmountStr.trim())); }
            catch (NumberFormatException ignored) {}
        }

        String statusIdStr = req.getParameter("rentalStatusId");
        if (statusIdStr != null && !statusIdStr.isBlank()) {
            try { rental.setRentalStatusId(Long.parseLong(statusIdStr.trim())); }
            catch (NumberFormatException ignored) {}
        }

        rentalService.update(rental);
        resp.sendRedirect(req.getContextPath() + "/admin/rentals/" + id + "?message=updated");
    }

    private void doDelete(HttpServletRequest req, HttpServletResponse resp, String pathInfo) throws IOException {
        Long id = extractId(pathInfo);
        if (id == null) {
            resp.sendError(HttpServletResponse.SC_NOT_FOUND);
            return;
        }

        rentalService.delete(id);
        resp.sendRedirect(req.getContextPath() + "/admin/rentals?message=deleted");
    }

    private void setDropdowns(HttpServletRequest req) {
        req.setAttribute("cars", carService.findAll());
        req.setAttribute("clients", clientService.findAll());
        req.setAttribute("statuses", rentalStatusService.findAll());
    }

    private Long extractId(String pathInfo) {
        try {
            String[] parts = pathInfo.split("/");
            return Long.parseLong(parts[1]);
        } catch (Exception e) {
            return null;
        }
    }

    private Long parseLong(String value) {
        if (value == null || value.isBlank()) return null;
        try { return Long.parseLong(value.trim()); }
        catch (NumberFormatException e) { return null; }
    }
}
