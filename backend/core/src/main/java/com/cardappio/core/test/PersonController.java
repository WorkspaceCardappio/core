package com.cardappio.core.test;

import com.cardappio.core.controller.CrudController;
import com.cardappio.core.entity.Entity;
import com.cardappio.core.service.CrudService;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/people")
class PersonController extends CrudController<Person, PersonDTO, Long> {

    PersonController(CrudService<Person, PersonDTO, Long> service) {
        super(service);
    }
}
