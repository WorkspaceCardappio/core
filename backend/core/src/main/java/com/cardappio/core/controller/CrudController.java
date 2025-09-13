package com.cardappio.core.controller;

import com.cardappio.core.entity.EntityModel;
import com.cardappio.core.service.CrudService;
import jakarta.validation.Valid;
import org.apache.logging.log4j.util.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

public abstract class CrudController<Entity extends EntityModel<ID>, ID, ListDTO, CreateDTO> {

    @Autowired
    protected CrudService<Entity, ID, ListDTO, CreateDTO> service;

    public void setService(final CrudService<Entity, ID, ListDTO, CreateDTO> service) {
        this.service = service;
    }

    @RequestMapping(
            method = RequestMethod.GET
    )
    protected ResponseEntity<Page<Entity>> findAll(
            @RequestParam(value = "search", defaultValue = Strings.EMPTY) final String search,
            Pageable pageable
    ) {

        if (search.isBlank()) {

            return ResponseEntity.ok(service.findAll(pageable));
        }

        return ResponseEntity.ok(service.findAllRSQL(search, pageable));
    }

    @RequestMapping(
            method = RequestMethod.GET,
            value = "/{id}"
    )
    protected ResponseEntity<ListDTO> findById(@PathVariable final ID id) {
        return ResponseEntity.ok(service.findById(id));
    }

    @RequestMapping(
            method = RequestMethod.GET,
            value = "/dto"
    )
    protected ResponseEntity<Page<ListDTO>> findAllDTO(
            @RequestParam(value = "search", defaultValue = Strings.EMPTY) final String search,
            Pageable pageable) {

        if (search.isBlank()) {

            return ResponseEntity.ok(service.findAllDTO(pageable));
        }

        return ResponseEntity.ok(service.findAllRSQLDTO(search, pageable));
    }

    @RequestMapping(
            method = RequestMethod.POST
    )
    protected ResponseEntity<Void> create(@RequestBody @Valid final CreateDTO newDTO) {

        final ID idSaved = service.create(newDTO);

        final URI uri = ServletUriComponentsBuilder
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
    protected ResponseEntity<Void> update(@PathVariable final ID id, @RequestBody @Valid final CreateDTO updatedDTO) {

        service.update(id, updatedDTO);

        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @RequestMapping(
            method = RequestMethod.DELETE,
            value = "/{id}"
    )
    protected ResponseEntity<Void> delete(@PathVariable final ID id) {

        service.delete(id);

        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

}
