package com.healthcare.healthcaremanagement;

import com.healthcare.healthcaremanagement.admin.Admin;
import com.healthcare.healthcaremanagement.admin.AdminService;
import com.healthcare.healthcaremanagement.doctor.Doctor;
import com.healthcare.healthcaremanagement.doctor.DoctorService;
import com.healthcare.healthcaremanagement.patient.Patient;
import com.healthcare.healthcaremanagement.patient.PatientService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class LoginController {

    @Autowired private AdminService adminService;
    @Autowired private DoctorService doctorService;
    @Autowired private PatientService patientService;

    @GetMapping("/")
    public String root() { return "redirect:/login"; }

    @GetMapping("/login")
    public String loginPage() { return "login"; }

    @GetMapping("/login/admin")
    public String adminLoginPage() { return "login-admin"; }

    @GetMapping("/login/doctor")
    public String doctorLoginPage() { return "login-doctor"; }

    @GetMapping("/login/patient")
    public String patientLoginPage() { return "login-patient"; }

    @PostMapping("/login")
    public String login(@RequestParam String username,
                        @RequestParam String password,
                        @RequestParam(required = false) String role,
                        HttpSession session, Model model) {

        if ("ADMIN".equals(role)) {
            Admin admin = adminService.findByUsername(username);
            if (admin != null && admin.getPassword().equals(password)) {
                session.setAttribute("loggedInUser", admin.getName());
                session.setAttribute("userRole", "ADMIN");
                return "redirect:/home/admin";
            }
            model.addAttribute("error", "Invalid admin credentials!");
            return "login-admin";
        }

        if ("DOCTOR".equals(role)) {
            Doctor doctor = doctorService.findByNameAndPassword(username, password);
            if (doctor != null) {
                session.setAttribute("loggedInUser", doctor.getName());
                session.setAttribute("userRole", "DOCTOR");
                session.setAttribute("doctorId", doctor.getId());
                return "redirect:/home/doctor";
            }
            model.addAttribute("error", "Invalid credentials or account not approved!");
            return "login-doctor";
        }

        if ("PATIENT".equals(role)) {
            Patient patient = patientService.findByPhoneAndPassword(username, password);
            if (patient != null) {
                session.setAttribute("loggedInUser", patient.getName());
                session.setAttribute("userRole", "PATIENT");
                session.setAttribute("patientId", patient.getId());
                return "redirect:/home/patient";
            }
            model.addAttribute("error", "Invalid credentials or account not approved!");
            return "login-patient";
        }

        return "redirect:/login";
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/login";
    }
}