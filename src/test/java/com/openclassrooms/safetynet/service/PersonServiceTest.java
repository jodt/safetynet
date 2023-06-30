package com.openclassrooms.safetynet.service;

import com.openclassrooms.safetynet.dto.*;
import com.openclassrooms.safetynet.exception.*;
import com.openclassrooms.safetynet.model.FireStation;
import com.openclassrooms.safetynet.model.MedicalRecord;
import com.openclassrooms.safetynet.model.Person;
import com.openclassrooms.safetynet.repository.PersonRepository;
import com.openclassrooms.safetynet.service.Mapper.PersonMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PersonServiceTest {

    @Mock
    PersonRepository personRepository;
    @Mock
    MedicalRecordService medicalRecordService;
    @Mock
    FireStationService fireStationService;
    @Mock
    PersonMapper personMapper;

    @InjectMocks
    PersonServiceImpl personServiceImpl;

    private Person person1;
    private Person person2;
    private Person person3;
    private Person child;
    private Person personToUpdate;
    private Person updatedPerson;
    private MedicalRecord medicalRecord1;
    private MedicalRecord medicalRecord2;
    private MedicalRecord medicalRecord3;
    private MedicalRecord childMedicalRecord;
    private FireStation fireStation;
    private FireStation fireStation2;


    @BeforeEach
    public void init() {

        person1 = Person.builder()
                .firstName("firstname1")
                .lastName("lastname1")
                .address("person address 1")
                .city("city")
                .zip(59000)
                .phone("001-001-001")
                .email("person1@mail.com")
                .build();

        person2 = Person.builder()
                .firstName("firstname2")
                .lastName("lastname2")
                .address("person address 2")
                .city("city")
                .zip(59000)
                .phone("001-001-002")
                .email("person2@mail.com")
                .build();

        person3 = Person.builder()
                .firstName("firstname3")
                .lastName("lastname3")
                .address("person address 1")
                .city("city")
                .zip(59000)
                .phone("001-001-004")
                .email("person3@mail.com")
                .build();

        child = Person.builder()
                .firstName("firstnameChild1")
                .lastName("lastname1")
                .address("person address 1")
                .city("city")
                .zip(59000)
                .phone("001-001-003")
                .email("person1@mail.com")
                .build();

        personToUpdate = Person.builder()
                .firstName("firstname")
                .lastName("lastname")
                .address("person address")
                .city("city")
                .zip(59000)
                .phone("001-001-001")
                .email("person@mail.com")
                .build();

        updatedPerson = Person.builder()
                .firstName("firstname")
                .lastName("lastname")
                .address("person address")
                .city("city")
                .zip(59500)
                .phone("001-001-000")
                .email("person@mail.com")
                .build();

        medicalRecord1 = MedicalRecord.builder()
                .firstName("firstname1")
                .lastName("lastname1")
                .birthdate(LocalDate.of(2000, 01, 01))
                .medications(List.of("dodoxadin:30mg"))
                .allergies(List.of("peanut"))
                .build();

        medicalRecord2 = MedicalRecord.builder()
                .firstName("firstname2")
                .lastName("lastname2")
                .birthdate(LocalDate.of(2000, 12, 12))
                .medications(List.of("dodoxadin:630mg"))
                .allergies(List.of("shellfish"))
                .build();

        medicalRecord3 = MedicalRecord.builder()
                .firstName("firstname3")
                .lastName("lastname3")
                .birthdate(LocalDate.of(2001, 01, 01))
                .medications(List.of("hydrapermazol:300mg"))
                .allergies(List.of("aznol"))
                .build();

        childMedicalRecord = MedicalRecord.builder()
                .firstName("firstnameChild1")
                .lastName("lastname1")
                .birthdate(LocalDate.of(2008, 01, 01))
                .medications(List.of("ibupurin:200mg"))
                .allergies(List.of("illisoxian"))
                .build();

        fireStation = FireStation.builder()
                .station(1)
                .address("person address 1")
                .build();

        fireStation2 = FireStation.builder()
                .station(2)
                .address("person address 2")
                .build();

    }


    @DisplayName("Should get all people")
    @Test
    void shouldGetAllPerson() {

        List<Person> personList = List.of(person1, person2, person3, child);

        when(this.personRepository.getPersons()).thenReturn(personList);

        List<Person> result = this.personServiceImpl.getAllPerson();

        assertNotNull(result);
        assertEquals(4, result.size());
        assertEquals(person1, result.get(0));
        assertEquals(person2, result.get(1));
        assertEquals(person3, result.get(2));
        assertEquals(child, result.get(3));

        verify(this.personRepository, times(1)).getPersons();

    }


    @DisplayName("Should add a person")
    @Test
    void shouldAddPerson() throws PersonAlreadyExistException {

        when(this.personRepository.addPerson(any(Person.class))).thenReturn(person1);

        Person result = this.personServiceImpl.addPerson(person1);

        assertNotNull(result);
        assertEquals(person1, result);

        verify(this.personRepository, times(1)).addPerson(any(Person.class));

    }


    @DisplayName("Should not add a person -> person already exist")
    @Test
    void shouldNotAddPerson() throws PersonAlreadyExistException {

        when(this.personRepository.findPersonByFirstNameAndLastName(anyString(), anyString())).thenReturn(person1);

        assertThrows(PersonAlreadyExistException.class, () -> this.personServiceImpl.addPerson(person1));

        verify(this.personRepository, times(1)).findPersonByFirstNameAndLastName(anyString(), anyString());
        verify(this.personRepository, never()).addPerson(any(Person.class));

    }


    @DisplayName("Should update a person")
    @Test
    void updatePerson() throws PersonNotFoundException {

        when(this.personRepository.findPersonByFirstNameAndLastName(anyString(), anyString())).thenReturn(personToUpdate);
        when(this.personRepository.updatePerson(any(Person.class))).thenReturn(updatedPerson);

        Person result = this.personServiceImpl.updatePerson(updatedPerson);

        assertNotNull(result);
        assertEquals(updatedPerson, result);

        verify(this.personRepository, times(1)).findPersonByFirstNameAndLastName(anyString(), anyString());
        verify(this.personRepository, times(1)).updatePerson(any(Person.class));

    }

    @DisplayName("Should not update a person -> person not found")
    @Test
    void shouldNotUpdatePerson() throws PersonNotFoundException {

        when(this.personRepository.findPersonByFirstNameAndLastName(anyString(), anyString())).thenReturn(null);

        Exception exception = assertThrows(PersonNotFoundException.class, () -> this.personServiceImpl.updatePerson(updatedPerson));

        assertEquals("Person not found with firstname firstname and lastname lastname", exception.getMessage());

        verify(this.personRepository, times(1)).findPersonByFirstNameAndLastName(anyString(), anyString());
        verify(this.personRepository, never()).updatePerson(any(Person.class));

    }


    @DisplayName("Should delete a person")
    @Test
    void deletePerson() throws Exception {

        when(this.personRepository.findPersonByFirstNameAndLastName(anyString(), anyString())).thenReturn(person1);
        doNothing().when(this.personRepository).deletePerson(person1);

        this.personServiceImpl.deletePerson("firstname", "lastname");

        verify(this.personRepository, times(1)).deletePerson(any(Person.class));

    }

    @DisplayName("Should get people concerned by a fire station with the number of adults and children")
    @Test
    void shouldFindPeopleConcernedByFireStation() throws FireStationNotFoundException, MedicalRecordNotFoundException {

        PersonWithAddressAndPhoneDTO person1DTO = PersonWithAddressAndPhoneDTO.builder()
                .firstName("firstname1")
                .lastName("lastname1")
                .address("person address 1")
                .phone("001-001-001")
                .age(22)
                .build();

        PersonWithAddressAndPhoneDTO childDTO = PersonWithAddressAndPhoneDTO.builder()
                .firstName("firstnameChild1")
                .lastName("lastname1")
                .address("person address 1")
                .phone("001-001-003")
                .age(5)
                .build();

        when(this.fireStationService.getFireStationByStationNumber(anyInt())).thenReturn(List.of(fireStation));
        when(this.personRepository.getPersons()).thenReturn(List.of(person1, person2, child));
        when(this.medicalRecordService.findMedicalRecordByFirstNameAndLastName(person1.getFirstName(), person1.getLastName())).thenReturn(medicalRecord1);
        when(this.medicalRecordService.findMedicalRecordByFirstNameAndLastName(child.getFirstName(), child.getLastName())).thenReturn(childMedicalRecord);
        when(this.personMapper.asPersonWithAddressAndPhoneDTO(person1, medicalRecord1)).thenReturn(person1DTO);
        when(this.personMapper.asPersonWithAddressAndPhoneDTO(child, childMedicalRecord)).thenReturn(childDTO);

        PersonsConcernedByFireStationDTO result = this.personServiceImpl.findPeopleConcernedByFireStation(1);

        assertNotNull(result);
        assertEquals(2, result.getPersonWithAddressAndPhoneDTOList().size());
        assertEquals(person1DTO, result.getPersonWithAddressAndPhoneDTOList().get(0));
        assertEquals(childDTO, result.getPersonWithAddressAndPhoneDTOList().get(1));
        assertEquals(1, result.getAdults().intValue());
        assertEquals(1, result.getChildren().intValue());

        verify(this.fireStationService, times(1)).getFireStationByStationNumber(anyInt());
        verify(this.personRepository, times(1)).getPersons();
        verify(this.medicalRecordService, times(2)).findMedicalRecordByFirstNameAndLastName(anyString(), anyString());
        verify(this.personMapper, times(2)).asPersonWithAddressAndPhoneDTO(any(Person.class), any(MedicalRecord.class));

    }

    @DisplayName("Should return children by address")
    @Test
    void ShouldFindChildrenByAddress() throws MedicalRecordNotFoundException, PersonNotFoundException {

        PersonWithAgeAndFamilyMembersDTO adultDTO = PersonWithAgeAndFamilyMembersDTO.builder()
                .firstName("firstname1")
                .lastName("lastname1")
                .age(23)
                .otherFamilyMembers(List.of(child))
                .build();

        PersonWithAgeAndFamilyMembersDTO childDTO = PersonWithAgeAndFamilyMembersDTO.builder()
                .firstName("firstnameChild1")
                .lastName("lastname1")
                .age(5)
                .otherFamilyMembers(List.of(person1))
                .build();

        MedicalRecord childMedicalRecord = MedicalRecord.builder()
                .firstName("firstnameChild1")
                .lastName("lastname1")
                .birthdate(LocalDate.of(2018, 01, 01))
                .medications(List.of("aznol:350mg"))
                .allergies(List.of("peanut"))
                .build();


        when(this.personRepository.findPersonsByAddress(anyString())).thenReturn(List.of(person1, child));
        when(this.medicalRecordService.findMedicalRecordByFirstNameAndLastName(person1.getFirstName(), person1.getLastName())).thenReturn(medicalRecord1);
        when(this.medicalRecordService.findMedicalRecordByFirstNameAndLastName(child.getFirstName(), child.getLastName())).thenReturn(childMedicalRecord);
        when(this.personMapper.asPersonWithAgeDTO(medicalRecord1)).thenReturn(adultDTO);
        when(this.personMapper.asPersonWithAgeDTO(childMedicalRecord)).thenReturn(childDTO);


        List<PersonWithAgeAndFamilyMembersDTO> result = this.personServiceImpl.findChildrenByAddress("person address 1");

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(childDTO, result.get(0));

        verify(personRepository, times(1)).findPersonsByAddress(anyString());
        verify(this.medicalRecordService, times(2)).findMedicalRecordByFirstNameAndLastName(anyString(), anyString());
        verify(this.personMapper, times(2)).asPersonWithAgeDTO(any(MedicalRecord.class));

    }

    @DisplayName("Should get all phone number of people concerned by a fire Station")
    @Test
    void ShouldFindPhoneNumberByFireStationNumber() throws FireStationNotFoundException {

        FireStation fireStation = FireStation.builder()
                .station(1)
                .address("person address 1")
                .build();

        when(this.fireStationService.getFireStationByStationNumber(any(Integer.class))).thenReturn(List.of(fireStation));
        when(this.personRepository.getPersons()).thenReturn(List.of(person1, person2, child));

        List<String> result = this.personServiceImpl.findPhoneNumberByFireStationNumber(1);

        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("001-001-001", result.get(0));
        assertEquals("001-001-003", result.get(1));

        verify(this.fireStationService, times(1)).getFireStationByStationNumber(any(Integer.class));
        verify(this.personRepository, times(1)).getPersons();
    }


    @DisplayName("Should not get all phone number of people concerned by a fire Station")
    @Test
    void ShouldNotFindPhoneNumberByFireStationNumber() throws FireStationNotFoundException {

        FireStation fireStation = FireStation.builder()
                .station(1)
                .address("person address 1")
                .build();

        when(this.fireStationService.getFireStationByStationNumber(any(Integer.class))).thenThrow(FireStationNotFoundException.class);

        Exception exception = assertThrows(FireStationNotFoundException.class, () -> this.personServiceImpl.findPhoneNumberByFireStationNumber(1));

        assertEquals("Fire stations not found with the number 1", exception.getMessage());

        verify(this.fireStationService, times(1)).getFireStationByStationNumber(any(Integer.class));
        verify(this.personRepository, never()).getPersons();
    }


    @DisplayName("Should get people list and number of the station in Fire Case")
    @Test
    void findAllPeopleInFireCase() throws MedicalRecordNotFoundException, FireStationNotFoundException, PersonNotFoundException {

        PersonWithMedicalRecordDTO person1DTO = PersonWithMedicalRecordDTO.builder()
                .lastName("lastname1")
                .phone("001-001-001")
                .age(23)
                .medications(List.of("dodoxadin:30mg"))
                .allergies(List.of("peanut"))
                .build();

        PersonWithMedicalRecordDTO person3DTO = PersonWithMedicalRecordDTO.builder()
                .lastName("lastname3")
                .phone("001-001-004")
                .age(22)
                .medications(List.of("hydrapermazol:300mg"))
                .allergies(List.of("aznolm"))
                .build();

        when(this.personRepository.findPersonsByAddress(anyString())).thenReturn(List.of(person1, person3));
        when(this.medicalRecordService.findMedicalRecordByFirstNameAndLastName("firstname1", "lastname1")).thenReturn(medicalRecord1);
        when(this.medicalRecordService.findMedicalRecordByFirstNameAndLastName("firstname3", "lastname3")).thenReturn(medicalRecord3);
        when(this.personMapper.asPersonWithMedicalRecordDTO(person1, medicalRecord1)).thenReturn(person1DTO);
        when(this.personMapper.asPersonWithMedicalRecordDTO(person3, medicalRecord3)).thenReturn(person3DTO);
        when(this.fireStationService.getFireStationByAddress(anyString())).thenReturn(fireStation);

        FireDTO result = this.personServiceImpl.findAllPeopleInFireCase("person address 1");

        assertNotNull(result);
        assertEquals(2, result.getPersonsListInFireCase().size());
        assertEquals(person1DTO, result.getPersonsListInFireCase().get(0));
        assertEquals(person3DTO, result.getPersonsListInFireCase().get(1));
        assertEquals(1, result.getFireStationNumber());

        verify(this.personRepository, times(1)).findPersonsByAddress(anyString());
        verify(this.medicalRecordService, times(2)).findMedicalRecordByFirstNameAndLastName(anyString(), anyString());
        verify(this.personMapper, times(2)).asPersonWithMedicalRecordDTO(any(Person.class), any(MedicalRecord.class));
        verify(this.fireStationService, times(1)).getFireStationByAddress(anyString());

    }


    @DisplayName("Should not get people list and number of the station in Fire Case -> persons not found")
    @Test
    void shouldNotFindAllPeopleInFireCase() throws MedicalRecordNotFoundException, FireStationNotFoundException, PersonNotFoundException {


        when(this.personRepository.findPersonsByAddress(anyString())).thenReturn(List.of());

        Exception exception = assertThrows(PersonNotFoundException.class, () -> this.personServiceImpl.findAllPeopleInFireCase("person address 1"));

        assertEquals("nobody found at person address 1", exception.getMessage());

        verify(this.personRepository, times(1)).findPersonsByAddress(anyString());
        verify(this.medicalRecordService, never()).findMedicalRecordByFirstNameAndLastName(anyString(), anyString());
        verify(this.personMapper, never()).asPersonWithMedicalRecordDTO(any(Person.class), any(MedicalRecord.class));
        verify(this.fireStationService, never()).getFireStationByAddress(anyString());

    }

    @DisplayName("should get people grouped by addresses")
    @Test
    void shouldFindAllPeopleInFloodCase() throws FireStationNotFoundException, PersonNotFoundException, MedicalRecordNotFoundException {
        List<Integer> stationNumberList = List.of(1, 2);

        PersonWithMedicalRecordDTO person1DTO = PersonWithMedicalRecordDTO.builder()
                .lastName("lastname1")
                .phone("001-001-001")
                .age(23)
                .medications(List.of("dodoxadin:30mg"))
                .allergies(List.of("peanut"))
                .build();

        PersonWithMedicalRecordDTO person2DTO = PersonWithMedicalRecordDTO.builder()
                .lastName("lastname2")
                .phone("001-001-002")
                .age(22)
                .medications(List.of("hydrapermazol:300mg"))
                .allergies(List.of("aznolm"))
                .build();

        when(this.fireStationService.getFireStationByStationNumber(1)).thenReturn(List.of(fireStation));
        when(this.fireStationService.getFireStationByStationNumber(2)).thenReturn(List.of(fireStation2));
        when(this.personRepository.findPersonsByAddress("person address 1")).thenReturn(List.of(person1));
        when(this.personRepository.findPersonsByAddress("person address 2")).thenReturn(List.of(person2));
        when(this.medicalRecordService.findMedicalRecordByFirstNameAndLastName(person1.getFirstName(), person1.getLastName())).thenReturn(medicalRecord1);
        when(this.medicalRecordService.findMedicalRecordByFirstNameAndLastName(person2.getFirstName(), person2.getLastName())).thenReturn(medicalRecord2);
        when(this.personMapper.asPersonWithMedicalRecordDTO(person1, medicalRecord1)).thenReturn(person1DTO);
        when(this.personMapper.asPersonWithMedicalRecordDTO(person2, medicalRecord2)).thenReturn(person2DTO);

        Map<String, List<PersonWithMedicalRecordDTO>> result = personServiceImpl.findAllPeopleInFloodCase(stationNumberList);


        assertNotNull(result);
        assertTrue(result.containsKey("person address 1"));
        assertTrue(result.containsKey("person address 2"));
        assertEquals(1, result.get("person address 1").size());
        assertEquals(1, result.get("person address 2").size());
        assertEquals(person1DTO, result.get("person address 1").get(0));
        assertEquals(person2DTO, result.get("person address 2").get(0));

        verify(this.fireStationService, times(2)).getFireStationByStationNumber(anyInt());
        verify(this.personRepository, times(2)).findPersonsByAddress(anyString());
        verify(this.medicalRecordService, times(2)).findMedicalRecordByFirstNameAndLastName(anyString(), anyString());
        verify(this.personMapper, times(2)).asPersonWithMedicalRecordDTO(any(Person.class), any(MedicalRecord.class));

    }


    @DisplayName("should get people grouped by addresses -> person not found")
    @Test
    void shouldNotFindAllPeopleInFloodCase() throws FireStationNotFoundException {
        List<Integer> stationNumberList = List.of(1, 2);

        when(this.fireStationService.getFireStationByStationNumber(1)).thenThrow(FireStationNotFoundException.class);
        when(this.fireStationService.getFireStationByStationNumber(2)).thenThrow(FireStationNotFoundException.class);

        assertThrows(PersonNotFoundException.class, () -> personServiceImpl.findAllPeopleInFloodCase(stationNumberList));

        verify(this.fireStationService, times(2)).getFireStationByStationNumber(anyInt());

    }


    @DisplayName("Should get person information")
    @Test
    void shouldGetPersonInfo() throws MedicalRecordNotFoundException, PersonNotFoundException {

        PersonInfoDTO personInfoDTO1 = PersonInfoDTO.builder()
                .lastName("lastname1")
                .address("person address 1")
                .age(23)
                .email("person1@mail.com")
                .medications(List.of("dodoxadin:30mg"))
                .allergies(List.of("peanut"))
                .build();

        PersonInfoDTO personInfoDTO2 = PersonInfoDTO.builder()
                .lastName("lastname2")
                .address("person address 2")
                .age(22)
                .email("person2@mail.com")
                .medications(List.of("dodoxadin:630mg"))
                .allergies(List.of("shellfish"))
                .build();

        when(this.personRepository.getPersons()).thenReturn(List.of(person1, person2));
        when(this.medicalRecordService.findMedicalRecordByFirstNameAndLastName("firstname2", "lastname2")).thenReturn(medicalRecord2);
        when(this.personMapper.asPersonInfoDTO(any(Person.class), any(MedicalRecord.class))).thenReturn(personInfoDTO2);

        List<PersonInfoDTO> result = this.personServiceImpl.getPersonInfo("firstname2", "lastname2");

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(personInfoDTO2, result.get(0));
    }

    @DisplayName("Should not get person information -> person not found")
    @Test
    void shouldNotGetPersonInfo() throws MedicalRecordNotFoundException {

        PersonInfoDTO personInfoDTO1 = PersonInfoDTO.builder()
                .lastName("lastname1")
                .address("person address 1")
                .age(23)
                .email("person1@mail.com")
                .medications(List.of("dodoxadin:30mg"))
                .allergies(List.of("peanut"))
                .build();

        PersonInfoDTO personInfoDTO2 = PersonInfoDTO.builder()
                .lastName("lastname2")
                .address("person address 2")
                .age(22)
                .email("person2@mail.com")
                .medications(List.of("dodoxadin:630mg"))
                .allergies(List.of("shellfish"))
                .build();

        when(this.personRepository.getPersons()).thenReturn(List.of(person1, person2));

        Exception exception = assertThrows(PersonNotFoundException.class, () -> this.personServiceImpl.getPersonInfo("firstname3", "lastname3"));

        assertEquals("Nobody found with firstname firstname3 and lastname lastname3", exception.getMessage());

        verify(this.personRepository, times(1)).getPersons();
        verify(this.medicalRecordService, never()).findMedicalRecordByFirstNameAndLastName(anyString(), anyString());
        verify(this.personMapper, never()).asPersonInfoDTO(any(Person.class), any(MedicalRecord.class));

    }


    @DisplayName("Should get persons mail by city")
    @Test
    void shouldGetMailsByCity() throws MailsNotFoundException {

        when(this.personRepository.getPersons()).thenReturn(List.of(person1, person2));

        List<String> result = this.personServiceImpl.getMailsByCity("city");

        assertFalse(result.isEmpty());
        assertEquals(2, result.size());
        assertEquals("person1@mail.com", result.get(0));
        assertEquals("person2@mail.com", result.get(1));

        verify(this.personRepository, times(1)).getPersons();

    }


    @DisplayName("Should not get persons mail by city")
    @Test
    void shouldNotGetMailsByCity() {

        when(this.personRepository.getPersons()).thenReturn(List.of());

        Exception exception = assertThrows(MailsNotFoundException.class, () -> this.personServiceImpl.getMailsByCity("city"));

        assertEquals("No email found for people living in city", exception.getMessage());

        verify(this.personRepository, times(1)).getPersons();

    }

}