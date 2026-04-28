package com.healthcare.healthcaremanagement.doctor;

import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/doctors")
public class DoctorController {

    @Autowired
    private DoctorService doctorService;

    @GetMapping
    public String listDoctors(HttpSession session, Model model) {
        if (!"ADMIN".equals(session.getAttribute("userRole"))) return "redirect:/login";
        model.addAttribute("doctors", doctorService.getAllDoctors());
        return "doctor/list";
    }

    @GetMapping("/add")
    public String showAddForm(HttpSession session, Model model) {
        if (!"ADMIN".equals(session.getAttribute("userRole"))) return "redirect:/login";
        model.addAttribute("doctor", new Doctor());
        return "doctor/add";
    }

    @PostMapping("/add")
    public String addDoctor(HttpSession session, @ModelAttribute Doctor doctor) {
        if (!"ADMIN".equals(session.getAttribute("userRole"))) return "redirect:/login";
        doctorService.saveDoctor(doctor);
        return "redirect:/doctors";
    }

    @GetMapping("/edit/{id}")
    public String showEditForm(HttpSession session, @PathVariable String id, Model model) {
        if (!"ADMIN".equals(session.getAttribute("userRole"))) return "redirect:/login";
        model.addAttribute("doctor", doctorService.getDoctorById(id));
        return "doctor/edit";
    }

    @PostMapping("/edit/{id}")
    public String editDoctor(HttpSession session, @PathVariable String id, @ModelAttribute Doctor doctor) {
        if (!"ADMIN".equals(session.getAttribute("userRole"))) return "redirect:/login";
        doctorService.updateDoctor(id, doctor);
        return "redirect:/doctors";
    }

    @GetMapping("/delete/{id}")
    public String deleteDoctor(HttpSession session, @PathVariable String id) {
        if (!"ADMIN".equals(session.getAttribute("userRole"))) return "redirect:/login";
        doctorService.deleteDoctor(id);
        return "redirect:/doctors";
    }
}