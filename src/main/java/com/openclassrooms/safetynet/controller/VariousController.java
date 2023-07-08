package com.openclassrooms.safetynet.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
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

@RestController
public class VariousController {

    private final Logger logger = LoggerFactory.getLogger(VariousController.class);
    private final PersonService personService;
    private final ObjectMapper mapper;


    public VariousController(PersonService personService, ObjectMapper mapper) {
        this.personService = personService;
        this.mapper = mapper.enable(SerializationFeature.INDENT_OUTPUT);
    }


    @GetMapping("/fire")
    public FireDTO getPeopleListInFireCase(@RequestParam String address) throws PersonNotFoundException, FireStationNotFoundException, JsonProcessingException {
        logger.info("GET /fire called to retrieve people at {} in the event of fire", address);
        FireDTO peopleList = this.personService.findAllPeopleInFireCase(address);
        logger.info("Process end successfully with response: {}", mapper.writeValueAsString(peopleList));
        return peopleList;
    }


    @GetMapping("/flood/stations")
    public Map<String, List<PersonWithMedicalRecordDTO>> getPeopleListInFloodCase(@RequestParam List<Integer> stations) throws PersonNotFoundException, FireStationNotFoundException, JsonProcessingException {
        stations.forEach(station -> logger.info("GET /flood/stations called to find people concerned by the fire station number {} in the event of flooding", station));
        Map<String, List<PersonWithMedicalRecordDTO>> peopleList = this.personService.findAllPeopleInFloodCase(stations);
        logger.info("Process end successfully with response: {}", mapper.writeValueAsString(peopleList));
        return peopleList;
    }


    @GetMapping("/personInfo")
    public List<PersonInfoDTO> getPersonInfo(@RequestParam String firstName, @RequestParam String lastName) throws PersonNotFoundException, JsonProcessingException {
        logger.info("GET /personInfo called to retrieve {} {}'s information", firstName, lastName);
        List<PersonInfoDTO> personInfoList = this.personService.getPersonInfo(firstName, lastName);
        logger.info("Process end successfully with response: {}", mapper.writeValueAsString(personInfoList));
        return personInfoList;
    }


}
