package com.openclassrooms.safetynet.service;

import com.openclassrooms.safetynet.exception.MedicalRecordNotFoundException;
import com.openclassrooms.safetynet.model.MedicalRecord;
import com.openclassrooms.safetynet.repository.MedicalRecordRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MedicalRecordService {

    private final MedicalRecordRepository medicalRecordRepository;

    public MedicalRecordService(MedicalRecordRepository medicalRecordRepository) {
        this.medicalRecordRepository = medicalRecordRepository;
    }

    public MedicalRecord addMedicalRecord(MedicalRecord medicalRecord){
        this.medicalRecordRepository.addMedicalRecord(medicalRecord);
        return medicalRecord;
    }

    public MedicalRecord updateMedicalrecord(MedicalRecord medicalRecord) throws MedicalRecordNotFoundException {
        this.findMedicalRecordByFirstNameAndLastName(medicalRecord.getFirstName(), medicalRecord.getLastName());
        this.medicalRecordRepository.updateMedicalRecord(medicalRecord);
        return medicalRecord;
    }

    public void deleteMedicalRecord(String firstName, String lastName) throws MedicalRecordNotFoundException {
        MedicalRecord medicalRecordFound = this.findMedicalRecordByFirstNameAndLastName(firstName, lastName);
        this.medicalRecordRepository.deleteMedicalRecord(medicalRecordFound);
    }

    private MedicalRecord findMedicalRecordByFirstNameAndLastName(String firstName, String lastName) throws MedicalRecordNotFoundException {
        MedicalRecord medicalRecordFound = this.medicalRecordRepository.findMedicalRecordByFirstNameAndLastName(firstName,lastName);
        if (medicalRecordFound == null){
            throw new MedicalRecordNotFoundException("Medical record not found for " + firstName + " " + lastName);
        }
        return medicalRecordFound;
    }
}
