package com.openclassrooms.safetynet.controller;

import com.openclassrooms.safetynet.dto.*;
import com.openclassrooms.safetynet.exception.FireStationNotFoundException;
import com.openclassrooms.safetynet.exception.MailsNotFoundException;
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
import java.util.concurrent.atomic.AtomicInteger;

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

    @Captor
    ArgumentCaptor<Integer> stationNumberCaptor;

    @Captor
    ArgumentCaptor<String> cityCaptor;

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