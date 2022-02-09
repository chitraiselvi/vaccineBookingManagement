package com.demo.vaccinebooking.Model;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class Vaccine {
    @Id
    private String  vaccineId;
    private String vaccineName;
    private int availableShots;

    public String getVaccineId() {
        return vaccineId;
    }

    public void setVaccineId(String vaccineId) {
        this.vaccineId = vaccineId;
    }

    public String getVaccineName() {
        return vaccineName;
    }

    public void setVaccineName(String vaccineName) {
        this.vaccineName = vaccineName;
    }

    public int getAvailableShots() {
        return availableShots;
    }

    public void setAvailableShots(int availableShots) {
        this.availableShots = availableShots;
    }
}
