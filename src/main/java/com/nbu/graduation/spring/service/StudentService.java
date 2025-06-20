package com.nbu.graduation.spring.service;


import com.nbu.graduation.spring.model.service.StudentServiceModel;

import java.time.LocalDate;
import java.util.List;

public interface StudentService {
    StudentServiceModel createStudent(StudentServiceModel studentServiceModel);

    StudentServiceModel updateStudent(StudentServiceModel studentServiceModel);

    void deleteStudentById(Long id);

    StudentServiceModel getStudentById(Long id);

    String generateFacultyNumber();

    List<StudentServiceModel> findAll();

    StudentServiceModel findByUsername(String username);

    void initializeStudents();

    void markParticipation(Long studentId);

    boolean canParticipate(Long studentId);

   List<StudentServiceModel> getGraduatedInPeriod(LocalDate from, LocalDate to);

    long countSuccessfulBySupervisor(Long supervisorId);
}
