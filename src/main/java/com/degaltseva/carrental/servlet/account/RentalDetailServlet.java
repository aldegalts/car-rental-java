package com.degaltseva.carrental.servlet.account;

import com.degaltseva.carrental.model.Rental;
import com.degaltseva.carrental.model.User;
import com.degaltseva.carrental.service.RentalService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.Optional;

@WebServlet("/account/rentals/*")
public class RentalDetailServlet extends HttpServlet {

    private final RentalService rentalService = new RentalService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        User user = (User) req.getSession().getAttribute("user");

        Long rentalId = extractId(req.getPathInfo());
        if (rentalId == null) {
            resp.sendError(HttpServletResponse.SC_NOT_FOUND);
            return;
        }

        Optional<Rental> rental = rentalService.findByUserIdAndRentalId(user.getId(), rentalId);
        if (rental.isEmpty()) {
            resp.sendError(HttpServletResponse.SC_NOT_FOUND);
            return;
        }

        req.setAttribute("rental", rental.get());
        req.setAttribute("pageTitle", "Аренда #" + rentalId);
        req.getRequestDispatcher("/WEB-INF/jsp/account/rental-detail.jsp").forward(req, resp);
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
