package com.demo.vaccinebooking.Controller;

import com.demo.vaccinebooking.Model.APIResponse;
import com.demo.vaccinebooking.Model.Vaccine;
import com.demo.vaccinebooking.Service.VaccineService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

@RestController
@Validated
public class VaccineController {
    @Autowired
    VaccineService vaccineService;

    @PostMapping("/addVaccine")
    public ResponseEntity<APIResponse> addVaccine(@Valid @RequestBody Vaccine vaccine) throws Exception {
        return vaccineService.addVaccine(vaccine);
    }

    @PutMapping("/updateVaccine/{vaccineId}")
    public ResponseEntity<APIResponse> updateVaccine(@PathVariable String vaccineId,
                                                     @Valid @NotBlank(message = "Number of shots cannot be blank") @Pattern(regexp = "[0-9]*", message = "Only enter positive digits.") @RequestParam String numberOfShots) throws Exception {
        return vaccineService.updateVaccine(vaccineId, numberOfShots);
    }

    @DeleteMapping("/deleteVaccine/{vaccineId}")
    public ResponseEntity<APIResponse> deleteVaccine(@PathVariable String vaccineId){
        return vaccineService.deleteVaccine(vaccineId);
    }
}
