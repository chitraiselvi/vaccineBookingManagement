package com.demo.vaccinebooking.Service;

import com.demo.vaccinebooking.Model.APIResponse;
import com.demo.vaccinebooking.Model.Slot;
import com.demo.vaccinebooking.Repository.SlotRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@SpringBootTest
public class SlotServiceTest {
    @InjectMocks
    SlotService slotService;
    @Mock
    SlotRepository slotRepository;
    private List<Slot> slots;
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
    }
    @Test
    public void addSlotsTest()
    {
        when(slotRepository.saveAll(slots)).thenReturn(slots);
        ResponseEntity<APIResponse> response = slotService.addSlots(slots);
        assertEquals(3, response.getBody().getData().size());
    }
    @Test
    public void saveSlotTest(){
        when(slotRepository.save(slots.get(0))).thenReturn(slots.get(0));
        Slot savedSlot = slotService.saveSlot(slots.get(0));
        assertEquals(1,savedSlot.getSlotId());
    }
    @Test
    public void getSlotsByDate()
    {
        when(slotRepository.findAllByAvailableDate(LocalDate.of(2022, 02, 19))).thenReturn(slots);
        List<Slot> returnedSlots=slotService.getSlotsByDate(LocalDate.of(2022, 02, 19));
        assertEquals(3,returnedSlots.size());

    }

    @Test
    public void getSlotsById()
{
    when(slotRepository.findById(1)).thenReturn(Optional.of(slots.get(0)));
    Optional<Slot> returnedSlots=slotService.getSlotsById(1);
    assertEquals(1,returnedSlots.get().getSlotId());
}

    @Test
    public void assignSlotToBookingTest()
    {
        when(slotRepository.findById(1)).thenReturn(Optional.of(slots.get(0)));
        when(slotRepository.save(slots.get(0))).thenReturn(slots.get(0));
        slotService.assignSlotToBooking(1);
        assertEquals(false, slots.get(0).isAvailable());
    }

    @Test
    public void unassignSlotToBookingTest()
    {
        when(slotRepository.findById(2)).thenReturn(Optional.of(slots.get(1)));
        when(slotRepository.save(slots.get(1))).thenReturn(slots.get(1));
        slotService.unassignSlotFromBooking(2);
        assertEquals(true, slots.get(1).isAvailable());
    }
}
