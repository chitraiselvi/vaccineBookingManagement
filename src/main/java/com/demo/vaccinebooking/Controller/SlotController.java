package com.demo.vaccinebooking.Controller;

import com.demo.vaccinebooking.Model.APIResponse;
import com.demo.vaccinebooking.Model.Slot;
import com.demo.vaccinebooking.Service.SlotService;
import com.demo.vaccinebooking.Service.VaccineBookingService;
import org.hibernate.annotations.common.util.impl.LoggerFactory;
import org.jboss.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;

@RestController
public class SlotController {

    @Autowired
    SlotService slotService;

    @PostMapping("/addSlots")
    public ResponseEntity<APIResponse> addSlots(@Valid @RequestBody List<Slot> slots) {
        return slotService.addSlots(slots);
    }
}
