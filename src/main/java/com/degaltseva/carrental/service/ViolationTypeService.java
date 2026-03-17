package com.degaltseva.carrental.service;

import com.degaltseva.carrental.model.ViolationType;
import com.degaltseva.carrental.repository.ViolationTypeRepository;

import java.util.List;
import java.util.Optional;

public class ViolationTypeService {

    private final ViolationTypeRepository repository = new ViolationTypeRepository();

    public List<ViolationType> findAll() {
        return repository.findAll();
    }

    public Optional<ViolationType> findById(Long id) {
        return repository.findById(id);
    }

    public ViolationType save(ViolationType violationType) {
        return repository.save(violationType);
    }

    public ViolationType update(ViolationType violationType) {
        return repository.update(violationType);
    }

    public void delete(Long id) {
        repository.delete(id);
    }
}
