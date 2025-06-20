package com.nbu.graduation.spring.controller;

import com.nbu.graduation.spring.model.dto.ThesisAssignmentDTO;
import com.nbu.graduation.spring.model.enums.Role;
import com.nbu.graduation.spring.model.service.UserServiceModel;
import com.nbu.graduation.spring.service.ThesisAssignmentService;
import com.nbu.graduation.spring.service.UserService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@Controller
@RequestMapping("/assignments")
public class ThesisAssignmentController {

    private final ThesisAssignmentService thesisAssignmentService;
    private final UserService userService;

    public ThesisAssignmentController(ThesisAssignmentService thesisAssignmentService, UserService userService) {
        this.thesisAssignmentService = thesisAssignmentService;
        this.userService = userService;
    }

    @GetMapping
    public String showAssignments(Model model, Principal principal) {
        String username = principal.getName();
        UserServiceModel user = userService.findByUsername(username);

        model.addAttribute("user", user);
        model.addAllAttributes(thesisAssignmentService.getAssignmentsViewModel(user));

        return "assignments";
    }

    @GetMapping("/propose")
    public String showProposeForm(Model model, Principal principal) {
        model.addAttribute("assignmentForm", new ThesisAssignmentDTO());

        String username = principal.getName();
        model.addAllAttributes(thesisAssignmentService.getProposeViewModel(username));

        return "assignments-propose";
    }

    @PostMapping("/propose")
    public String proposeAssignment(
            @Valid @ModelAttribute("assignmentForm") ThesisAssignmentDTO thesisAssignmentDTO,
            BindingResult bindingResult,
            Model model,
            Principal principal) {

        if (bindingResult.hasErrors()) {
            model.addAllAttributes(thesisAssignmentService.getProposeViewModel(principal.getName()));
            return "assignments-propose";
        }

        thesisAssignmentService.proposeAssignment(thesisAssignmentDTO, principal.getName());

        return "redirect:/assignments";
    }

    @PostMapping
    public String handleSupervisorAction(
            @RequestParam Long assignmentId,
            @RequestParam String action) {

        thesisAssignmentService.handleSupervisorDecision(assignmentId, action);
        return "redirect:/assignments";
    }

    @GetMapping("/approval")
    public String showApprovalPage(Model model, Principal principal) {
        String username = principal.getName();
        UserServiceModel user = userService.findByUsername(username);

        model.addAttribute("assignments", thesisAssignmentService.getAssignmentsForDepartmentApproval(user));
        model.addAttribute("showButtons", user.getRole() == Role.TEACHER);
        return "assignments-approval";
    }

    @PostMapping("/approval")
    public String handleDepartmentApproval(
            @RequestParam Long assignmentId,
            @RequestParam String action,
            Principal principal) {

        thesisAssignmentService.handleDepartmentDecision(assignmentId, principal.getName(), "approve".equals(action));
        return "redirect:/assignments/approval";
    }
}
