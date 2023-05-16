package com.openclassrooms.safetynet.controller;

import com.openclassrooms.safetynet.exception.MailsNotFoundException;
import com.openclassrooms.safetynet.exception.PersonNotFoundException;
import com.openclassrooms.safetynet.model.Person;
import com.openclassrooms.safetynet.repository.PersonRepository;
import com.openclassrooms.safetynet.service.PersonService;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import org.slf4j.Logger;


@RestController
public class PersonController {
    private final Logger logger = LoggerFactory.getLogger(PersonController.class);
    private final PersonRepository personRepository;
    private final PersonService personService;

    public PersonController(PersonRepository personRepository, PersonService personService) {
        this.personRepository = personRepository;
        this.personService = personService;
    }

    @GetMapping("/")
    public List<Person>getPersons() {
        logger.info("Start process to collect all persons");
        return this.personRepository.getPersons();
    }

    @PostMapping("/person")
    public ResponseEntity<Person> addPerson(@RequestBody Person person) {
        logger.info("Start process to add a new person {} {}", person.getFirstName(), person.getLastName());
        this.personService.addPerson(person);
        return new ResponseEntity<>(person, HttpStatus.CREATED);
    }

    @PutMapping("/person")
    public ResponseEntity<Object> updatePerson(@RequestBody Person person) throws PersonNotFoundException {
        logger.info("Start process to update the person with firstname {} and lastname {}", person.getFirstName(), person.getLastName());
        this.personService.updatePerson(person);
        return new ResponseEntity<>(person, HttpStatus.CREATED);
    }

    @DeleteMapping("/person")
    public ResponseEntity<Object> deletePerson(@RequestParam String firstName, @RequestParam String lastName) throws Exception {
        logger.info("Start process to delete the person: {} {}", firstName,lastName);
        this.personService.deletePerson(firstName,lastName);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @GetMapping("/communityEmail")
    public List<String> getCommunityEmail(@RequestParam String city) throws MailsNotFoundException {
        logger.info("Start process to collect all people's emails for the city {}", city);
        return this.personService.getMailsByCity(city);
    }
}
