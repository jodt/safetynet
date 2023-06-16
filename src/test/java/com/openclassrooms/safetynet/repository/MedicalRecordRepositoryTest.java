package com.openclassrooms.safetynet.repository;

import com.openclassrooms.safetynet.model.MedicalRecord;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class MedicalRecordRepositoryTest {

    private MedicalRecordRepository medicalRecordRepository = new MedicalRecordRepository();
    private MedicalRecord medicalRecord1;
    private MedicalRecord medicalRecord2;
    private List<MedicalRecord> medicalRecordList = new ArrayList<>();


    @BeforeEach
    public void init() {

        medicalRecord1 = MedicalRecord.builder()
                .firstName("firstname1")
                .lastName("lastname1")
                .birthdate(LocalDate.of(2000, 01, 01))
                .medications(List.of("aznol:350mg", "hydrapermazol:100mg"))
                .allergies(List.of("peanut"))
                .build();

        medicalRecord2 = MedicalRecord.builder()
                .firstName("firstname2")
                .lastName("lastname2")
                .birthdate(LocalDate.of(2010, 01, 01))
                .medications(List.of("terazine:10mg"))
                .allergies(List.of("xilliathal"))
                .build();

        Collections.addAll(medicalRecordList, medicalRecord1, medicalRecord2);
        this.medicalRecordRepository.setMedicalRecords(medicalRecordList);
    }


    @DisplayName("Should get all medical records")
    @Test
    void getMedicalRecords() {

        List<MedicalRecord> result = this.medicalRecordRepository.getMedicalRecords();

        assertNotNull(result);
        assertEquals(2, result.size());

        assertEquals(medicalRecord1, result.get(0));
        assertEquals(medicalRecord2, result.get(1));

    }


    @DisplayName("Should add a medical record")
    @Test
    void addMedicalRecord() {

        MedicalRecord medicalRecordToAdd = MedicalRecord.builder()
                .firstName("firstname3")
                .lastName("lastname3")
                .birthdate(LocalDate.of(2015, 01, 01))
                .medications(List.of("terazine:1000mg"))
                .allergies(List.of("shellfish"))
                .build();

        MedicalRecord result = this.medicalRecordRepository.addMedicalRecord(medicalRecordToAdd);

        assertNotNull(result);
        assertEquals(3, this.medicalRecordList.size());

        assertEquals(medicalRecordToAdd, result);
        assertEquals(medicalRecordToAdd, this.medicalRecordList.get(2));
    }


    @DisplayName("Should delete a medical record")
    @Test
    void deleteMedicalRecord() {

        this.medicalRecordRepository.deleteMedicalRecord(medicalRecord2);

        assertEquals(1, this.medicalRecordList.size());
        assertFalse(this.medicalRecordList.contains(medicalRecord2));
    }


    @DisplayName("Should update a medical record")
    @Test
    void updateMedicalRecord() {

        MedicalRecord medicalRecordToUpdate = MedicalRecord.builder()
                .firstName("firstname1")
                .lastName("lastname1")
                .birthdate(LocalDate.of(2000, 01, 01))
                .medications(List.of("aznol:350mg", "hydrapermazol:100mg"))
                .allergies(List.of("shellfish"))
                .build();

        MedicalRecord result = this.medicalRecordRepository.updateMedicalRecord(medicalRecordToUpdate);

        assertNotNull(result);

        assertEquals(medicalRecordToUpdate, result);
        assertEquals(medicalRecordToUpdate, this.medicalRecordList.get(0));
    }

    @DisplayName("Should find a medical record by first and last name")
    @Test
    void findMedicalRecordByFirstNameAndLastName() {

        MedicalRecord result = this.medicalRecordRepository.findMedicalRecordByFirstNameAndLastName("firstname2", "lastname2");

        assertNotNull(result);

        assertEquals(medicalRecord2, result);

    }

}