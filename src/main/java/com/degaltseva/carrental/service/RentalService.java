package com.degaltseva.carrental.service;

import com.degaltseva.carrental.model.*;
import com.degaltseva.carrental.repository.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

public class RentalService {

    private final RentalRepository rentalRepository = new RentalRepository();
    private final ClientRepository clientRepository = new ClientRepository();
    private final CarRepository carRepository = new CarRepository();
    private final RentalStatusRepository rentalStatusRepository = new RentalStatusRepository();
    private final CarStatusRepository carStatusRepository = new CarStatusRepository();

    public List<Rental> findAll() {
        List<Rental> rentals = rentalRepository.findAll();
        enrichAll(rentals);
        return rentals;
    }

    public Optional<Rental> findById(Long id) {
        Optional<Rental> rental = rentalRepository.findById(id);
        rental.ifPresent(this::enrich);
        return rental;
    }

    public List<Rental> findByUserId(Long userId) {
        List<Rental> rentals = rentalRepository.findByUserId(userId);
        enrichAll(rentals);
        return rentals;
    }

    public Optional<Rental> findByUserIdAndRentalId(Long userId, Long rentalId) {
        Optional<Rental> rental = rentalRepository.findByUserIdAndRentalId(userId, rentalId);
        rental.ifPresent(this::enrich);
        return rental;
    }

    public List<Rental> filter(Long carId, Long clientId) {
        List<Rental> rentals = rentalRepository.filter(carId, clientId);
        enrichAll(rentals);
        return rentals;
    }

    public Rental createRental(Long clientId, Long carId, LocalDateTime endDate) {
        Car car = carRepository.findById(carId)
                .orElseThrow(() -> new IllegalArgumentException("Машина не найдена"));

        CarStatus rentedStatus = carStatusRepository.findByStatus("В аренде")
                .orElseThrow(() -> new IllegalStateException("Статус 'В аренде' не найден"));
        if (car.getCarStatusId().equals(rentedStatus.getId())) {
            throw new IllegalArgumentException("Машина уже в аренде");
        }

        RentalStatus activeStatus = rentalStatusRepository.findByStatus("Активна")
                .orElseThrow(() -> new IllegalStateException("Статус 'Активна' не найден"));

        LocalDateTime startDate = LocalDateTime.now();
        long days = ChronoUnit.DAYS.between(startDate.toLocalDate(), endDate.toLocalDate()) + 1;
        if (days < 1) {
            throw new IllegalArgumentException("Дата окончания должна быть не раньше сегодня");
        }

        BigDecimal totalAmount = car.getDailyCost().multiply(BigDecimal.valueOf(days));

        Rental rental = new Rental();
        rental.setClientId(clientId);
        rental.setCarId(carId);
        rental.setStartDate(startDate);
        rental.setEndDate(endDate);
        rental.setTotalAmount(totalAmount);
        rental.setRentalStatusId(activeStatus.getId());

        Rental saved = rentalRepository.save(rental);

        car.setCarStatusId(rentedStatus.getId());
        carRepository.update(car);

        return saved;
    }

    public Rental update(Rental rental) {
        return rentalRepository.update(rental);
    }

    public void updateStatus(Long rentalId, Long statusId) {
        rentalRepository.updateStatus(rentalId, statusId);
    }

    public void delete(Long id) {
        rentalRepository.delete(id);
    }

    public void completeExpiredRentals() {
        RentalStatus activeStatus = rentalStatusRepository.findByStatus("Активна").orElse(null);
        RentalStatus completedStatus = rentalStatusRepository.findByStatus("Завершена").orElse(null);
        CarStatus freeStatus = carStatusRepository.findByStatus("Доступен").orElse(null);

        if (activeStatus == null || completedStatus == null || freeStatus == null) return;

        List<Rental> expired = rentalRepository.findExpiredActive(activeStatus.getId());
        for (Rental rental : expired) {
            rentalRepository.updateStatus(rental.getId(), completedStatus.getId());
            carRepository.findById(rental.getCarId()).ifPresent(car -> {
                car.setCarStatusId(freeStatus.getId());
                carRepository.update(car);
            });
        }
    }

    private void enrichAll(List<Rental> rentals) {
        if (rentals.isEmpty()) return;

        Map<Long, Client> clients = clientRepository.findAll().stream()
                .collect(Collectors.toMap(Client::getId, Function.identity()));
        Map<Long, Car> cars = carRepository.findAll().stream()
                .collect(Collectors.toMap(Car::getId, Function.identity()));
        Map<Long, RentalStatus> statuses = rentalStatusRepository.findAll().stream()
                .collect(Collectors.toMap(RentalStatus::getId, Function.identity()));

        for (Rental rental : rentals) {
            Client client = clients.get(rental.getClientId());
            if (client != null) {
                rental.setClientName(client.getName() + " " + client.getSurname());
            }

            Car car = cars.get(rental.getCarId());
            if (car != null) {
                rental.setCarName(car.getBrand() + " " + car.getModel());
                rental.setCarLicensePlate(car.getLicensePlate());
            }

            RentalStatus status = statuses.get(rental.getRentalStatusId());
            if (status != null) {
                rental.setStatusName(status.getStatus());
            }
        }
    }

    private void enrich(Rental rental) {
        clientRepository.findById(rental.getClientId()).ifPresent(c ->
                rental.setClientName(c.getName() + " " + c.getSurname()));
        carRepository.findById(rental.getCarId()).ifPresent(c -> {
            rental.setCarName(c.getBrand() + " " + c.getModel());
            rental.setCarLicensePlate(c.getLicensePlate());
        });
        rentalStatusRepository.findById(rental.getRentalStatusId()).ifPresent(s ->
                rental.setStatusName(s.getStatus()));
    }
}
