package com.openclassrooms.safetynet.service.Mapper;

import com.openclassrooms.safetynet.dto.PersonWithAgeDTO;
import com.openclassrooms.safetynet.model.MedicalRecord;
import com.openclassrooms.safetynet.utils.CalculUtility;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class PersonMapperImpl implements PersonMapper{
     private final Logger logger = LoggerFactory.getLogger(PersonMapper.class);

    @Override
    public PersonWithAgeDTO asPersonWithAgeDTO(MedicalRecord medicalRecord) {
        logger.debug("Convert person to personWithAgeDTO");
        PersonWithAgeDTO personWithAgeDTO = new PersonWithAgeDTO();
        personWithAgeDTO.setFirstName(medicalRecord.getFirstName());
        personWithAgeDTO.setLastName(medicalRecord.getLastName());
        personWithAgeDTO.setAge(CalculUtility.calculateAgeOfPerson(medicalRecord.getBirthdate()));
        personWithAgeDTO.setOtherFamilyMembers(new ArrayList<>());
        logger.debug("person convert successfully");
        return personWithAgeDTO;
    }
}
