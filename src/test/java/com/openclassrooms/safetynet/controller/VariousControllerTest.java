package com.openclassrooms.safetynet.controller;

import com.openclassrooms.safetynet.dto.FireDTO;
import com.openclassrooms.safetynet.dto.PersonInfoDTO;
import com.openclassrooms.safetynet.dto.PersonWithMedicalRecordDTO;
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

import java.util.HashMap;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = VariousController.class)
class VariousControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    private PersonService personService;

    @Captor
    ArgumentCaptor<String> addressCaptor;

    @Captor
    ArgumentCaptor<List<Integer>> fireStationNumbersCaptor;

    @Captor
    ArgumentCaptor<String> firstNameCaptor;

    @Captor
    ArgumentCaptor<String> lastNameCaptor;

    PersonWithMedicalRecordDTO firstPersonDTO;

    PersonWithMedicalRecordDTO secondPersonDTO;

    @BeforeEach
    public void init() {
        firstPersonDTO = PersonWithMedicalRecordDTO.builder()
                .lastName("lastname1")
                .phone("001-001-001")
                .age(23)
                .medications(List.of("hydrapermazol:300mg"))
                .allergies(List.of("peanut"))
                .build();

        secondPersonDTO = PersonWithMedicalRecordDTO.builder()
                .lastName("lastname2")
                .phone("001-001-002")
                .age(20)
                .medications(List.of("tramamazol:300mg"))
                .allergies(List.of("apple"))
                .build();
    }

    @DisplayName("Should get people list and the number of the fire station in Fire case")
    @Test
    void shouldGetPeopleListInFireCase() throws Exception {

        List<PersonWithMedicalRecordDTO> personList = List.of(firstPersonDTO, secondPersonDTO);

        int fireStationNumber = 5;

        FireDTO fireDTO = FireDTO.builder()
                .personsListInFireCase(personList)
                .fireStationNumber(fireStationNumber)
                .build();

        when(this.personService.findAllPeopleInFireCase(anyString())).thenReturn(fireDTO);

        mockMvc.perform(get("/fire")
                        .param("address", "test address"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.personsListInFireCase", hasSize(2)))
                .andExpect(jsonPath("$.personsListInFireCase[0].lastName", is("lastname1")))
                .andExpect(jsonPath("$.personsListInFireCase[1].lastName", is("lastname2")));

        verify(this.personService, times(1)).findAllPeopleInFireCase(addressCaptor.capture());
        String addressCaptorValue = addressCaptor.getValue();
        assertEquals("test address", addressCaptorValue);
    }

    @DisplayName("Should get people list in flood case")
    @Test
    void shouldGetPeopleListInFloodCase() throws Exception {

        String address = "test address";
        List<PersonWithMedicalRecordDTO> personList = List.of(firstPersonDTO, secondPersonDTO);
        HashMap response = new HashMap<>();
        response.put(address, personList);

        when(this.personService.findAllPeopleInFloodCase(anyList())).thenReturn(response);

        mockMvc.perform(get("/flood/stations")
                        .param("stations", "1,2"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$['test address']", hasSize(2)))
                .andExpect(jsonPath("$['test address'][0]['lastName']", is("lastname1")));

        verify(this.personService, times(1)).findAllPeopleInFloodCase(fireStationNumbersCaptor.capture());
        List<Integer> fireStationNumbersCaptorValue = fireStationNumbersCaptor.getValue();
        assertEquals(List.of(1, 2), fireStationNumbersCaptorValue);

    }

    @DisplayName("Should get person info")
    @Test
    void getPersonInfo() throws Exception {

        PersonInfoDTO personInfoDTO = PersonInfoDTO.builder()
                .lastName("lastname")
                .address("person address")
                .age(23)
                .email("person@mail.com")
                .medications((List.of("tramamazol:300mg")))
                .allergies(List.of("peanut"))
                .build();

        when(this.personService.getPersonInfo(anyString(), anyString())).thenReturn(List.of(personInfoDTO));

        mockMvc.perform(get("/personInfo")
                        .param("lastName", "lastname")
                        .param("firstName", "firstname"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].lastName", is("lastname")))
                .andExpect(jsonPath("$[0].address", is("person address")))
                .andExpect(jsonPath("$[0].age", is(23)))
                .andExpect(jsonPath("$[0].email", is("person@mail.com")))
                .andExpect(jsonPath("$[0].medications", hasSize(1)))
                .andExpect(jsonPath("$[0].allergies", hasSize(1)));

        verify(this.personService, times(1)).getPersonInfo(firstNameCaptor.capture(), lastNameCaptor.capture());
        String firstNameCaptorValue = firstNameCaptor.getValue();
        String lastNameCaptorValue = lastNameCaptor.getValue();
        assertEquals("firstname", firstNameCaptorValue);
        assertEquals("lastname", lastNameCaptorValue);

    }
}