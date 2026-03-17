package com.degaltseva.carrental.servlet.admin;

import com.degaltseva.carrental.service.StatisticsService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;

@WebServlet("/admin/statistics")
public class AdminStatisticsServlet extends HttpServlet {

    private final StatisticsService statisticsService = new StatisticsService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String startDateStr = req.getParameter("startDate");
        String endDateStr = req.getParameter("endDate");

        if (startDateStr != null && !startDateStr.isBlank()
                && endDateStr != null && !endDateStr.isBlank()) {
            try {
                LocalDateTime startDate = LocalDateTime.parse(startDateStr);
                LocalDateTime endDate = LocalDateTime.parse(endDateStr);

                Map<String, Object> stats = statisticsService.getRentalStatistics(startDate, endDate);
                req.setAttribute("stats", stats);
                req.setAttribute("startDate", startDateStr);
                req.setAttribute("endDate", endDateStr);
            } catch (Exception e) {
                req.setAttribute("error", "Неправильный формат даты");
            }
        }

        req.setAttribute("pageTitle", "Статистика аренд");
        req.getRequestDispatcher("/WEB-INF/jsp/admin/statistics.jsp").forward(req, resp);
    }
}
