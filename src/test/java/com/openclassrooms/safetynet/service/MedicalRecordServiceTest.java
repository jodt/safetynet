package com.openclassrooms.safetynet.service;

import com.openclassrooms.safetynet.exception.MedicalRecordNotFoundException;
import com.openclassrooms.safetynet.model.MedicalRecord;
import com.openclassrooms.safetynet.repository.MedicalRecordRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.AbstractSet;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MedicalRecordServiceTest {

    @Mock
    MedicalRecordRepository medicalRecordRepository;

    @InjectMocks
    MedicalRecordService medicalRecordService;

    private MedicalRecord medicalRecord;
    private MedicalRecord medicalRecordToUpdate;
    private MedicalRecord updatedMedicalRecord;


    @BeforeEach
    public void init() {

        medicalRecord = MedicalRecord.builder()
                .firstName("firstname")
                .lastName("lastname")
                .birthdate(LocalDate.of(2000,01,01))
                .medications(List.of("aznol:350mg"))
                .allergies(List.of("nillacilan"))
                .build();

        medicalRecordToUpdate = MedicalRecord.builder()
                .firstName("firstname")
                .lastName("lastname")
                .birthdate(LocalDate.of(2000,01,01))
                .medications(List.of("aznol:350mg"))
                .allergies(List.of("nillacilan"))
                .build();

        updatedMedicalRecord = MedicalRecord.builder()
                .firstName("firstname")
                .lastName("lastname")
                .birthdate(LocalDate.of(2000,01,01))
                .medications(List.of("aznol:350mg"))
                .allergies(List.of("peanut"))
                .build();

    }


    @DisplayName("Should add a medical record")
    @Test
    void shouldAddAMedicalRecord() {

         when(this.medicalRecordRepository.addMedicalRecord(any(MedicalRecord.class))).thenReturn(medicalRecord);

         MedicalRecord result = this.medicalRecordService.addMedicalRecord(medicalRecord);

         assertNotNull(result);
         assertEquals("firstname", result.getFirstName());
         assertEquals("lastname", result.getLastName());
         assertEquals(LocalDate.of(2000,01,01), result.getBirthdate());
         assertEquals("aznol:350mg", result.getMedications().get(0));
         assertEquals("nillacilan", result.getAllergies().get(0));

         verify(this.medicalRecordRepository, times(1)).addMedicalRecord(any(MedicalRecord.class));

    }

    @DisplayName("Should update a medical record")
    @Test
    void updateMedicalRecord() throws MedicalRecordNotFoundException {

        when(this.medicalRecordRepository.findMedicalRecordByFirstNameAndLastName(anyString(),anyString())).thenReturn(medicalRecordToUpdate);
        when(this.medicalRecordRepository.updateMedicalRecord(any(MedicalRecord.class))).thenReturn(updatedMedicalRecord);

        MedicalRecord result = this.medicalRecordService.updateMedicalRecord(updatedMedicalRecord);

        assertNotNull(result);
        assertNotNull(result);
        assertEquals("firstname", result.getFirstName());
        assertEquals("lastname", result.getLastName());
        assertEquals(LocalDate.of(2000,01,01), result.getBirthdate());
        assertEquals("aznol:350mg", result.getMedications().get(0));
        assertEquals("peanut", result.getAllergies().get(0));

        verify(this.medicalRecordRepository, times(1)).findMedicalRecordByFirstNameAndLastName(anyString(),anyString());
        verify(this.medicalRecordRepository, times(1)).updateMedicalRecord(any(MedicalRecord.class));

    }


    @DisplayName("Should delete a medical record")
    @Test
    void shouldDeleteMedicalRecord() throws MedicalRecordNotFoundException {

        when(this.medicalRecordRepository.findMedicalRecordByFirstNameAndLastName(anyString(),anyString())).thenReturn(medicalRecord);
        doNothing().when(medicalRecordRepository).deleteMedicalRecord(medicalRecord);

        this.medicalRecordService.deleteMedicalRecord("firstname","lastname");

        verify(this.medicalRecordRepository,times(1)).deleteMedicalRecord(medicalRecord);

    }


    @DisplayName("Should get a medical record")
    @Test
    void shouldFindMedicalRecordByFirstNameAndLastName() throws MedicalRecordNotFoundException {

        when(this.medicalRecordRepository.findMedicalRecordByFirstNameAndLastName(anyString(),anyString())).thenReturn(medicalRecord);

        MedicalRecord result = this.medicalRecordService.findMedicalRecordByFirstNameAndLastName("firstname","lastname");

        assertNotNull(result);
        assertNotNull(result);
        assertEquals("firstname", result.getFirstName());
        assertEquals("lastname", result.getLastName());
        assertEquals(LocalDate.of(2000,01,01), result.getBirthdate());
        assertEquals("aznol:350mg", result.getMedications().get(0));
        assertEquals("nillacilan", result.getAllergies().get(0));

        verify(this.medicalRecordRepository, times(1)).findMedicalRecordByFirstNameAndLastName(anyString(),anyString());

    }


    @DisplayName("Should not get a medical record")
    @Test
    void shouldNotFindMedicalRecordByFirstNameAndLastName() throws MedicalRecordNotFoundException {

        when(this.medicalRecordRepository.findMedicalRecordByFirstNameAndLastName(anyString(),anyString())).thenReturn(null);

        Exception exception = assertThrows(MedicalRecordNotFoundException.class, () -> this.medicalRecordService.findMedicalRecordByFirstNameAndLastName("firstname","lastname"));

        assertEquals("Medical record not found for firstname lastname", exception.getMessage());

        verify(this.medicalRecordRepository, times(1)).findMedicalRecordByFirstNameAndLastName(anyString(),anyString());

    }

}