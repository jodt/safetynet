package com.openclassrooms.safetynet.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Class represent a person
 * @author Joel DUMORTIER
 */
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class Person {

    private String firstName;
    private String lastName;
    private String address;
    private String city;
    private int zip;
    private String phone;
    private String email;

}
