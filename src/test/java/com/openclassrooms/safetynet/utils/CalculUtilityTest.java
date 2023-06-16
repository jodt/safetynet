package com.openclassrooms.safetynet.utils;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;


class CalculUtilityTest {

    LocalDate birthdate = LocalDate.of(2020, 01, 01);

    @DisplayName("should get the person age")
    @Test
    void shouldCalculateAgeOfPerson() {
        int age = CalculUtility.calculateAgeOfPerson(birthdate);

        assertEquals(3, age);
    }

    @DisplayName("should not get the person age but 0")
    @Test
    void shouldNotCalculateAgeOfPerson() {
        int age = CalculUtility.calculateAgeOfPerson(null);

        assertEquals(0, age);
    }

}