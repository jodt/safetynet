package com.openclassrooms.safetynet.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.openclassrooms.safetynet.exception.PersonAlreadyExistException;
import com.openclassrooms.safetynet.exception.PersonNotFoundException;
import com.openclassrooms.safetynet.model.Person;
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
    private final ObjectMapper mapper;


    public PersonController(PersonService personService, ObjectMapper objectMapper) {
        this.personService = personService;
        this.mapper = objectMapper.enable(SerializationFeature.INDENT_OUTPUT);
    }


    @GetMapping("/person/all")
    public List<Person> getPersons() throws JsonProcessingException {
        logger.info("GET /person/all called");
        List<Person> response = this.personService.getAllPerson();
        logger.info("Process end successfully with response: {}", mapper.writeValueAsString(response));
        return response;
    }


    @PostMapping("/person")
    public ResponseEntity<Person> addPerson(@RequestBody Person person) throws PersonAlreadyExistException, JsonProcessingException {
        logger.info("POST /person called to add a new person {} {}", person.getFirstName(), person.getLastName());
        this.personService.addPerson(person);
        logger.info("Process end successfully with response: {}", mapper.writeValueAsString(person));
        return new ResponseEntity<>(person, HttpStatus.CREATED);
    }


    @PutMapping("/person")
    public ResponseEntity<Object> updatePerson(@RequestBody Person person) throws PersonNotFoundException, JsonProcessingException {
        logger.info("PUT /person called to update the person with firstname {} and lastname {}", person.getFirstName(), person.getLastName());
        this.personService.updatePerson(person);
        logger.info("Process end successfully with response: {}", mapper.writeValueAsString(person));
        return new ResponseEntity<>(person, HttpStatus.OK);
    }


    @DeleteMapping("/person")
    public ResponseEntity<Object> deletePerson(@RequestParam String firstName, @RequestParam String lastName) throws Exception {
        logger.info("DELETE /person called to delete the person: {} {}", firstName, lastName);
        this.personService.deletePerson(firstName, lastName);
        logger.info("Process end successfully");
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }


}
