package com.nbu.graduation.spring.service.Impl;

import com.nbu.graduation.spring.model.ThesisAssignment;
import com.nbu.graduation.spring.model.dto.ThesisAssignmentDTO;
import com.nbu.graduation.spring.model.enums.Department;
import com.nbu.graduation.spring.model.enums.Role;
import com.nbu.graduation.spring.model.service.*;
import com.nbu.graduation.spring.repository.ThesisAssignmentRepository;
import com.nbu.graduation.spring.service.*;
import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class ThesisAssignmentServiceImpl implements ThesisAssignmentService {
    private final ThesisAssignmentRepository thesisAssignmentRepository;
    private final ModelMapper modelMapper;
    private final StudentService studentService;
    private final TeacherService teacherService;
    private final DepartmentApprovalService departmentApprovalService;
    private final UserService userService;

    public ThesisAssignmentServiceImpl(ThesisAssignmentRepository thesisAssignmentRepository, ModelMapper modelMapper,
                                       StudentService studentService, TeacherService teacherService,
                                       @Lazy DepartmentApprovalService departmentApprovalService, UserService userService) {
        this.thesisAssignmentRepository = thesisAssignmentRepository;
        this.modelMapper = modelMapper;
        this.studentService = studentService;
        this.teacherService = teacherService;
        this.departmentApprovalService = departmentApprovalService;
        this.userService = userService;
    }

    @Override
    public ThesisAssignmentServiceModel createAssignment(ThesisAssignmentServiceModel model) {
        ThesisAssignment thesisAssignment = modelMapper.map(model, ThesisAssignment.class);
        thesisAssignmentRepository.save(thesisAssignment);

        return this.modelMapper.map(thesisAssignment, ThesisAssignmentServiceModel.class);
    }

    @Override
    public ThesisAssignmentServiceModel updateAssignment(ThesisAssignmentServiceModel model) {
        ThesisAssignment thesisAssignment = modelMapper.map(model, ThesisAssignment.class);
        thesisAssignmentRepository.save(thesisAssignment);

        return this.modelMapper.map(thesisAssignment, ThesisAssignmentServiceModel.class);
    }

    @Override
    public void deleteAssignmentById(Long id) {
        if (!thesisAssignmentRepository.existsById(id)) {
            throw new RuntimeException("Assignment with ID " + id + " not found for deletion.");
        }
        this.thesisAssignmentRepository.deleteById(id);
    }

    @Override
    public ThesisAssignmentServiceModel getAssignmentById(Long id) {
        ThesisAssignment thesisAssignment = this.thesisAssignmentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Assignment with ID " + id + " not found."));
        return this.modelMapper.map(thesisAssignment, ThesisAssignmentServiceModel.class);
    }

    @Override
    public ThesisAssignmentServiceModel getAssignmentByStudentId(Long id) {
        ThesisAssignment thesisAssignment = this.thesisAssignmentRepository.findByStudent_Id(id);
        if (thesisAssignment == null) {
            throw new RuntimeException("Assignment for student with ID " + id + " not found.");
        }
        return this.modelMapper.map(thesisAssignment, ThesisAssignmentServiceModel.class);
    }

    @Override
    public List<ThesisAssignmentServiceModel> getAllBySupervisor(TeacherServiceModel teacher) {
        return thesisAssignmentRepository.findAllBySupervisorId(teacher.getId())
                .stream()
                .map(entity -> modelMapper.map(entity, ThesisAssignmentServiceModel.class))
                .toList();
    }

    @Override
    public void approveAssignment(Long assignmentId) {
        ThesisAssignment thesisAssignment = this.thesisAssignmentRepository.findById(assignmentId)
                .orElseThrow(() -> new RuntimeException("Assignment with ID " + assignmentId + " not found."));
        thesisAssignment.setApproved(true);
        this.updateAssignment(this.modelMapper.map(thesisAssignment, ThesisAssignmentServiceModel.class));
    }

    @Override
    public void rejectAssignment(Long assignmentId) {
        ThesisAssignment assignment = this.thesisAssignmentRepository.findById(assignmentId)
                .orElseThrow(() -> new RuntimeException("Assignment with ID " + assignmentId + " not found."));
        assignment.setApproved(false);
        this.updateAssignment(this.modelMapper.map(assignment, ThesisAssignmentServiceModel.class));
    }

    @Override
    public List<ThesisAssignmentServiceModel> getAllByDepartmentApproved(Department department) {
        return this.thesisAssignmentRepository.findAllByDepartment(department)
                .stream()
                .filter(ThesisAssignment::isApproved)
                .map(thesisAssignment -> this.modelMapper.map(thesisAssignment, ThesisAssignmentServiceModel.class))
                .collect(Collectors.toList());
    }

    @Override
    public void departmentApproveAssignment(Long assignmentId) {
        ThesisAssignment thesisAssignment = this.thesisAssignmentRepository.findById(assignmentId)
                .orElseThrow(() -> new RuntimeException("Assignment with ID " + assignmentId + " not found."));

        if (!thesisAssignment.isApproved()) {
            throw new RuntimeException("Assignment with ID " + assignmentId + " is not approved.");
        }
        thesisAssignment.setDepartmentApproved(true);
        this.updateAssignment(this.modelMapper.map(thesisAssignment, ThesisAssignmentServiceModel.class));
    }

    @Override
    public void departmentRejectAssignment(Long assignmentId) {
        ThesisAssignment thesisAssignment = this.thesisAssignmentRepository.findById(assignmentId)
                .orElseThrow(() -> new RuntimeException("Assignment with ID " + assignmentId + " not found."));

        if (!thesisAssignment.isApproved()) {
            throw new RuntimeException("Assignment with ID " + assignmentId + " is not approved.");
        }
        thesisAssignment.setDepartmentApproved(false);
        this.updateAssignment(this.modelMapper.map(thesisAssignment, ThesisAssignmentServiceModel.class));
    }

    @Override
    public Map<String, Object> getAssignmentsViewModel(UserServiceModel user) {
        Map<String, Object> modelMap = new HashMap<>();

        if (user.getRole() == Role.STUDENT) {
            StudentServiceModel student = studentService.findByUsername(user.getUsername());
            ThesisAssignmentServiceModel assignment = null;
            if (student.getAssignment() != null) {
                assignment = getAssignmentByStudentId(student.getId());
            }
            modelMap.put("isStudent", true);
            modelMap.put("assignment", assignment);
        } else if (user.getRole() == Role.TEACHER) {
            TeacherServiceModel teacher = teacherService.findByUsername(user.getUsername());
            List<ThesisAssignmentServiceModel> assignments = getAllBySupervisor(teacher);
            modelMap.put("isTeacher", true);
            modelMap.put("assignments", assignments);
        }

        return modelMap;
    }

    @Override
    public Map<String, Object> getProposeViewModel(String username) {
        UserServiceModel user = userService.findByUsername(username);
        List<TeacherServiceModel> teachers = teacherService.findByDepartment(user.getDepartment());
        Map<String, Object> modelMap = new HashMap<>();
        modelMap.put("teachers", teachers);
        return modelMap;
    }

    @Override
    public void proposeAssignment(ThesisAssignmentDTO dto, String username) {
        StudentServiceModel student = studentService.findByUsername(username);
        TeacherServiceModel supervisor = teacherService.findById(dto.getSupervisorId());
        if (supervisor == null) {
            throw new RuntimeException("Supervisor with ID " + dto.getSupervisorId() + " not found.");
        }

        ThesisAssignmentServiceModel assignment = new ThesisAssignmentServiceModel();
        assignment.setTopic(dto.getTopic());
        assignment.setGoal(dto.getGoal());
        assignment.setTasks(dto.getTasks());
        assignment.setTechnologies(dto.getTechnologies());
        assignment.setSupervisor(supervisor);
        assignment.setStudent(student);
        assignment.setDepartment(student.getUser().getDepartment());

        createAssignment(assignment);
    }

    @Override
    public void handleSupervisorDecision(Long assignmentId, String action) {
        if ("approve".equals(action)) {
            approveAssignment(assignmentId);
        } else if ("reject".equals(action)) {
            rejectAssignment(assignmentId);
        } else {
            throw new RuntimeException("Invalid action '" + action + "' for supervisor decision.");
        }
    }

    @Override
    public List<ThesisAssignmentServiceModel> getAssignmentsForDepartmentApproval(UserServiceModel user) {
        return getAllByDepartmentApproved(user.getDepartment());
    }

    @Override
    public void handleDepartmentDecision(Long assignmentId, String username, boolean isApprove) {
        TeacherServiceModel teacher = teacherService.findByUsername(username);
        if (teacher == null) {
            throw new RuntimeException("Teacher with username '" + username + "' not found.");
        }
        departmentApprovalService.processApproval(assignmentId, teacher.getId(), isApprove);
    }

    @Override
    public List<ThesisAssignmentServiceModel> getAllApprovedAssignments() {
        return this.thesisAssignmentRepository
                .findAllByApprovedAndDepartmentApproved(true, true)
                .stream()
                .map(thesisAssignment -> this.modelMapper.map(thesisAssignment, ThesisAssignmentServiceModel.class))
                .collect(Collectors.toList());
    }

    @Override
    public List<ThesisAssignmentServiceModel> findByTitleContaining(String keyword) {
        return this.thesisAssignmentRepository.findAllByTopicContainingIgnoreCase(keyword)
                .stream()
                .map(thesisAssignment -> this.modelMapper.map(thesisAssignment, ThesisAssignmentServiceModel.class))
                .toList();
    }

    @Override
    public List<ThesisAssignmentServiceModel> findAllByApprovedAndSupervisor(Long teacherId) {
        return this.thesisAssignmentRepository
                .findAllByApprovedAndDepartmentApprovedAndSupervisor_Id(true, true, teacherId)
                .stream()
                .map(thesisAssignment -> this.modelMapper.map(thesisAssignment, ThesisAssignmentServiceModel.class))
                .toList();
    }

}
