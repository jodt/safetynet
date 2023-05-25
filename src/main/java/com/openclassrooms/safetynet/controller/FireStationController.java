package com.openclassrooms.safetynet.controller;

import com.openclassrooms.safetynet.exception.FireStationNotFoundException;
import com.openclassrooms.safetynet.model.FireStation;
import com.openclassrooms.safetynet.repository.FireStationRepository;
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

    private final FireStationRepository fireStationRepository;

    public FireStationController(FireStationService fireStationService, FireStationRepository fireStationRepository) {
        this.fireStationService = fireStationService;
        this.fireStationRepository = fireStationRepository;
    }

    @GetMapping
    public List<FireStation> findAllFireStations(){
        logger.info("Start process to collect all fire stations");
        return this.fireStationRepository.getFireStations();
    }

    @PostMapping
    public ResponseEntity<FireStation> addFireStation(@RequestBody FireStation fireStation){
        logger.info("Start process to add a new fire station for the address {} with the station's number {}", fireStation.getAddress(), fireStation.getStation());
        this.fireStationService.addFireStation(fireStation);
        logger.info("Process end successfully");
        return new ResponseEntity<>(fireStation, HttpStatus.CREATED);
    }

    @PutMapping
    public ResponseEntity<FireStation> updateFireStation(@RequestBody FireStation fireStation) throws FireStationNotFoundException {
        logger.info("Start process to update the fire station for the address {} ", fireStation.getAddress());
        this.fireStationService.updateStationNumber(fireStation);
        logger.info("Process end successfully");
        return new ResponseEntity<>(fireStation, HttpStatus.OK);
    }

    @DeleteMapping
    public ResponseEntity<Object> deleteFireStationByAddress(@RequestParam String address) throws FireStationNotFoundException {
        logger.info("Start process to delete the fire station for the address {} ", address);
        this.fireStationService.deleteFireStationByAddress(address);
        logger.info("Process end successfully");
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @DeleteMapping("{stationNumber}")
    public ResponseEntity<Object> deleteFireStationByStationNumber(@PathVariable int stationNumber) throws FireStationNotFoundException {
        logger.info("Start process to delete the fire station with the number {} ", stationNumber);
        this.fireStationService.deleteFireStationByStationNumber(stationNumber);
        logger.info("Process end successfully");
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
