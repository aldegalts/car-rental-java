package com.degaltseva.carrental.service;

import com.degaltseva.carrental.model.CarStatus;
import com.degaltseva.carrental.repository.CarStatusRepository;

import java.util.List;
import java.util.Optional;

public class CarStatusService {

    private final CarStatusRepository repository = new CarStatusRepository();

    public List<CarStatus> findAll() {
        return repository.findAll();
    }

    public Optional<CarStatus> findById(Long id) {
        return repository.findById(id);
    }

    public CarStatus save(CarStatus status) {
        return repository.save(status);
    }

    public CarStatus update(CarStatus status) {
        return repository.update(status);
    }

    public void delete(Long id) {
        repository.delete(id);
    }
}
