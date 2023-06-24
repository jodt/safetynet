package com.openclassrooms.safetynet.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class FireDTO {
    private List<PersonWithMedicalRecordDTO> personsListInFireCase;
    private int fireStationNumber;
}
