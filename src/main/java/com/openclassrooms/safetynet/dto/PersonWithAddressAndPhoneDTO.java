package com.openclassrooms.safetynet.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class PersonWithAddressAndPhoneDTO {

    private String firstName;
    private String lastName;
    private String address;
    private String phone;
    @JsonIgnore
    private int age;

}
