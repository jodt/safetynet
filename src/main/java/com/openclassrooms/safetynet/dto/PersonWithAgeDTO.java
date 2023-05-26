package com.openclassrooms.safetynet.dto;

import com.openclassrooms.safetynet.model.Person;
import lombok.Data;

import java.util.List;

@Data
public class PersonWithAgeDTO {

    private String firstName;
    private String lastName;
    private int age;
    private List<Person> otherFamilyMembers;
}
