package com.openclassrooms.safetynet.utils;

import com.openclassrooms.safetynet.model.FireStation;
import com.openclassrooms.safetynet.model.MedicalRecord;
import com.openclassrooms.safetynet.model.Person;
import lombok.Data;
import org.springframework.stereotype.Component;

import java.util.List;


/**
 * This class is used to deserialize the json data from the file
 */
@Data
@Component
public class Datas {

    private List<Person> persons;
    private List<FireStation> firestations;
    private List<MedicalRecord> medicalrecords;

}
