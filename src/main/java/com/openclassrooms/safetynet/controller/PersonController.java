package com.openclassrooms.safetynet.controller;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.openclassrooms.safetynet.model.Person;
import com.openclassrooms.safetynet.repository.PersonRepository;
import com.openclassrooms.safetynet.service.PersonService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.http.HttpResponse;

@RestController
public class PersonController {

    private final PersonRepository personRepository;
    private final PersonService personService;

    public PersonController(PersonRepository personRepository, PersonService personService) {
        this.personRepository = personRepository;
        this.personService = personService;
    }

    @GetMapping("/")
    public String getPersons(){
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        String result = gson.toJson(personRepository.getPersons());
        return result;
    }

    @PostMapping("/person")
    public ResponseEntity<Person> addPerson(@RequestBody Person person){
        this.personService.addPerson(person);
        return new ResponseEntity<>(person, HttpStatus.CREATED);
    }

    @PutMapping("/person")
    public ResponseEntity<?> updatePerson(@RequestBody Person person){
        Person updatePerson = this.personService.findPersonByFirstNameAndLastName(person.getFirstName(),person.getLastName());
        if (updatePerson != null){
            this.personService.updatePerson(person);
            return new ResponseEntity<>(person, HttpStatus.CREATED);
        } else {
            return new ResponseEntity<>("Personne non trouvée", HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/person")
    public ResponseEntity<String> deletePerson(@RequestParam String firstName, @RequestParam String lastName){
        boolean isDeleted = false;
        isDeleted = this.personService.deletePerson(firstName,lastName);
        if (isDeleted){
            return new ResponseEntity<>("Person deleted", HttpStatus.NO_CONTENT);
        } else {
            return new ResponseEntity<>(firstName + " " +lastName + " Not Found", HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/communityEmail")
    public ResponseEntity<String> getCommunityEmail(@RequestParam String city){
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        return new ResponseEntity<>(gson.toJson(personService.getMailsByCity(city)), HttpStatus.OK) ;
    }
}
