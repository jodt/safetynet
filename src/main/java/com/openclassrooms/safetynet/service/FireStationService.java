package com.openclassrooms.safetynet.service;

import com.openclassrooms.safetynet.exception.FireStationAlreadyExistException;
import com.openclassrooms.safetynet.exception.FireStationNotFoundException;
import com.openclassrooms.safetynet.model.FireStation;

import java.util.List;


public interface FireStationService {
    public List<FireStation> getAllFireStation();

    public FireStation addFireStation(FireStation fireStation) throws FireStationAlreadyExistException;

    public FireStation updateStationNumber(FireStation fireStation) throws FireStationNotFoundException;

    public void deleteFireStationsByStationNumber(int stationNumber) throws FireStationNotFoundException;

    public void deleteFireStationByAddress(String address) throws FireStationNotFoundException;

    public FireStation getFireStationByAddress(String stationAddress) throws FireStationNotFoundException;

    public List<FireStation> getFireStationByStationNumber(int stationNumber) throws FireStationNotFoundException;

    public List<String> getAddressesByStationNumber(int number);
}
