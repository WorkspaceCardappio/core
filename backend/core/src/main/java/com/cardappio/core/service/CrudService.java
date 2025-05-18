package com.cardappio.core.service;

import com.cardappio.core.adapter.Adapter;
import com.cardappio.core.entity.Entity;
import com.cardappio.core.repository.CrudRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public abstract class CrudService<T extends Entity<K>, V, K> {

    protected CrudRepository<T, K> repository;

    public CrudService(CrudRepository<T, K> repository) {
        this.repository = repository;
    }

    public Page<V> findAll(int pageSize) {
        return repository.findAll(Pageable.ofSize(pageSize))
                .map(entity -> getAdapter().toDTO(entity));
    }

    public V findById(final K id) {
        return repository.findById(id)
                .map(entity -> getAdapter().toDTO(entity))
                .orElseThrow(EntityNotFoundException::new);
    }

    public K create(final V dto) {
        return repository.save(getAdapter().toEntity(dto)).getId();
    }

    public void update(final K id, final V newEntity) {
        repository.findById(id)
                .map(client -> repository.save(getAdapter().toEntity(newEntity)))
                .orElseThrow(EntityNotFoundException::new);
    }

    public void delete(final K id) {
        repository.findById(id)
                .ifPresentOrElse(entity -> repository.delete(entity),
                        () -> { throw new EntityNotFoundException(); });
    }

    protected abstract Adapter<V, T> getAdapter();
}
