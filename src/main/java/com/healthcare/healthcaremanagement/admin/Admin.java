package com.healthcare.healthcaremanagement.admin;

import com.healthcare.healthcaremanagement.Person;


public class Admin extends Person {
    private String username;
    private String password;
    private String role;

    public Admin() {}

    public Admin(String id, String name, String phone,
                 String username, String password, String role) {
        super(id, name, phone);
        this.username = username;
        this.password = password;
        this.role = role;
    }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

  
    @Override
    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }
}