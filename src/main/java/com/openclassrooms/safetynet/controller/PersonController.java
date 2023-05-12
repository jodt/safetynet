package com.openclassrooms.safetynet.controller;

import com.openclassrooms.safetynet.exception.PersonNotFoundException;
import com.openclassrooms.safetynet.model.Person;
import com.openclassrooms.safetynet.repository.PersonRepository;
import com.openclassrooms.safetynet.service.PersonService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.http.HttpResponse;
import java.util.List;

@RestController
public class PersonController {

    private final PersonRepository personRepository;
    private final PersonService personService;

    public PersonController(PersonRepository personRepository, PersonService personService) {
        this.personRepository = personRepository;
        this.personService = personService;
    }

    @GetMapping("/")
    public List<Person>getPersons() throws Exception {
        return this.personRepository.getPersons();
    }

    @PostMapping("/person")
    public ResponseEntity<Person> addPerson(@RequestBody Person person){
        this.personService.addPerson(person);
        return new ResponseEntity<>(person, HttpStatus.CREATED);
    }

    @PutMapping("/person")
    public ResponseEntity<?> updatePerson(@RequestBody Person person) throws PersonNotFoundException {
        this.personService.updatePerson(person);
        return new ResponseEntity<>(person, HttpStatus.CREATED);
    }

    @DeleteMapping("/person")
    public ResponseEntity<String> deletePerson(@RequestParam String firstName, @RequestParam String lastName) throws Exception {
        this.personService.deletePerson(firstName,lastName);
        return new ResponseEntity<>("Person deleted", HttpStatus.NO_CONTENT);
    }

    @GetMapping("/communityEmail")
    public List<String> getCommunityEmail(@RequestParam String city){
        return this.personService.getMailsByCity(city);
    }
}
