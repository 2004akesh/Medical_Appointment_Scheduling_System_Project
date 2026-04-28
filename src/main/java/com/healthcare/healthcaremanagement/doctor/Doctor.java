package com.healthcare.healthcaremanagement.doctor;

import com.healthcare.healthcaremanagement.Person;

public class Doctor extends Person {
    private String specialization;
    private String availability;
    private String password;
    private String status; 

    public Doctor() {}

    public Doctor(String id, String name, String phone, String specialization,
                  String availability, String password, String status) {
        super(id, name, phone);
        this.specialization = specialization;
        this.availability = availability;
        this.password = password;
        this.status = status;
    }

    public String getSpecialization() { return specialization; }
    public void setSpecialization(String specialization) { this.specialization = specialization; }

    public String getAvailability() { return availability; }
    public void setAvailability(String availability) { this.availability = availability; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    @Override
    public String getRole() { return "Doctor"; }
}