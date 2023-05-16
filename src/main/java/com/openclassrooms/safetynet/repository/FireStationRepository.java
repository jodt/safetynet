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

    public FireStation getFireStationByNumber(int stationNumber) {
        FireStation fireStationResult = null;
        for (FireStation fireStation : this.firestations){
            if (fireStation.getStation() == stationNumber){
                fireStationResult = fireStation;
            }
        }
        return fireStationResult;
    }

    public FireStation getFireStationByAddress(String stationAddress) {
        FireStation fireStationResult = null;
        for (FireStation fireStation : this.firestations){
            if (fireStation.getAddress().equals(stationAddress)){
                fireStationResult = fireStation;
            }
        }
        return fireStationResult;
    }

    public void deleteStation (FireStation fireStation){
        this.firestations.remove(fireStation);
    }

    public void updateStationNumber (FireStation fireStation){
        FireStation updateFireStation = this.getFireStationByAddress(fireStation.getAddress());
        updateFireStation.setStation(fireStation.getStation());
    }
}
