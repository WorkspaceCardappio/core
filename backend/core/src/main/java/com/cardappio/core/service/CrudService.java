package com.cardappio.core.service;

import com.cardappio.core.adapter.Adapter;
import com.cardappio.core.entity.EntityModel;
import com.cardappio.core.repository.CrudRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import static io.github.perplexhub.rsql.RSQLJPASupport.toSpecification;

public abstract class CrudService<Entity extends EntityModel<ID>, ID, ListDTO, CreateDTO> {

    @Autowired
    protected CrudRepository<Entity, ID> repository;

    public void setRepository(final CrudRepository<Entity, ID> repository) {
        this.repository = repository;
    }


    public Page<Entity> findAll(final int pageSize) {

        return repository.findAll(Pageable.ofSize(pageSize));
    }

    public Page<Entity> findAllRSQL(final String search, final int pageSize) {

        return repository.findAll(toSpecification(search), Pageable.ofSize(pageSize));
    }

    public Page<ListDTO> findAllRSQLDTO(final String search, final int pageSize) {

        return findAllRSQL(search, pageSize)
                .map(page -> getAdapter().toDTO(page));
    }

    public ListDTO findById(final ID id) {

        return repository.findById(id)
                .map(entity -> getAdapter().toDTO(entity))
                .orElseThrow(EntityNotFoundException::new);
    }


    public Page<ListDTO> findAllDTO(final int pageSize) {

        return repository.findAll(Pageable.ofSize(pageSize))
                .map(entity -> getAdapter().toDTO(entity));
    }

    public ID create(final CreateDTO dto) {
        return repository.save(getAdapter().toEntity(dto)).getId();
    }

    public void update(final ID id, final CreateDTO newEntity) {

        repository.findById(id)
                .map(client -> repository.save(getAdapter().toEntity(newEntity)))
                .orElseThrow(EntityNotFoundException::new);
    }

    public void delete(final ID id) {

        repository.findById(id)
                .ifPresentOrElse(repository::delete,
                        () -> {
                            throw new EntityNotFoundException();
                        });
    }

    protected abstract Adapter<Entity, ListDTO, CreateDTO> getAdapter();
}
