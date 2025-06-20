package com.nbu.graduation.spring.service;


import com.nbu.graduation.spring.model.dto.ThesisAssignmentDTO;
import com.nbu.graduation.spring.model.enums.Department;
import com.nbu.graduation.spring.model.service.TeacherServiceModel;
import com.nbu.graduation.spring.model.service.ThesisAssignmentServiceModel;
import com.nbu.graduation.spring.model.service.UserServiceModel;

import java.util.List;
import java.util.Map;

public interface ThesisAssignmentService {
    ThesisAssignmentServiceModel createAssignment(ThesisAssignmentServiceModel model);

    ThesisAssignmentServiceModel updateAssignment(ThesisAssignmentServiceModel model);

    void deleteAssignmentById(Long id);

    ThesisAssignmentServiceModel getAssignmentById(Long id);

    ThesisAssignmentServiceModel getAssignmentByStudentId(Long id);

    List<ThesisAssignmentServiceModel> getAllBySupervisor(TeacherServiceModel teacher);

    void approveAssignment(Long assignmentId);

    void rejectAssignment(Long assignmentId);

    List<ThesisAssignmentServiceModel> getAllByDepartmentApproved(Department department);

    void departmentApproveAssignment(Long assignmentId);

    void departmentRejectAssignment(Long assignmentId);

    Map<String, Object> getAssignmentsViewModel(UserServiceModel user);

    Map<String, Object> getProposeViewModel(String username);

    void proposeAssignment(ThesisAssignmentDTO dto, String username);

    void handleSupervisorDecision(Long assignmentId, String action);

    List<ThesisAssignmentServiceModel> getAssignmentsForDepartmentApproval(UserServiceModel user);

    void handleDepartmentDecision(Long assignmentId, String username, boolean isApprove);

    List<ThesisAssignmentServiceModel> getAllApprovedAssignments();

   List<ThesisAssignmentServiceModel> findByTitleContaining(String keyword);

    List<ThesisAssignmentServiceModel> findAllByApprovedAndSupervisor(Long teacherId);
}
