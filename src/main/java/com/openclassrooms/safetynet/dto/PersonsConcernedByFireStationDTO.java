package com.openclassrooms.safetynet.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class PersonsConcernedByFireStationDTO {
    private List<PersonWithAddressAndPhoneDTO> personWithAddressAndPhoneDTOList;
    private AtomicInteger children;
    private AtomicInteger adults;
}