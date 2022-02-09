package com.demo.vaccinebooking.Controller;

import com.demo.vaccinebooking.Model.Vaccine;
import com.demo.vaccinebooking.Service.VaccineService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
public class VaccineController {
    @Autowired
    VaccineService vaccineService;

    @PostMapping("/addVaccine")
    public Vaccine addVaccine(@RequestBody Vaccine vaccine){
        return vaccineService.addVaccine(vaccine);
    }

    @PutMapping("/updateVaccine")
    public Vaccine updateVaccine(@RequestBody Vaccine vaccine){
        return  vaccineService.updateVaccine(vaccine);
    }

    @DeleteMapping("/deleteVaccine/{vaccineId}")
    public String deleteVaccine(@PathVariable String vaccineId){
        return vaccineService.deleteVaccine(vaccineId);
    }
}
