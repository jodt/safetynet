package com.openclassrooms.safetynet.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDate;
import java.time.Period;

public class CalculUtility {

    private static final Logger logger = LoggerFactory.getLogger(CalculUtility.class);

    public static int calculateAgeOfPerson(LocalDate birthdate){
        logger.debug("Calulate age of the person");
        LocalDate today = LocalDate.now();
        if (birthdate != null && today != null){
            logger.debug("age calculated successfully");
            return Period.between(birthdate,today).getYears();
        } else {
            logger.debug("unable to calculate age");
            return 0;
        }
    }
}
