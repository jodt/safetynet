package com.openclassrooms.safetynet.service;

import com.openclassrooms.safetynet.exception.PersonNotFoundException;
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

    public Person updatePerson(Person person) throws PersonNotFoundException {
        this.findPersonByFirstNameAndLastName(person.getFirstName(),person.getLastName());
        this.personRepository.updatePerson(person);
        return person;
    }
    public void deletePerson(String firstName, String lastName) throws Exception {
        Person personFound = this.findPersonByFirstNameAndLastName(firstName,lastName);
        this.personRepository.deletePerson(personFound);
    }

    private Person findPersonByFirstNameAndLastName(String firstName, String lastName) throws PersonNotFoundException {
        Person personFound = this.personRepository.findPersonByFirstNameAndLastName(firstName,lastName);
        if(personFound == null){
            throw new PersonNotFoundException("Personne non trouvée avec le prénom " + firstName + " et le nom "+ lastName) ;
        }
        return personFound;
    }
}
