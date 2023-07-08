package com.openclassrooms.safetynet.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.openclassrooms.safetynet.dto.PersonWithAgeAndFamilyMembersDTO;
import com.openclassrooms.safetynet.exception.FireStationNotFoundException;
import com.openclassrooms.safetynet.exception.MedicalRecordNotFoundException;
import com.openclassrooms.safetynet.exception.PersonNotFoundException;
import com.openclassrooms.safetynet.service.PersonService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class AlertController {

    private Logger logger = LoggerFactory.getLogger(AlertController.class);
    private final PersonService personService;
    private final ObjectMapper mapper;


    public AlertController(PersonService personService, ObjectMapper mapper) {
        this.personService = personService;
        this.mapper = mapper.enable(SerializationFeature.INDENT_OUTPUT);
    }


    @GetMapping("/childAlert")
    public List<PersonWithAgeAndFamilyMembersDTO> getchildrenList(@RequestParam String address) throws MedicalRecordNotFoundException, PersonNotFoundException, JsonProcessingException {
        logger.info("GET /childAlert called to collect children at {}", address);
        List<PersonWithAgeAndFamilyMembersDTO> childrenListResult = this.personService.findChildrenByAddress(address);
        logger.info("Process end successfully with response: {}", mapper.writeValueAsString(childrenListResult));
        return childrenListResult;
    }

    @GetMapping("/phoneAlert")
    public List<String> getPeoplePhoneNumberByFIreStationNumber(@RequestParam(name = "firestation") int station) throws FireStationNotFoundException, JsonProcessingException {
        logger.info("GET /phoneAlert to collect the telephone number of the people concerned by fire station number {}", station);
        List<String> phoneNumberList = this.personService.findPhoneNumberByFireStationNumber(station);
        logger.info("Process end successfully with response: {}", mapper.writeValueAsString(phoneNumberList));
        return phoneNumberList;
    }
}
