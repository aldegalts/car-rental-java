package com.degaltseva.carrental.service;

import com.degaltseva.carrental.model.Rental;
import com.degaltseva.carrental.repository.CarRepository;
import com.degaltseva.carrental.repository.ClientRepository;
import com.degaltseva.carrental.repository.RentalRepository;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class StatisticsService {

    private final RentalRepository rentalRepository = new RentalRepository();
    private final CarRepository carRepository = new CarRepository();
    private final ClientRepository clientRepository = new ClientRepository();

    public Map<String, Object> getDashboardStats() {
        Map<String, Object> stats = new HashMap<>();
        stats.put("cars", carRepository.findAll().size());
        stats.put("clients", clientRepository.findAll().size());
        stats.put("rentals", rentalRepository.findAll().size());
        return stats;
    }

    public Map<String, Object> getRentalStatistics(LocalDateTime startDate, LocalDateTime endDate) {
        List<Rental> rentals = rentalRepository.findByDateRange(startDate, endDate);
        int totalCount = rentals.size();

        double percentWith = 0;
        double percentWithout = 0;

        if (totalCount > 0) {
            int violationCount = rentalRepository.countRentalsWithViolations(startDate, endDate);
            percentWith = Math.round((double) violationCount / totalCount * 10000.0) / 100.0;
            percentWithout = Math.round((100.0 - percentWith) * 100.0) / 100.0;
        }

        Map<String, Object> stats = new HashMap<>();
        stats.put("totalRentals", totalCount);
        stats.put("rentals", rentals);
        stats.put("percentWithViolations", percentWith);
        stats.put("percentWithoutViolations", percentWithout);
        return stats;
    }
}
