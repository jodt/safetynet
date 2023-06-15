package com.openclassrooms.safetynet.dto;

import com.openclassrooms.safetynet.model.Person;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class PersonWithAgeAndFamilyMembersDTO {

    private String firstName;
    private String lastName;
    private int age;
    private List<Person> otherFamilyMembers;
}
