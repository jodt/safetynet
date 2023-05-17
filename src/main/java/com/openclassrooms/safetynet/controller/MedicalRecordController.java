package com.openclassrooms.safetynet.controller;

import com.openclassrooms.safetynet.exception.MedicalRecordNotFoundException;
import com.openclassrooms.safetynet.model.MedicalRecord;
import com.openclassrooms.safetynet.repository.MedicalRecordRepository;
import com.openclassrooms.safetynet.service.MedicalRecordService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/medicalRecord")
public class MedicalRecordController {
    private final MedicalRecordRepository medicalRecordRepository;
    private final MedicalRecordService medicalRecordService;

    public MedicalRecordController(MedicalRecordRepository medicalRecordRepository, MedicalRecordService medicalRecordService) {
        this.medicalRecordRepository = medicalRecordRepository;
        this.medicalRecordService = medicalRecordService;
    }

    @GetMapping()
    public List<MedicalRecord> getMedicalRecords(){
        return medicalRecordRepository.getMedicalRecords();
    }

    @PostMapping()
    public ResponseEntity<MedicalRecord> addMedicalRecord(@RequestBody MedicalRecord medicalRecord){
        this.medicalRecordService.addMedicalRecord(medicalRecord);
        return new ResponseEntity<>(medicalRecord, HttpStatus.CREATED);
    }

    @PutMapping()
    public ResponseEntity<MedicalRecord> updateMedicalRecord(@RequestBody MedicalRecord medicalRecord) throws MedicalRecordNotFoundException {
        this.medicalRecordService.updateMedicalrecord(medicalRecord);
        return new ResponseEntity<>(medicalRecord, HttpStatus.OK);
    }

    @DeleteMapping()
    public ResponseEntity<Object> deleteMedicalrecord(@RequestParam String firstName, @RequestParam String lastName) throws MedicalRecordNotFoundException {
        this.medicalRecordService.deleteMedicalRecord(firstName,lastName);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
