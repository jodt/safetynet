package com.openclassrooms.safetynet.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.*;
import com.openclassrooms.safetynet.repository.FireStationRepository;
import com.openclassrooms.safetynet.repository.MedicalRecordRepository;
import com.openclassrooms.safetynet.repository.PersonRepository;
import lombok.Data;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.FileReader;
import java.lang.reflect.Type;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

@Data
@Component
public class DataLoader implements CommandLineRunner {

    private Datas datas;
    private final PersonRepository personRepository;
    private final FireStationRepository fireStationRepository;
    private final MedicalRecordRepository medicalRecordRepository;

    public DataLoader(PersonRepository personRepository, FireStationRepository fireStationRepository, MedicalRecordRepository medicalRecordRepository) {
        this.personRepository = personRepository;
        this.fireStationRepository = fireStationRepository;
        this.medicalRecordRepository = medicalRecordRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        BufferedReader br = new BufferedReader(new FileReader("src/main/resources/data.json"));
        String jsonString ="";

        try {
            String line;
            while ((line = br.readLine()) != null){
                jsonString += line;
            }
        } catch (Exception e){
            e.printStackTrace();
        } finally {
            br.close();
        }

        datas = new ObjectMapper().readValue(jsonString,Datas.class);
        personRepository.setPersons(datas.getPersons());
        fireStationRepository.setFireStations(datas.getFirestations());
        medicalRecordRepository.setMedicalRecords(datas.getMedicalrecords());
    }

}
