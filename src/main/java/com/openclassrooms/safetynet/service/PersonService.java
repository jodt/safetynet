package com.openclassrooms.safetynet.service;

import com.openclassrooms.safetynet.dto.*;
import com.openclassrooms.safetynet.exception.FireStationNotFoundException;
import com.openclassrooms.safetynet.exception.MailsNotFoundException;
import com.openclassrooms.safetynet.exception.MedicalRecordNotFoundException;
import com.openclassrooms.safetynet.exception.PersonNotFoundException;
import com.openclassrooms.safetynet.model.Person;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;


@Service
public interface PersonService {

    public List<Person> getAllPerson();

    public Person addPerson(Person person);

    public Person updatePerson(Person person) throws PersonNotFoundException;

    public void deletePerson(String firstName, String lastName) throws Exception;

    public PersonsConcernedByFireStationDTO findPeopleConcernedByFireStation(int number) throws FireStationNotFoundException;

    public List<PersonWithAgeAndFamilyMembersDTO> findChildrenByAddress(String address) throws MedicalRecordNotFoundException, PersonNotFoundException;

    public List<String> findPhoneNumberByFireStationNumber(int number) throws FireStationNotFoundException;

    public FireDTO findAllPeopleInFireCase(String address) throws PersonNotFoundException, FireStationNotFoundException;

    public Map<String, List<PersonWithMedicalRecordDTO>> findAllPeopleInFloodCase(List<Integer> fireStationsNumber) throws PersonNotFoundException;

    public List<PersonInfoDTO> getPersonInfo(String firstName, String lastName);

    public List<String> getMailsByCity(String city) throws MailsNotFoundException;


}
