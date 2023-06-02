package com.openclassrooms.safetynet.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class FloodDTO {
    private String address;
    private List<PersonWithMedicalRecordDTO> personsListInFloodCase;

}
