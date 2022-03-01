package com.demo.vaccinebooking.Service;

import com.demo.vaccinebooking.Exception.VaccineBookingException;
import com.demo.vaccinebooking.Model.APIResponse;
import com.demo.vaccinebooking.Model.Vaccine;
import com.demo.vaccinebooking.Repository.VaccineRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;
@SpringBootTest
public class VaccineServiceTest {
    @InjectMocks
    private VaccineService vaccineservice;

    @Mock
    private VaccineRepository vaccineRepository;

    private Vaccine vaccine;

    @BeforeEach
    public void init() {
        vaccine = new Vaccine();
        vaccine.setVaccineId("Pf001");
        vaccine.setVaccineName("Pfizer");
        vaccine.setAvailableShots(100);
    }

    @Test
    public void getVaccineByNameTest()
    {
        when(vaccineRepository.getByVaccineName("Pfizer")).thenReturn(Optional.of(vaccine));
        Vaccine returnedVaccine = vaccineservice.getVaccineByName("Pfizer").get();
        assertEquals("Pfizer",returnedVaccine.getVaccineName());
        assertEquals("Pf001",returnedVaccine.getVaccineId());
        assertEquals(100,returnedVaccine.getAvailableShots());
    }

    @Test
    public void addVaccineTest() throws Exception {

        Vaccine newVaccine=new Vaccine();
        newVaccine.setVaccineId("MA001");
        newVaccine.setVaccineName("Moderna");
        newVaccine.setAvailableShots(10);
        when(vaccineRepository.save(newVaccine)).thenReturn(newVaccine);
        when(vaccineRepository.findById("Pf001")).thenReturn(Optional.of(vaccine));

        ResponseEntity<APIResponse> response = vaccineservice.addVaccine(newVaccine);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(true, response.getBody().getSuccess());
    }

    @Test
    public void addVaccineTestNegative() throws Exception {

        Vaccine newVaccine=new Vaccine();
        newVaccine.setVaccineId("Pf001");
        newVaccine.setVaccineName("Moderna");
        newVaccine.setAvailableShots(10);
        when(vaccineRepository.save(newVaccine)).thenReturn(newVaccine);
        when(vaccineRepository.findById("Pf001")).thenReturn(Optional.of(vaccine));

        VaccineBookingException exception = assertThrows(VaccineBookingException.class, () -> {
            vaccineservice.addVaccine(newVaccine);
        });
        assertEquals("There is already a vaccine record available for this id. You cannot use the same id for two vaccines.", exception.getMessage());
    }
    @Test
    public void updateVaccineTest() throws Exception {
        when(vaccineRepository.findById("Pf001")).thenReturn(Optional.of(vaccine));
        when(vaccineRepository.save(vaccine)).thenReturn(vaccine);
        ResponseEntity<APIResponse> response = vaccineservice.updateVaccine("Pf001","150");
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(true, response.getBody().getSuccess());
    }

    @Test
    public void updateVaccineTestNegative() throws Exception {
        when(vaccineRepository.findById("Pf001")).thenReturn(Optional.of(vaccine));
        when(vaccineRepository.save(vaccine)).thenReturn(vaccine);
        VaccineBookingException exception = assertThrows(VaccineBookingException.class, () -> {
            vaccineservice.updateVaccine("MA001","150");
        });
        assertEquals("There is no vaccine available for this id.", exception.getMessage());
    }
    @Test
    public void deleteVaccineTest()  {
        //when(vaccineRepository.deleteById("Pf001")).thenReturn(null);
        ResponseEntity<APIResponse> response = vaccineservice.deleteVaccine("Pf001");
        assertEquals("Vaccine deleted successfully", response.getBody().getMessage());
    }

    @Test
    public void saveVaccineTest()  {
        when(vaccineRepository.save(vaccine)).thenReturn(vaccine);
        Vaccine savedVaccine= vaccineservice.saveVaccine(vaccine);
        assertEquals("Pf001", savedVaccine.getVaccineId());
    }

    @Test
    public void decreaseVaccineShotsTest(){
        when(vaccineRepository.getByVaccineName("Pfizer")).thenReturn(Optional.of(vaccine));
        when(vaccineRepository.save(vaccine)).thenReturn(vaccine);
        vaccineservice.decreaseVaccineShots("Pfizer");
        assertEquals(99, vaccine.getAvailableShots());
    }

    @Test
    public void increaseVaccineShotsTest(){
        when(vaccineRepository.getByVaccineName("Pfizer")).thenReturn(Optional.of(vaccine));
        when(vaccineRepository.save(vaccine)).thenReturn(vaccine);
        vaccineservice.increaseVaccineShots("Pfizer");
        assertEquals(101, vaccine.getAvailableShots());
    }

}

