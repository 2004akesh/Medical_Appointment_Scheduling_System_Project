package com.healthcare.healthcaremanagement.appointment;

import com.healthcare.healthcaremanagement.scheduling.SchedulingService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@Controller
@RequestMapping("/appointments")
public class AppointmentController {

    @Autowired
    private AppointmentService appointmentService;

    @Autowired
    private SchedulingService schedulingService;

    private boolean isAdminOrDoctor(HttpSession session) {
        String role = (String) session.getAttribute("userRole");
        return "ADMIN".equals(role) || "DOCTOR".equals(role);
    }

    @GetMapping
    public String list(HttpSession session, Model model) {
        if (!isAdminOrDoctor(session)) return "redirect:/login";
        model.addAttribute("appointments", appointmentService.getAllAppointments());
        model.addAttribute("sortType", "default");
        return "appointment/list";
    }

    @GetMapping("/sort-by-priority")
    public String sortByPriority(HttpSession session, Model model) {
        if (!isAdminOrDoctor(session)) return "redirect:/login";
        model.addAttribute("appointments", appointmentService.getAppointmentsByPriority());
        model.addAttribute("sortType", "priority");
        return "appointment/list";
    }

    @GetMapping("/sort-by-time")
    public String sortByTime(HttpSession session, Model model) {
        if (!isAdminOrDoctor(session)) return "redirect:/login";
        model.addAttribute("appointments", appointmentService.getAppointmentsSortedByTime());
        model.addAttribute("sortType", "time");
        return "appointment/list";
    }

    @GetMapping("/add")
    public String showAddForm(HttpSession session, Model model) {
        if (!isAdminOrDoctor(session)) return "redirect:/login";
        model.addAttribute("appointment", new Appointment());
        return "appointment/add";
    }

    @PostMapping("/add")
    public String add(HttpSession session, @ModelAttribute Appointment appointment) {
        if (!isAdminOrDoctor(session)) return "redirect:/login";
        appointmentService.saveAppointment(appointment);
        return "redirect:/appointments";
    }

    @GetMapping("/edit/{id}")
    public String showEditForm(HttpSession session, @PathVariable String id, Model model) {
        if (!isAdminOrDoctor(session)) return "redirect:/login";
        model.addAttribute("appointment", appointmentService.getAppointmentById(id));
        return "appointment/edit";
    }

    @PostMapping("/edit/{id}")
    public String edit(HttpSession session, @PathVariable String id,
                       @ModelAttribute Appointment appointment) {
        if (!isAdminOrDoctor(session)) return "redirect:/login";
        appointmentService.updateAppointment(id, appointment);
        return "redirect:/appointments";
    }

    @GetMapping("/delete/{id}")
    public String delete(HttpSession session, @PathVariable String id) {
        if (!isAdminOrDoctor(session)) return "redirect:/login";
        appointmentService.deleteAppointment(id);
        return "redirect:/appointments";
    }

    @PostMapping("/book")
    public String bookAppointment(@RequestParam String patientName,
                                  @RequestParam String doctorName,
                                  @RequestParam String date,
                                  @RequestParam String timeSlot,
                                  @RequestParam int urgency) {
        // Save the appointment
        Appointment appointment = new Appointment();
        appointment.setPatientName(patientName);
        appointment.setDoctorName(doctorName);
        appointment.setDate(date);
        appointment.setTimeSlot(timeSlot);
        appointment.setUrgency(urgency);
        appointment.setStatus("Pending");
        appointmentService.saveAppointment(appointment);

        // Auto update doctor's current patients count
        List<com.healthcare.healthcaremanagement.scheduling.Schedule> schedules =
                schedulingService.getSchedulesByDoctor(doctorName);
        if (!schedules.isEmpty()) {
            com.healthcare.healthcaremanagement.scheduling.Schedule schedule = schedules.get(0);
            schedule.setCurrentPatients(schedule.getCurrentPatients() + 1);
            schedulingService.updateSchedule(schedule.getId(), schedule);
        }

        return "redirect:/home/patient";
    }
}