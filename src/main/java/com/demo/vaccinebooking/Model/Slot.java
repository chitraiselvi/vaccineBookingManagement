package com.demo.vaccinebooking.Model;
import lombok.Getter;
import lombok.Setter;
import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.time.LocalTime;

@Entity
public class Slot {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Getter
    @Setter
    private int slotId;
    @NotNull(message = "Available Date cannot be blank")
    //@Pattern(regexp = "[0-3][0-9]/[0-1][1-9]/[0-9][0-9][0-9][0-9]", message = "Invalid Date")
    @Getter
    @Setter
    private LocalDate availableDate;
    @NotNull(message = "Available Time cannot be blank")
    @Getter
    @Setter
    private LocalTime availableTime;
    @Getter
    @Setter
    private boolean isAvailable = true;
}
