package com.openclassrooms.safetynet.service;

import com.openclassrooms.safetynet.dto.PersonWithAgeDTO;
import com.openclassrooms.safetynet.exception.FireStationNotFoundException;
import com.openclassrooms.safetynet.exception.MailsNotFoundException;
import com.openclassrooms.safetynet.exception.MedicalRecordNotFoundException;
import com.openclassrooms.safetynet.exception.PersonNotFoundException;
import com.openclassrooms.safetynet.model.MedicalRecord;
import com.openclassrooms.safetynet.model.Person;
import com.openclassrooms.safetynet.repository.PersonRepository;
import com.openclassrooms.safetynet.service.Mapper.PersonMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;



@Service
public class PersonService {

    private final Logger logger = LoggerFactory.getLogger(PersonService.class);
    private final PersonRepository personRepository;
    private final MedicalRecordService medicalRecordService;
    private final FireStationService fireStationService;
    private final PersonMapper personMapper;

    public PersonService(PersonRepository personRepository, MedicalRecordService medicalRecordService, FireStationService fireStationService, PersonMapper personMapper) {
        this.personRepository = personRepository;
        this.medicalRecordService = medicalRecordService;
        this.fireStationService = fireStationService;
        this.personMapper = personMapper;
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


    public List<PersonWithAgeDTO> findChildrenByAddress(String address) throws MedicalRecordNotFoundException, PersonNotFoundException {

        List<MedicalRecord> medicalRecordList = new ArrayList<>();
        List<PersonWithAgeDTO> childrenList = new ArrayList<>();

        List<Person> personList = this.findPersonByAddress(address);

        if (!personList.isEmpty()) {
            for (Person person : personList) {
                medicalRecordList.add(this.medicalRecordService.findMedicalRecordByFirstNameAndLastName(person.getFirstName(), person.getLastName()));
            }
        }

        if (!medicalRecordList.isEmpty()) {
            logger.debug("Try to find children at {}", address);
            childrenList = medicalRecordList.stream().
                    map(medicalRecord -> personMapper.asPersonWithAgeDTO(medicalRecord)) // convert person to personWithAgeDTO
                    .filter(personWithAgeDTO -> personWithAgeDTO.getAge() <= 18) // filter people to retrieve only children
                    .collect(Collectors.toList());

            if (!childrenList.isEmpty()){
                logger.debug("Children found, try to add other family members");
                childrenList.forEach(
                        child -> child.setOtherFamilyMembers(
                                personList.stream()
                                        .filter(person -> person.getLastName().equals(child.getLastName()) && !(person.getFirstName().equals(child.getFirstName())))
                                        .collect(Collectors.toList())
                        )
                );
            } else {
                logger.debug("No children found at {}", address);
            }
        }

        return childrenList;
    }


    public List<String> findPhoneNumberByFireStationNumber(int number) throws FireStationNotFoundException {

        List <String> adresses = this.fireStationService.getFireStationByStationNumber(number).stream()
                .map(fireStation -> fireStation.getAddress())//retrieve addresses of fire Stations
                .collect(Collectors.toList());

        logger.debug("Try to retrieve the telephone numbers of the people concerned by the fire station number {}", number);
        List<String> phoneNumber = this.personRepository.getPersons().stream()
                .filter(person -> adresses.stream()
                        .anyMatch(address -> person.getAddress().equals(address))) //filter people based on retrieved addresses
                .map(person -> person.getPhone())//Get people's phone number
                .distinct()
                .collect(Collectors.toList());

        logger.debug("Phone numbers retrieved successfully");
        return phoneNumber;
    }

    private Person findPersonByFirstNameAndLastName(String firstName, String lastName) throws PersonNotFoundException {
        logger.debug("Try to find person with firstname {} and lastname {}", firstName, lastName);
        Person personFound = this.personRepository.findPersonByFirstNameAndLastName(firstName,lastName);
        if(personFound == null){
            logger.error("Person not found with firstname {} and lastname {}", firstName, lastName);
            throw new PersonNotFoundException("Person not found with firstname " + firstName + " and lastname "+ lastName) ;
        }
        logger.debug("Person found with firstname {} and lastname {}", firstName, lastName);
        return personFound;
    }


    private List<Person> findPersonByAddress(String address) throws PersonNotFoundException {
        logger.debug("try to find people at {}", address);
        List<Person> personsFound = this.personRepository.findPersonsByAddress(address);
        if (personsFound.isEmpty()){
            logger.error("nobody found at {}",address);
            throw new PersonNotFoundException("nobody found at " + address);
        }
        logger.debug("People found at {}", address);
        return this.personRepository.findPersonsByAddress(address);
    }

}
