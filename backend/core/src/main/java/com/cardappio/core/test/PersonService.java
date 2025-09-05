package com.cardappio.core.test;

import com.cardappio.core.adapter.Adapter;
import com.cardappio.core.service.CrudService;
import org.springframework.stereotype.Service;

@Service
class PersonService extends CrudService<Person, Long, PersonDTO, CreateDTO> {

    @Override
    protected Adapter<Person, PersonDTO, CreateDTO> getAdapter() {
        return new PersonAdapter();
    }

    private static final class PersonAdapter implements Adapter<Person, PersonDTO, CreateDTO> {

        @Override
        public PersonDTO toDTO(Person entity) {
            return new PersonDTO(entity.getId(), entity.getName());
        }

        @Override
        public Person toEntity(CreateDTO createDTO) {
            return new Person(createDTO.id(), createDTO.name());
        }
    }
}
