package com.demo.vaccinebooking.Service;

import com.demo.vaccinebooking.Model.Vaccine;
import com.demo.vaccinebooking.Repository.VaccineRepository;
import org.hibernate.annotations.common.util.impl.LoggerFactory;
import org.jboss.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class VaccineService {
    Logger logger = LoggerFactory.logger(VaccineService.class);
    @Autowired
    VaccineRepository vaccineRepository;

    public Vaccine addVaccine(Vaccine vaccine){
        return vaccineRepository.save(vaccine);
    }

    public Vaccine updateVaccine(Vaccine vaccine){
        return vaccineRepository.save(vaccine);
    }

    public String deleteVaccine(String vaccineId){
        vaccineRepository.deleteById(vaccineId);
        return "Vaccine deleted successfully";
    }

    //to get vaccine by name
    public Vaccine getVaccineByName(String name){
        return vaccineRepository.getByVaccineName(name);
    }

    public Vaccine saveVaccine(Vaccine vaccine){
        return vaccineRepository.save(vaccine);
    }

    //decrease available shots count
    public void decreaseVaccineShots(String vaccineName){
        logger.info("decrease available shots count");
        Vaccine bookedVaccine = getVaccineByName(vaccineName);
        int availableShots = bookedVaccine.getAvailableShots();
        bookedVaccine.setAvailableShots(--availableShots);
        saveVaccine(bookedVaccine);
    }

    //increase available shots count
    public void increaseVaccineShots(String vaccineName){
        logger.info("increase available shots count");
        Vaccine bookedVaccine = getVaccineByName(vaccineName);
        int availableShots = bookedVaccine.getAvailableShots();
        bookedVaccine.setAvailableShots(++availableShots);
        saveVaccine(bookedVaccine);
    }
}
