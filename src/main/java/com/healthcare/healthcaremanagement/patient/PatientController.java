package com.healthcare.healthcaremanagement.patient;

import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/patients") // Base URL for all patient-related pages
public class PatientController {

    @Autowired // Auto-links the service class so we don't have to instantiate it manually
    private PatientService patientService;

    @GetMapping
    public String listPatients(HttpSession session, Model model) {
        // Basic security check - kicking non-admins back to the login page
        if (!"ADMIN".equals(session.getAttribute("userRole"))) return "redirect:/login";
        model.addAttribute("patients", patientService.getAllPatients());
        return "patient/list";
    }

    @GetMapping("/add")
    public String showAddForm(HttpSession session, Model model) {
        if (!"ADMIN".equals(session.getAttribute("userRole"))) return "redirect:/login";
        model.addAttribute("patient", new Patient());
        return "patient/add";
    }

    @PostMapping("/add")
    public String addPatient(HttpSession session, @ModelAttribute Patient patient) {
        if (!"ADMIN".equals(session.getAttribute("userRole"))) return "redirect:/login";
        patientService.savePatient(patient);
        return "redirect:/patients";
    }

    @GetMapping("/edit/{id}")
    public String showEditForm(HttpSession session, @PathVariable String id, Model model) {
        if (!"ADMIN".equals(session.getAttribute("userRole"))) return "redirect:/login";
        model.addAttribute("patient", patientService.getPatientById(id));
        return "patient/edit";
    }

    @PostMapping("/edit/{id}")
    public String editPatient(HttpSession session, @PathVariable String id, @ModelAttribute Patient patient) {
        if (!"ADMIN".equals(session.getAttribute("userRole"))) return "redirect:/login";
        patientService.updatePatient(id, patient);
        return "redirect:/patients";
    }

    @GetMapping("/delete/{id}")
    public String deletePatient(HttpSession session, @PathVariable String id) {
        if (!"ADMIN".equals(session.getAttribute("userRole"))) return "redirect:/login";
        patientService.deletePatient(id);
        return "redirect:/patients";
    }
}
