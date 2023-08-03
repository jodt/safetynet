package com.openclassrooms.safetynet.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.openclassrooms.safetynet.model.FireStation;
import com.openclassrooms.safetynet.repository.FireStationRepository;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.hasSize;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class FireStationControllerIT {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    private FireStationRepository fireStationRepository;

    private FireStation fireStation;


    @BeforeEach
    void init() {
        fireStation = FireStation.builder()
                .station(10)
                .address("fire station address")
                .build();
    }


    @DisplayName("Should get all fire stations")
    @Test
    @Order(1)
    void shouldGetAllFireStations() throws Exception {

        mockMvc.perform(get("/firestation/all"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(this.fireStationRepository.getFireStations().size())));

    }


    @DisplayName("Should add a fire station")
    @Test
    @Order(2)
    void shouldAddFireStation() throws Exception {

        mockMvc.perform(post("/firestation")
                        .content(objectMapper.writeValueAsString(fireStation))
                        .contentType("application/json"))
                .andExpect(status().isCreated());

        FireStation fireStationAdded = this.fireStationRepository.getFireStationByAddress("fire station address");

        assertEquals(fireStation, fireStationAdded);
    }


    @DisplayName("Should update a fire station")
    @Test
    @Order(3)
    void shouldUpdateFireStation() throws Exception {

        FireStation fireStationUpdated = FireStation.builder()
                .station(14)
                .address("fire station address")
                .build();

        mockMvc.perform(put("/firestation")
                        .content(objectMapper.writeValueAsString(fireStationUpdated))
                        .contentType("application/json"))
                .andExpect(status().isOk());

        int stationNumberUpdated = this.fireStationRepository.getFireStationByAddress("fire station address").getStation();

        assertEquals(14, stationNumberUpdated);
    }


    @DisplayName("Should delete a firestation by address")
    @Test
    void shouldDeleteFireStationByAddress() throws Exception {

        mockMvc.perform(delete("/firestation")
                        .param("address", "fire station address"))
                .andExpect(status().isNoContent());

        assertFalse(this.fireStationRepository.getFireStations().contains(fireStation));
    }


    @DisplayName("Should delete firestations by number")
    @Test
    void shouldDeleteFireStationByNumber() throws Exception {

        this.fireStationRepository.addFireStation(fireStation);

        mockMvc.perform(delete("/firestation/10"))
                .andExpect(status().isNoContent());

        assertFalse(this.fireStationRepository.getFireStations().contains(fireStation));
    }


}
