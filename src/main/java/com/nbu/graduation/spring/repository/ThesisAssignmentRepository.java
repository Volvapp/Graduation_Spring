package com.nbu.graduation.spring.repository;

import com.nbu.graduation.spring.model.ThesisAssignment;
import com.nbu.graduation.spring.model.enums.Department;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ThesisAssignmentRepository extends JpaRepository<ThesisAssignment, Long> {
    ThesisAssignment findByStudent_Id(Long studentId);

    List<ThesisAssignment> findAllBySupervisorId(Long supervisorId);

    List<ThesisAssignment> findAllByDepartment(Department department);

    List<ThesisAssignment> findAllByApprovedAndDepartmentApproved(boolean approved, boolean departmentApproved);

    List<ThesisAssignment> findAllByTopicContainingIgnoreCase(String keyword);

    List<ThesisAssignment> findAllByApprovedAndDepartmentApprovedAndSupervisor_Id(boolean approved, boolean departmentApproved, Long supervisorId);

}
