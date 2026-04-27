package com.healthcare.healthcaremanagement.patient;

import com.healthcare.healthcaremanagement.Person;

// Inherits from the abstract class "Person"


public class Patient extends Person {

    // Encapsulation - prvt attributes patient class should have of its own
    private int age;
    private String gender;
    private String password;
    private String status; // Pending, Approved, Rejected

    //Default Constructor

    public Patient() {}

    //Overloaded / Parameterized Constructor...
    public Patient(String id, String name, int age, String gender,
                   String phone, String password, String status) {
        super(id, name, phone); // calls parent class constuctor for these attrubutes to be assinged
        this.age = age;
        this.gender = gender;
        this.password = password;
        this.status = status;
    }

    //setterr and getters for encapsulated attributes
    public int getAge() { return age; }
    public void setAge(int age) { this.age = age; }

    public String getGender() { return gender; }
    public void setGender(String gender) { this.gender = gender; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    @Override  //polymorphism
    public String getRole() { return "Patient"; }
}