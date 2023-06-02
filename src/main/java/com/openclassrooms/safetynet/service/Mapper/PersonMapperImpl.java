package com.openclassrooms.safetynet.service.Mapper;

import com.openclassrooms.safetynet.dto.PersonInfoDTO;
import com.openclassrooms.safetynet.dto.PersonWithAddressAndPhoneDTO;
import com.openclassrooms.safetynet.dto.PersonWithAgeAndFamilyMembersDTO;
import com.openclassrooms.safetynet.dto.PersonWithMedicalRecordDTO;
import com.openclassrooms.safetynet.model.MedicalRecord;
import com.openclassrooms.safetynet.model.Person;
import com.openclassrooms.safetynet.utils.CalculUtility;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service

public class PersonMapperImpl implements PersonMapper{
     private final Logger logger = LoggerFactory.getLogger(PersonMapper.class);

    @Override
    public PersonWithAgeAndFamilyMembersDTO asPersonWithAgeDTO(MedicalRecord medicalRecord) {
        logger.debug("Convert person to personWithAgeDTO");
        PersonWithAgeAndFamilyMembersDTO personWithAgeDTO = new PersonWithAgeAndFamilyMembersDTO();
        personWithAgeDTO.setFirstName(medicalRecord.getFirstName());
        personWithAgeDTO.setLastName(medicalRecord.getLastName());
        personWithAgeDTO.setAge(CalculUtility.calculateAgeOfPerson(medicalRecord.getBirthdate()));
        personWithAgeDTO.setOtherFamilyMembers(new ArrayList<>());
        logger.debug("person convert successfully");
        return personWithAgeDTO;
    }

    @Override
    public PersonWithAddressAndPhoneDTO asPersonWithAddressAndPhoneDTO(Person person,MedicalRecord medicalRecord) {
        logger.debug("Convert person to personWithAddressAndPhoneDTO");
        PersonWithAddressAndPhoneDTO personWithAddressAndPhoneDTO = new PersonWithAddressAndPhoneDTO();
        personWithAddressAndPhoneDTO.setFirstName(person.getFirstName());
        personWithAddressAndPhoneDTO.setLastName(person.getLastName());
        personWithAddressAndPhoneDTO.setAge(CalculUtility.calculateAgeOfPerson(medicalRecord.getBirthdate()));
        personWithAddressAndPhoneDTO.setAddress(person.getAddress());
        personWithAddressAndPhoneDTO.setPhone(person.getPhone());
        logger.debug("person convert successfully");
        return personWithAddressAndPhoneDTO;
    }

    @Override
    public PersonWithMedicalRecordDTO asPersonWithMedicalRecordDTO(Person person, MedicalRecord medicalRecord) {
        logger.debug("Convert person to personWithMedicalRecordDTO");
        PersonWithMedicalRecordDTO personWithMedicalRecordDTO = new PersonWithMedicalRecordDTO();
        personWithMedicalRecordDTO.setLastName(person.getLastName());
        personWithMedicalRecordDTO.setPhone(person.getPhone());
        personWithMedicalRecordDTO.setAge(CalculUtility.calculateAgeOfPerson(medicalRecord.getBirthdate()));
        personWithMedicalRecordDTO.setMedications(medicalRecord.getMedications());
        personWithMedicalRecordDTO.setAllergies(medicalRecord.getAllergies());
        logger.debug("person convert successfully");
        return personWithMedicalRecordDTO;
    }

    @Override
    public PersonInfoDTO asPersonInfoDTO(Person person, MedicalRecord medicalRecord) {
        logger.debug("Convert person to personInfoDTO");
        PersonInfoDTO personInfoDTO = new PersonInfoDTO();
        personInfoDTO.setLastName(person.getLastName());
        personInfoDTO.setAddress(person.getAddress());
        personInfoDTO.setAge(CalculUtility.calculateAgeOfPerson(medicalRecord.getBirthdate()));
        personInfoDTO.setEmail(person.getEmail());
        personInfoDTO.setMedications(medicalRecord.getMedications());
        personInfoDTO.setAllergies(medicalRecord.getMedications());
        return personInfoDTO;
    }

}
