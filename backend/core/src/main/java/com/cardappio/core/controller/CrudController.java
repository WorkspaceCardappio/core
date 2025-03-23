package com.cardappio.core.controller;

import com.cardappio.core.entity.Entity;
import com.cardappio.core.service.CrudService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

public class CrudController<T extends Entity<K>, V, K> {

    protected final CrudService<T, V, K> service;

    public CrudController(CrudService<T, V, K> service) {
        this.service = service;
    }

    @RequestMapping(
            method = RequestMethod.GET
    )
    protected ResponseEntity<Page<V>> findAll() {
        return ResponseEntity.ok(service.findAll());
    }

    @RequestMapping(
            method = RequestMethod.GET,
            value = "/{id}"
    )
    protected ResponseEntity<V> findById(@PathVariable final K id) {
        return ResponseEntity.ok(service.findById(id));
    }

    @RequestMapping(
            method = RequestMethod.POST
    )
    protected ResponseEntity<Void> create(@RequestBody @Valid final V newDTO) {

        K idSaved = service.create(newDTO);

        URI uri = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(idSaved)
                .toUri();

        return ResponseEntity.created(uri).build();
    }

    @RequestMapping(
            method = RequestMethod.PUT,
            value = "/{id}"
    )
    protected ResponseEntity<Void> update(@PathVariable final K id, @RequestBody @Valid final V updatedDTO) {

        service.update(id, updatedDTO);

        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @RequestMapping(
            method = RequestMethod.DELETE,
            value = "/{id}"
    )
    protected ResponseEntity<Void> delete(@PathVariable final K id) {

        service.delete(id);

        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

}
