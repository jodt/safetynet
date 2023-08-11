package com.openclassrooms.safetynet.service;

import com.openclassrooms.safetynet.dto.*;
import com.openclassrooms.safetynet.exception.*;
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


    /**
     * Get all people
     *
     * @return a list of person
     */
    public List<Person> getAllPerson() {
        return this.personRepository.getPersons();
    }

    public Person addPerson(Person person) throws PersonAlreadyExistException {
        logger.debug("Try to add the person {} {}", person.getFirstName(), person.getLastName());
        Boolean isPersonAlreadyRegistered = this.checkIfPersonAlreadyExist(person);
        if (isPersonAlreadyRegistered) {
            logger.error("{} {} is already registered", person.getFirstName(), person.getLastName());
            throw new PersonAlreadyExistException(person.getFirstName() + " " + person.getLastName() + " is already registered");
        } else {
            this.personRepository.addPerson(person);
            logger.debug("Person successfully added");
            return person;
        }

    }


    /**
     * Update a person
     *
     * @param person
     * @return the updated person
     * @throws PersonNotFoundException if the person is not found
     */
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


    /**
     * Delete a person
     *
     * @param firstName
     * @param lastName
     * @throws PersonNotFoundException if the person is not found
     */
    public void deletePerson(String firstName, String lastName) throws PersonNotFoundException {
        logger.debug("Try to delete the person {} {}", firstName, lastName);
        Person personFound = this.findPersonByFirstNameAndLastName(firstName, lastName);
        logger.debug("Person successfully deleted");
        this.personRepository.deletePerson(personFound);
    }


    /**
     * Method that takes the station number and returns people covered by the fire station
     *
     * @param number
     * @return an object with a list of people with the following information(firstname,lastname,address, phone and
     * age), and the number of children and adults.
     * @throws FireStationNotFoundException if the fire station is not found
     */
    public PersonsConcernedByFireStationDTO findPeopleConcernedByFireStation(int number) throws FireStationNotFoundException {
        AtomicInteger adults = new AtomicInteger(0);
        AtomicInteger children = new AtomicInteger(0);


        List<String> addresses = this.fireStationService.getFireStationByStationNumber(number).stream()
                .map(fireStation -> fireStation.getAddress()) //retrieve addresses covered by the fire station
                .collect(Collectors.toList());

        List<PersonWithAddressAndPhoneDTO> personWithAddressAndPhoneDTOList = this.personRepository.getPersons().stream()
                .filter(person -> addresses.stream()
                        .anyMatch(address -> person.getAddress().equals(address))) //check if the address matches a person's address for each person
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


    /**
     * Method that takes an address and return a list of children with the following
     * information ( firstname, lastname, age and other family members)
     *
     * @param address
     * @return a list of children
     * @throws MedicalRecordNotFoundException if medical record is not found
     * @throws PersonNotFoundException        if person is not found
     */
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
                logger.error("No children found at {}", address);
                throw new PersonNotFoundException("No children found at the address " + address);
            }
        }
        return childrenList;
    }


    /**
     * Method that takes the fire station number and return
     * a list of telephone numbers of people covered by the fire station
     *
     * @param number
     * @return a list of telephone numbers
     * @throws FireStationNotFoundException
     */
    public List<String> findPhoneNumberByFireStationNumber(int number) throws FireStationNotFoundException {

        List<String> addresses = this.fireStationService.getAddressesByStationNumber(number);

        if (addresses.isEmpty()) {
            logger.error("Fire stations not found with the number " + number);
            throw new FireStationNotFoundException("Fire stations not found with the number " + number);
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


    /**
     * Method that takes an address and return an object with a list of people with
     * the following information (lastname, phone, age, medications and allergies), and
     * the number of the fire station for this address.
     *
     * @param address
     * @return a list of person and the number of the fire station
     * @throws PersonNotFoundException
     * @throws FireStationNotFoundException
     */
    public FireDTO findAllPeopleInFireCase(String address) throws PersonNotFoundException, FireStationNotFoundException {
        logger.debug("Try to find People living at {} in fire case", address);
        List<PersonWithMedicalRecordDTO> personsList = this.findPersonByAddress(address).stream()
                .map(person -> createPersonWithMedicalRecordDTO(person))
                .collect(Collectors.toList());

        int firesStationNumber = (this.fireStationService.getFireStationByAddress(address)).getStation();

        return new FireDTO(personsList, firesStationNumber);
    }


    /**
     * Method that takes a list of fire station number and returns a list of people with
     * the following information(lastname, phone, age, medications and allergies) sorted by address
     *
     * @param fireStationsNumber a list of fire station number
     * @return a list of people sorted by address
     * @throws PersonNotFoundException if person is not found
     */
    public Map<String, List<PersonWithMedicalRecordDTO>> findAllPeopleInFloodCase(List<Integer> fireStationsNumber) throws PersonNotFoundException {

        Map<String, List<PersonWithMedicalRecordDTO>> personsListInFloodCaseDTO = new HashMap<>();
        List<String> addresses = fireStationsNumber.stream()
                .map(station -> this.fireStationService.getAddressesByStationNumber(station))
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
            logger.error("Person not found");
            throw new PersonNotFoundException("Person not found");
        }
        return personsListInFloodCaseDTO;
    }


    /**
     * Method that takes a firstname and a lastname and return a person with the following
     * information (lastname, address,age,email,medications,allergies)
     *
     * @param firstName
     * @param lastName
     * @return a person
     * @throws PersonNotFoundException if person not found
     */
    public List<PersonInfoDTO> getPersonInfo(String firstName, String lastName) throws PersonNotFoundException {
        logger.debug("try to find person with firstname {} and lastname {}", firstName, lastName);
        List<PersonInfoDTO> personInfoDTOList;
        personInfoDTOList = this.personRepository.getPersons().stream()
                .filter(person -> (person.getFirstName().toLowerCase()).equals(firstName.toLowerCase()) && (person.getLastName().toLowerCase()).equals(lastName.toLowerCase()))
                .map(person -> createPersonInfoDTO(person))
                .collect(Collectors.toList());
        if (personInfoDTOList.isEmpty()) {
            logger.error("Nobody found with firstname {} and lastname {}", firstName, lastName);
            throw new PersonNotFoundException("Nobody found with firstname " + firstName + " and lastname " + lastName);
        } else {
            logger.debug("Person found successfully");
        }
        return personInfoDTOList;
    }


    /**
     * Method that takes a city and returns a list of email addresses of everyone in the city
     *
     * @param city
     * @return a list of email addresses
     * @throws MailsNotFoundException if mail not found
     */
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


    /**
     * Method that takes firstname and lastname and return a person
     *
     * @param firstName
     * @param lastName
     * @return a person with this firstname and this lastname
     * @throws PersonNotFoundException if person is not found
     */
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


    /**
     * Method that takes an address and returns a list of people living at that address
     *
     * @param address
     * @return a list of person
     * @throws PersonNotFoundException if no one was found
     */
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


    /**
     * Method that takes a person, retrieves the person's medical record, and creates
     * a DTO which is a person with the following information (lastname,phone,age,medications,allergies)
     *
     * @param person
     * @return a PersonWithMedicalRecordDTO
     */
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


    /**
     * Method that takes a person, retrieves the person's medical record, and creates
     * a DTO which is a person with the following information (firstname,lastname,address,phone,age )
     *
     * @param person
     * @return a PersonWithAddressAndPhoneDTO
     */
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


    /**
     * Method that takes a person, retrieves the person's medical record, and creates
     * a DTO which is a person with the following information (lastname,address,age,email,medications,allergies)
     *
     * @param person
     * @return a PersonInfoDTO
     */
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


    /**
     * Method that takes a person and checks if they are already registered
     *
     * @param person
     * @return true if the person is already registered otherwise false
     */
    private Boolean checkIfPersonAlreadyExist(Person person) {
        return this.personRepository.findPersonByFirstNameAndLastName(person.getFirstName(), person.getLastName()) != null;
    }


}
