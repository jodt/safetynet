package com.openclassrooms.safetynet.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.openclassrooms.safetynet.model.Person;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class PersonControllerIT {

    @Autowired
    MockMvc mockMvc;
    @Autowired
    ObjectMapper objectMapper;

    private Person person;

    @BeforeEach
    public void init(){
        person = Person.builder()
                .firstName("firstname1")
                .lastName("lastname1")
                .address("person address 1")
                .city("city")
                .zip(59000)
                .phone("001-001-001")
                .email("person1@mail.com")
                .build();
    }

    @DisplayName("Should get all people")
    @Test
    void shouldGetPersons() throws Exception {

        mockMvc.perform(get("/person/all"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(23)));

    }

    @DisplayName("Should add a person")
    @Test
    void shouldAddPerson() throws Exception {

        mockMvc.perform(post("/person")
                        .content(objectMapper.writeValueAsString(person))
                        .contentType("application/json"))
                .andExpect(status().isCreated()).andReturn();

    }

    @DisplayName("Should update a person")
    @Test
    void shouldUpdatePerson() throws Exception {

        Person updatedPerson = person = Person.builder()
                .firstName("firstname1")
                .lastName("lastname1")
                .address("person address updated")
                .city("city")
                .zip(59000)
                .phone("001-001-001")
                .email("person1@mail.com")
                .build();

        mockMvc.perform(put("/person")
                        .content(objectMapper.writeValueAsString(updatedPerson))
                        .contentType("application/json"))
                .andExpect(status().isOk());

    }

    @DisplayName("Should delete a person")
    @Test
    void shouldDeletePerson() throws Exception {

        mockMvc.perform(delete("/person")
                        .param("firstName",person.getFirstName())
                        .param("lastName", person.getLastName()))
                .andExpect(status().isNoContent());

    }


}
