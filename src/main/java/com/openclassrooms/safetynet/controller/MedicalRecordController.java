package com.openclassrooms.safetynet.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.openclassrooms.safetynet.exception.MedicalRecordAlreadyExistException;
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
    private final ObjectMapper mapper;


    public MedicalRecordController(MedicalRecordRepository medicalRecordRepository, MedicalRecordService medicalRecordService, ObjectMapper mapper) {
        this.medicalRecordService = medicalRecordService;
        this.mapper = mapper.enable(SerializationFeature.INDENT_OUTPUT);
    }


    @GetMapping("/all")
    public List<MedicalRecord> getMedicalRecords() throws JsonProcessingException {
        logger.info("GET /medicalRecord/all called");
        List<MedicalRecord> response = this.medicalRecordService.getAllMedicalRecords();
        logger.info("Process end successfully with response: {}", mapper.writeValueAsString(response));
        return response;
    }


    @PostMapping()
    public ResponseEntity<MedicalRecord> addMedicalRecord(@RequestBody MedicalRecord medicalRecord) throws MedicalRecordAlreadyExistException, JsonProcessingException {
        logger.info("POST /medicalRecord called to add a new medical record for {} {}", medicalRecord.getFirstName(), medicalRecord.getLastName());
        this.medicalRecordService.addMedicalRecord(medicalRecord);
        logger.info("Process end successfully with response: {}", mapper.writeValueAsString(medicalRecord));
        return new ResponseEntity<>(medicalRecord, HttpStatus.CREATED);
    }


    @PutMapping()
    public ResponseEntity<MedicalRecord> updateMedicalRecord(@RequestBody MedicalRecord medicalRecord) throws MedicalRecordNotFoundException, JsonProcessingException {
        logger.info("PUT /medicalRecord called to update the medical record of {} {}", medicalRecord.getFirstName(), medicalRecord.getLastName());
        this.medicalRecordService.updateMedicalRecord(medicalRecord);
        logger.info("Process end successfully with response: {}", mapper.writeValueAsString(medicalRecord));
        return new ResponseEntity<>(medicalRecord, HttpStatus.OK);
    }


    @DeleteMapping()
    public ResponseEntity<Object> deleteMedicalRecord(@RequestParam String firstName, @RequestParam String lastName) throws MedicalRecordNotFoundException {
        logger.info("DELETE /medicalRecord called to delete the medical record of {} {}", firstName, lastName);
        this.medicalRecordService.deleteMedicalRecord(firstName, lastName);
        logger.info("Process end successfully");
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }


}
