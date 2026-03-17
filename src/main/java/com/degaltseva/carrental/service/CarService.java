package com.degaltseva.carrental.service;

import com.degaltseva.carrental.model.Car;
import com.degaltseva.carrental.model.CarCategory;
import com.degaltseva.carrental.model.CarColor;
import com.degaltseva.carrental.model.CarStatus;
import com.degaltseva.carrental.repository.CarCategoryRepository;
import com.degaltseva.carrental.repository.CarColorRepository;
import com.degaltseva.carrental.repository.CarRepository;
import com.degaltseva.carrental.repository.CarStatusRepository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

public class CarService {

    private final CarRepository carRepository = new CarRepository();
    private final CarCategoryRepository categoryRepository = new CarCategoryRepository();
    private final CarColorRepository colorRepository = new CarColorRepository();
    private final CarStatusRepository statusRepository = new CarStatusRepository();

    public List<Car> findAll() {
        List<Car> cars = carRepository.findAll();
        enrichAll(cars);
        return cars;
    }

    public Optional<Car> findById(Long id) {
        Optional<Car> car = carRepository.findById(id);
        car.ifPresent(this::enrich);
        return car;
    }

    public List<Car> filter(String brand, String model, Long categoryId, Long colorId,
                            Integer minYear, Integer maxYear, BigDecimal minCost, BigDecimal maxCost) {
        List<Car> cars = carRepository.filter(brand, model, categoryId, colorId,
                minYear, maxYear, minCost, maxCost);
        enrichAll(cars);
        return cars;
    }

    public Car save(Car car) {
        return carRepository.save(car);
    }

    public Car update(Car car) {
        return carRepository.update(car);
    }

    public void delete(Long id) {
        carRepository.delete(id);
    }

    private void enrichAll(List<Car> cars) {
        if (cars.isEmpty()) return;

        Map<Long, CarCategory> categories = categoryRepository.findAll().stream()
                .collect(Collectors.toMap(CarCategory::getId, Function.identity()));
        Map<Long, CarColor> colors = colorRepository.findAll().stream()
                .collect(Collectors.toMap(CarColor::getId, Function.identity()));
        Map<Long, CarStatus> statuses = statusRepository.findAll().stream()
                .collect(Collectors.toMap(CarStatus::getId, Function.identity()));

        for (Car car : cars) {
            CarCategory cat = categories.get(car.getCategoryId());
            if (cat != null) car.setCategoryName(cat.getCategoryName());

            CarColor color = colors.get(car.getColorId());
            if (color != null) {
                car.setColorName(color.getColor());
                car.setColorHex(color.getHex());
            }

            CarStatus status = statuses.get(car.getCarStatusId());
            if (status != null) car.setStatusName(status.getStatus());
        }
    }

    private void enrich(Car car) {
        categoryRepository.findById(car.getCategoryId())
                .ifPresent(c -> car.setCategoryName(c.getCategoryName()));
        colorRepository.findById(car.getColorId())
                .ifPresent(c -> {
                    car.setColorName(c.getColor());
                    car.setColorHex(c.getHex());
                });
        statusRepository.findById(car.getCarStatusId())
                .ifPresent(s -> car.setStatusName(s.getStatus()));
    }
}
