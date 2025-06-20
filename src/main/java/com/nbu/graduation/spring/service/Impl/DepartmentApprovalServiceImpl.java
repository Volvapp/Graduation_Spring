package com.nbu.graduation.spring.service.Impl;

import com.nbu.graduation.spring.model.DepartmentApproval;
import com.nbu.graduation.spring.model.Teacher;
import com.nbu.graduation.spring.model.ThesisAssignment;
import com.nbu.graduation.spring.model.service.TeacherServiceModel;
import com.nbu.graduation.spring.model.service.ThesisAssignmentServiceModel;
import com.nbu.graduation.spring.repository.DepartmentApprovalRepository;
import com.nbu.graduation.spring.service.DepartmentApprovalService;
import com.nbu.graduation.spring.service.TeacherService;
import com.nbu.graduation.spring.service.ThesisAssignmentService;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
@Service
public class DepartmentApprovalServiceImpl implements DepartmentApprovalService {

    private final DepartmentApprovalRepository departmentApprovalRepository;
    private final ModelMapper modelMapper;
    private final ThesisAssignmentService assignmentService;
    private final TeacherService teacherService;

    public DepartmentApprovalServiceImpl(DepartmentApprovalRepository departmentApprovalRepository,
                                         ModelMapper modelMapper,
                                         ThesisAssignmentService assignmentService,
                                         TeacherService teacherService) {
        this.departmentApprovalRepository = departmentApprovalRepository;
        this.modelMapper = modelMapper;
        this.assignmentService = assignmentService;
        this.teacherService = teacherService;
    }

    @Override
    public int getApprovalCount(Long assignmentId) {
        if (assignmentId == null) {
            throw new IllegalArgumentException("ID на задание не може да бъде null.");
        }
        return this.departmentApprovalRepository.countApprovedByAssignmentId(assignmentId);
    }


    private void saveApproval(Long assignmentId, Long teacherId, boolean approved) {
        if (assignmentId == null || teacherId == null) {
            throw new IllegalArgumentException("ID на задание и учител не могат да бъдат null.");
        }

        ThesisAssignmentServiceModel assignment = assignmentService.getAssignmentById(assignmentId);
        if (assignment == null) {
            throw new IllegalArgumentException("Заданието с ID " + assignmentId + " не е намерено.");
        }

        TeacherServiceModel teacher = teacherService.findById(teacherId);
        if (teacher == null) {
            throw new IllegalArgumentException("Учителят с ID " + teacherId + " не е намерен.");
        }

        DepartmentApproval approval = this.departmentApprovalRepository.findByAssignmentIdAndTeacherId(assignmentId, teacherId);

        if (approval == null) {
            approval = new DepartmentApproval();
            approval.setAssignment(modelMapper.map(assignment, ThesisAssignment.class));
            approval.setTeacher(modelMapper.map(teacher, Teacher.class));
        }

        approval.setApproved(approved);
        this.departmentApprovalRepository.save(approval);
    }

    @Override
    public void processApproval(Long assignmentId, Long teacherId, boolean approve) {
        if (assignmentId == null || teacherId == null) {
            throw new IllegalArgumentException("ID на задание и учител не могат да бъдат null.");
        }

        saveApproval(assignmentId, teacherId, approve);

        int approvalCount = getApprovalCount(assignmentId);

        TeacherServiceModel teacher = teacherService.findById(teacherId);
        if (teacher == null || teacher.getUser() == null || teacher.getUser().getDepartment() == null) {
            throw new IllegalArgumentException("Учителят или неговият департамент не са намерени.");
        }

        int teacherCountInDepartment = teacherService.findByDepartment(teacher.getUser().getDepartment()).size();

        double requiredMajority = teacherCountInDepartment / 2.0;

        ThesisAssignmentServiceModel assignment = assignmentService.getAssignmentById(assignmentId);
        if (assignment == null) {
            throw new IllegalArgumentException("Заданието с ID " + assignmentId + " не е намерено.");
        }

        if (approve && approvalCount >= requiredMajority) {
            if (assignment.isApproved()) {
                assignmentService.departmentApproveAssignment(assignmentId);
            } else {
                throw new IllegalStateException("Заданието не е одобрено от ръководителя.");
            }
        } else if (!approve && approvalCount < requiredMajority) {
            assignmentService.departmentRejectAssignment(assignmentId);
        }
    }
}
