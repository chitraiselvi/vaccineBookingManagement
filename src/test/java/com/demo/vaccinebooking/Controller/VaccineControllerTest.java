package com.demo.vaccinebooking.Controller;

import com.demo.vaccinebooking.Model.APIResponse;
import com.demo.vaccinebooking.Model.Vaccine;
import com.demo.vaccinebooking.Repository.VaccineRepository;
import com.demo.vaccinebooking.Service.VaccineService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@SpringBootTest
public class VaccineControllerTest {
    @InjectMocks
    VaccineController vaccineController;
    @Mock
    VaccineService vaccineService;
    @Mock
    VaccineRepository vaccineRepository;

    private Vaccine vaccine;
    private ResponseEntity<APIResponse> response;

    @BeforeEach
    public void init() {
        vaccine = new Vaccine();
        vaccine.setVaccineId("Pf001");
        vaccine.setVaccineName("Pfizer");
        vaccine.setAvailableShots(100);

        APIResponse apiResponse = new APIResponse();
        apiResponse.setSuccess(true);
        response = new ResponseEntity<>(apiResponse, HttpStatus.OK);
    }
    @Test
     public void addVaccineTest() throws Exception{
        when(vaccineService.addVaccine(vaccine)).thenReturn(response);
        ResponseEntity<APIResponse> response=vaccineController.addVaccine(vaccine);
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }
    @Test
    public void updateVaccineTest() throws Exception{
        when(vaccineService.updateVaccine("Pf001","200")).thenReturn(response);
        ResponseEntity<APIResponse> response= vaccineController.updateVaccine("Pf001","200");
        System.out.println(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    public void deleteVaccineTest() throws Exception{
        when(vaccineService.deleteVaccine("Pf001")).thenReturn(response);
        ResponseEntity<APIResponse> response= vaccineController.deleteVaccine("Pf001");
        System.out.println(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

}
