package com.openclassrooms.safetynet.service.Mapper;

import com.openclassrooms.safetynet.dto.PersonWithAgeDTO;
import com.openclassrooms.safetynet.model.MedicalRecord;

public interface PersonMapper {

    PersonWithAgeDTO asPersonWithAgeDTO (MedicalRecord medicalRecord);
}
