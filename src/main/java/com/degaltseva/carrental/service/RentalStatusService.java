package com.degaltseva.carrental.service;

import com.degaltseva.carrental.model.RentalStatus;
import com.degaltseva.carrental.repository.RentalStatusRepository;

import java.util.List;
import java.util.Optional;

public class RentalStatusService {

    private final RentalStatusRepository repository = new RentalStatusRepository();

    public List<RentalStatus> findAll() {
        return repository.findAll();
    }

    public Optional<RentalStatus> findById(Long id) {
        return repository.findById(id);
    }

    public RentalStatus save(RentalStatus status) {
        return repository.save(status);
    }

    public RentalStatus update(RentalStatus status) {
        return repository.update(status);
    }

    public void delete(Long id) {
        repository.delete(id);
    }
}
