package com.openclassrooms.safetynet.service;

import com.openclassrooms.safetynet.exception.MedicalRecordAlreadyExistException;
import com.openclassrooms.safetynet.exception.MedicalRecordNotFoundException;
import com.openclassrooms.safetynet.model.MedicalRecord;
import com.openclassrooms.safetynet.model.Person;
import com.openclassrooms.safetynet.repository.MedicalRecordRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
public class MedicalRecordServiceImpl implements MedicalRecordService {

    private final Logger logger = LoggerFactory.getLogger(MedicalRecordServiceImpl.class);

    private final MedicalRecordRepository medicalRecordRepository;


    public MedicalRecordServiceImpl(MedicalRecordRepository medicalRecordRepository) {
        this.medicalRecordRepository = medicalRecordRepository;
    }

    /**
     * Get all medical records
     * @return a list of medical records
     */
    public List<MedicalRecord> getAllMedicalRecords() {
        return this.medicalRecordRepository.getMedicalRecords();
    }

    public MedicalRecord addMedicalRecord(MedicalRecord medicalRecord) throws MedicalRecordAlreadyExistException {
        Boolean isMedicalRecordAlreadyRegistered = this.checkIfMedicalRecordAlreadyExist(medicalRecord);
        if(isMedicalRecordAlreadyRegistered){
            logger.error("The medical record of {} {} already exist", medicalRecord.getFirstName(),medicalRecord.getLastName());
            throw new MedicalRecordAlreadyExistException("The medical record of " + medicalRecord.getFirstName() + " " + medicalRecord.getLastName() + " already exist");
        } else {
            logger.debug("Try to add a new medical record {} {}", medicalRecord.getFirstName(), medicalRecord.getLastName());
            this.medicalRecordRepository.addMedicalRecord(medicalRecord);
            logger.debug("Medical record successfully added");
            return medicalRecord;
        }
    }


    /**
     * Method that takes a medical record and update the corresponding medical record (by name and firstname)
     * @param medicalRecord
     * @return the updated medical record
     * @throws MedicalRecordNotFoundException if medical record is not found
     */
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

    /**
     * Method that takes a firstname and a lastname and deletes the corresponding medical record
     * @param firstName
     * @param lastName
     * @throws MedicalRecordNotFoundException if the medical record is not found
     */
    public void deleteMedicalRecord(String firstName, String lastName) throws MedicalRecordNotFoundException {
        logger.debug("Try to delete the medical record of {} {}", firstName, lastName);
        MedicalRecord medicalRecordFound = this.findMedicalRecordByFirstNameAndLastName(firstName, lastName);
        logger.debug("Medical record successfully deleted");
        this.medicalRecordRepository.deleteMedicalRecord(medicalRecordFound);
    }

    /**
     * Method that takes a firstname and a lstname and returns the corresponding medical record
     * @param firstName
     * @param lastName
     * @return a medical record
     * @throws MedicalRecordNotFoundException if medical record is not found
     */
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


    /**
     * Method that takes a medical record and checks if it is already registered
     *
     * @param medicalRecord
     *
     * @return true if the medical record is already registered otherwise false
     */
    private Boolean checkIfMedicalRecordAlreadyExist (MedicalRecord medicalRecord) {
        return this.medicalRecordRepository.findMedicalRecordByFirstNameAndLastName(medicalRecord.getFirstName(),medicalRecord.getLastName()) != null;
    }


}
