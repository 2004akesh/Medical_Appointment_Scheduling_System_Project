package com.healthcare.healthcaremanagement.appointment;

import com.healthcare.healthcaremanagement.doctor.DoctorService;
import com.healthcare.healthcaremanagement.patient.PatientService;
import com.healthcare.healthcaremanagement.scheduling.SchedulingService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/appointments")
public class AppointmentController {

    @Autowired private AppointmentService appointmentService;
    @Autowired private SchedulingService schedulingService;
    @Autowired private DoctorService doctorService;
    @Autowired private PatientService patientService;

    private boolean isAdminOrDoctor(HttpSession session) {
        String role = (String) session.getAttribute("userRole");
        return "ADMIN".equals(role) || "DOCTOR".equals(role);
    }


    @GetMapping
    public String list(HttpSession session, Model model) {
        if (!isAdminOrDoctor(session)) return "redirect:/login";
        model.addAttribute("appointments",
                appointmentService.getAllAppointments());
        model.addAttribute("sortType", "default");
        return "appointment/list";
    }


    @GetMapping("/sort-by-priority")
    public String sortByPriority(HttpSession session, Model model) {
        if (!isAdminOrDoctor(session)) return "redirect:/login";
        model.addAttribute("appointments",
                appointmentService.getAppointmentsByPriority());
        model.addAttribute("sortType", "priority");
        return "appointment/list";
    }


    @GetMapping("/sort-by-time")
    public String sortByTime(HttpSession session, Model model) {
        if (!isAdminOrDoctor(session)) return "redirect:/login";
        model.addAttribute("appointments",
                appointmentService.getAppointmentsSortedByTime());
        model.addAttribute("sortType", "time");
        return "appointment/list";
    }


    @GetMapping("/add")
    public String showAddForm(HttpSession session, Model model) {
        if (session.getAttribute("userRole") == null)
            return "redirect:/login";
        model.addAttribute("appointment", new Appointment());
        model.addAttribute("doctors", doctorService.getApprovedDoctors());
        model.addAttribute("patients", patientService.getApprovedPatients());
        model.addAttribute("availableSchedules",
                schedulingService.getAvailableSchedules());
        model.addAttribute("availableTimeSlots", new ArrayList<>());
        model.addAttribute("selectedDoctor", "");
        model.addAttribute("selectedDate", "");
        model.addAttribute("selectedPatient", "");
        return "appointment/add";
    }


    @GetMapping("/add/slots")
    public String getAvailableSlots(
            @RequestParam String doctorName,
            @RequestParam String date,
            @RequestParam(required = false, defaultValue = "") String patientName,
            HttpSession session, Model model) {
        if (session.getAttribute("userRole") == null)
            return "redirect:/login";

        model.addAttribute("appointment", new Appointment());
        model.addAttribute("doctors", doctorService.getApprovedDoctors());
        model.addAttribute("patients", patientService.getApprovedPatients());
        model.addAttribute("availableSchedules",
                schedulingService.getAvailableSchedules());
        model.addAttribute("selectedDoctor", doctorName);
        model.addAttribute("selectedDate", date);
        model.addAttribute("selectedPatient", patientName);

        List<String> bookedSlots = appointmentService.getAll().stream()
                .filter(a -> a.getDoctorName().equals(doctorName)
                        && a.getDate().equals(date)
                        && !a.getStatus().equals("Cancelled"))
                .map(a -> a.getTimeSlot())
                .toList();

        List<String> availableSlots = schedulingService
                .getAvailableSlots(doctorName, bookedSlots);
        model.addAttribute("availableTimeSlots", availableSlots);
        return "appointment/add";
    }


    @PostMapping("/add")
    public String add(HttpSession session,
                      @RequestParam String patientName,
                      @RequestParam String doctorName,
                      @RequestParam String date,
                      @RequestParam String timeSlot,
                      @RequestParam int urgency) {
        if (!isAdminOrDoctor(session)) return "redirect:/login";
        Appointment appointment = new Appointment();
        appointment.setPatientName(patientName);
        appointment.setDoctorName(doctorName);
        appointment.setDate(date);
        appointment.setTimeSlot(timeSlot);
        appointment.setUrgency(urgency);
        appointment.setStatus("Pending");
        appointmentService.saveAppointment(appointment);
        return "redirect:/appointments";
    }


