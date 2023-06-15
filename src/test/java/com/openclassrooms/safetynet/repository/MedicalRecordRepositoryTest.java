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
        assertEquals("firstname1", result.get(0).getFirstName());
        assertEquals("lastname1", result.get(0).getLastName());
        assertEquals(LocalDate.of(2000, 01, 01), result.get(0).getBirthdate());
        assertEquals(2, result.get(0).getMedications().size());
        assertEquals("aznol:350mg", result.get(0).getMedications().get(0));
        assertEquals("hydrapermazol:100mg", result.get(0).getMedications().get(1));
        assertEquals(1, result.get(0).getAllergies().size());
        assertEquals("peanut", result.get(0).getAllergies().get(0));

        assertEquals(2, result.size());
        assertEquals("firstname2", result.get(1).getFirstName());
        assertEquals("lastname2", result.get(1).getLastName());
        assertEquals(LocalDate.of(2010, 01, 01), result.get(1).getBirthdate());
        assertEquals(1, result.get(1).getMedications().size());
        assertEquals("terazine:10mg", result.get(1).getMedications().get(0));
        assertEquals(1, result.get(1).getAllergies().size());
        assertEquals("xilliathal", result.get(1).getAllergies().get(0));

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

        assertEquals("firstname3", result.getFirstName());
        assertEquals("lastname3", result.getLastName());
        assertEquals(LocalDate.of(2015, 01, 01), result.getBirthdate());
        assertEquals(1, result.getMedications().size());
        assertEquals("terazine:1000mg", result.getMedications().get(0));
        assertEquals(1, result.getAllergies().size());
        assertEquals("shellfish", result.getAllergies().get(0));

        assertEquals("firstname3", this.medicalRecordList.get(2).getFirstName());
        assertEquals("lastname3", this.medicalRecordList.get(2).getLastName());
        assertEquals(LocalDate.of(2015, 01, 01), this.medicalRecordList.get(2).getBirthdate());
        assertEquals(1, this.medicalRecordList.get(2).getMedications().size());
        assertEquals("terazine:1000mg", this.medicalRecordList.get(2).getMedications().get(0));
        assertEquals(1, this.medicalRecordList.get(2).getAllergies().size());
        assertEquals("shellfish", this.medicalRecordList.get(2).getAllergies().get(0));
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

        assertEquals("firstname1", result.getFirstName());
        assertEquals("lastname1", result.getLastName());
        assertEquals(LocalDate.of(2000, 01, 01), result.getBirthdate());
        assertEquals(2, result.getMedications().size());
        assertEquals("aznol:350mg", result.getMedications().get(0));
        assertEquals("hydrapermazol:100mg", result.getMedications().get(1));
        assertEquals(1, result.getAllergies().size());
        assertEquals("shellfish", result.getAllergies().get(0));

        assertEquals("firstname1", this.medicalRecordList.get(0).getFirstName());
        assertEquals("lastname1", this.medicalRecordList.get(0).getLastName());
        assertEquals(LocalDate.of(2000, 01, 01), this.medicalRecordList.get(0).getBirthdate());
        assertEquals(2, this.medicalRecordList.get(0).getMedications().size());
        assertEquals("aznol:350mg", this.medicalRecordList.get(0).getMedications().get(0));
        assertEquals("hydrapermazol:100mg", this.medicalRecordList.get(0).getMedications().get(1));
        assertEquals(1, this.medicalRecordList.get(0).getAllergies().size());
        assertEquals("shellfish", this.medicalRecordList.get(0).getAllergies().get(0));

    }

    @DisplayName("Should find a medical record by first and last name")
    @Test
    void findMedicalRecordByFirstNameAndLastName() {

        MedicalRecord result = this.medicalRecordRepository.findMedicalRecordByFirstNameAndLastName("firstname2", "lastname2");

        assertNotNull(result);

        assertEquals("firstname2", result.getFirstName());
        assertEquals("lastname2", result.getLastName());
        assertEquals(LocalDate.of(2010, 01, 01), result.getBirthdate());
        assertEquals(1, result.getMedications().size());
        assertEquals("terazine:10mg", result.getMedications().get(0));
        assertEquals(1, result.getAllergies().size());
        assertEquals("xilliathal", result.getAllergies().get(0));

    }

}