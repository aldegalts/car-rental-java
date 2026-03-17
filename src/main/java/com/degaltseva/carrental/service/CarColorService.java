package com.degaltseva.carrental.service;

import com.degaltseva.carrental.model.CarColor;
import com.degaltseva.carrental.repository.CarColorRepository;

import java.util.List;
import java.util.Optional;

public class CarColorService {

    private final CarColorRepository repository = new CarColorRepository();

    public List<CarColor> findAll() {
        return repository.findAll();
    }

    public Optional<CarColor> findById(Long id) {
        return repository.findById(id);
    }

    public CarColor save(CarColor color) {
        return repository.save(color);
    }

    public CarColor update(CarColor color) {
        return repository.update(color);
    }

    public void delete(Long id) {
        repository.delete(id);
    }
}
