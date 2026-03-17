package com.degaltseva.carrental.repository;

import java.util.List;
import java.util.Optional;

public interface BaseRepository<T> {

    List<T> findAll();

    Optional<T> findById(Long id);

    T save(T entity);

    T update(T entity);

    void delete(Long id);
}
