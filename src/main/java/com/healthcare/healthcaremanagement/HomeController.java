package com.healthcare.healthcaremanagement;

import com.healthcare.healthcaremanagement.admin.AdminService;
import com.healthcare.healthcaremanagement.appointment.AppointmentService;
import com.healthcare.healthcaremanagement.doctor.DoctorService;
import com.healthcare.healthcaremanagement.patient.PatientService;
import com.healthcare.healthcaremanagement.scheduling.SchedulingService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/home")
public class HomeController {

    @Autowired private DoctorService doctorService;
    @Autowired private AppointmentService appointmentService;
    @Autowired private PatientService patientService;
    @Autowired private AdminService adminService;
    @Autowired private SchedulingService schedulingService;

    @GetMapping("/admin")
    public String adminHome(HttpSession session, Model model) {
        if (!"ADMIN".equals(session.getAttribute("userRole")))
            return "redirect:/login";
        model.addAttribute("userName", session.getAttribute("loggedInUser"));
        int pendingCount = doctorService.getPendingDoctors().size()
                + patientService.getPendingPatients().size();
        model.addAttribute("pendingCount", pendingCount);
        return "home/admin";
    }

    @GetMapping("/doctor")
    public String doctorHome(HttpSession session, Model model) {
        if (!"DOCTOR".equals(session.getAttribute("userRole")))
            return "redirect:/login";
        model.addAttribute("userName", session.getAttribute("loggedInUser"));
        String doctorName = (String) session.getAttribute("loggedInUser");
        // Show only this doctor's appointments
        model.addAttribute("myAppointments", appointmentService.getAll().stream()
                .filter(a -> a.getDoctorName().equals(doctorName))
                .toList());
        return "home/doctor";
    }

    @GetMapping("/patient")
    public String patientHome(HttpSession session, Model model) {
        if (!"PATIENT".equals(session.getAttribute("userRole")))
            return "redirect:/login";
        model.addAttribute("userName", session.getAttribute("loggedInUser"));

        // Show only approved doctors who have available schedules
        model.addAttribute("doctors", doctorService.getApprovedDoctors());

        // Show available schedules for patient to see
        model.addAttribute("availableSchedules",
                schedulingService.getAvailableSchedules());

        String patientName = (String) session.getAttribute("loggedInUser");
        // Show only this patient's appointments
        model.addAttribute("myAppointments", appointmentService.getAll().stream()
                .filter(a -> a.getPatientName().equals(patientName))
                .toList());
        return "home/patient";
    }
}