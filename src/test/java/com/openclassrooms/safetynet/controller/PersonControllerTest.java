package com.openclassrooms.safetynet.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.openclassrooms.safetynet.exception.PersonAlreadyExistException;
import com.openclassrooms.safetynet.exception.PersonNotFoundException;
import com.openclassrooms.safetynet.model.Person;
import com.openclassrooms.safetynet.service.PersonService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = PersonController.class)
class PersonControllerTest {

    @Autowired
    MockMvc mockMvc;
    @Autowired
    ObjectMapper objectMapper;

    @MockBean
    PersonService personService;

    @Captor
    ArgumentCaptor<Person> personCaptor;

    @Captor
    ArgumentCaptor<String> firstNameCaptor;

    @Captor
    ArgumentCaptor<String> lastNameCaptor;

    private Person person;


    @BeforeEach
    public void init() {
        person = Person.builder()
                .firstName("firstname")
                .lastName("lastname")
                .address("person address")
                .city("city")
                .zip(59000)
                .phone("001-001-001")
                .email("person@main.com")
                .build();
    }


    @DisplayName("Should get all people")
    @Test
    void shouldGetPersons() throws Exception {

        when(this.personService.getAllPerson()).thenReturn(List.of(person));

        mockMvc.perform(get("/person/all"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].firstName", is("firstname")))
                .andExpect(jsonPath("$[0].lastName", is("lastname")));

        verify(this.personService, times(1)).getAllPerson();

    }


    @DisplayName("Should add a person")
    @Test
    void shouldAddPerson() throws Exception {

        mockMvc.perform(post("/person")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(person)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.firstName", is("firstname")))
                .andExpect(jsonPath("$.lastName", is("lastname")));

        verify(this.personService, times(1)).addPerson(personCaptor.capture());
        Person personCaptorValue = personCaptor.getValue();
        assertEquals("firstname", personCaptorValue.getFirstName());
        assertEquals("lastname", personCaptorValue.getLastName());

    }


    @DisplayName("Should not add a person -> person already exist")
    @Test
    void shouldNotAddPerson() throws Exception {

        when(this.personService.addPerson(any(Person.class))).thenThrow(PersonAlreadyExistException.class);

        mockMvc.perform(post("/person")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(person)))
                .andExpect(status().isConflict());

        verify(this.personService, times(1)).addPerson(personCaptor.capture());
        Person personCaptorValue = personCaptor.getValue();

        assertEquals("firstname", personCaptorValue.getFirstName());
        assertEquals("lastname", personCaptorValue.getLastName());

    }


    @DisplayName("Should update a person")
    @Test
    void shouldUpdatePerson() throws Exception {

        mockMvc.perform(put("/person")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(person)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName", is("firstname")))
                .andExpect((jsonPath("$.lastName", is("lastname"))));

        verify(this.personService, times(1)).updatePerson(personCaptor.capture());
        Person personCaptorValue = personCaptor.getValue();
        assertEquals("firstname", personCaptorValue.getFirstName());
        assertEquals("lastname", personCaptorValue.getLastName());

    }


    @DisplayName("Should not update a person -> person not found")
    @Test
    void shouldNotUpdatePerson() throws Exception {

        when(this.personService.updatePerson(any(Person.class))).thenThrow(PersonNotFoundException.class);

        mockMvc.perform(put("/person")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(person)))
                .andExpect(status().isNotFound());

        verify(this.personService, times(1)).updatePerson(any(Person.class));

    }


    @DisplayName("Should not update a person -> server error")
    @Test
    void shouldNotUpdatePersonServerError() throws Exception {

        when(this.personService.updatePerson(any(Person.class))).thenThrow(RuntimeException.class);

        mockMvc.perform(put("/person")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(person)))
                .andExpect(status().is5xxServerError());

        verify(this.personService, times(1)).updatePerson(any(Person.class));

    }


    @DisplayName("Should delete a person")
    @Test
    void shouldDeletePerson() throws Exception {

        doNothing().when((this.personService)).deletePerson(anyString(), anyString());

        mockMvc.perform(delete("/person")
                        .param("firstName", "firstname")
                        .param("lastName", "lastname"))
                .andExpect(status().isNoContent());

        verify(this.personService, times(1)).deletePerson(firstNameCaptor.capture(), lastNameCaptor.capture());
        String firstNameCaptorValue = firstNameCaptor.getValue();
        String lastNameCaptorValue = lastNameCaptor.getValue();
        assertEquals("firstname", firstNameCaptorValue);
        assertEquals("lastname", lastNameCaptorValue);

    }


    @DisplayName("Should not delete a person -> person not found")
    @Test
    void shouldNotDeletePerson() throws Exception {

        doThrow(PersonNotFoundException.class).when((this.personService)).deletePerson(anyString(), anyString());

        mockMvc.perform(delete("/person")
                        .param("firstName", "firstname")
                        .param("lastName", "lastname"))
                .andExpect(status().isNotFound());

        verify(this.personService, times(1)).deletePerson(anyString(), anyString());

    }


}