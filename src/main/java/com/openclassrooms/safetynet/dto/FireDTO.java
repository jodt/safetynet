package com.openclassrooms.safetynet.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class FireDTO {
    private List<PersonWithMedicalRecordDTO> personsListInFireCase;
    private int fireStationNumber;
}
