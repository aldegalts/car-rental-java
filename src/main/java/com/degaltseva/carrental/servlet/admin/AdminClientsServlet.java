package com.degaltseva.carrental.servlet.admin;

import com.degaltseva.carrental.model.Client;
import com.degaltseva.carrental.service.ClientService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.time.LocalDate;
import java.util.Optional;

@WebServlet("/admin/clients/*")
public class AdminClientsServlet extends HttpServlet {

    private final ClientService clientService = new ClientService();

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

        if (pathInfo != null && pathInfo.matches("/\\d+/edit")) {
            doUpdate(req, resp, pathInfo);
        } else {
            resp.sendError(HttpServletResponse.SC_NOT_FOUND);
        }
    }

    private void showList(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setAttribute("clients", clientService.findAll());
        req.setAttribute("pageTitle", "Клиенты — Админ");
        req.getRequestDispatcher("/WEB-INF/jsp/admin/clients-list.jsp").forward(req, resp);
    }

    private void showDetail(HttpServletRequest req, HttpServletResponse resp, String pathInfo)
            throws ServletException, IOException {
        Long id = extractId(pathInfo);
        if (id == null) {
            resp.sendError(HttpServletResponse.SC_NOT_FOUND);
            return;
        }

        Optional<Client> client = clientService.findById(id);
        if (client.isEmpty()) {
            resp.sendError(HttpServletResponse.SC_NOT_FOUND);
            return;
        }

        req.setAttribute("client", client.get());
        req.setAttribute("pageTitle", "Клиент #" + id);
        req.getRequestDispatcher("/WEB-INF/jsp/admin/client-detail.jsp").forward(req, resp);
    }

    private void showEditForm(HttpServletRequest req, HttpServletResponse resp, String pathInfo)
            throws ServletException, IOException {
        Long id = extractId(pathInfo);
        if (id == null) {
            resp.sendError(HttpServletResponse.SC_NOT_FOUND);
            return;
        }

        Optional<Client> client = clientService.findById(id);
        if (client.isEmpty()) {
            resp.sendError(HttpServletResponse.SC_NOT_FOUND);
            return;
        }

        req.setAttribute("client", client.get());
        req.setAttribute("pageTitle", "Редактирование клиента");
        req.getRequestDispatcher("/WEB-INF/jsp/admin/client-edit.jsp").forward(req, resp);
    }

    private void doUpdate(HttpServletRequest req, HttpServletResponse resp, String pathInfo)
            throws ServletException, IOException {
        Long id = extractId(pathInfo);
        if (id == null) {
            resp.sendError(HttpServletResponse.SC_NOT_FOUND);
            return;
        }

        Optional<Client> existing = clientService.findById(id);
        if (existing.isEmpty()) {
            resp.sendError(HttpServletResponse.SC_NOT_FOUND);
            return;
        }

        Client client = existing.get();
        client.setName(req.getParameter("name"));
        client.setSurname(req.getParameter("surname"));
        client.setPhone(req.getParameter("phone"));
        client.setEmail(req.getParameter("email"));
        client.setDriverLicense(req.getParameter("driverLicense"));

        String error = validate(client);
        if (error != null) {
            req.setAttribute("error", error);
            req.setAttribute("client", client);
            req.setAttribute("pageTitle", "Редактирование клиента");
            req.getRequestDispatcher("/WEB-INF/jsp/admin/client-edit.jsp").forward(req, resp);
            return;
        }

        clientService.update(client);
        resp.sendRedirect(req.getContextPath() + "/admin/clients/" + id + "?message=updated");
    }

    private String validate(Client client) {
        if (client.getName() == null || client.getName().isBlank()) return "Имя обязательно";
        if (client.getSurname() == null || client.getSurname().isBlank()) return "Фамилия обязательна";
        if (client.getPhone() == null || client.getPhone().isBlank()) return "Телефон обязателен";
        if (client.getEmail() == null || client.getEmail().isBlank()) return "Email обязателен";
        if (client.getDriverLicense() == null || client.getDriverLicense().isBlank())
            return "Номер ВУ обязателен";
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
