package com.cardappio.core.test;

import com.cardappio.core.adapter.Adapter;
import com.cardappio.core.service.CrudService;
import org.springframework.stereotype.Service;

@Service
class PersonService extends CrudService<Person, PersonDTO, Long> {

    @Override
    protected Adapter<PersonDTO, Person> getAdapter() {
        return new PersonAdapter();
    }

    private static final class PersonAdapter implements Adapter<PersonDTO, Person> {

        @Override
        public Person toEntity(PersonDTO dto) {
            return new Person(dto.id(), dto.name());
        }

        @Override
        public PersonDTO toDTO(Person entity) {
            return new PersonDTO(entity.getId(), entity.getName());
        }
    }
}
