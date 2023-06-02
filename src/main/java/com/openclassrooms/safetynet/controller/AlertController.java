package com.openclassrooms.safetynet.controller;

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

    public AlertController(PersonService personService) {
        this.personService = personService;
    }


    @GetMapping("/childAlert")
    public List<PersonWithAgeAndFamilyMembersDTO> getchildrenList(@RequestParam String address) throws MedicalRecordNotFoundException, PersonNotFoundException {
        logger.info("Start process to collect children at {}", address);
        List<PersonWithAgeAndFamilyMembersDTO> childrenListResult = this.personService.findChildrenByAddress(address);
        logger.info("Process end successfully");
        return childrenListResult;
    }

    @GetMapping("/phoneAlert")
    public List<String> getPeoplePhoneNumberByFIreStationNumber(@RequestParam (name = "firestation") int station) throws FireStationNotFoundException {
        logger.info("Start process to collect the telephone number of the people concerned by fire station number {}", station);
        List<String> phoneNumberList = this.personService.findPhoneNumberByFireStationNumber(station);
        logger.info("Process end successfully");
        return phoneNumberList;
    }
}
