package com.demo.vaccinebooking.Controller;

import com.demo.vaccinebooking.Model.APIResponse;
import com.demo.vaccinebooking.Model.Slot;
import com.demo.vaccinebooking.Service.SlotService;
import com.demo.vaccinebooking.Service.VaccineService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@SpringBootTest
public class SlotControllerTest {
    @InjectMocks
    SlotController slotController;
    @Mock
    SlotService slotService;
    private List<Slot> slots;
    private ResponseEntity<APIResponse> response;
    @BeforeEach
    public void init() {
        slots = new ArrayList<>();
        Slot slot1 = new Slot();
        slot1.setAvailableDate(LocalDate.of(2022, 02, 19));
        slot1.setAvailableTime(LocalTime.of(10, 00));
        slot1.setAvailable(true);
        slot1.setSlotId(1);

        Slot slot2 = new Slot();
        slot2.setAvailableDate(LocalDate.of(2022, 02, 19));
        slot2.setAvailableTime(LocalTime.of(11, 00));
        slot2.setAvailable(false);
        slot2.setSlotId(2);

        Slot slot3 = new Slot();
        slot3.setAvailableDate(LocalDate.of(2022, 02, 19));
        slot3.setAvailableTime(LocalTime.of(13, 00));
        slot3.setAvailable(true);
        slot3.setSlotId(3);

        slots.add(slot1);
        slots.add(slot2);
        slots.add(slot3);

        APIResponse apiResponse = new APIResponse();
        apiResponse.setSuccess(true);
        response = new ResponseEntity<>(apiResponse, HttpStatus.OK);
    }
    @Test
    public void addSlotsTest(){
        when(slotService.addSlots(slots)).thenReturn(response);
        ResponseEntity<APIResponse> response = slotController.addSlots(slots);
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }
}
