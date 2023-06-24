package com.openclassrooms.safetynet.controller;

import com.openclassrooms.safetynet.dto.PersonsConcernedByFireStationDTO;
import com.openclassrooms.safetynet.exception.FireStationNotFoundException;
import com.openclassrooms.safetynet.exception.MailsNotFoundException;
import com.openclassrooms.safetynet.exception.PersonNotFoundException;
import com.openclassrooms.safetynet.model.Person;
import com.openclassrooms.safetynet.repository.PersonRepository;
import com.openclassrooms.safetynet.service.PersonService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
public class PersonController {
    private final Logger logger = LoggerFactory.getLogger(PersonController.class);
    private final PersonService personService;


    public PersonController(PersonRepository personRepository, PersonService personService) {
        this.personService = personService;
    }

    @GetMapping("/person/all")
    public List<Person> getPersons() {
        logger.info("Start process to collect all persons");
        return this.personService.getAllPerson();
    }

    @PostMapping("/person")
    public ResponseEntity<Person> addPerson(@RequestBody Person person) {
        logger.info("Start process to add a new person {} {}", person.getFirstName(), person.getLastName());
        this.personService.addPerson(person);
        logger.info("Process end successfully");
        return new ResponseEntity<>(person, HttpStatus.CREATED);
    }

    @PutMapping("/person")
    public ResponseEntity<Object> updatePerson(@RequestBody Person person) throws PersonNotFoundException {
        logger.info("Start process to update the person with firstname {} and lastname {}", person.getFirstName(), person.getLastName());
        this.personService.updatePerson(person);
        logger.info("Process end successfully");
        return new ResponseEntity<>(person, HttpStatus.OK);
    }

    @DeleteMapping("/person")
    public ResponseEntity<Object> deletePerson(@RequestParam String firstName, @RequestParam String lastName) throws Exception {
        logger.info("Start process to delete the person: {} {}", firstName, lastName);
        this.personService.deletePerson(firstName, lastName);
        logger.info("Process end successfully");
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @GetMapping("/communityEmail")
    public List<String> getCommunityEmail(@RequestParam String city) throws MailsNotFoundException {
        logger.info("Start process to collect all people's emails for the city {}", city);
        return this.personService.getMailsByCity(city);
    }

    @GetMapping("/firestation")
    public PersonsConcernedByFireStationDTO getPeopleConcernedByFiresStation(@RequestParam int stationNumber) throws FireStationNotFoundException {
        logger.info("Start process to get people concerned by the fire station number {} ", stationNumber);
        PersonsConcernedByFireStationDTO personsConcernedByFireStation = this.personService.findPeopleConcernedByFireStation(stationNumber);
        logger.info("Process end successfully");
        return personsConcernedByFireStation;
    }


}
