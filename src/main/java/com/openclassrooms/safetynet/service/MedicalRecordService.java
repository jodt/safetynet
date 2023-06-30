package com.openclassrooms.safetynet.service;

import com.openclassrooms.safetynet.exception.MedicalRecordNotFoundException;
import com.openclassrooms.safetynet.model.MedicalRecord;

import java.util.List;

public interface MedicalRecordService {

    public List<MedicalRecord> getAllMedicalRecords();

    public MedicalRecord addMedicalRecord(MedicalRecord medicalRecord);

    public MedicalRecord updateMedicalRecord(MedicalRecord medicalRecord) throws MedicalRecordNotFoundException;

    public void deleteMedicalRecord(String firstName, String lastName) throws MedicalRecordNotFoundException;

    public MedicalRecord findMedicalRecordByFirstNameAndLastName(String firstName, String lastName) throws MedicalRecordNotFoundException;


}