    @GetMapping("/edit/{id}")
    public String showEditForm(HttpSession session,
                               @PathVariable String id, Model model) {
        if (!isAdminOrDoctor(session)) return "redirect:/login";
        Appointment appointment = appointmentService.getAppointmentById(id);
        model.addAttribute("appointment", appointment);
        model.addAttribute("doctors", doctorService.getApprovedDoctors());
        model.addAttribute("patients", patientService.getApprovedPatients());
        model.addAttribute("availableSchedules",
                schedulingService.getAvailableSchedules());
        model.addAttribute("selectedDoctor", appointment.getDoctorName());
        model.addAttribute("selectedDate", appointment.getDate());
        model.addAttribute("selectedPatient", appointment.getPatientName());
        model.addAttribute("selectedTimeSlot", appointment.getTimeSlot());

        List<String> bookedSlots = appointmentService.getAll().stream()
                .filter(a -> a.getDoctorName().equals(appointment.getDoctorName())
                        && a.getDate().equals(appointment.getDate())
                        && !a.getStatus().equals("Cancelled")
                        && !a.getId().equals(id))
                .map(a -> a.getTimeSlot())
                .toList();

        List<String> availableSlots = schedulingService
                .getAvailableSlots(appointment.getDoctorName(), bookedSlots);

        if (!availableSlots.contains(appointment.getTimeSlot())) {
            availableSlots.add(0, appointment.getTimeSlot());
        }

        model.addAttribute("availableTimeSlots", availableSlots);
        return "appointment/edit";
    }


    @GetMapping("/edit/{id}/slots")
    public String getEditAvailableSlots(
            @PathVariable String id,
            @RequestParam String doctorName,
            @RequestParam String date,
            @RequestParam(required = false, defaultValue = "") String patientName,
            HttpSession session, Model model) {
        if (!isAdminOrDoctor(session)) return "redirect:/login";

        Appointment appointment = appointmentService.getAppointmentById(id);
        model.addAttribute("appointment", appointment);
        model.addAttribute("doctors", doctorService.getApprovedDoctors());
        model.addAttribute("patients", patientService.getApprovedPatients());
        model.addAttribute("availableSchedules",
                schedulingService.getAvailableSchedules());
        model.addAttribute("selectedDoctor", doctorName);
        model.addAttribute("selectedDate", date);
        model.addAttribute("selectedPatient", patientName);
        model.addAttribute("selectedTimeSlot", "");

        List<String> bookedSlots = appointmentService.getAll().stream()
                .filter(a -> a.getDoctorName().equals(doctorName)
                        && a.getDate().equals(date)
                        && !a.getStatus().equals("Cancelled")
                        && !a.getId().equals(id))
                .map(a -> a.getTimeSlot())
                .toList();

        List<String> availableSlots = schedulingService
                .getAvailableSlots(doctorName, bookedSlots);
        model.addAttribute("availableTimeSlots", availableSlots);
        return "appointment/edit";
    }


    @PostMapping("/edit/{id}")
    public String edit(HttpSession session,
                       @PathVariable String id,
                       @RequestParam String patientName,
                       @RequestParam String doctorName,
                       @RequestParam String date,
                       @RequestParam String timeSlot,
                       @RequestParam String status,
                       @RequestParam int urgency) {
        if (!isAdminOrDoctor(session)) return "redirect:/login";
        Appointment appointment = appointmentService.getAppointmentById(id);
        appointment.setPatientName(patientName);
        appointment.setDoctorName(doctorName);
        appointment.setDate(date);
        appointment.setTimeSlot(timeSlot);
        appointment.setStatus(status);
        appointment.setUrgency(urgency);
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
                                  @RequestParam int urgency,
                                  HttpSession session) {

        if (appointmentService.isSlotAlreadyBooked(
                doctorName, date, timeSlot)) {
            session.setAttribute("bookingError",
                    "Sorry! This time slot is already booked! " +
                            "Please choose another.");
            return "redirect:/home/patient";
        }

        Appointment appointment = new Appointment();
        appointment.setPatientName(patientName);
        appointment.setDoctorName(doctorName);
        appointment.setDate(date);
        appointment.setTimeSlot(timeSlot);
        appointment.setUrgency(urgency);
        appointment.setStatus("Pending");
        appointmentService.saveAppointment(appointment);

        session.setAttribute("bookingError", null);
        return "redirect:/home/patient";
    }
}