package com.degaltseva.carrental.service;

import com.degaltseva.carrental.model.Violation;
import com.degaltseva.carrental.model.ViolationType;
import com.degaltseva.carrental.repository.ViolationRepository;
import com.degaltseva.carrental.repository.ViolationTypeRepository;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

public class ViolationService {

    private final ViolationRepository violationRepository = new ViolationRepository();
    private final ViolationTypeRepository violationTypeRepository = new ViolationTypeRepository();

    public List<Violation> findAll() {
        List<Violation> violations = violationRepository.findAll();
        enrichAll(violations);
        return violations;
    }

    public Optional<Violation> findById(Long id) {
        Optional<Violation> violation = violationRepository.findById(id);
        violation.ifPresent(this::enrich);
        return violation;
    }

    public List<Violation> findByRentalId(Long rentalId) {
        List<Violation> violations = violationRepository.findByRentalId(rentalId);
        enrichAll(violations);
        return violations;
    }

    public List<Violation> findByUserId(Long userId) {
        List<Violation> violations = violationRepository.findByUserId(userId);
        enrichAll(violations);
        return violations;
    }

    public Optional<Violation> findByUserIdAndViolationId(Long userId, Long violationId) {
        Optional<Violation> violation = violationRepository.findByUserIdAndViolationId(userId, violationId);
        violation.ifPresent(this::enrich);
        return violation;
    }

    public Violation save(Violation violation) {
        return violationRepository.save(violation);
    }

    public Violation update(Violation violation) {
        return violationRepository.update(violation);
    }

    public void delete(Long id) {
        violationRepository.delete(id);
    }

    private void enrichAll(List<Violation> violations) {
        if (violations.isEmpty()) return;

        Map<Long, ViolationType> types = violationTypeRepository.findAll().stream()
                .collect(Collectors.toMap(ViolationType::getId, Function.identity()));

        for (Violation v : violations) {
            ViolationType type = types.get(v.getViolationTypeId());
            if (type != null) {
                v.setViolationTypeName(type.getTypeName());
            }
        }
    }

    private void enrich(Violation v) {
        violationTypeRepository.findById(v.getViolationTypeId())
                .ifPresent(t -> v.setViolationTypeName(t.getTypeName()));
    }
}
