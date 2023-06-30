package com.openclassrooms.safetynet.service;

import com.openclassrooms.safetynet.exception.FireStationNotFoundException;
import com.openclassrooms.safetynet.model.FireStation;

import java.util.List;


public interface FireStationService {
    public List<FireStation> geAllFireStation();

    public FireStation addFireStation(FireStation fireStation);

    public FireStation updateStationNumber(FireStation fireStation) throws FireStationNotFoundException;

    public void deleteFireStationsByStationNumber(int stationNumber) throws FireStationNotFoundException;

    public void deleteFireStationByAddress(String address) throws FireStationNotFoundException;

    public FireStation getFireStationByAddress(String stationAddress) throws FireStationNotFoundException;

    public List<FireStation> getFireStationByStationNumber(int stationNumber) throws FireStationNotFoundException;
}
