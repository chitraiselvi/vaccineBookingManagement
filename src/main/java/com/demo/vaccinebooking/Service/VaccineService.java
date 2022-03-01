package com.demo.vaccinebooking.Service;

import com.demo.vaccinebooking.Exception.VaccineBookingException;
import com.demo.vaccinebooking.Model.APIResponse;
import com.demo.vaccinebooking.Model.Vaccine;
import com.demo.vaccinebooking.Repository.VaccineRepository;
import org.hibernate.annotations.common.util.impl.LoggerFactory;
import org.jboss.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import java.util.Optional;

@Service
public class VaccineService {
    Logger logger = LoggerFactory.logger(VaccineService.class);
    @Autowired
    VaccineRepository vaccineRepository;

    public ResponseEntity<APIResponse> addVaccine(Vaccine vaccine) throws Exception {
        APIResponse apiResponse = new APIResponse();
        Optional<Vaccine> oldVaccine = vaccineRepository.findById(vaccine.getVaccineId());
        if (oldVaccine.isPresent()) {
            throw new VaccineBookingException("There is already a vaccine record available for this id. You cannot use the same id for two vaccines.", HttpStatus.FORBIDDEN);
        }
        else{
            Vaccine createdVaccine = vaccineRepository.save(vaccine);
            apiResponse.setSuccess(true);
            apiResponse.setMessage("New vaccine has been added successfully");
            return new ResponseEntity<APIResponse>(apiResponse,HttpStatus.OK);
        }
    }

    public ResponseEntity<APIResponse> updateVaccine(String vaccineId, String numberOfShots) throws Exception{
        APIResponse apiResponse = new APIResponse();
        int numberOfNewShots = Integer.valueOf(numberOfShots);
        Optional<Vaccine> oldVaccine = vaccineRepository.findById(vaccineId);
        if (!oldVaccine.isPresent()) {
            throw new VaccineBookingException("There is no vaccine available for this id.", HttpStatus.NOT_FOUND);
        } else {
            Vaccine vaccine = oldVaccine.get();
            int availableShots = vaccine.getAvailableShots();
            vaccine.setAvailableShots(availableShots + numberOfNewShots);
            vaccineRepository.save(vaccine);
            apiResponse.setSuccess(true);
            apiResponse.setMessage("New shots have been added to the vaccine "+ vaccine.getVaccineName() +". The number of available shots is " +vaccine.getAvailableShots());
            return new ResponseEntity<APIResponse>(apiResponse,HttpStatus.OK);
        }
    }


    public ResponseEntity<APIResponse> deleteVaccine(String vaccineId){
        APIResponse apiResponse = new APIResponse();
        vaccineRepository.deleteById(vaccineId);
        apiResponse.setSuccess(true);
        apiResponse.setMessage("Vaccine deleted successfully");
        return new ResponseEntity<APIResponse>(apiResponse,HttpStatus.OK);
    }

    //to get vaccine by name
    public Optional<Vaccine> getVaccineByName(String name){
        return vaccineRepository.getByVaccineName(name);
    }

    public Vaccine saveVaccine(Vaccine vaccine){
        return vaccineRepository.save(vaccine);
    }

    //decrease available shots count
    public void decreaseVaccineShots(String vaccineName){
        logger.info("decrease available shots count");
        Vaccine bookedVaccine = getVaccineByName(vaccineName).get();
        int availableShots = bookedVaccine.getAvailableShots();
        bookedVaccine.setAvailableShots(--availableShots);
        saveVaccine(bookedVaccine);
    }

    //increase available shots count
    public void increaseVaccineShots(String vaccineName){
        logger.info("increase available shots count");
        Vaccine bookedVaccine = getVaccineByName(vaccineName).get();
        int availableShots = bookedVaccine.getAvailableShots();
        bookedVaccine.setAvailableShots(++availableShots);
        saveVaccine(bookedVaccine);
    }
}
