package com.demo.vaccinebooking.Model;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;
import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@Entity
@JsonIgnoreProperties(value = {"hibernateLazyInitializer","handler"})
public class Booking {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Getter
    @Setter
    private int bookingId;
    @NotBlank(message = "Email Id cannot be blank")
    @Email(message = "Your email id is invalid ")
    @Getter
    @Setter
    private String emailId;
    @NotBlank(message = "First name cannot be blank")
    @Getter
    @Setter
    private String firstName;
    @NotBlank(message = "Last Name cannot be blank")
    @Getter
    @Setter
    private String lastName;
    @NotBlank(message = "Vaccine name cannot be blank")
    @Getter
    @Setter
    private String vaccineName;
    @Getter
    @Setter
    private String mobileNo;
    @OneToOne(cascade = {CascadeType.DETACH})
    @JoinColumn(name = "slotId")
    @Getter
    @Setter
    private Slot slot;
}
