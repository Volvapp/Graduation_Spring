package com.nbu.graduation.spring.init;

import com.nbu.graduation.spring.repository.StudentRepository;
import com.nbu.graduation.spring.repository.TeacherRepository;
import com.nbu.graduation.spring.repository.UserRepository;
import com.nbu.graduation.spring.service.StudentService;
import com.nbu.graduation.spring.service.TeacherService;
import com.nbu.graduation.spring.service.UserService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class AppInit implements CommandLineRunner {
    private final UserRepository userRepository;
    private final StudentRepository studentRepository;
    private final TeacherRepository teacherRepository;

    private final UserService userService;
    private final StudentService studentService;
    private final TeacherService teacherService;

    public AppInit(UserRepository userRepository, StudentRepository studentRepository, TeacherRepository teacherRepository, UserService userService, StudentService studentService, TeacherService teacherService) {
        this.userRepository = userRepository;
        this.studentRepository = studentRepository;
        this.teacherRepository = teacherRepository;
        this.userService = userService;
        this.studentService = studentService;
        this.teacherService = teacherService;
    }


    @Override
    public void run(String... args) throws Exception {
        if (studentRepository.count() == 0) {
            studentService.initializeStudents();
        }if (teacherRepository.count() == 0) {
            teacherService.initializeTeachers();
        }
        if (userRepository.count() == 0){
            userService.initializeUsers();
        }
    }
}
