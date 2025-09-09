package com.cardappio.core.test;

import com.cardappio.core.controller.CrudController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/people")
class PersonController extends CrudController<Person, Long, PersonDTO, CreateDTO> {
}
