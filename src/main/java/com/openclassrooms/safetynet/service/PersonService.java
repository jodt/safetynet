package com.openclassrooms.safetynet.service;

import com.openclassrooms.safetynet.exception.PersonNotFoundException;
import com.openclassrooms.safetynet.exception.MailsNotFoundException;
import com.openclassrooms.safetynet.model.Person;
import com.openclassrooms.safetynet.repository.PersonRepository;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import org.slf4j.Logger;

@Service
public class PersonService {

    private final Logger logger = LoggerFactory.getLogger(PersonService.class);
    private final PersonRepository personRepository;

    public PersonService(PersonRepository personRepository) {
        this.personRepository = personRepository;
    }


    public List<String> getMailsByCity(String city) throws MailsNotFoundException {
        logger.debug("try to collect all people's mail of the city: {}", city);
        List<String> mails = new ArrayList<>();
        List<Person> persons = this.personRepository.getPersons();
        for(Person person : persons) {
            if (person.getCity().equals(city)){
                mails.add(person.getEmail());
            }
        }
        if(mails.isEmpty()){
            logger.error("No email found for people living in {}", city);
            throw new MailsNotFoundException("Aucun mail trouvé pour la ville " + city);
        }
        logger.debug("emails successfully collected");
        return mails;
    }

    public Person addPerson(Person person){
        logger.debug("Try to add the person {} {}", person.getFirstName(), person.getLastName());
        this.personRepository.addPerson(person);
        logger.debug("Person successfully added");
        return person;
    }

    public Person updatePerson(Person person) throws PersonNotFoundException {
        logger.debug("Try to update the person {} {}", person.getFirstName(), person.getLastName());
        this.findPersonByFirstNameAndLastName(person.getFirstName(),person.getLastName());
        this.personRepository.updatePerson(person);
        logger.debug("Person successfully updated");
        return person;
    }
    public void deletePerson(String firstName, String lastName) throws Exception {
        logger.debug("Try to delete the person {} {}", firstName,lastName);
        Person personFound = this.findPersonByFirstNameAndLastName(firstName,lastName);
        logger.debug("Person successfully deleted");
        this.personRepository.deletePerson(personFound);
    }

    private Person findPersonByFirstNameAndLastName(String firstName, String lastName) throws PersonNotFoundException {
        logger.debug("Try to find person with firstname {} and lastname {}", firstName, lastName);
        Person personFound = this.personRepository.findPersonByFirstNameAndLastName(firstName,lastName);
        if(personFound == null){
            logger.error("Person not found with firstname {} and lastname {}", firstName, lastName);
            throw new PersonNotFoundException("Personne non trouvée avec le prénom " + firstName + " et le nom "+ lastName) ;
        }
        logger.debug("Person found with firstname {} and lastname {}", firstName, lastName);
        return personFound;
    }
}
