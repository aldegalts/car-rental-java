package com.degaltseva.carrental.servlet.admin;

import com.degaltseva.carrental.model.Car;
import com.degaltseva.carrental.service.CarCategoryService;
import com.degaltseva.carrental.service.CarColorService;
import com.degaltseva.carrental.service.CarService;
import com.degaltseva.carrental.service.CarStatusService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.Optional;

@WebServlet("/admin/cars/*")
public class AdminCarsServlet extends HttpServlet {

    private final CarService carService = new CarService();
    private final CarCategoryService categoryService = new CarCategoryService();
    private final CarColorService colorService = new CarColorService();
    private final CarStatusService statusService = new CarStatusService();

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
        req.setAttribute("cars", carService.findAll());
        req.setAttribute("pageTitle", "Машины — Админ");
        req.getRequestDispatcher("/WEB-INF/jsp/admin/cars-list.jsp").forward(req, resp);
    }

    private void showCreateForm(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        setDropdowns(req);
        req.setAttribute("pageTitle", "Новая машина");
        req.getRequestDispatcher("/WEB-INF/jsp/admin/car-form.jsp").forward(req, resp);
    }

    private void showEditForm(HttpServletRequest req, HttpServletResponse resp, String pathInfo)
            throws ServletException, IOException {
        Long id = extractId(pathInfo);
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
        setDropdowns(req);
        req.setAttribute("pageTitle", "Редактирование машины");
        req.getRequestDispatcher("/WEB-INF/jsp/admin/car-form.jsp").forward(req, resp);
    }

    private void doCreate(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Car car = parseForm(req);
        String error = validate(car);

        if (error != null) {
            req.setAttribute("error", error);
            req.setAttribute("car", car);
            setDropdowns(req);
            req.setAttribute("pageTitle", "Новая машина");
            req.getRequestDispatcher("/WEB-INF/jsp/admin/car-form.jsp").forward(req, resp);
            return;
        }

        carService.save(car);
        resp.sendRedirect(req.getContextPath() + "/admin/cars?message=created");
    }

    private void doUpdate(HttpServletRequest req, HttpServletResponse resp, String pathInfo)
            throws ServletException, IOException {
        Long id = extractId(pathInfo);
        if (id == null) {
            resp.sendError(HttpServletResponse.SC_NOT_FOUND);
            return;
        }

        Optional<Car> existing = carService.findById(id);
        if (existing.isEmpty()) {
            resp.sendError(HttpServletResponse.SC_NOT_FOUND);
            return;
        }

        Car car = parseForm(req);
        car.setId(id);
        String error = validate(car);

        if (error != null) {
            req.setAttribute("error", error);
            req.setAttribute("car", car);
            setDropdowns(req);
            req.setAttribute("pageTitle", "Редактирование машины");
            req.getRequestDispatcher("/WEB-INF/jsp/admin/car-form.jsp").forward(req, resp);
            return;
        }

        carService.update(car);
        resp.sendRedirect(req.getContextPath() + "/admin/cars?message=updated");
    }

    private void doDelete(HttpServletRequest req, HttpServletResponse resp, String pathInfo) throws IOException {
        Long id = extractId(pathInfo);
        if (id == null) {
            resp.sendError(HttpServletResponse.SC_NOT_FOUND);
            return;
        }

        carService.delete(id);
        resp.sendRedirect(req.getContextPath() + "/admin/cars?message=deleted");
    }

    private Car parseForm(HttpServletRequest req) {
        Car car = new Car();
        car.setBrand(req.getParameter("brand"));
        car.setModel(req.getParameter("model"));

        String yearStr = req.getParameter("year");
        if (yearStr != null && !yearStr.isBlank()) {
            try { car.setYear(Integer.parseInt(yearStr.trim())); }
            catch (NumberFormatException ignored) {}
        }

        String categoryId = req.getParameter("categoryId");
        if (categoryId != null && !categoryId.isBlank()) {
            try { car.setCategoryId(Long.parseLong(categoryId.trim())); }
            catch (NumberFormatException ignored) {}
        }

        car.setLicensePlate(req.getParameter("licensePlate"));

        String colorId = req.getParameter("colorId");
        if (colorId != null && !colorId.isBlank()) {
            try { car.setColorId(Long.parseLong(colorId.trim())); }
            catch (NumberFormatException ignored) {}
        }

        String dailyCost = req.getParameter("dailyCost");
        if (dailyCost != null && !dailyCost.isBlank()) {
            try { car.setDailyCost(new BigDecimal(dailyCost.trim())); }
            catch (NumberFormatException ignored) {}
        }

        String statusId = req.getParameter("carStatusId");
        if (statusId != null && !statusId.isBlank()) {
            try { car.setCarStatusId(Long.parseLong(statusId.trim())); }
            catch (NumberFormatException ignored) {}
        }

        return car;
    }

    private String validate(Car car) {
        if (car.getBrand() == null || car.getBrand().isBlank()) return "Марка обязательна";
        if (car.getModel() == null || car.getModel().isBlank()) return "Модель обязательна";
        if (car.getYear() < 1900 || car.getYear() > 2100) return "Некорректный год выпуска";
        if (car.getCategoryId() == null) return "Категория обязательна";
        if (car.getLicensePlate() == null || car.getLicensePlate().isBlank()) return "Гос. номер обязателен";
        if (car.getColorId() == null) return "Цвет обязателен";
        if (car.getDailyCost() == null || car.getDailyCost().compareTo(BigDecimal.ZERO) <= 0)
            return "Стоимость аренды должна быть положительной";
        if (car.getCarStatusId() == null) return "Статус обязателен";
        return null;
    }

    private void setDropdowns(HttpServletRequest req) {
        req.setAttribute("categories", categoryService.findAll());
        req.setAttribute("colors", colorService.findAll());
        req.setAttribute("statuses", statusService.findAll());
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
