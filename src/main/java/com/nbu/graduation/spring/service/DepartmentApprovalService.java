package com.nbu.graduation.spring.service;

public interface DepartmentApprovalService {
    int getApprovalCount(Long assignmentId);

    void processApproval(Long assignmentId, Long teacherId, boolean approve);
}
