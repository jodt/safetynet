package com.openclassrooms.safetynet.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.openclassrooms.safetynet.dto.PersonWithAddressAndPhoneDTO;
import com.openclassrooms.safetynet.dto.PersonsConcernedByFireStationDTO;
import com.openclassrooms.safetynet.exception.FireStationNotFoundException;
import com.openclassrooms.safetynet.exception.MailsNotFoundException;
import com.openclassrooms.safetynet.exception.PersonNotFoundException;
import com.openclassrooms.safetynet.model.Person;
import com.openclassrooms.safetynet.repository.PersonRepository;
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
import java.util.concurrent.atomic.AtomicInteger;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.*;
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
    private PersonService personService;

    @MockBean
    private PersonRepository personRepository;

    @Captor
    ArgumentCaptor<Person> personCaptor;

    @Captor
    ArgumentCaptor<String> firstNameCaptor;

    @Captor
    ArgumentCaptor<String> lastNameCaptor;

    @Captor
    ArgumentCaptor<String> cityCaptor;

    @Captor
    ArgumentCaptor<Integer> stationNumberCaptor;


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


    @DisplayName("Should get a list of email")
    @Test
    void shouldGetCommunityEmail() throws Exception {

        List<String> mailList = List.of("firstmail@mail.com", "secondmail@mail.com");

        when(this.personService.getMailsByCity(anyString())).thenReturn(mailList);

        mockMvc.perform(get("/communityEmail")
                        .param("city", "city"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0]", is("firstmail@mail.com")))
                .andExpect(jsonPath("$[1]", is("secondmail@mail.com")));

        verify(this.personService, times(1)).getMailsByCity(cityCaptor.capture());
        String cityCaptorValue = cityCaptor.getValue();
        assertEquals("city", cityCaptorValue);

    }


    @DisplayName("Should not get a list of email -> EmailNotFoundException")
    @Test
    void shouldNotGetCommunityEmail() throws Exception {

        when(this.personService.getMailsByCity(anyString())).thenThrow(MailsNotFoundException.class);

        mockMvc.perform(get("/communityEmail")
                        .param("city", "city"))
                .andExpect(status().isNotFound());

        verify(this.personService, times(1)).getMailsByCity(anyString());

    }


    @DisplayName("Should get list of person concerned by a fire station with adult and children number")
    @Test
    void shouldGetPeopleConcernedByFiresStation() throws Exception {

        List<PersonWithAddressAndPhoneDTO> personList = List.of(
                PersonWithAddressAndPhoneDTO.builder()
                        .firstName("firstname1")
                        .lastName("lastName1")
                        .address("person address 1")
                        .phone("001-001-001")
                        .age(23)
                        .build(),
                PersonWithAddressAndPhoneDTO.builder()
                        .firstName("firstname2")
                        .lastName("lastName2")
                        .address("person address 2")
                        .phone("001-001-002")
                        .age(5)
                        .build()
        );

        AtomicInteger adults = new AtomicInteger(1);
        AtomicInteger children = new AtomicInteger(1);

        PersonsConcernedByFireStationDTO personsConcernedByFireStationDTO = PersonsConcernedByFireStationDTO.builder()
                .personWithAddressAndPhoneDTOList(personList)
                .adults(adults)
                .children(children)
                .build();

        when(this.personService.findPeopleConcernedByFireStation(anyInt())).thenReturn(personsConcernedByFireStationDTO);

        mockMvc.perform(get("/firestation")
                        .param("stationNumber", "1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.personWithAddressAndPhoneDTOList", hasSize(2)))
                .andExpect(jsonPath("$.personWithAddressAndPhoneDTOList[0].firstName", is("firstname1")))
                .andExpect(jsonPath("$.personWithAddressAndPhoneDTOList[1].firstName", is("firstname2")))
                .andExpect(jsonPath("$.adults", is(1)))
                .andExpect(jsonPath("$.children", is(1)));

        verify(this.personService, times(1)).findPeopleConcernedByFireStation(stationNumberCaptor.capture());
        int stationNumberCaptorValue = stationNumberCaptor.getValue();
        assertEquals(1, stationNumberCaptorValue);

    }


    @DisplayName("Should get list of person concerned by a fire station -> FireStationNotFoundException")
    @Test
    void shouldNotGetPeopleConcernedByFiresStation() throws Exception {

        when(this.personService.findPeopleConcernedByFireStation(anyInt())).thenThrow(FireStationNotFoundException.class);

        mockMvc.perform(get("/firestation")
                        .param("stationNumber", "1"))
                .andExpect(status().isNotFound());

        verify(this.personService, times(1)).findPeopleConcernedByFireStation(anyInt());

    }

}