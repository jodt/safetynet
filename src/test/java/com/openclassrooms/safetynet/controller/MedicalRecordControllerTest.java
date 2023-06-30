package com.openclassrooms.safetynet.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.openclassrooms.safetynet.exception.MedicalRecordNotFoundException;
import com.openclassrooms.safetynet.model.MedicalRecord;
import com.openclassrooms.safetynet.repository.MedicalRecordRepository;
import com.openclassrooms.safetynet.service.MedicalRecordService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = MedicalRecordController.class)
class MedicalRecordControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @MockBean
    private MedicalRecordService medicalRecordService;

    @MockBean
    private MedicalRecordRepository medicalRecordRepository;

    @Captor
    ArgumentCaptor<MedicalRecord> medicalCaptor;

    @Captor
    ArgumentCaptor<String> firstNameCaptor;

    @Captor
    ArgumentCaptor<String> lastNameCaptor;

    private MedicalRecord medicalRecord;


    @BeforeEach
    public void init() {
        medicalRecord = MedicalRecord.builder()
                .firstName("firstname")
                .lastName("lastname")
                .birthdate(LocalDate.of(2000, 01, 01))
                .medications(List.of("aznol:350mg"))
                .allergies(List.of("nillacilan"))
                .build();

    }


    @DisplayName("Should get all medical records")
    @Test
    void shouldGetMedicalRecords() throws Exception {

        when(this.medicalRecordService.getAllMedicalRecords()).thenReturn(List.of(medicalRecord));

        mockMvc.perform(get("/medicalRecord/all"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$",hasSize(1)))
                .andExpect(jsonPath("$[0].firstName", is("firstname")))
                .andExpect(jsonPath("$[0].lastName", is("lastname")));

        verify(this.medicalRecordService,times(1)).getAllMedicalRecords();

    }


    @DisplayName("Should add a medical record")
    @Test
    void shouldAddMedicalRecord() throws Exception {

        mockMvc.perform(post("/medicalRecord")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(medicalRecord)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.firstName", is("firstname")))
                .andExpect(jsonPath("$.lastName", is("lastname")));

        verify(this.medicalRecordService, times(1)).addMedicalRecord(medicalCaptor.capture());
        MedicalRecord medicalCaptorValue = medicalCaptor.getValue();
        assertEquals("firstname", medicalCaptorValue.getFirstName());
        assertEquals("lastname", medicalCaptorValue.getLastName());

    }


    @DisplayName("Should update a medical record")
    @Test
    void shouldUpdateMedicalRecord() throws Exception {

        mockMvc.perform(put("/medicalRecord")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(medicalRecord)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName", is("firstname")))
                .andExpect(jsonPath("$.lastName", is("lastname")));

        verify(this.medicalRecordService, times(1)).updateMedicalRecord(medicalCaptor.capture());
        MedicalRecord medicalCaptorValue = medicalCaptor.getValue();
        assertEquals("firstname", medicalCaptorValue.getFirstName());
        assertEquals("lastname", medicalCaptorValue.getLastName());

    }


    @DisplayName("Should not update a medical record -> medical record not found")
    @Test
    void shouldNotUpdateMedicalRecord() throws Exception {

        when(this.medicalRecordService.updateMedicalRecord(any(MedicalRecord.class))).thenThrow(MedicalRecordNotFoundException.class);

        mockMvc.perform(put("/medicalRecord")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(medicalRecord)))
                .andExpect(status().isNotFound());

    }


    @DisplayName("Should delete a medical record")
    @Test
    void shouldDeleteMedicalRecord() throws Exception {

        mockMvc.perform(delete("/medicalRecord")
                        .contentType("application/json")
                        .param("firstName", "firstname")
                        .param("lastName", "lastname"))
                .andExpect(status().isNoContent());

        verify(this.medicalRecordService, times(1)).deleteMedicalRecord(firstNameCaptor.capture(), lastNameCaptor.capture());
        String firstNameCaptorValue = firstNameCaptor.getValue();
        String lastNameCaptorValue = lastNameCaptor.getValue();
        assertEquals("firstname", firstNameCaptorValue);
        assertEquals("lastname", lastNameCaptorValue);

    }


    @DisplayName("Should delete a medical record -> medical record not found")
    @Test
    void shouldNotDeleteMedicalRecord() throws Exception {

        doThrow(MedicalRecordNotFoundException.class).when(this.medicalRecordService).deleteMedicalRecord(anyString(), anyString());

        mockMvc.perform(delete("/medicalRecord")
                        .contentType("application/json")
                        .param("firstName", "firstname")
                        .param("lastName", "lastname"))
                .andExpect(status().isNotFound());

    }

}