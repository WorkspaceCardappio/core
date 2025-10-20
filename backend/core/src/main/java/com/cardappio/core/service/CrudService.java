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


    public Page<Entity> findAll(final Pageable pageable) {

        return repository.findAll(pageable);
    }

    public Page<Entity> findAllRSQL(final String search, final Pageable pageable) {

        return repository.findAll(toSpecification(search), pageable);
    }

    public Page<ListDTO> findAllRSQLDTO(final String search, final Pageable pageable) {

        return findAllRSQL(search, pageable)
                .map(page -> getAdapter().toDTO(page));
    }

    public ListDTO findById(final ID id) {

        return repository.findById(id)
                .map(entity -> getAdapter().toDTO(entity))
                .orElseThrow(EntityNotFoundException::new);
    }


    public Page<ListDTO> findAllDTO(final Pageable pageable) {

        return repository.findAll(pageable)
                .map(entity -> getAdapter().toDTO(entity));
    }

    public ID create(final CreateDTO dto) {

        Entity entity = getAdapter().toEntity(dto);

        this.beforeSave(entity);

        return repository.save(entity).getId();
    }

    public void update(final ID id, final CreateDTO newEntity) {

        Entity entity = getAdapter().toEntity(newEntity);

        this.beforeEdit(entity);

        repository.findById(id)
                .map(client -> repository.save(entity))
                .orElseThrow(EntityNotFoundException::new);
    }

    public void delete(final ID id) {

        repository.findById(id)
                .ifPresentOrElse(entity -> {

                            this.beforeDelete(entity);

                            repository.delete(entity);
                        },
                        () -> {
                            throw new EntityNotFoundException();
                        });
    }

    protected abstract Adapter<Entity, ListDTO, CreateDTO> getAdapter();

    protected void beforeSave(Entity entity) {}

    protected void beforeEdit(Entity entity) {}

    protected void beforeDelete(Entity entity) {}
}
