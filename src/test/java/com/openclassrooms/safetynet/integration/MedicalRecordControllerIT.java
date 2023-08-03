package com.openclassrooms.safetynet.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.openclassrooms.safetynet.model.MedicalRecord;
import com.openclassrooms.safetynet.repository.MedicalRecordRepository;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class MedicalRecordControllerIT {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    private MedicalRecordRepository medicalRecordRepository;

    private MedicalRecord medicalRecord;


    @BeforeEach
    void init() {
        medicalRecord = MedicalRecord.builder()
                .firstName("firstname")
                .lastName("lastname")
                .birthdate(LocalDate.of(2000, 01, 01))
                .medications(List.of("hydrapermazol:300mg"))
                .allergies(List.of("penaut"))
                .build();
    }


    @DisplayName("Should get all medical records")
    @Test
    @Order(1)
    void shouldGetMedicalRecords() throws Exception {

        mockMvc.perform(get("/medicalRecord/all"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(this.medicalRecordRepository.getMedicalRecords().size())));
    }


    @DisplayName("Should add a medical record")
    @Test
    @Order(2)
    void shouldAddMedicalRecord() throws Exception {

        mockMvc.perform(post("/medicalRecord")
                        .content(objectMapper.writeValueAsString(medicalRecord))
                        .contentType("application/json"))
                .andExpect(status().isCreated());

        MedicalRecord medicalRecordAdded = this.medicalRecordRepository.findMedicalRecordByFirstNameAndLastName("firstname", "lastname");

        assertEquals(medicalRecord, medicalRecordAdded);
    }


    @DisplayName("Should update a medical record")
    @Test
    @Order(3)
    void shouldUpdateMedicalRecord() throws Exception {

        MedicalRecord medicalRecordUpdated = MedicalRecord.builder()
                .firstName("firstname")
                .lastName("lastname")
                .birthdate(LocalDate.of(2000, 01, 01))
                .medications(List.of("Tramadol:800mg"))
                .allergies(List.of("penaut"))
                .build();

        mockMvc.perform(put("/medicalRecord")
                        .content(objectMapper.writeValueAsString(medicalRecordUpdated))
                        .contentType("application/json"))
                .andExpect(status().isOk());

        List<String> medications = this.medicalRecordRepository.findMedicalRecordByFirstNameAndLastName("firstname", "lastname").getMedications();

        assertEquals("Tramadol:800mg", medications.get(0));
    }


    @DisplayName("Should delete a medical record")
    @Test
    @Order(4)
    void shouldDeleteMedicalRecord() throws Exception {

        mockMvc.perform(delete("/medicalRecord")
                        .param("firstName", "firstname")
                        .param("lastName", "lastname"))
                .andExpect(status().isNoContent());

        assertFalse(this.medicalRecordRepository.getMedicalRecords().contains(medicalRecord));
    }

}
