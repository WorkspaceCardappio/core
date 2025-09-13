package com.cardappio.core.test;

import jakarta.persistence.EntityNotFoundException;
import org.assertj.core.groups.Tuple;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PersonServiceTest {

    @InjectMocks
    private PersonService service;

    @Mock
    private PersonRepository repository;

    @BeforeEach
    public void setup() {

        service.setRepository(repository);
    }

    @Test
    void findAll() {

        List<Person> people = List.of(
                new Person(1L, "Ricardo"),
                new Person(2L, "Jean")
        );

        final Pageable pageable = Pageable.ofSize(20);
        when(repository.findAll(pageable))
                .thenReturn(new PageImpl<>(people, PageRequest.of(0, 20), people.size()));

        Page<Person> result = service.findAll(pageable);

        assertThat(result).isNotNull();
        assertThat(result.getContent()).hasSize(2);
        assertThat(result.getTotalElements()).isEqualTo(2);
        assertThat(result.getTotalPages()).isEqualTo(1);
        assertThat(result)
                .extracting(Person::getId, Person::getName)
                .containsExactlyInAnyOrder(
                        Tuple.tuple(1L, "Ricardo"),
                        Tuple.tuple(2L, "Jean"));

        verify(repository, times(1)).findAll(pageable);
        verifyNoMoreInteractions(repository);
    }

    @Test
    void findAllDTO() {

        List<Person> people = List.of(
                new Person(1L, "Ricardo"),
                new Person(2L, "Jean")
        );

        final Pageable pageable = Pageable.ofSize(20);
        when(repository.findAll(pageable))
                .thenReturn(new PageImpl<>(people, PageRequest.of(0, 20), people.size()));

        Page<PersonDTO> result = service.findAllDTO(pageable);

        assertThat(result.getContent().getFirst().id()).isEqualTo(1L);
        verify(repository, times(1)).findAll(pageable);
        verifyNoMoreInteractions(repository);
    }

    @Test
    void findAllRSQL() {

        List<Person> people = List.of(
                new Person(1L, "Ricardo"),
                new Person(2L, "Jean")
        );

        final Pageable pageable = Pageable.ofSize(20);
        when(repository.findAll(any(Specification.class), eq(pageable)))
                .thenReturn(new PageImpl<>(people, PageRequest.of(0, 20), people.size()));

        Page<Person> result = service.findAllRSQL("id=bt=(1,2)", pageable);

        assertThat(result).isNotNull();
        assertThat(result.getContent()).hasSize(2);
        assertThat(result.getTotalElements()).isEqualTo(2);
        assertThat(result.getTotalPages()).isEqualTo(1);
        assertThat(result)
                .extracting(Person::getId, Person::getName)
                .containsExactlyInAnyOrder(
                        Tuple.tuple(1L, "Ricardo"),
                        Tuple.tuple(2L, "Jean"));

        verify(repository, times(1)).findAll(any(Specification.class), eq(pageable));
        verifyNoMoreInteractions(repository);
    }

    @Test
    void findAllRSQLDTO() {

        List<Person> people = List.of(
                new Person(1L, "Ricardo"),
                new Person(2L, "Jean")
        );

        final Pageable pageable = Pageable.ofSize(20);
        when(repository.findAll(any(Specification.class), eq(pageable)))
                .thenReturn(new PageImpl<>(people, PageRequest.of(0, 20), people.size()));

        final Page<PersonDTO> result = service.findAllRSQLDTO("id=bt=(1,2)", pageable);

        assertThat(result.getContent().getFirst().id()).isEqualTo(1L);
        verify(repository, times(1)).findAll(any(Specification.class), eq(pageable));
        verifyNoMoreInteractions(repository);
    }

    @Test
    void findById() {

        when(repository.findById(1L)).thenReturn(Optional.of(new Person(1L, "Ricardo")));

        PersonDTO result = service.findById(1L);

        assertThat(result).isNotNull();
        assertThat(result.id()).isEqualTo(1L);
        assertThat(result.name()).isEqualTo("Ricardo");

        verify(repository, times(1)).findById(1L);
        verifyNoMoreInteractions(repository);
    }

    @Test
    void findByIdThrowingException() {

        when(repository.findById(1L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.findById(1L))
                .isInstanceOf(EntityNotFoundException.class);

        verify(repository, times(1)).findById(1L);
        verifyNoMoreInteractions(repository);
    }

    @Test
    void create() {

        final CreateDTO person = new CreateDTO(1L, "Jean");
        final Person personToSave = new Person(1L, "Jean");
        final ArgumentCaptor<Person> captor = ArgumentCaptor.forClass(Person.class);
        when(repository.save(personToSave)).thenReturn(personToSave);

        service.create(person);

        verify(repository, times(1)).save(captor.capture());
        final Person newPerson = captor.getValue();

        assertThat(newPerson).isInstanceOf(Person.class);
        assertThat(newPerson.getId()).isEqualTo(1L);
        assertThat(newPerson.getName()).isEqualTo("Jean");
    }

    @Test
    void update() {

        final CreateDTO person = new CreateDTO(1L, "Jean");
        final Person personToSave = new Person(1L, "Jean");
        final ArgumentCaptor<Person> captor = ArgumentCaptor.forClass(Person.class);
        when(repository.findById(1L)).thenReturn(Optional.of(personToSave));
        when(repository.save(personToSave)).thenReturn(personToSave);

        service.update(1L, person);

        verify(repository, times(1)).save(captor.capture());
        final Person newPerson = captor.getValue();

        assertThat(newPerson).isInstanceOf(Person.class);
        assertThat(newPerson.getId()).isEqualTo(1L);
        assertThat(newPerson.getName()).isEqualTo("Jean");

    }

    @Test
    void updateThrowException() {

        final CreateDTO person = new CreateDTO(1L, "Jean");
        final Person personToSave = new Person(1L, "Jean");
        when(repository.findById(1L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.update(1L, person))
                .isInstanceOf(EntityNotFoundException.class);

        verify(repository, never()).save(personToSave);
    }

    @Test
    void delete() {

        final Person person = new Person(1L, "Jean");
        when(repository.findById(1L)).thenReturn(Optional.of(person));

        service.delete(1L);

        verify(repository, times(1)).delete(person);

    }

    @Test
    void deleteThrowException() {

        final Person person = new Person(1L, "Jean");
        when(repository.findById(1L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.delete(1L))
                .isInstanceOf(EntityNotFoundException.class);

        verify(repository, never()).delete(person);
    }
}
