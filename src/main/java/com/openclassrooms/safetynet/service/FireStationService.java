package com.openclassrooms.safetynet.service;

import com.openclassrooms.safetynet.exception.FireStationNotFoundException;
import com.openclassrooms.safetynet.model.FireStation;
import com.openclassrooms.safetynet.repository.FireStationRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class FireStationService {

    private final Logger logger =  LoggerFactory.getLogger(FireStationService.class);
    private final FireStationRepository fireStationRepository;

    public FireStationService(FireStationRepository fireStationRepository) {
        this.fireStationRepository = fireStationRepository;
    }

    public FireStation addFireStation(FireStation fireStation){
        logger.debug("Try to add a new fire station");
        this.fireStationRepository.addFireStation(fireStation);
        logger.debug("Fire station successfully added");
        return fireStation;
    }

    public FireStation updateStationNumber(FireStation fireStation) throws FireStationNotFoundException {
        logger.debug("Try to update the fire station's number for the address {}", fireStation.getAddress());
        this.getFireStationByAddress(fireStation.getAddress());
        this.fireStationRepository.updateStationNumber(fireStation);
        logger.debug("Fire station successfully updated with the station's number {}", fireStation.getStation());
        return fireStation;
    }

    public void deleteFireStationByStationNumber(int stationNumber) throws FireStationNotFoundException {
        logger.debug("Try to delete the fire station with the station's number {}", stationNumber);
        FireStation fireStationToDelete = this.getFireStationByStationNumber(stationNumber);
        logger.debug("Fire station successfully deleted");
        this.fireStationRepository.deleteStation(fireStationToDelete);
    }

    public void deleteFireStationByAddress(String address) throws FireStationNotFoundException {
        logger.debug("Try to delete the fire station for the address {}", address);
        FireStation fireStationToDelete = this.getFireStationByAddress(address);
        logger.debug("Fire station successfully deleted");
        this.fireStationRepository.deleteStation(fireStationToDelete);
    }

    private FireStation getFireStationByAddress(String stationAddress) throws FireStationNotFoundException {
        logger.debug("Try to find fire station for the address {}", stationAddress);
        FireStation fireStationResult = this.fireStationRepository.getFireStationByAddress(stationAddress);
        if(fireStationResult == null){
            logger.error("Fire station not found for the address {}", stationAddress);
            throw new FireStationNotFoundException("firestation not found with the address " + stationAddress);
        }
        logger.debug("Fire station found for the address {}", stationAddress);
        return fireStationResult;
    }

    private FireStation getFireStationByStationNumber(int stationNumber) throws FireStationNotFoundException {
        logger.debug("Try to find fire station with the station's number {}", stationNumber);
        FireStation fireStationResult = this.fireStationRepository.getFireStationByNumber(stationNumber);
        if(fireStationResult == null){
            logger.error("Fire station not found with the station's number {}", stationNumber);
            throw new FireStationNotFoundException("firestation not found with the station's number " + stationNumber);
        }
        logger.debug("Fire station found with the station's number {}", stationNumber);
        return fireStationResult;
    }
}
