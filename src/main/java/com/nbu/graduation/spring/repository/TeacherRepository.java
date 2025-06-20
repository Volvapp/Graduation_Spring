package com.nbu.graduation.spring.repository;

import com.nbu.graduation.spring.model.Teacher;
import com.nbu.graduation.spring.model.enums.Department;
import com.nbu.graduation.spring.model.service.TeacherServiceModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TeacherRepository extends JpaRepository<Teacher, Long> {
    Teacher findByUserId(Long userId);

    Teacher findByUser_username(String username);

    List<Teacher> findAllByUser_department(Department department);

    List<Teacher> findAllByUser_Department(Department department);
}
