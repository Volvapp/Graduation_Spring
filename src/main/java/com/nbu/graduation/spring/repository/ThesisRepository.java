package com.nbu.graduation.spring.repository;

import com.nbu.graduation.spring.model.Thesis;
import com.nbu.graduation.spring.model.enums.Department;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ThesisRepository extends JpaRepository<Thesis, Long> {
    List<Thesis> findByAssignment_Department(Department department);

    boolean existsByAssignment_Student_User_Id(Long userId);

    Thesis findByAssignment_Student_User_Id(Long userId);
}
