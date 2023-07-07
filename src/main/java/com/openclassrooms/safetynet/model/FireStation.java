package com.openclassrooms.safetynet.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Class represent a fire station
 * @author Joel DUMORTIER
 */
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class FireStation {

    private String address;
    private int station;
}
