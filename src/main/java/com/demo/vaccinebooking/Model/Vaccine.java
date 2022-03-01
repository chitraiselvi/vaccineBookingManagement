package com.demo.vaccinebooking.Model;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Entity
@JsonIgnoreProperties(value = {"hibernateLazyInitializer","handler"})
public class Vaccine {
    @Id
    @NotBlank(message = "Vaccine Id cannot be blank")
    @Getter
    @Setter
    private String  vaccineId;
    @NotBlank(message = "Vaccine Name cannot be blank")
    @Getter
    @Setter
    private String vaccineName;
    @NotNull(message = "Available shots cannot be blank")
    @Getter
    @Setter
    private int availableShots;
}
