package com.healthcare.healthcaremanagement.feedback;

import com.healthcare.healthcaremanagement.doctor.DoctorService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/feedback")
public class FeedbackController {

    @Autowired private FeedbackService feedbackService;
    @Autowired private DoctorService doctorService;

    @GetMapping
    public String list(HttpSession session, Model model) {
        if (session.getAttribute("userRole") == null)
            return "redirect:/login";
        model.addAttribute("feedbacks", feedbackService.getAllFeedbacks());
        return "feedback/list";
    }

    @GetMapping("/add")
    public String showAddForm(HttpSession session, Model model) {
        if (session.getAttribute("userRole") == null)
            return "redirect:/login";
        if ("DOCTOR".equals(session.getAttribute("userRole")))
            return "redirect:/feedback";

        Feedback feedback = new Feedback();

        if ("PATIENT".equals(session.getAttribute("userRole"))) {
            feedback.setPatientName(
                    (String) session.getAttribute("loggedInUser"));
        }

        model.addAttribute("feedback", feedback);
        model.addAttribute("doctors", doctorService.getApprovedDoctors());
        return "feedback/add";
    }

    @PostMapping("/add")
    public String add(HttpSession session,
                      @ModelAttribute Feedback feedback) {
        if (session.getAttribute("userRole") == null)
            return "redirect:/login";
        if ("DOCTOR".equals(session.getAttribute("userRole")))
            return "redirect:/feedback";
        feedbackService.saveFeedback(feedback);
        return "redirect:/feedback";
    }


    @GetMapping("/edit/{id}")
    public String showEditForm(HttpSession session,
                               @PathVariable String id, Model model) {
        if (session.getAttribute("userRole") == null)
            return "redirect:/login";
        if ("DOCTOR".equals(session.getAttribute("userRole")))
            return "redirect:/feedback";
        model.addAttribute("feedback", feedbackService.getFeedbackById(id));
        model.addAttribute("doctors", doctorService.getApprovedDoctors());
        return "feedback/edit";
    }


    @PostMapping("/edit/{id}")
    public String edit(HttpSession session,
                       @PathVariable String id,
                       @ModelAttribute Feedback feedback) {
        if (session.getAttribute("userRole") == null)
            return "redirect:/login";
        if ("DOCTOR".equals(session.getAttribute("userRole")))
            return "redirect:/feedback";
        feedbackService.updateFeedback(id, feedback);
        return "redirect:/feedback";
    }

    @GetMapping("/delete/{id}")
    public String delete(HttpSession session, @PathVariable String id) {
        if (session.getAttribute("userRole") == null)
            return "redirect:/login";
        if ("DOCTOR".equals(session.getAttribute("userRole")))
            return "redirect:/feedback";
        feedbackService.deleteFeedback(id);
        return "redirect:/feedback";
    }
}