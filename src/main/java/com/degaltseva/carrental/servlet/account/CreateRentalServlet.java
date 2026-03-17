package com.degaltseva.carrental.servlet.account;

import com.degaltseva.carrental.model.Car;
import com.degaltseva.carrental.model.Client;
import com.degaltseva.carrental.model.User;
import com.degaltseva.carrental.service.CarService;
import com.degaltseva.carrental.service.ClientService;
import com.degaltseva.carrental.service.RentalService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Optional;

@WebServlet("/rentals/create/*")
public class CreateRentalServlet extends HttpServlet {

    private final RentalService rentalService = new RentalService();
    private final CarService carService = new CarService();
    private final ClientService clientService = new ClientService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        User user = (User) req.getSession().getAttribute("user");

        Optional<Client> client = clientService.findByUserId(user.getId());
        if (client.isEmpty()) {
            resp.sendRedirect(req.getContextPath() + "/profile/fill");
            return;
        }

        Long carId = extractCarId(req.getPathInfo());
        if (carId == null) {
            resp.sendError(HttpServletResponse.SC_NOT_FOUND);
            return;
        }

        Optional<Car> car = carService.findById(carId);
        if (car.isEmpty()) {
            resp.sendError(HttpServletResponse.SC_NOT_FOUND);
            return;
        }

        req.setAttribute("car", car.get());
        req.setAttribute("pageTitle", "Оформление аренды");
        req.getRequestDispatcher("/WEB-INF/jsp/account/create-rental.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        User user = (User) req.getSession().getAttribute("user");

        Optional<Client> client = clientService.findByUserId(user.getId());
        if (client.isEmpty()) {
            resp.sendRedirect(req.getContextPath() + "/profile/fill");
            return;
        }

        Long carId = extractCarId(req.getPathInfo());
        if (carId == null) {
            resp.sendError(HttpServletResponse.SC_NOT_FOUND);
            return;
        }

        Optional<Car> car = carService.findById(carId);
        if (car.isEmpty()) {
            resp.sendError(HttpServletResponse.SC_NOT_FOUND);
            return;
        }

        String endDateStr = req.getParameter("endDate");
        if (endDateStr == null || endDateStr.isBlank()) {
            req.setAttribute("error", "Укажите дату окончания аренды");
            req.setAttribute("car", car.get());
            req.setAttribute("pageTitle", "Оформление аренды");
            req.getRequestDispatcher("/WEB-INF/jsp/account/create-rental.jsp").forward(req, resp);
            return;
        }

        try {
            LocalDateTime endDate = LocalDateTime.parse(endDateStr);
            rentalService.createRental(client.get().getId(), carId, endDate);
            resp.sendRedirect(req.getContextPath() + "/account/rentals?message=created");
        } catch (IllegalArgumentException e) {
            req.setAttribute("error", e.getMessage());
            req.setAttribute("car", car.get());
            req.setAttribute("pageTitle", "Оформление аренды");
            req.getRequestDispatcher("/WEB-INF/jsp/account/create-rental.jsp").forward(req, resp);
        }
    }

    private Long extractCarId(String pathInfo) {
        if (pathInfo == null) return null;
        try {
            return Long.parseLong(pathInfo.substring(1));
        } catch (NumberFormatException e) {
            return null;
        }
    }
}
