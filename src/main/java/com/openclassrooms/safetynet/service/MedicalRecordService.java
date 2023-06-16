package com.openclassrooms.safetynet.service;

import com.openclassrooms.safetynet.exception.MedicalRecordNotFoundException;
import com.openclassrooms.safetynet.model.MedicalRecord;
import com.openclassrooms.safetynet.repository.MedicalRecordRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
public class MedicalRecordService {

    private final Logger logger = LoggerFactory.getLogger(MedicalRecordService.class);

    private final MedicalRecordRepository medicalRecordRepository;


    public MedicalRecordService(MedicalRecordRepository medicalRecordRepository) {
        this.medicalRecordRepository = medicalRecordRepository;
    }

    public MedicalRecord addMedicalRecord(MedicalRecord medicalRecord) {
        logger.debug("Try to add a new medical record {} {}", medicalRecord.getFirstName(), medicalRecord.getLastName());
        this.medicalRecordRepository.addMedicalRecord(medicalRecord);
        logger.debug("Medical record successfully added");
        return medicalRecord;
    }

    public MedicalRecord updateMedicalRecord(MedicalRecord medicalRecord) throws MedicalRecordNotFoundException {
        logger.debug("Try to update the medical record of {} {}", medicalRecord.getFirstName(), medicalRecord.getLastName());
        MedicalRecord medicalRecordToUpdate = this.findMedicalRecordByFirstNameAndLastName(medicalRecord.getFirstName(), medicalRecord.getLastName());
        medicalRecordToUpdate.setBirthdate(medicalRecord.getBirthdate());
        medicalRecordToUpdate.setAllergies(medicalRecord.getAllergies());
        medicalRecordToUpdate.setMedications(medicalRecord.getMedications());
        if (!Objects.isNull(medicalRecordToUpdate)) {
            this.medicalRecordRepository.updateMedicalRecord(medicalRecordToUpdate);
            logger.debug("Medical record successfully updated");
        }
        return medicalRecord;
    }

    public void deleteMedicalRecord(String firstName, String lastName) throws MedicalRecordNotFoundException {
        logger.debug("Try to delete the medical record of {} {}", firstName, lastName);
        MedicalRecord medicalRecordFound = this.findMedicalRecordByFirstNameAndLastName(firstName, lastName);
        logger.debug("Medical record successfully deleted");
        this.medicalRecordRepository.deleteMedicalRecord(medicalRecordFound);
    }

    public MedicalRecord findMedicalRecordByFirstNameAndLastName(String firstName, String lastName) throws MedicalRecordNotFoundException {
        logger.debug("Try to find the medical record of {} {}", firstName, lastName);
        MedicalRecord medicalRecordFound = this.medicalRecordRepository.findMedicalRecordByFirstNameAndLastName(firstName, lastName);
        if (medicalRecordFound == null) {
            logger.error("Medical record not found for {} {}", firstName, lastName);
            throw new MedicalRecordNotFoundException("Medical record not found for " + firstName + " " + lastName);
        }
        logger.debug("Medical record found for {} {}", firstName, lastName);
        return medicalRecordFound;
    }


}
