package com.nbu.graduation.spring.controller;

import com.nbu.graduation.spring.model.service.StudentServiceModel;

import com.nbu.graduation.spring.service.StudentService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@RequestMapping("/students")
public class StudentController {
    private final StudentService studentService;

    public StudentController(StudentService studentService) {
        this.studentService = studentService;
    }

    @GetMapping
    public String students(Model model) {
        List<StudentServiceModel> students = this.studentService.findAll();

        model.addAttribute("students", students);
        return "students";
    }

    @PostMapping("/defend")
    public String participate(@RequestParam Long studentId){
        this.studentService.markParticipation(studentId);
        return "redirect:/defenses";
    }

}
