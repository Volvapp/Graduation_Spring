package com.nbu.graduation.spring.controller;

import com.nbu.graduation.spring.model.enums.Position;

import com.nbu.graduation.spring.model.service.TeacherServiceModel;
import com.nbu.graduation.spring.model.service.UserServiceModel;
import com.nbu.graduation.spring.service.DefenseService;
import com.nbu.graduation.spring.service.TeacherService;
import com.nbu.graduation.spring.service.UserService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;
import java.time.LocalDate;

@Controller
@RequestMapping("/teachers")
public class TeacherController {
    private final TeacherService teacherService;
    private final UserService userService;
    private final DefenseService defenseService;

    public TeacherController(TeacherService teacherService, UserService userService, DefenseService defenseService) {
        this.teacherService = teacherService;
        this.userService = userService;
        this.defenseService = defenseService;
    }

    @GetMapping()
    public String showTeachers(Model model) {
        model.addAttribute("teachers", teacherService.findAll());
        return "teachers";
    }

    @GetMapping("/roles")
    public String showRolesPage(Model model, Principal principal) {
        UserServiceModel user = userService.findByUsername(principal.getName());
        TeacherServiceModel teacherByRole = teacherService.findByUserId(user.getId());

        model.addAttribute("positions", Position.values());
        model.addAttribute("currentPosition", teacherByRole.getPosition());

        return "teachers-roles";
    }

    @PostMapping("/roles")
    public String updateRole(@RequestParam Position position,
                             Principal principal,
                             RedirectAttributes redirectAttributes) {
        teacherService.updateTeacherPosition(principal.getName(), position);
        redirectAttributes.addFlashAttribute("successMessage", "Длъжността беше успешно променена!");
        return "redirect:/teachers/roles";
    }

    @PostMapping("/set-defense-date")
    public String setDefenseDate(@RequestParam Long defenseId,
                                 @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate defenseDate,
                                 RedirectAttributes redirectAttributes,
                                 Principal principal) {
        if (defenseDate.isBefore(LocalDate.now())) {
            redirectAttributes.addFlashAttribute("error", "Дата не може да е преди днес!");
            return "redirect:/defenses";
        }
        TeacherServiceModel teacher = teacherService.findByUsername(principal.getName());

        defenseService.setDefenseDateIfCommitteeMember(defenseId, teacher.getId(), defenseDate);

        return "redirect:/defenses";
    }


    @PostMapping("/grade")
    public String grade(@RequestParam Long studentId,
                        @RequestParam Double grade,
                        Principal principal) {

        String name = principal.getName();
        TeacherServiceModel teacher = teacherService.findByUsername(name);

        defenseService.assignGradeToStudent(studentId, grade, teacher.getId());

        return "redirect:/defenses";
    }


}
