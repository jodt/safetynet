package com.openclassrooms.safetynet.controller;

import com.openclassrooms.safetynet.exception.MedicalRecordNotFoundException;
import com.openclassrooms.safetynet.model.MedicalRecord;
import com.openclassrooms.safetynet.repository.MedicalRecordRepository;
import com.openclassrooms.safetynet.service.MedicalRecordService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/medicalRecord")
public class MedicalRecordController {

    private final Logger logger = LoggerFactory.getLogger(MedicalRecordController.class);
    private final MedicalRecordService medicalRecordService;

    public MedicalRecordController(MedicalRecordRepository medicalRecordRepository, MedicalRecordService medicalRecordService) {
        this.medicalRecordService = medicalRecordService;
    }

    @GetMapping("/all")
    public List<MedicalRecord> getMedicalRecords(){
        logger.info("Start process to collect all Medical Records");
        return this.medicalRecordService.getAllMedicalRecords();
    }

    @PostMapping()
    public ResponseEntity<MedicalRecord> addMedicalRecord(@RequestBody MedicalRecord medicalRecord){
        logger.info("Start process to add a new medical record for {} {}", medicalRecord.getFirstName(), medicalRecord.getLastName());
        this.medicalRecordService.addMedicalRecord(medicalRecord);
        logger.info("Process end successfully");
        return new ResponseEntity<>(medicalRecord, HttpStatus.CREATED);
    }

    @PutMapping()
    public ResponseEntity<MedicalRecord> updateMedicalRecord(@RequestBody MedicalRecord medicalRecord) throws MedicalRecordNotFoundException {
        logger.info("Start process to update the medical record of {} {}", medicalRecord.getFirstName(),medicalRecord.getLastName());
        this.medicalRecordService.updateMedicalRecord(medicalRecord);
        logger.info("Process end successfully");
        return new ResponseEntity<>(medicalRecord, HttpStatus.OK);
    }

    @DeleteMapping()
    public ResponseEntity<Object> deleteMedicalRecord(@RequestParam String firstName, @RequestParam String lastName) throws MedicalRecordNotFoundException {
        logger.info("Start process to delete the medical record of {} {}", firstName, lastName);
        this.medicalRecordService.deleteMedicalRecord(firstName,lastName);
        logger.info("Process end successfully");
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
