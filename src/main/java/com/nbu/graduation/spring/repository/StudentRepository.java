package com.nbu.graduation.spring.repository;

import com.nbu.graduation.spring.model.Student;
import com.nbu.graduation.spring.model.Teacher;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface StudentRepository extends JpaRepository<Student, Long> {
    Student findByUser_Username(String username);

    List<Student> findAllByGradeGreaterThanAndDefense_DefenseDateBetween(Double grade, LocalDate from, LocalDate to);

    Integer countByGradeGreaterThanAndAssignment_Supervisor_Id(Double grade, Long supervisorId);
}
