package com.degaltseva.carrental.servlet.catalog;

import com.degaltseva.carrental.model.Car;
import com.degaltseva.carrental.model.CarCategory;
import com.degaltseva.carrental.model.CarColor;
import com.degaltseva.carrental.service.CarCategoryService;
import com.degaltseva.carrental.service.CarColorService;
import com.degaltseva.carrental.service.CarService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;

@WebServlet("/catalog")
public class CatalogServlet extends HttpServlet {

    private final CarService carService = new CarService();
    private final CarCategoryService categoryService = new CarCategoryService();
    private final CarColorService colorService = new CarColorService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String brand = req.getParameter("brand");
        String model = req.getParameter("model");
        Long categoryId = parseLong(req.getParameter("categoryId"));
        Long colorId = parseLong(req.getParameter("colorId"));
        Integer minYear = parseInteger(req.getParameter("minYear"));
        Integer maxYear = parseInteger(req.getParameter("maxYear"));
        BigDecimal minCost = parseBigDecimal(req.getParameter("minCost"));
        BigDecimal maxCost = parseBigDecimal(req.getParameter("maxCost"));

        boolean hasFilters = brand != null && !brand.isBlank()
                || model != null && !model.isBlank()
                || categoryId != null || colorId != null
                || minYear != null || maxYear != null
                || minCost != null || maxCost != null;

        List<Car> cars;
        if (hasFilters) {
            cars = carService.filter(brand, model, categoryId, colorId, minYear, maxYear, minCost, maxCost);
        } else {
            cars = carService.findAll();
        }

        List<CarCategory> categories = categoryService.findAll();
        List<CarColor> colors = colorService.findAll();

        req.setAttribute("cars", cars);
        req.setAttribute("categories", categories);
        req.setAttribute("colors", colors);
        req.setAttribute("pageTitle", "Каталог автомобилей");

        // Preserve filter values
        req.setAttribute("filterBrand", brand);
        req.setAttribute("filterModel", model);
        req.setAttribute("filterCategoryId", categoryId);
        req.setAttribute("filterColorId", colorId);
        req.setAttribute("filterMinYear", minYear);
        req.setAttribute("filterMaxYear", maxYear);
        req.setAttribute("filterMinCost", minCost);
        req.setAttribute("filterMaxCost", maxCost);

        req.getRequestDispatcher("/WEB-INF/jsp/catalog/list.jsp").forward(req, resp);
    }

    private Long parseLong(String value) {
        if (value == null || value.isBlank()) return null;
        try { return Long.parseLong(value.trim()); }
        catch (NumberFormatException e) { return null; }
    }

    private Integer parseInteger(String value) {
        if (value == null || value.isBlank()) return null;
        try { return Integer.parseInt(value.trim()); }
        catch (NumberFormatException e) { return null; }
    }

    private BigDecimal parseBigDecimal(String value) {
        if (value == null || value.isBlank()) return null;
        try { return new BigDecimal(value.trim()); }
        catch (NumberFormatException e) { return null; }
    }
}
