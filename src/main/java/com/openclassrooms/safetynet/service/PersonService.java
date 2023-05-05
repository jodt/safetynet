package com.openclassrooms.safetynet.service;

import com.openclassrooms.safetynet.model.Person;
import com.openclassrooms.safetynet.repository.PersonRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class PersonService {

    private final PersonRepository personRepository;

    public PersonService(PersonRepository personRepository) {
        this.personRepository = personRepository;
    }


    public List<String> getMailsByCity(String city){
        List<String> mails = new ArrayList<>();
        List<Person> persons = this.personRepository.getPersons();
        for(Person person : persons) {
            if (person.getCity().equals(city)){
                mails.add(person.getEmail());
            }
        }
        return mails;
    }

    public Person addPerson(Person person){
        this.personRepository.addPerson(person);
        return person;
    }

    public Person updatePerson(Person person){
        this.personRepository.updatePerson(person);
        return person;
    }
    public boolean deletePerson(String firstName, String lastName){
        Boolean isDeleted = false;
        Person personFound = personRepository.findPersonByFirstNameAndLastName(firstName,lastName);
        if (personFound != null){
            isDeleted = this.personRepository.deletePerson(personFound);
        }
        return isDeleted;
    }

    public Person findPersonByFirstNameAndLastName(String firstname, String lastName){
        return this.personRepository.findPersonByFirstNameAndLastName(firstname,lastName);
    }
}
