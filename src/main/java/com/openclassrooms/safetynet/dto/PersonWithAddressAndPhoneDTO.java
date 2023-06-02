package com.openclassrooms.safetynet.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

@Data
public class PersonWithAddressAndPhoneDTO{

    private String firstName;
    private String lastName;
    private String address;
    private String phone;
    @JsonIgnore
    private int age;

}
