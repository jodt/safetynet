package com.openclassrooms.safetynet.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDate;
import java.time.Period;

/**
 * utility class
 * @author joel DUMORTIER
 */
public class CalculUtility {

    private static final Logger logger = LoggerFactory.getLogger(CalculUtility.class);

    /**
     * Method that takes a localDate and return the age as of today's date
     * @param birthdate
     * @return the age of the person
     */
    public static int calculateAgeOfPerson(LocalDate birthdate){
        logger.debug("Calulate age of the person");
        LocalDate today = LocalDate.now();
        if (birthdate != null){
            logger.debug("age calculated successfully");
            return Period.between(birthdate,today).getYears();
        } else {
            logger.debug("unable to calculate age");
            return 0;
        }
    }
}
