package com.healthcare.healthcaremanagement.scheduling;

import com.healthcare.healthcaremanagement.appointment.AppointmentService;
import com.healthcare.healthcaremanagement.doctor.DoctorService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.List;

@Controller
@RequestMapping("/scheduling")
public class SchedulingController {

    @Autowired private SchedulingService schedulingService;
    @Autowired private AppointmentService appointmentService;
    @Autowired private DoctorService doctorService;

    private boolean isAdminOrDoctor(HttpSession session) {
        String role = (String) session.getAttribute("userRole");
        return "ADMIN".equals(role) || "DOCTOR".equals(role);
    }


    @GetMapping
    public String list(HttpSession session, Model model) {
        if (!isAdminOrDoctor(session)) return "redirect:/login";

        String role = (String) session.getAttribute("userRole");
        List<Schedule> schedules;

        if ("DOCTOR".equals(role)) {
            String doctorName = (String) session.getAttribute("loggedInUser");
            schedules = schedulingService.getSchedulesByDoctor(doctorName);
        } else {
            schedules = schedulingService.getAllSchedules();
        }

        String today = java.time.LocalDate.now().toString();


        Map<String, Long> bookedToday = appointmentService.getAll().stream()
                .filter(a -> a.getDate().equals(today)
                        && !a.getStatus().equals("Cancelled"))
                .collect(Collectors.groupingBy(
                        a -> a.getDoctorName(),
                        Collectors.counting()));


        long totalBooked = bookedToday.values().stream()
                .mapToLong(v -> v).sum();

        model.addAttribute("schedules", schedules);
        model.addAttribute("bookedToday", bookedToday);
        model.addAttribute("totalBookedToday", totalBooked); // ← ADD THIS
        model.addAttribute("today", today);
        return "scheduling/list";
    }

    @GetMapping("/add")
    public String showAddForm(HttpSession session, Model model) {
        if (!isAdminOrDoctor(session)) return "redirect:/login";
        Schedule schedule = new Schedule();
        if ("DOCTOR".equals(session.getAttribute("userRole"))) {
            schedule.setDoctorName(
                    (String) session.getAttribute("loggedInUser"));
        }
        model.addAttribute("schedule", schedule);
        model.addAttribute("doctors", doctorService.getApprovedDoctors());
        return "scheduling/add";
    }

    @PostMapping("/add")
    public String add(HttpSession session,
                      @ModelAttribute Schedule schedule) {
        if (!isAdminOrDoctor(session)) return "redirect:/login";
        if ("DOCTOR".equals(session.getAttribute("userRole"))) {
            schedule.setDoctorName(
                    (String) session.getAttribute("loggedInUser"));
        }
        schedulingService.saveSchedule(schedule);
        return "redirect:/scheduling";
    }

    @GetMapping("/edit/{id}")
    public String showEditForm(HttpSession session,
                               @PathVariable String id, Model model) {
        if (!isAdminOrDoctor(session)) return "redirect:/login";
        model.addAttribute("schedule",
                schedulingService.getScheduleById(id));
        return "scheduling/edit";
    }


    @PostMapping("/edit/{id}")
    public String edit(HttpSession session,
                       @PathVariable String id,
                       @ModelAttribute Schedule schedule) {
        if (!isAdminOrDoctor(session)) return "redirect:/login";
        if ("DOCTOR".equals(session.getAttribute("userRole"))) {
            schedule.setDoctorName(
                    (String) session.getAttribute("loggedInUser"));
        }
        schedulingService.updateSchedule(id, schedule);
        return "redirect:/scheduling";
    }


    @GetMapping("/delete/{id}")
    public String delete(HttpSession session, @PathVariable String id) {
        if (!isAdminOrDoctor(session)) return "redirect:/login";
        schedulingService.deleteSchedule(id);
        return "redirect:/scheduling";
    }
}