package com.openclassrooms.safetynet.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

@Data
@AllArgsConstructor
public class PersonsConcernedByFireStationDTO {
    private List<PersonWithAddressAndPhoneDTO> personWithAddressAndPhoneDTOList;
    private AtomicInteger children;
    private AtomicInteger adults;
}