package com.openclassrooms.safetynet.service;

import com.openclassrooms.safetynet.exception.FireStationAlreadyExistException;
import com.openclassrooms.safetynet.exception.FireStationNotFoundException;
import com.openclassrooms.safetynet.model.FireStation;
import com.openclassrooms.safetynet.model.MedicalRecord;
import com.openclassrooms.safetynet.repository.FireStationRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
public class FireStationServiceImpl implements FireStationService {

    private final Logger logger = LoggerFactory.getLogger(FireStationServiceImpl.class);
    private final FireStationRepository fireStationRepository;

    public FireStationServiceImpl(FireStationRepository fireStationRepository) {
        this.fireStationRepository = fireStationRepository;
    }

    /**
     * Get all fire stations
     * @return a list of fire stations
     */
    public List<FireStation> getAllFireStation() {
        return this.fireStationRepository.getFireStations();
    }

    public FireStation addFireStation(FireStation fireStation) throws FireStationAlreadyExistException {
        Boolean isFireStationAlreadyRegistered = this.checkIfFireStationAlreadyExist(fireStation);
        if(isFireStationAlreadyRegistered){
            logger.error("Fire station with the number {} and the address {} is already registered", fireStation.getStation(),fireStation.getAddress());
            throw new FireStationAlreadyExistException("Fire station with the number " + fireStation.getStation() + " and the address " + fireStation.getAddress() + " is already registered");
        } else {
            logger.debug("Try to add a new fire station");
            this.fireStationRepository.addFireStation(fireStation);
            logger.debug("Fire station successfully added");
            return fireStation;
        }
    }

    /**
     * Method that takes a fire station and update the station n
     * @param fireStation
     * @return
     * @throws FireStationNotFoundException if the fire station is not found
     */
    public FireStation updateStationNumber(FireStation fireStation) throws FireStationNotFoundException {
        logger.debug("Try to update the fire station's number for the address {}", fireStation.getAddress());
        FireStation fireStationToUpdate = this.getFireStationByAddress(fireStation.getAddress());
        if (!Objects.isNull(fireStationToUpdate)) {
            fireStationToUpdate.setStation(fireStation.getStation());
            this.fireStationRepository.updateStationNumber(fireStationToUpdate);
            logger.debug("Fire station successfully updated with the station's number {}", fireStation.getStation());
        }
        return fireStation;
    }

    /**
     * Delete a fire station by station number
     * @param stationNumber
     * @throws FireStationNotFoundException if fire station is not found
     */
    public void deleteFireStationsByStationNumber(int stationNumber) throws FireStationNotFoundException {
        logger.debug("Try to delete the fire stations with the station's number {}", stationNumber);
        List<FireStation> fireStationToDelete = this.getFireStationByStationNumber(stationNumber);
        logger.debug("Fire stations successfully deleted");
        this.fireStationRepository.deleteStations(fireStationToDelete);
    }

    /**
     * Delete a fire station by address
     * @param address
     * @throws FireStationNotFoundException if fire station is not found
     */
    public void deleteFireStationByAddress(String address) throws FireStationNotFoundException {
        logger.debug("Try to delete the fire station for the address {}", address);
        FireStation fireStationToDelete = this.getFireStationByAddress(address);
        logger.debug("Fire station successfully deleted");
        this.fireStationRepository.deleteStation(fireStationToDelete);
    }

    /**
     * Method that takes an address and return the fire station corresponding to this address
     * @param stationAddress
     * @return a fire station
     * @throws FireStationNotFoundException
     */
    public FireStation getFireStationByAddress(String stationAddress) throws FireStationNotFoundException {
        logger.debug("Try to find fire station for the address {}", stationAddress);
        FireStation fireStationResult = this.fireStationRepository.getFireStationByAddress(stationAddress);
        if (fireStationResult == null) {
            logger.error("Fire station not found for the address {}", stationAddress);
            throw new FireStationNotFoundException("firestation not found with the address " + stationAddress);
        }
        logger.debug("Fire station found for the address {}", stationAddress);
        return fireStationResult;
    }

    /**
     * Method that takes a fire station number and return all the fire stations corresponding to this number
     * @param stationNumber
     * @return a list fo fire stations
     * @throws FireStationNotFoundException if fire stations not found
     */
    public List<FireStation> getFireStationByStationNumber(int stationNumber) throws FireStationNotFoundException {
        logger.debug("Try to find fire stations with the station's number {}", stationNumber);
        List<FireStation> fireStationResult = this.fireStationRepository.getFireStationsByNumber(stationNumber);
        if (fireStationResult.isEmpty()) {
            logger.error("Fire stations not found with the station's number {}", stationNumber);
            throw new FireStationNotFoundException("fire stations not found with the station's number " + stationNumber);
        }
        logger.debug("Fire stations found with the station's number {}", stationNumber);
        return fireStationResult;
    }

    /**
     * Method that takes a fire station and checks if it exists
     *
     * @param fireStation
     *
     * @return true if the fire station is already registered otherwise false
     */
    private Boolean checkIfFireStationAlreadyExist (FireStation fireStation) {
        return this.fireStationRepository.getFireStationByNumberAndAddress(fireStation) != null;
    }
}
