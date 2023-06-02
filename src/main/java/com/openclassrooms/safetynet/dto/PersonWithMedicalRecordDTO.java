package com.openclassrooms.safetynet.dto;

import lombok.Data;

import java.util.List;

@Data
public class PersonWithMedicalRecordDTO {
    private String lastName;
    private String phone;
    private int age;
    private List<String> medications;
    private List<String> allergies;
}
