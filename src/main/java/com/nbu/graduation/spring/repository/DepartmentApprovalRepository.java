package com.nbu.graduation.spring.repository;

import com.nbu.graduation.spring.model.DepartmentApproval;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface DepartmentApprovalRepository extends JpaRepository<DepartmentApproval, Long> {
    @Query("SELECT COUNT(d) FROM DepartmentApproval d WHERE d.assignment.id = :assignmentId AND d.approved = true")
    int countApprovedByAssignmentId(@Param("assignmentId") Long assignmentId);

    DepartmentApproval findByAssignmentId(Long assignmentId);

    DepartmentApproval findByAssignmentIdAndTeacherId(Long assignmentId, Long teacherId);
}
