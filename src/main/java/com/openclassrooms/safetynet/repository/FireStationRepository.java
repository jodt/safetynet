package com.openclassrooms.safetynet.repository;

import com.openclassrooms.safetynet.model.FireStation;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public class FireStationRepository {

    private List<FireStation> firestations = new ArrayList<>();

    public List<FireStation> getFireStations() {
        return firestations;
    }

    public void setFireStations(List<FireStation> fireStations) {
        this.firestations = fireStations;
    }

    public FireStation addFireStation(FireStation fireStation) {
        this.firestations.add(fireStation);
        return fireStation;
    }


    public List<FireStation> getFireStationsByNumber(int stationNumber) {
        List<FireStation> fireStationsResult = new ArrayList<>();
        for (FireStation fireStation : this.firestations) {
            if (fireStation.getStation() == stationNumber) {
                fireStationsResult.add(fireStation);
            }
        }
        return fireStationsResult;
    }


    public FireStation getFireStationByAddress(String stationAddress) {
        FireStation fireStationResult = null;
        for (FireStation fireStation : this.firestations) {
            if (fireStation.getAddress().equals(stationAddress)) {
                fireStationResult = fireStation;
            }
        }
        return fireStationResult;
    }


    public void deleteStation(FireStation fireStation) {
        this.firestations.remove(fireStation);
    }


    public void deleteStations(List<FireStation> fireStations) {
        for (FireStation firestation : fireStations) {
            this.firestations.remove(firestation);
        }
    }


    public FireStation updateStationNumber(FireStation updatedFireStation) {
        int index = this.firestations.indexOf(this.getFireStationByAddress(updatedFireStation.getAddress()));
        this.firestations.set(index, updatedFireStation);
        return updatedFireStation;
    }


    public FireStation getFireStationByNumberAndAddress(FireStation fireStationToRetrieve) {
        FireStation result = null;
        for (FireStation fireStation : this.firestations) {
            if (fireStation.getStation() == fireStationToRetrieve.getStation() && fireStation.getAddress().toLowerCase().equals(fireStationToRetrieve.getAddress().toLowerCase())) {
                result = fireStation;
            }
        }
        return result;
    }


}
