package com.healthcare.healthcaremanagement.admin;

import com.healthcare.healthcaremanagement.doctor.DoctorService;
import com.healthcare.healthcaremanagement.patient.PatientService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/admin")
public class AdminController {

    @Autowired private AdminService adminService;
    @Autowired private DoctorService doctorService;
    @Autowired private PatientService patientService;

    @GetMapping
    public String list(HttpSession session, Model model) {
        if (!"ADMIN".equals(session.getAttribute("userRole"))) return "redirect:/login";
        model.addAttribute("admins", adminService.getAllAdmins());
        return "admin/list";
    }

    @GetMapping("/add")
    public String showAddForm(HttpSession session, Model model) {
        if (!"ADMIN".equals(session.getAttribute("userRole"))) return "redirect:/login";
        model.addAttribute("admin", new Admin());
        return "admin/add";
    }

    @PostMapping("/add")
    public String add(HttpSession session, @ModelAttribute Admin admin) {
        if (!"ADMIN".equals(session.getAttribute("userRole"))) return "redirect:/login";
        adminService.saveAdmin(admin);
        return "redirect:/admin";
    }

    @GetMapping("/edit/{id}")
    public String showEditForm(HttpSession session, @PathVariable String id, Model model) {
        if (!"ADMIN".equals(session.getAttribute("userRole"))) return "redirect:/login";
        model.addAttribute("admin", adminService.getAdminById(id));
        return "admin/edit";
    }

    @PostMapping("/edit/{id}")
    public String edit(HttpSession session, @PathVariable String id, @ModelAttribute Admin admin) {
        if (!"ADMIN".equals(session.getAttribute("userRole"))) return "redirect:/login";
        adminService.updateAdmin(id, admin);
        return "redirect:/admin";
    }

    @GetMapping("/delete/{id}")
    public String delete(HttpSession session, @PathVariable String id) {
        if (!"ADMIN".equals(session.getAttribute("userRole"))) return "redirect:/login";
        adminService.deleteAdmin(id);
        return "redirect:/admin";
    }

    @GetMapping("/requests")
    public String viewRequests(HttpSession session, Model model) {
        if (!"ADMIN".equals(session.getAttribute("userRole"))) return "redirect:/login";
        model.addAttribute("pendingDoctors", doctorService.getPendingDoctors());
        model.addAttribute("pendingPatients", patientService.getPendingPatients());
        return "admin/requests";
    }

    @GetMapping("/approve/doctor/{id}")
    public String approveDoctor(HttpSession session, @PathVariable String id) {
        if (!"ADMIN".equals(session.getAttribute("userRole"))) return "redirect:/login";
        doctorService.approveDoctor(id);
        return "redirect:/admin/requests";
    }

    @GetMapping("/reject/doctor/{id}")
    public String rejectDoctor(HttpSession session, @PathVariable String id) {
        if (!"ADMIN".equals(session.getAttribute("userRole"))) return "redirect:/login";
        doctorService.rejectDoctor(id);
        return "redirect:/admin/requests";
    }

    @GetMapping("/approve/patient/{id}")
    public String approvePatient(HttpSession session, @PathVariable String id) {
        if (!"ADMIN".equals(session.getAttribute("userRole"))) return "redirect:/login";
        patientService.approvePatient(id);
        return "redirect:/admin/requests";
    }

    @GetMapping("/reject/patient/{id}")
    public String rejectPatient(HttpSession session, @PathVariable String id) {
        if (!"ADMIN".equals(session.getAttribute("userRole"))) return "redirect:/login";
        patientService.rejectPatient(id);
        return "redirect:/admin/requests";
    }
}