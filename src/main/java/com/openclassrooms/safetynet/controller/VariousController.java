package com.openclassrooms.safetynet.controller;

import com.openclassrooms.safetynet.dto.FireDTO;
import com.openclassrooms.safetynet.dto.PersonInfoDTO;
import com.openclassrooms.safetynet.dto.PersonWithMedicalRecordDTO;
import com.openclassrooms.safetynet.exception.FireStationNotFoundException;
import com.openclassrooms.safetynet.exception.PersonNotFoundException;
import com.openclassrooms.safetynet.service.PersonService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;
import java.util.Objects;

@RestController
public class VariousController {

    private final Logger logger = LoggerFactory.getLogger(VariousController.class);
    private final PersonService personService;

    public VariousController(PersonService personService) {
        this.personService = personService;
    }


    @GetMapping("/fire")
    public FireDTO getPeopleListInFireCase(@RequestParam String address) throws PersonNotFoundException, FireStationNotFoundException {
        logger.info("Start process to retrieve people at {} in the event of fire", address);
        FireDTO peopleList = this.personService.findAllPeopleInFireCase(address);
        logger.info("Process end successfully");
        return peopleList;
    }

    @GetMapping("/flood/stations")
    public Map<String,List<PersonWithMedicalRecordDTO>> getPeopleListInFloodCase(@RequestParam List<Integer> stations) throws PersonNotFoundException, FireStationNotFoundException {
        stations.forEach(station -> logger.info("Start process to find people concerned by the fire station number {} in the event of flooding", station));
        Map <String,List<PersonWithMedicalRecordDTO>> peopleList = this.personService.findAllPeopleInFloodCase(stations);
        if (Objects.isNull(peopleList)){
            logger.error("No fire stations found");
            throw new FireStationNotFoundException("No fire stations found for these numbers");
        }
        else {
            logger.info("Process end successfully");
            return peopleList;
        }
    }

    @GetMapping("/personInfo")
    public List<PersonInfoDTO>getPersonInfo(@RequestParam String firstName, @RequestParam String lastName){
        logger.info("Start process to retrieve {} {}'s information", firstName,lastName);
        List<PersonInfoDTO> personInfoList = this.personService.getPersonInfo(firstName, lastName);
        return personInfoList;
    }

}
