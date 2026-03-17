package com.degaltseva.carrental.servlet.account;

import com.degaltseva.carrental.model.Client;
import com.degaltseva.carrental.model.User;
import com.degaltseva.carrental.service.ClientService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.time.LocalDate;
import java.util.Optional;

@WebServlet(urlPatterns = {"/profile", "/profile/fill"})
public class ProfileServlet extends HttpServlet {

    private final ClientService clientService = new ClientService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String path = req.getServletPath();
        User user = (User) req.getSession().getAttribute("user");

        if ("/profile/fill".equals(path)) {
            showFillForm(req, resp, user);
        } else {
            showProfile(req, resp, user);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String path = req.getServletPath();
        User user = (User) req.getSession().getAttribute("user");

        if ("/profile/fill".equals(path)) {
            doFill(req, resp, user);
        } else {
            resp.sendError(HttpServletResponse.SC_NOT_FOUND);
        }
    }

    private void showProfile(HttpServletRequest req, HttpServletResponse resp, User user)
            throws ServletException, IOException {
        Optional<Client> client = clientService.findByUserId(user.getId());

        if (client.isEmpty()) {
            resp.sendRedirect(req.getContextPath() + "/profile/fill");
            return;
        }

        req.setAttribute("client", client.get());
        req.setAttribute("pageTitle", "Мой профиль");
        req.getRequestDispatcher("/WEB-INF/jsp/account/profile.jsp").forward(req, resp);
    }

    private void showFillForm(HttpServletRequest req, HttpServletResponse resp, User user)
            throws ServletException, IOException {
        Optional<Client> existing = clientService.findByUserId(user.getId());
        if (existing.isPresent()) {
            resp.sendRedirect(req.getContextPath() + "/profile");
            return;
        }

        req.setAttribute("pageTitle", "Заполнение профиля");
        req.getRequestDispatcher("/WEB-INF/jsp/account/profile-fill.jsp").forward(req, resp);
    }

    private void doFill(HttpServletRequest req, HttpServletResponse resp, User user)
            throws ServletException, IOException {
        Optional<Client> existing = clientService.findByUserId(user.getId());
        if (existing.isPresent()) {
            resp.sendRedirect(req.getContextPath() + "/profile");
            return;
        }

        Client client = parseForm(req);
        client.setUserId(user.getId());

        String error = validate(client);
        if (error != null) {
            req.setAttribute("error", error);
            req.setAttribute("client", client);
            req.setAttribute("pageTitle", "Заполнение профиля");
            req.getRequestDispatcher("/WEB-INF/jsp/account/profile-fill.jsp").forward(req, resp);
            return;
        }

        clientService.save(client);
        resp.sendRedirect(req.getContextPath() + "/profile");
    }

    private Client parseForm(HttpServletRequest req) {
        Client client = new Client();
        client.setName(req.getParameter("name"));
        client.setSurname(req.getParameter("surname"));
        client.setPhone(req.getParameter("phone"));
        client.setEmail(req.getParameter("email"));
        client.setDriverLicense(req.getParameter("driverLicense"));

        String birthDate = req.getParameter("birthDate");
        if (birthDate != null && !birthDate.isBlank()) {
            try { client.setBirthDate(LocalDate.parse(birthDate)); }
            catch (Exception ignored) {}
        }

        String licenseExpiry = req.getParameter("licenseExpiryDate");
        if (licenseExpiry != null && !licenseExpiry.isBlank()) {
            try { client.setLicenseExpiryDate(LocalDate.parse(licenseExpiry)); }
            catch (Exception ignored) {}
        }

        return client;
    }

    private String validate(Client client) {
        if (client.getName() == null || client.getName().isBlank()) return "Имя обязательно";
        if (client.getSurname() == null || client.getSurname().isBlank()) return "Фамилия обязательна";
        if (client.getBirthDate() == null) return "Дата рождения обязательна";
        if (client.getPhone() == null || client.getPhone().isBlank()) return "Телефон обязателен";
        if (client.getEmail() == null || client.getEmail().isBlank()) return "Email обязателен";
        if (client.getDriverLicense() == null || client.getDriverLicense().isBlank())
            return "Номер водительского удостоверения обязателен";
        if (client.getLicenseExpiryDate() == null) return "Дата окончания ВУ обязательна";
        return null;
    }
}
