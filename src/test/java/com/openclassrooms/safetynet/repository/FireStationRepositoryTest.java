package com.openclassrooms.safetynet.repository;

import com.openclassrooms.safetynet.model.FireStation;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class FireStationRepositoryTest {

    private FireStationRepository fireStationRepository = new FireStationRepository();
    private FireStation fireStation1;
    private FireStation fireStation2;
    private FireStation fireStation3;
    private FireStation fireStation4;

    private List<FireStation> fireStationList = new ArrayList<>();


    @BeforeEach
    public void init() {
        fireStation1 = FireStation.builder()
                .station(1)
                .address("first address")
                .build();

        fireStation2 = FireStation.builder()
                .station(2)
                .address("second address")
                .build();

        fireStation3 = FireStation.builder()
                .station(3)
                .address("third address")
                .build();

        fireStation4 = FireStation.builder()
                .station(1)
                .address("fourth address")
                .build();

        Collections.addAll(fireStationList, fireStation1, fireStation2, fireStation3, fireStation4);
        this.fireStationRepository.setFireStations(fireStationList);

    }


    @DisplayName("Should get all fire stations")
    @Test
    void getFireStations() {

        List<FireStation> result = this.fireStationRepository.getFireStations();

        assertNotNull(result);

        assertEquals(4, result.size());
        assertEquals(1, result.get(0).getStation());
        assertEquals("first address", result.get(0).getAddress());
        assertEquals(2, result.get(1).getStation());
        assertEquals("second address", result.get(1).getAddress());
        assertEquals(3, result.get(2).getStation());
        assertEquals("third address", result.get(2).getAddress());
        assertEquals(1, result.get(3).getStation());
        assertEquals("fourth address", result.get(3).getAddress());

    }


    @DisplayName("Should add a fire station")
    @Test
    void addFireStation() {

        FireStation fireStationToAdd = FireStation.builder()
                .station(5)
                .address("third address")
                .build();

        FireStation result = this.fireStationRepository.addFireStation(fireStationToAdd);

        assertNotNull(result);

        assertEquals(5, this.fireStationList.size());
        assertEquals(5, result.getStation());
        assertEquals("third address", result.getAddress());
        assertEquals(5, this.fireStationList.get(4).getStation());
        assertEquals("third address", this.fireStationList.get(4).getAddress());

    }


    @DisplayName("Should get fire stations by number")
    @Test
    void getFireStationsByNumber() {

        List<FireStation> result = this.fireStationRepository.getFireStationsByNumber(1);

        assertNotNull(result);

        assertEquals(2, result.size());
        assertEquals("first address", result.get(0).getAddress());
        assertEquals(1, result.get(0).getStation());
        assertEquals("fourth address", result.get(1).getAddress());
        assertEquals(1, result.get(1).getStation());

    }


    @DisplayName("Should get a fire station address")
    @Test
    void getFireStationByAddress() {

        FireStation result = this.fireStationRepository.getFireStationByAddress("second address");

        assertNotNull(result);

        assertEquals(2, result.getStation());
        assertEquals("second address", result.getAddress());

    }


    @DisplayName("Should delete a fire station")
    @Test
    void deleteStation() {
        this.fireStationRepository.deleteStation(fireStation4);

        assertEquals(3, this.fireStationList.size());
        assertFalse(this.fireStationList.contains(fireStation4));
    }


    @DisplayName("Should delete several fire stations")
    @Test
    void deleteStations() {

        List<FireStation> fireStationsToDelete = List.of(fireStation1, fireStation3);

        this.fireStationRepository.deleteStations(fireStationsToDelete);

        assertEquals(2, this.fireStationList.size());
        assertFalse(this.fireStationList.contains(fireStation1));
        assertFalse(this.fireStationList.contains(fireStation3));

    }


    @DisplayName("Should update fire station")
    @Test
    void updateStationNumber() {

        FireStation fireStationToUpdate = FireStation.builder()
                .address("first address")
                .station(10)
                .build();

        FireStation result = this.fireStationRepository.updateStationNumber(fireStationToUpdate);

        assertNotNull(result);

        assertEquals(10, result.getStation());
        assertEquals("first address", result.getAddress());
        assertEquals(10, this.fireStationList.get(0).getStation());
        assertEquals("first address", this.fireStationList.get(0).getAddress());

    }

}