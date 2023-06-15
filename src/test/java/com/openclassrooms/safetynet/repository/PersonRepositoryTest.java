package com.openclassrooms.safetynet.repository;

import com.openclassrooms.safetynet.model.Person;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class PersonRepositoryTest {

    private PersonRepository personRepository = new PersonRepository();
    private Person person1;
    private Person person2;
    private List<Person> personList = new ArrayList<>();


    @BeforeEach
    public void init() {

        person1 = Person.builder()
                .firstName("firstname1")
                .lastName("lastname1")
                .address("person address 1")
                .city("city")
                .zip(59000)
                .phone("001-001-001")
                .email("person1@mail.com")
                .build();

        person2 = Person.builder()
                .firstName("firstname2")
                .lastName("lastname2")
                .address("person address 2")
                .city("city")
                .zip(59100)
                .phone("001-001-002")
                .email("person2@mail.com")
                .build();

        Collections.addAll(personList, person1, person2);

        this.personRepository.setPersons(personList);
    }


    @DisplayName("Should get all people")
    @Test
    void shouldGetPersons() {

        List<Person> result = this.personRepository.getPersons();

        assertNotNull(result);

        assertEquals(2, result.size());
        assertEquals("firstname1", result.get(0).getFirstName());
        assertEquals("lastname1", result.get(0).getLastName());
        assertEquals("person address 1", result.get(0).getAddress());
        assertEquals("city", result.get(0).getCity());
        assertEquals(59000, result.get(0).getZip());
        assertEquals("001-001-001", result.get(0).getPhone());
        assertEquals("person1@mail.com", result.get(0).getEmail());

        assertEquals("firstname2", result.get(1).getFirstName());
        assertEquals("lastname2", result.get(1).getLastName());
        assertEquals("person address 2", result.get(1).getAddress());
        assertEquals("city", result.get(1).getCity());
        assertEquals(59100, result.get(1).getZip());
        assertEquals("001-001-002", result.get(1).getPhone());
        assertEquals("person2@mail.com", result.get(1).getEmail());

    }


    @DisplayName("Should find a person by first and last name")
    @Test
    void shouldFindPersonByFirstNameAndLastName() {

        Person result = this.personRepository.findPersonByFirstNameAndLastName("firstname1", "lastname1");

        assertNotNull(result);

        assertEquals("firstname1", result.getFirstName());
        assertEquals("lastname1", result.getLastName());
        assertEquals("person address 1", result.getAddress());
        assertEquals("city", result.getCity());
        assertEquals(59000, result.getZip());
        assertEquals("001-001-001", result.getPhone());
        assertEquals("person1@mail.com", result.getEmail());

    }


    @DisplayName("Should find people by address")
    @Test
    void ShouldFindPersonsByAddress() {

        List<Person> result = this.personRepository.findPersonsByAddress("person address 1");

        assertNotNull(result);

        assertEquals(1, result.size());
        assertEquals("firstname1", result.get(0).getFirstName());
        assertEquals("lastname1", result.get(0).getLastName());
        assertEquals("person address 1", result.get(0).getAddress());
        assertEquals("city", result.get(0).getCity());
        assertEquals(59000, result.get(0).getZip());
        assertEquals("001-001-001", result.get(0).getPhone());
        assertEquals("person1@mail.com", result.get(0).getEmail());

    }


    @DisplayName("Should add a person")
    @Test
    void shouldAddPerson() {

        Person personToAdd = Person.builder()
                .firstName("firstname3")
                .lastName("lastname3")
                .address("person address 3")
                .city("city")
                .zip(59300)
                .phone("001-001-003")
                .email("person3@mail.com")
                .build();

        Person result = this.personRepository.addPerson(personToAdd);

        assertNotNull(result);
        assertEquals(3, this.personList.size());

        assertEquals("firstname3", result.getFirstName());
        assertEquals("lastname3", result.getLastName());
        assertEquals("person address 3", result.getAddress());
        assertEquals("city", result.getCity());
        assertEquals(59300, result.getZip());
        assertEquals("001-001-003", result.getPhone());
        assertEquals("person3@mail.com", result.getEmail());

        assertEquals("firstname3", this.personList.get(2).getFirstName());
        assertEquals("lastname3", this.personList.get(2).getLastName());
        assertEquals("person address 3", this.personList.get(2).getAddress());
        assertEquals("city", this.personList.get(2).getCity());
        assertEquals(59300, this.personList.get(2).getZip());
        assertEquals("001-001-003", this.personList.get(2).getPhone());
        assertEquals("person3@mail.com", this.personList.get(2).getEmail());

    }


    @DisplayName("Should delete a person")
    @Test
    void shouldDeletePerson() {

        this.personRepository.deletePerson(person1);

        assertEquals(1, this.personList.size());
        assertFalse(this.personList.contains(person1));

    }


    @DisplayName("Should update a person")
    @Test
    void shouldUpdatePerson() {

        Person personToUpdate = Person.builder()
                .firstName("firstname1")
                .lastName("lastname1")
                .address("person address 1")
                .city("city")
                .zip(59000)
                .phone("111-111-111")
                .email("person1@mail.com")
                .build();

        Person result = this.personRepository.updatePerson(personToUpdate);

        assertNotNull(result);

        assertEquals("111-111-111", result.getPhone());
        assertEquals("111-111-111", this.personList.get(0).getPhone());

    }

}