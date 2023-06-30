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
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MedicalRecordServiceTest {

    @Mock
    MedicalRecordRepository medicalRecordRepository;

    @InjectMocks
    MedicalRecordServiceImpl medicalRecordServiceImpl;

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

         MedicalRecord result = this.medicalRecordServiceImpl.addMedicalRecord(medicalRecord);

         assertNotNull(result);

         assertEquals(medicalRecord,result);

         verify(this.medicalRecordRepository, times(1)).addMedicalRecord(any(MedicalRecord.class));

    }

    @DisplayName("Should update a medical record")
    @Test
    void updateMedicalRecord() throws MedicalRecordNotFoundException {

        when(this.medicalRecordRepository.findMedicalRecordByFirstNameAndLastName(anyString(),anyString())).thenReturn(medicalRecordToUpdate);
        when(this.medicalRecordRepository.updateMedicalRecord(any(MedicalRecord.class))).thenReturn(updatedMedicalRecord);

        MedicalRecord result = this.medicalRecordServiceImpl.updateMedicalRecord(updatedMedicalRecord);

        assertNotNull(result);

        assertEquals(updatedMedicalRecord,result);

        verify(this.medicalRecordRepository, times(1)).findMedicalRecordByFirstNameAndLastName(anyString(),anyString());
        verify(this.medicalRecordRepository, times(1)).updateMedicalRecord(any(MedicalRecord.class));

    }


    @DisplayName("Should delete a medical record")
    @Test
    void shouldDeleteMedicalRecord() throws MedicalRecordNotFoundException {

        when(this.medicalRecordRepository.findMedicalRecordByFirstNameAndLastName(anyString(),anyString())).thenReturn(medicalRecord);
        doNothing().when(medicalRecordRepository).deleteMedicalRecord(medicalRecord);

        this.medicalRecordServiceImpl.deleteMedicalRecord("firstname","lastname");

        verify(this.medicalRecordRepository,times(1)).deleteMedicalRecord(medicalRecord);

    }


    @DisplayName("Should get a medical record")
    @Test
    void shouldFindMedicalRecordByFirstNameAndLastName() throws MedicalRecordNotFoundException {

        when(this.medicalRecordRepository.findMedicalRecordByFirstNameAndLastName(anyString(),anyString())).thenReturn(medicalRecord);

        MedicalRecord result = this.medicalRecordServiceImpl.findMedicalRecordByFirstNameAndLastName("firstname","lastname");

        assertNotNull(result);

        assertEquals(medicalRecord,result);

        verify(this.medicalRecordRepository, times(1)).findMedicalRecordByFirstNameAndLastName(anyString(),anyString());

    }


    @DisplayName("Should not get a medical record")
    @Test
    void shouldNotFindMedicalRecordByFirstNameAndLastName() throws MedicalRecordNotFoundException {

        when(this.medicalRecordRepository.findMedicalRecordByFirstNameAndLastName(anyString(),anyString())).thenReturn(null);

        Exception exception = assertThrows(MedicalRecordNotFoundException.class, () -> this.medicalRecordServiceImpl.findMedicalRecordByFirstNameAndLastName("firstname","lastname"));

        assertEquals("Medical record not found for firstname lastname", exception.getMessage());

        verify(this.medicalRecordRepository, times(1)).findMedicalRecordByFirstNameAndLastName(anyString(),anyString());

    }

}