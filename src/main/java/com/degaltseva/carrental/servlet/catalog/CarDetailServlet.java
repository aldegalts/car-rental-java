package com.degaltseva.carrental.servlet.catalog;

import com.degaltseva.carrental.model.Car;
import com.degaltseva.carrental.service.CarService;
import com.degaltseva.carrental.util.PathUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.Optional;

@WebServlet("/catalog/*")
public class CarDetailServlet extends HttpServlet {

    private final CarService carService = new CarService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Long id = PathUtil.getPathId(req);
        if (id == null) {
            resp.sendError(HttpServletResponse.SC_NOT_FOUND);
            return;
        }

        Optional<Car> car = carService.findById(id);
        if (car.isEmpty()) {
            resp.sendError(HttpServletResponse.SC_NOT_FOUND);
            return;
        }

        req.setAttribute("car", car.get());
        req.setAttribute("pageTitle", car.get().getBrand() + " " + car.get().getModel());
        req.getRequestDispatcher("/WEB-INF/jsp/catalog/detail.jsp").forward(req, resp);
    }
}
