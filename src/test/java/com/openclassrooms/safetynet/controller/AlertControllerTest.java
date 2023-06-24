package com.openclassrooms.safetynet.controller;

import com.openclassrooms.safetynet.dto.PersonWithAgeAndFamilyMembersDTO;
import com.openclassrooms.safetynet.exception.FireStationNotFoundException;
import com.openclassrooms.safetynet.exception.MedicalRecordNotFoundException;
import com.openclassrooms.safetynet.exception.PersonNotFoundException;
import com.openclassrooms.safetynet.model.Person;
import com.openclassrooms.safetynet.service.PersonService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = AlertController.class)
class AlertControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PersonService personService;

    @Captor
    ArgumentCaptor<String> addressCaptor;

    @Captor
    ArgumentCaptor<Integer> stationNumberCaptor;


    @DisplayName("Should get children list")
    @Test
    void ShouldGetChildrenList() throws Exception {

        List<PersonWithAgeAndFamilyMembersDTO> response = new ArrayList<>();

        PersonWithAgeAndFamilyMembersDTO childDTO = PersonWithAgeAndFamilyMembersDTO.builder()
                .firstName("firstnameChild1")
                .lastName("lastname1")
                .age(5)
                .otherFamilyMembers(List.of(Person.builder()
                        .firstName("firstname1")
                        .lastName("lastname1")
                        .address("person address 1")
                        .city("city")
                        .zip(59000)
                        .phone("001-001-001")
                        .email("person1@mail.com")
                        .build()))
                .build();

        response.add(childDTO);

        when(personService.findChildrenByAddress(anyString())).thenReturn(response);

        this.mockMvc.perform(get("/childAlert").param("address", "1509 Culver St"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].firstName", is("firstnameChild1")));

        verify(this.personService, times(1)).findChildrenByAddress(addressCaptor.capture());
        String addressCaptorValue = addressCaptor.getValue();
        assertEquals("1509 Culver St", addressCaptorValue);

    }


    @DisplayName("Should not get children list -> medical record not found")
    @Test
    void shouldNotGetChildrenListMedicalRecordNotFoundException() throws Exception {

        when(personService.findChildrenByAddress(anyString())).thenThrow(MedicalRecordNotFoundException.class);

        this.mockMvc.perform(get("/childAlert").param("address", "1509 Culver St"))
                .andExpect(status().isNotFound());

        verify(this.personService, times(1)).findChildrenByAddress(anyString());

    }


    @DisplayName("Should not get children list -> person not found")
    @Test
    void shouldNotGetChildrenListPersonNotFoundException() throws Exception {

        when(personService.findChildrenByAddress(anyString())).thenThrow(PersonNotFoundException.class);

        this.mockMvc.perform(get("/childAlert").param("address", "1509 Culver St"))
                .andExpect(status().isNotFound());

        verify(this.personService, times(1)).findChildrenByAddress(anyString());

    }


    @DisplayName("Should get phone number list")
    @Test
    void shouldGetPeoplePhoneNumberByFIreStationNumber() throws Exception {
        List<String> response = List.of("001-001-001", "001-001-002");

        when(personService.findPhoneNumberByFireStationNumber(anyInt())).thenReturn(response);

        this.mockMvc.perform(get("/phoneAlert").param("firestation", "5"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0]", is("001-001-001")))
                .andExpect(jsonPath("$[1]", is("001-001-002")));

        verify(this.personService, times(1)).findPhoneNumberByFireStationNumber(stationNumberCaptor.capture());
        int stationNumberCaptorValue = stationNumberCaptor.getValue();
        assertEquals(5, stationNumberCaptorValue);

    }


    @DisplayName("Should not get phone number list -> fire station not found")
    @Test
    void shouldNotGetPeoplePhoneNumberByFIreStationNumber() throws Exception {

        when(personService.findPhoneNumberByFireStationNumber(anyInt())).thenThrow(FireStationNotFoundException.class);

        this.mockMvc.perform(get("/phoneAlert").param("firestation", "5"))
                .andExpect(status().isNotFound());

        verify(this.personService, times(1)).findPhoneNumberByFireStationNumber(anyInt());

    }

}