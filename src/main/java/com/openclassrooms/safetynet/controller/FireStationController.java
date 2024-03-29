package com.openclassrooms.safetynet.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.openclassrooms.safetynet.exception.FireStationAlreadyExistException;
import com.openclassrooms.safetynet.exception.FireStationNotFoundException;
import com.openclassrooms.safetynet.model.FireStation;
import com.openclassrooms.safetynet.service.FireStationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/firestation")
public class FireStationController {

    private final Logger logger = LoggerFactory.getLogger(FireStationController.class);
    private final FireStationService fireStationService;
    private final ObjectMapper mapper;


    public FireStationController(FireStationService fireStationService, ObjectMapper mapper) {

        this.fireStationService = fireStationService;
        this.mapper = mapper.enable(SerializationFeature.INDENT_OUTPUT);
    }


    @GetMapping("/all")
    public List<FireStation> getAllFireStations() throws JsonProcessingException {
        logger.info("GET /firestation/all called");
        List<FireStation> response = this.fireStationService.getAllFireStation();
        logger.info("Process end successfully with response: {}", mapper.writeValueAsString(response));
        return response;
    }


    @PostMapping
    public ResponseEntity<FireStation> addFireStation(@RequestBody FireStation fireStation) throws FireStationAlreadyExistException {
        logger.info("POST /firestation called to add a new fire station for the address {} with the station's number {}", fireStation.getAddress(), fireStation.getStation());
        this.fireStationService.addFireStation(fireStation);
        logger.info("Process end successfully");
        return new ResponseEntity<>(fireStation, HttpStatus.CREATED);
    }


    @PutMapping
    public ResponseEntity<FireStation> updateFireStation(@RequestBody FireStation fireStation) throws FireStationNotFoundException, JsonProcessingException {
        logger.info("PUT /firestation called to update the fire station for the address {} ", fireStation.getAddress());
        this.fireStationService.updateStationNumber(fireStation);
        logger.info("Process end successfully with response: {}", mapper.writeValueAsString(fireStation));
        return new ResponseEntity<>(fireStation, HttpStatus.OK);
    }


    @DeleteMapping
    public ResponseEntity<Object> deleteFireStationByAddress(@RequestParam String address) throws FireStationNotFoundException {
        logger.info("DELETE /firestation called to delete the fire station for the address {} ", address);
        this.fireStationService.deleteFireStationByAddress(address);
        logger.info("Process end successfully");
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }


    @DeleteMapping("{stationNumber}")
    public ResponseEntity<Object> deleteFireStationsByStationNumber(@PathVariable int stationNumber) throws FireStationNotFoundException {
        logger.info("DELETE /firestation called to delete the fire station with the number {} ", stationNumber);
        this.fireStationService.deleteFireStationsByStationNumber(stationNumber);
        logger.info("Process end successfully");
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }


}
