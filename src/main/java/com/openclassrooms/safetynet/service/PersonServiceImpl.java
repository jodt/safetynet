package com.openclassrooms.safetynet.service;

import com.openclassrooms.safetynet.dto.*;
import com.openclassrooms.safetynet.exception.FireStationNotFoundException;
import com.openclassrooms.safetynet.exception.MailsNotFoundException;
import com.openclassrooms.safetynet.exception.MedicalRecordNotFoundException;
import com.openclassrooms.safetynet.exception.PersonNotFoundException;
import com.openclassrooms.safetynet.model.FireStation;
import com.openclassrooms.safetynet.model.MedicalRecord;
import com.openclassrooms.safetynet.model.Person;
import com.openclassrooms.safetynet.repository.PersonRepository;
import com.openclassrooms.safetynet.service.Mapper.PersonMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;


@Service
public class PersonServiceImpl implements PersonService {

    private final Logger logger = LoggerFactory.getLogger(PersonServiceImpl.class);
    private final PersonRepository personRepository;
    private final MedicalRecordService medicalRecordService;
    private final FireStationService fireStationService;
    private final PersonMapper personMapper;

    public PersonServiceImpl(PersonRepository personRepository, MedicalRecordService medicalRecordService, FireStationService fireStationService, PersonMapper personMapper) {
        this.personRepository = personRepository;
        this.medicalRecordService = medicalRecordService;
        this.fireStationService = fireStationService;
        this.personMapper = personMapper;
    }

    public List<Person> getAllPerson() {
        return this.personRepository.getPersons();
    }

    public Person addPerson(Person person) {
        logger.debug("Try to add the person {} {}", person.getFirstName(), person.getLastName());
        this.personRepository.addPerson(person);
        logger.debug("Person successfully added");
        return person;
    }

    public Person updatePerson(Person person) throws PersonNotFoundException {
        logger.debug("Try to update the person {} {}", person.getFirstName(), person.getLastName());
        Person personToUpdate = this.findPersonByFirstNameAndLastName(person.getFirstName(), person.getLastName());
        personToUpdate.setAddress(person.getAddress());
        personToUpdate.setCity(person.getCity());
        personToUpdate.setPhone(person.getPhone());
        personToUpdate.setZip(person.getZip());
        personToUpdate.setEmail(person.getEmail());
        this.personRepository.updatePerson(personToUpdate);
        logger.debug("Person successfully updated");
        return person;
    }

    public void deletePerson(String firstName, String lastName) throws Exception {
        logger.debug("Try to delete the person {} {}", firstName, lastName);
        Person personFound = this.findPersonByFirstNameAndLastName(firstName, lastName);
        logger.debug("Person successfully deleted");
        this.personRepository.deletePerson(personFound);
    }

    public PersonsConcernedByFireStationDTO findPeopleConcernedByFireStation(int number) throws FireStationNotFoundException {
        AtomicInteger adults = new AtomicInteger(0);
        AtomicInteger children = new AtomicInteger(0);


        List<String> addresses = this.fireStationService.getFireStationByStationNumber(number).stream()
                .map(fireStation -> fireStation.getAddress())//retrieve addresses of fire Stations
                .collect(Collectors.toList());

        List<PersonWithAddressAndPhoneDTO> personWithAddressAndPhoneDTOList = this.personRepository.getPersons().stream()
                .filter(person -> addresses.stream()
                        .anyMatch(address -> person.getAddress().equals(address)))
                .map(person -> createPersonWithAddressAndPhoneDTO(person))
                .collect(Collectors.toList());

        personWithAddressAndPhoneDTOList.forEach(personWithAddressAndPhoneDTO -> {
            if (personWithAddressAndPhoneDTO.getAge() > 18) {
                adults.getAndIncrement();
            } else {
                children.getAndIncrement();
            }
        });

        return new PersonsConcernedByFireStationDTO(personWithAddressAndPhoneDTOList, children, adults);
    }

