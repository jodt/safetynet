package com.openclassrooms.safetynet.service;

import com.openclassrooms.safetynet.exception.FireStationAlreadyExistException;
import com.openclassrooms.safetynet.exception.FireStationNotFoundException;
import com.openclassrooms.safetynet.model.FireStation;
import com.openclassrooms.safetynet.repository.FireStationRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class FireStationServiceTest {

    @Mock
    private FireStationRepository fireStationRepository;
    @InjectMocks
    private FireStationServiceImpl fireStationServiceImpl;

    private FireStation fireStation1;
    private FireStation fireStation2;
    private FireStation fireStationToUpdate;
    private FireStation updatedFireStation;


    @BeforeEach
    public void init() {

        fireStation1 = FireStation.builder()
                .station(1)
                .address("Test address")
                .build();

        fireStation2 = FireStation.builder()
                .station(1)
                .address("Another test address")
                .build();

        fireStationToUpdate = FireStation.builder()
                .station(1)
                .address("Test address")
                .build();

        updatedFireStation = FireStation.builder()
                .station(2)
                .address("Test address")
                .build();
    }


    @Test
    @DisplayName("Should get all fire stations")
    public void shouldGetAllFireStation() {

        when(this.fireStationRepository.getFireStations()).thenReturn(List.of(fireStation1, fireStation2));

        List<FireStation> result = this.fireStationServiceImpl.getAllFireStation();

        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals(fireStation1, result.get(0));
        assertEquals(fireStation2, result.get(1));

        verify(this.fireStationRepository, times(1)).getFireStations();

    }


    @Test
    @DisplayName("Should add a fire station")
    public void shouldAddFireStation() throws FireStationAlreadyExistException {

        when(this.fireStationRepository.addFireStation(any(FireStation.class))).thenReturn(fireStation1);

        FireStation result = this.fireStationServiceImpl.addFireStation(fireStation1);

        assertNotNull(result);
        assertEquals(1, result.getStation());
        assertEquals("Test address", result.getAddress());

        verify(this.fireStationRepository, times(1)).addFireStation(any(FireStation.class));

    }

    @Test
    @DisplayName("Should not add a fire station -> fire station already exist")
    public void shouldNotAddFireStation() throws FireStationAlreadyExistException {

        when(this.fireStationRepository.getFireStationByNumberAndAddress(any(FireStation.class))).thenReturn(fireStation1);

        assertThrows(FireStationAlreadyExistException.class, () -> this.fireStationServiceImpl.addFireStation(fireStation1));

        verify(this.fireStationRepository, times(1)).getFireStationByNumberAndAddress(any(FireStation.class));
        verify(this.fireStationRepository, never()).addFireStation(any(FireStation.class));

    }


    @Test
    @DisplayName("Should update a fire station")
    public void shouldUpdateFireStation() throws FireStationNotFoundException {

        when(this.fireStationRepository.getFireStationByAddress(any(String.class))).thenReturn(fireStationToUpdate);
        when(this.fireStationRepository.updateStationNumber(any(FireStation.class))).thenReturn(updatedFireStation);

        FireStation result = this.fireStationServiceImpl.updateStationNumber(updatedFireStation);

        assertNotNull(result);
        assertEquals(2, result.getStation());
        assertEquals("Test address", result.getAddress());

        verify(this.fireStationRepository, times(1)).getFireStationByAddress(any(String.class));
        verify(this.fireStationRepository, times(1)).updateStationNumber(any(FireStation.class));

    }


    @Test
    @DisplayName("Should delete fire stations by number")
    public void shouldDeleteFireStationByNumber() throws FireStationNotFoundException {

        List<FireStation> fireStationsToDelete = List.of(fireStation1, fireStation2);

        when(this.fireStationRepository.getFireStationsByNumber(any(Integer.class))).thenReturn(fireStationsToDelete);

        this.fireStationServiceImpl.deleteFireStationsByStationNumber(any(Integer.class));

        verify(this.fireStationRepository, times(1)).deleteStations(fireStationsToDelete);

    }


    @Test
    @DisplayName("Should delete a fire station by address")
    public void shouldDeleteFireStationByAddress() throws FireStationNotFoundException {

        FireStation fireStationToDelete = FireStation.builder()
                .station(1)
                .address("Test address")
                .build();

        when(this.fireStationRepository.getFireStationByAddress(any(String.class))).thenReturn(fireStationToDelete);
        doNothing().when(fireStationRepository).deleteStation(fireStationToDelete);

        this.fireStationServiceImpl.deleteFireStationByAddress("Test address");

        verify(this.fireStationRepository, times(1)).deleteStation(fireStationToDelete);

    }


    @Test
    @DisplayName("Should get fires stations by number")
    public void shouldGetFireStationByNumber() throws FireStationNotFoundException {


        List<FireStation> fireStationList = List.of(fireStation1, fireStation2);


        when(this.fireStationRepository.getFireStationsByNumber(any(Integer.class))).thenReturn(fireStationList);

        List<FireStation> result = this.fireStationServiceImpl.getFireStationByStationNumber(1);

        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("Test address", result.get(0).getAddress());
        assertEquals("Another test address", result.get(1).getAddress());
        verify(this.fireStationRepository, times(1)).getFireStationsByNumber(any(Integer.class));

    }


    @Test
    @DisplayName("Should Not get fires stations by number")
    public void shouldNotGetFireStationByNumber() throws FireStationNotFoundException {

        when(this.fireStationRepository.getFireStationsByNumber(any(Integer.class))).thenReturn(List.of());

        Exception exception = assertThrows(FireStationNotFoundException.class, () -> this.fireStationServiceImpl.getFireStationByStationNumber(9));

        assertEquals("fire stations not found with the station's number 9", exception.getMessage());

    }


    @Test
    @DisplayName("Should get fires stations by address")
    public void shouldGetFireStationByAddress() throws FireStationNotFoundException {


        when(this.fireStationRepository.getFireStationByAddress(any(String.class))).thenReturn(fireStation1);

        FireStation result = this.fireStationServiceImpl.getFireStationByAddress("Test address");

        assertNotNull(result);
        assertEquals("Test address", result.getAddress());
        assertEquals(1, result.getStation());
        verify(this.fireStationRepository, times(1)).getFireStationByAddress(any(String.class));

    }


    @Test
    @DisplayName("Should Not get fires stations by address")
    public void shouldNotGetFireStationByAddress() throws FireStationNotFoundException {

        when(this.fireStationRepository.getFireStationByAddress(any(String.class))).thenReturn(null);

        Exception exception = assertThrows(FireStationNotFoundException.class, () -> this.fireStationServiceImpl.getFireStationByAddress("test address"));

        assertEquals("firestation not found with the address test address", exception.getMessage());

    }


    @Test
    @DisplayName("Should get addresses by fire station number")
    public void getAddressesByStationNumber() {

        when(this.fireStationRepository.getFireStationsByNumber(anyInt())).thenReturn(List.of(fireStation1));

        List<String> result = this.fireStationServiceImpl.getAddressesByStationNumber(1);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Test address", result.get(0));

        verify(fireStationRepository,times(1)).getFireStationsByNumber(anyInt());

    }

}