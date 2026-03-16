package com.degaltseva.carrental.service;

import com.degaltseva.carrental.model.CarCategory;
import com.degaltseva.carrental.repository.CarCategoryRepository;

import java.util.List;
import java.util.Optional;

public class CarCategoryService {

    private final CarCategoryRepository repository = new CarCategoryRepository();

    public List<CarCategory> findAll() {
        return repository.findAll();
    }

    public Optional<CarCategory> findById(Long id) {
        return repository.findById(id);
    }

    public CarCategory save(CarCategory category) {
        return repository.save(category);
    }

    public CarCategory update(CarCategory category) {
        return repository.update(category);
    }

    public void delete(Long id) {
        repository.delete(id);
    }
}