    public List<PersonWithAgeAndFamilyMembersDTO> findChildrenByAddress(String address) throws MedicalRecordNotFoundException, PersonNotFoundException {

        List<MedicalRecord> medicalRecordList = new ArrayList<>();
        List<PersonWithAgeAndFamilyMembersDTO> childrenList = new ArrayList<>();

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

            if (!childrenList.isEmpty()) {
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

        List<String> addresses = getAddressesByStationNumber(number);

        if (addresses.isEmpty()){
            throw new FireStationNotFoundException("Fire station not found with the number " + number);
        } else {
            logger.debug("Try to retrieve the telephone numbers of the people concerned by the fire station number {}", number);
            List<String> phoneNumber = this.personRepository.getPersons().stream()
                    .filter(person -> addresses.stream()
                            .anyMatch(address -> person.getAddress().equals(address))) //filter people based on retrieved addresses
                    .map(person -> person.getPhone())//Get people's phone number
                    .distinct()
                    .collect(Collectors.toList());

            logger.debug("Phone numbers retrieved successfully");
            return phoneNumber;
        }
    }


    public FireDTO findAllPeopleInFireCase(String address) throws PersonNotFoundException, FireStationNotFoundException {
        logger.debug("Try to find People living at {} in fire case", address);
        List<PersonWithMedicalRecordDTO> personsList = this.findPersonByAddress(address).stream()
                .map(person -> createPersonWithMedicalRecordDTO(person))
                .collect(Collectors.toList());

        int firesStationNumber = (this.fireStationService.getFireStationByAddress(address)).getStation();

        return new FireDTO(personsList, firesStationNumber);
    }

    public Map<String, List<PersonWithMedicalRecordDTO>> findAllPeopleInFloodCase(List<Integer> fireStationsNumber) throws PersonNotFoundException {

        Map<String, List<PersonWithMedicalRecordDTO>> personsListInFloodCaseDTO = new HashMap<>();
        List<String> addresses = fireStationsNumber.stream()
                .map(station -> getAddressesByStationNumber(station))
                .flatMap(addressList -> addressList.stream())
                .collect(Collectors.toList());

        if (!addresses.isEmpty()) {

            for (String address : addresses) {
                List<PersonWithMedicalRecordDTO> personList = this.findPersonByAddress(address).stream()
                        .map(person -> createPersonWithMedicalRecordDTO(person))
                        .collect(Collectors.toList());
                personsListInFloodCaseDTO.put(address, personList);
            }

        }
        if (personsListInFloodCaseDTO.isEmpty()) {
            throw new PersonNotFoundException("Person not found");
        }
        return personsListInFloodCaseDTO;
    }

    public List<PersonInfoDTO> getPersonInfo(String firstName, String lastName) {
        return this.personRepository.getPersons().stream()
                .filter(person -> (person.getFirstName().toLowerCase()).equals(firstName) && (person.getLastName().toLowerCase()).equals(lastName))
                .map(person -> createPersonInfoDTO(person))
                .collect(Collectors.toList());
    }


    public List<String> getMailsByCity(String city) throws MailsNotFoundException {
        logger.debug("try to collect all people's mail of the city: {}", city);
        List<String> mails = this.personRepository.getPersons()
                .stream()
                .filter(person -> person.getCity().equals(city))
                .map(person -> person.getEmail())
                .distinct()
                .collect(Collectors.toList());

        if (mails.isEmpty()) {
            logger.error("No email found for people living in {}", city);
            throw new MailsNotFoundException("No email found for people living in " + city);
        }
        logger.debug("emails successfully collected");
        return mails;
    }


    private Person findPersonByFirstNameAndLastName(String firstName, String lastName) throws PersonNotFoundException {
        logger.debug("Try to find person with firstname {} and lastname {}", firstName, lastName);
        Person personFound = this.personRepository.findPersonByFirstNameAndLastName(firstName, lastName);
        if (personFound == null) {
            logger.error("Person not found with firstname {} and lastname {}", firstName, lastName);
            throw new PersonNotFoundException("Person not found with firstname " + firstName + " and lastname " + lastName);
        }
        logger.debug("Person found with firstname {} and lastname {}", firstName, lastName);
        return personFound;
    }


    private List<Person> findPersonByAddress(String address) throws PersonNotFoundException {
        logger.debug("Try to find people at {}", address);
        List<Person> personsFound = this.personRepository.findPersonsByAddress(address);
        if (personsFound.isEmpty()) {
            logger.error("nobody found at {}", address);
            throw new PersonNotFoundException("nobody found at " + address);
        }
        logger.debug("People found at {}", address);
        return personsFound;
    }

    private List<String> getAddressesByStationNumber(int number) {
        List<String> addresses;
        try {
            addresses = this.fireStationService.getFireStationByStationNumber(number).stream()
                    .map(fireStation -> fireStation.getAddress())//retrieve addresses of fire Stations
                    .collect(Collectors.toList());
            return addresses;
        } catch (FireStationNotFoundException e) {
            addresses = new ArrayList<>();
        }
        return addresses;
    }

    private PersonWithMedicalRecordDTO createPersonWithMedicalRecordDTO(Person person) {
        MedicalRecord medicalRecord;
        try {
            medicalRecord = this.medicalRecordService.findMedicalRecordByFirstNameAndLastName(person.getFirstName(), person.getLastName());
            return this.personMapper.asPersonWithMedicalRecordDTO(person, medicalRecord);
        } catch (MedicalRecordNotFoundException e) {
            logger.error("Not medical Record Found for {} {}", person.getFirstName(), person.getLastName());
            return null;
        }
    }

    private PersonWithAddressAndPhoneDTO createPersonWithAddressAndPhoneDTO(Person person) {
        MedicalRecord medicalRecord;
        try {
            medicalRecord = this.medicalRecordService.findMedicalRecordByFirstNameAndLastName(person.getFirstName(), person.getLastName());
            return this.personMapper.asPersonWithAddressAndPhoneDTO(person, medicalRecord);
        } catch (MedicalRecordNotFoundException e) {
            logger.error("Not medical Record Found for {} {}", person.getFirstName(), person.getLastName());
            return null;
        }
    }

    private PersonInfoDTO createPersonInfoDTO(Person person) {
        MedicalRecord medicalRecord;
        try {
            medicalRecord = this.medicalRecordService.findMedicalRecordByFirstNameAndLastName(person.getFirstName(), person.getLastName());
            return this.personMapper.asPersonInfoDTO(person, medicalRecord);
        } catch (MedicalRecordNotFoundException e) {
            logger.error("Not medical Record Found for {} {}", person.getFirstName(), person.getLastName());
            return null;
        }
    }


}
