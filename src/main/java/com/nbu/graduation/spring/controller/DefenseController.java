package com.nbu.graduation.spring.controller;

import com.nbu.graduation.spring.model.enums.Role;
import com.nbu.graduation.spring.model.service.DefenseServiceModel;
import com.nbu.graduation.spring.model.service.StudentServiceModel;
import com.nbu.graduation.spring.model.service.TeacherServiceModel;
import com.nbu.graduation.spring.model.service.UserServiceModel;
import com.nbu.graduation.spring.service.DefenseService;
import com.nbu.graduation.spring.service.StudentService;
import com.nbu.graduation.spring.service.TeacherService;
import com.nbu.graduation.spring.service.UserService;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.security.Principal;
import java.util.List;

@Controller
@RequestMapping("/defenses")
public class DefenseController {
private final UserService userService;
private final ModelMapper modelMapper;
private final TeacherService teacherService;
private final StudentService studentService;
private final DefenseService defenseService;

    public DefenseController(UserService userService, ModelMapper modelMapper, TeacherService teacherService, StudentService studentService, DefenseService defenseService) {
        this.userService = userService;
        this.modelMapper = modelMapper;
        this.teacherService = teacherService;
        this.studentService = studentService;
        this.defenseService = defenseService;
    }

    @GetMapping
    public String defenses(Model model, Principal principal) {
        UserServiceModel user = this.userService.findByUsername(principal.getName());

        if (user.getRole() == Role.STUDENT){
            StudentServiceModel student = this.studentService.findByUsername(principal.getName());
            model.addAttribute("student", student);
            model.addAttribute("isStudent", true);
            model.addAttribute("canParticipate",this.studentService.canParticipate(student.getId()));
        } else if (user.getRole() == Role.TEACHER){
            TeacherServiceModel teacher = this.teacherService.findByUsername(principal.getName());

            List<DefenseServiceModel> defenses = this.teacherService.findAllDefensesWhereTeacherParticipates(teacher.getId());

            model.addAttribute("teacher", teacher);
            model.addAttribute("isTeacher", true);
            model.addAttribute("defenses", defenses);
        }

        model.addAttribute("user", user);
        return "defenses";
    }

}
