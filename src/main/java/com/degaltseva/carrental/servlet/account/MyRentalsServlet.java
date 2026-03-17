package com.degaltseva.carrental.servlet.account;

import com.degaltseva.carrental.model.User;
import com.degaltseva.carrental.service.RentalService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet("/account/rentals")
public class MyRentalsServlet extends HttpServlet {

    private final RentalService rentalService = new RentalService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        User user = (User) req.getSession().getAttribute("user");

        rentalService.completeExpiredRentals();

        String messageParam = req.getParameter("message");
        if ("created".equals(messageParam)) {
            req.setAttribute("message", "Аренда успешно оформлена!");
        }

        req.setAttribute("rentals", rentalService.findByUserId(user.getId()));
        req.setAttribute("pageTitle", "Мои аренды");
        req.getRequestDispatcher("/WEB-INF/jsp/account/my-rentals.jsp").forward(req, resp);
    }
}
