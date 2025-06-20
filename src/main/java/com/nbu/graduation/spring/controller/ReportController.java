package com.nbu.graduation.spring.controller;

import com.nbu.graduation.spring.service.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDate;

@Controller
@RequestMapping("/reports")
public class ReportController {

    private final StudentService studentService;

    private final ThesisReviewService reviewService;
    private final ThesisAssignmentService thesisAssignmentService;
    private final DefenseService defenseService;

    public ReportController(StudentService studentService, ThesisReviewService reviewService, ThesisAssignmentService thesisAssignmentService, DefenseService defenseService) {
        this.studentService = studentService;
        this.reviewService = reviewService;
        this.thesisAssignmentService = thesisAssignmentService;
        this.defenseService = defenseService;
    }

    @GetMapping
    public String showReportsPage() {
        return "reports";
    }

    @GetMapping("/approved")
    public String getApprovedTheses(Model model) {
        model.addAttribute("approvedAssignments", thesisAssignmentService.getAllApprovedAssignments());
        return "reports";
    }

    @GetMapping("/by-title")
    public String getByTitle(@RequestParam String keyword, Model model) {
        model.addAttribute("assignmentsByTitle", thesisAssignmentService.findByTitleContaining(keyword));
        return "reports";
    }

    @GetMapping("/approved-by-supervisor")
    public String getBySupervisor(@RequestParam Long supervisorId, Model model) {
        
        model.addAttribute("approvedAssignmentsBySupervisor", thesisAssignmentService.findAllByApprovedAndSupervisor(supervisorId));
        return "reports";
    }

    @GetMapping("/graduated")
    public String graduated(@RequestParam LocalDate from, @RequestParam LocalDate to, Model model,
                            RedirectAttributes redirectAttributes) {
        if (from.isAfter(to)) {
            redirectAttributes.addFlashAttribute("error", "Грешка при заявката!");
                return "redirect:/reports";
        }
        model.addAttribute("graduatedStudentsInPeriod", studentService.getGraduatedInPeriod(from, to));
        return "reports";
    }

    @GetMapping("/negative-count")
    public String negativeCount(Model model) {
        model.addAttribute("negativeReviewsCount", reviewService.countNegativeReviews());
        return "reports";
    }

    @GetMapping("/average-defended")
    public String averageDefended(@RequestParam LocalDate from, @RequestParam LocalDate to, Model model,
            RedirectAttributes redirectAttributes) {
        if (from.isAfter(to)){
            redirectAttributes.addFlashAttribute("error", "Грешка при заявката!");
            return "redirect:/reports";
        }
        model.addAttribute("averageStudentsDefended", defenseService.averagePresentedInPeriod(from, to));
        return "reports";
    }

    @GetMapping("/successful-by-supervisor")
    public String successfulBySupervisor(@RequestParam Long supervisorId, Model model) {
        model.addAttribute("successfulStudentsBySupervisor", studentService.countSuccessfulBySupervisor(supervisorId));
        return "reports";
    }

}
