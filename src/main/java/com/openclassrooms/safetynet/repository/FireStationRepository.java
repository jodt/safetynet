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
}
