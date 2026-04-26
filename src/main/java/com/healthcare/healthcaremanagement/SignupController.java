package com.healthcare.healthcaremanagement;

import com.healthcare.healthcaremanagement.doctor.Doctor;
import com.healthcare.healthcaremanagement.doctor.DoctorService;
import com.healthcare.healthcaremanagement.patient.Patient;
import com.healthcare.healthcaremanagement.patient.PatientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/signup")
public class SignupController {

    @Autowired private PatientService patientService;
    @Autowired private DoctorService doctorService;

    @GetMapping
    public String signupPage() { return "signup"; }

    @GetMapping("/patient")
    public String patientSignupPage(Model model) {
        model.addAttribute("patient", new Patient());
        return "signup-patient";
    }

    @PostMapping("/patient")
    public String patientSignup(@ModelAttribute Patient patient, Model model) {
        patient.setStatus("Pending");
        patientService.save(patient);
        model.addAttribute("message",
                "Registration submitted! Please wait for admin approval before logging in.");
        return "signup-success";
    }

    @GetMapping("/doctor")
    public String doctorSignupPage(Model model) {
        model.addAttribute("doctor", new Doctor());
        return "signup-doctor";
    }

    @PostMapping("/doctor")
    public String doctorSignup(@ModelAttribute Doctor doctor, Model model) {
        doctor.setStatus("Pending");
        doctorService.save(doctor);
        model.addAttribute("message",
                "Registration submitted! Please wait for admin approval before logging in.");
        return "signup-success";
    }
}