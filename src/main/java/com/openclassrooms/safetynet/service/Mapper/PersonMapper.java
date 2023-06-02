package com.openclassrooms.safetynet.service.Mapper;

import com.openclassrooms.safetynet.dto.PersonInfoDTO;
import com.openclassrooms.safetynet.dto.PersonWithAddressAndPhoneDTO;
import com.openclassrooms.safetynet.dto.PersonWithAgeAndFamilyMembersDTO;
import com.openclassrooms.safetynet.dto.PersonWithMedicalRecordDTO;
import com.openclassrooms.safetynet.model.MedicalRecord;
import com.openclassrooms.safetynet.model.Person;

public interface PersonMapper {

    PersonWithAgeAndFamilyMembersDTO asPersonWithAgeDTO (MedicalRecord medicalRecord);

    PersonWithAddressAndPhoneDTO asPersonWithAddressAndPhoneDTO (Person person,MedicalRecord medicalRecord);

    PersonWithMedicalRecordDTO asPersonWithMedicalRecordDTO (Person person, MedicalRecord medicalRecord);

    PersonInfoDTO asPersonInfoDTO(Person person, MedicalRecord medicalRecord);

}
