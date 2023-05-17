package com.openclassrooms.safetynet.repository;

import com.openclassrooms.safetynet.model.MedicalRecord;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public class MedicalRecordRepository {

    private List<MedicalRecord> medicalRecords = new ArrayList<>();

    public List<MedicalRecord> getMedicalRecords() {
        return medicalRecords;
    }

    public void setMedicalRecords(List<MedicalRecord> medicalRecords) {
        this.medicalRecords = medicalRecords;
    }

    public MedicalRecord addMedicalRecord (MedicalRecord medicalRecord){
        this.medicalRecords.add(medicalRecord);
        return medicalRecord;
    }

    public void deleteMedicalRecord (MedicalRecord medicalRecord){
        this.medicalRecords.remove(medicalRecord);
    }

    public void updateMedicalRecord(MedicalRecord medicalRecord){
        MedicalRecord updateMedicalRecord = this.findMedicalRecordByFirstNameAndLastName(medicalRecord.getFirstName(),medicalRecord.getLastName());
        updateMedicalRecord.setBirthdate(medicalRecord.getBirthdate());
        updateMedicalRecord.setAllergies(medicalRecord.getAllergies());
        updateMedicalRecord.setMedications(medicalRecord.getMedications());
    }

    public MedicalRecord findMedicalRecordByFirstNameAndLastName(String firstName, String lastName) {
        MedicalRecord medicalRecordResult = null;
        for (MedicalRecord medicalRecord : this.medicalRecords){
            if (medicalRecord.getFirstName().toLowerCase().equals(firstName.toLowerCase()) && medicalRecord.getLastName().toLowerCase().equals(lastName.toLowerCase())){
                medicalRecordResult = medicalRecord;
            }
        }
        return medicalRecordResult;
    }

}
