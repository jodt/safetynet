package com.openclassrooms.safetynet.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.openclassrooms.safetynet.exception.FireStationNotFoundException;
import com.openclassrooms.safetynet.model.FireStation;
import com.openclassrooms.safetynet.repository.FireStationRepository;
import com.openclassrooms.safetynet.service.FireStationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = FireStationController.class)
class FireStationControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private FireStationService fireStationService;

    @MockBean
    private FireStationRepository fireStationRepository;

    @Captor
    ArgumentCaptor<FireStation> fireStationCaptor;

    @Captor
    ArgumentCaptor<String> addressCaptor;

    @Captor
    ArgumentCaptor<Integer> stationNumberCaptor;

    private FireStation fireStation;

    @BeforeEach
    public void init() {
        fireStation = FireStation.builder()
                .station(1)
                .address("fire station address")
                .build();
    }


    @DisplayName("Should get all fire stations")
    @Test
    void shouldGetAllFireStations() throws Exception {

        when(this.fireStationService.getAllFireStation()).thenReturn(List.of(fireStation));

        mockMvc.perform(get("/firestation/all"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$",hasSize(1)))
                .andExpect(jsonPath("$[0].station", is(1)))
                .andExpect(jsonPath("$[0].address", is("fire station address")));

        verify(this.fireStationService,times(1)).getAllFireStation();

    }


    @DisplayName("Should add a fire station")
    @Test
    void ShouldAddFireStation() throws Exception {

        mockMvc.perform(post("/firestation")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(fireStation)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.address", is("fire station address")))
                .andExpect(jsonPath("$.station", is(1)));

        verify(this.fireStationService, times(1)).addFireStation(fireStationCaptor.capture());
        FireStation fireStationCaptorValue = fireStationCaptor.getValue();
        assertEquals(1, fireStationCaptorValue.getStation());
        assertEquals("fire station address", fireStationCaptorValue.getAddress());

    }


    @DisplayName("Should update a fire station")
    @Test
    void ShouldUpdateFireStation() throws Exception {

        mockMvc.perform(put("/firestation")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(fireStation)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.address", is("fire station address")))
                .andExpect(jsonPath("$.station", is(1)));

        verify(this.fireStationService, times(1)).updateStationNumber(fireStationCaptor.capture());
        FireStation fireStationCaptorValue = fireStationCaptor.getValue();
        assertEquals(1, fireStationCaptorValue.getStation());
        assertEquals("fire station address", fireStationCaptorValue.getAddress());

    }


    @DisplayName("Should not update a fire station -> fire station not found")
    @Test
    void ShouldNotUpdateFireStation() throws Exception {

        when(this.fireStationService.updateStationNumber(any(FireStation.class))).thenThrow(FireStationNotFoundException.class);

        mockMvc.perform(put("/firestation")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(fireStation)))
                .andExpect(status().isNotFound());

        verify(this.fireStationService, times(1)).updateStationNumber(any(FireStation.class));

    }


    @DisplayName("Should delete a fire station by address")
    @Test
    void deleteFireStationByAddress() throws Exception {

        mockMvc.perform(delete("/firestation")
                        .param("address", "fire station address"))
                .andExpect(status().isNoContent());

        verify(this.fireStationService, times(1)).deleteFireStationByAddress(addressCaptor.capture());
        String addressCaptorValue = addressCaptor.getValue();
        assertEquals("fire station address", addressCaptorValue);

    }


    @DisplayName("Should not delete a fire station by address-> fire station not found")
    @Test
    void ShouldNotDeleteFireStationByAddress() throws Exception {

        doThrow(FireStationNotFoundException.class).when(this.fireStationService).deleteFireStationByAddress(anyString());

        mockMvc.perform(delete("/firestation")
                        .param("address", anyString()))
                .andExpect(status().isNotFound());

        verify(this.fireStationService, times(1)).deleteFireStationByAddress(anyString());

    }


    @DisplayName("Should Delete fire stations by number")
    @Test
    void shouldDeleteFireStationsByStationNumber() throws Exception {

        mockMvc.perform(delete("/firestation/{stationNumber}", 4))
                .andExpect(status().isNoContent());

        verify(this.fireStationService, times(1)).deleteFireStationsByStationNumber(stationNumberCaptor.capture());
        int stationNumberCaptorValue = stationNumberCaptor.getValue();
        assertEquals(4, stationNumberCaptorValue);

    }


    @DisplayName("Should not delete fire station by number -> fire station not found")
    @Test
    void ShouldNotDeleteFireStationByStationsNumber() throws Exception {

        doThrow(FireStationNotFoundException.class).when(this.fireStationService).deleteFireStationsByStationNumber(anyInt());

        mockMvc.perform(delete("/firestation/{stationNumber}", anyInt()))
                .andExpect(status().isNotFound());

        verify(this.fireStationService, times(1)).deleteFireStationsByStationNumber(anyInt());

    }

}
