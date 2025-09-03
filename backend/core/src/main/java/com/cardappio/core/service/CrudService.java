package com.cardappio.core.service;

import com.cardappio.core.adapter.Adapter;
import com.cardappio.core.entity.EntityModel;
import com.cardappio.core.repository.CrudRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import static io.github.perplexhub.rsql.RSQLJPASupport.toSpecification;

public abstract class CrudService<T extends EntityModel<K>, V, K> {

    @Autowired
    protected CrudRepository<T, K> repository;

    public void setRepository(final CrudRepository<T, K> repository) {
        this.repository = repository;
    }


    public Page<V> findAll(final int pageSize) {

        return repository.findAll(Pageable.ofSize(pageSize))
                .map(entity -> getAdapter().toDTO(entity));
    }

    public Page<V> findAllRSQL(final String search, final int pageSize) {

        return repository.findAll(toSpecification(search), Pageable.ofSize(pageSize))
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
                .ifPresentOrElse(repository::delete,
                        () -> {
                            throw new EntityNotFoundException();
                        });
    }

    protected abstract Adapter<V, T> getAdapter();
}
