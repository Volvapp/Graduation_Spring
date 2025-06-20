package com.nbu.graduation.spring;

import com.nbu.graduation.spring.model.ThesisAssignment;
import com.nbu.graduation.spring.model.dto.ThesisAssignmentDTO;
import com.nbu.graduation.spring.model.enums.Department;
import com.nbu.graduation.spring.model.enums.Role;
import com.nbu.graduation.spring.model.service.*;
import com.nbu.graduation.spring.repository.ThesisAssignmentRepository;
import com.nbu.graduation.spring.service.DepartmentApprovalService;
import com.nbu.graduation.spring.service.Impl.ThesisAssignmentServiceImpl;
import com.nbu.graduation.spring.service.StudentService;
import com.nbu.graduation.spring.service.TeacherService;
import com.nbu.graduation.spring.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.modelmapper.ModelMapper;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ThesisAssignmentServiceImplTest {

    @Mock
    private ThesisAssignmentRepository thesisAssignmentRepository;

    @Mock
    private ModelMapper modelMapper;

    @Mock
    private StudentService studentService;

    @Mock
    private TeacherService teacherService;

    @Mock
    private DepartmentApprovalService departmentApprovalService;

    @Mock
    private UserService userService;

    @InjectMocks
    private ThesisAssignmentServiceImpl service;

    private ThesisAssignment assignmentEntity;
    private ThesisAssignmentServiceModel assignmentServiceModel;
    private StudentServiceModel studentServiceModel;
    private TeacherServiceModel teacherServiceModel;
    private UserServiceModel userServiceModel;

    @BeforeEach
    void setUp() {
        assignmentEntity = new ThesisAssignment();
        assignmentEntity.setId(1L);
        assignmentEntity.setApproved(false);
        assignmentEntity.setDepartment(Department.IT);
        assignmentEntity.setDepartmentApproved(false);

        assignmentServiceModel = new ThesisAssignmentServiceModel();
        assignmentServiceModel.setId(1L);
        assignmentServiceModel.setApproved(false);
        assignmentServiceModel.setDepartment(Department.IT);
        assignmentServiceModel.setDepartmentApproved(false);

        studentServiceModel = new StudentServiceModel();
        studentServiceModel.setId(10L);
        UserServiceModel studentUser = new UserServiceModel();
        studentUser.setUsername("studentUser");
        studentUser.setDepartment(Department.IT);
        studentUser.setRole(Role.STUDENT);
        studentServiceModel.setUser(studentUser);

        teacherServiceModel = new TeacherServiceModel();
        teacherServiceModel.setId(20L);
        UserServiceModel teacherUser = new UserServiceModel();
        teacherUser.setUsername("teacherUser");
        teacherUser.setDepartment(Department.IT);
        teacherUser.setRole(Role.TEACHER);
        teacherServiceModel.setUser(teacherUser);

        userServiceModel = new UserServiceModel();
        userServiceModel.setUsername("user");
        userServiceModel.setDepartment(Department.IT);
        userServiceModel.setRole(Role.STUDENT);
    }

    @Test
    void testCreateAssignment() {
        when(modelMapper.map(assignmentServiceModel, ThesisAssignment.class)).thenReturn(assignmentEntity);
        when(thesisAssignmentRepository.save(assignmentEntity)).thenReturn(assignmentEntity);
        when(modelMapper.map(assignmentEntity, ThesisAssignmentServiceModel.class)).thenReturn(assignmentServiceModel);

        ThesisAssignmentServiceModel result = service.createAssignment(assignmentServiceModel);

        assertNotNull(result);
        assertEquals(assignmentServiceModel.getId(), result.getId());
        verify(thesisAssignmentRepository).save(assignmentEntity);
    }

    @Test
    void testUpdateAssignment() {
        when(modelMapper.map(assignmentServiceModel, ThesisAssignment.class)).thenReturn(assignmentEntity);
        when(thesisAssignmentRepository.save(assignmentEntity)).thenReturn(assignmentEntity);
        when(modelMapper.map(assignmentEntity, ThesisAssignmentServiceModel.class)).thenReturn(assignmentServiceModel);

        ThesisAssignmentServiceModel result = service.updateAssignment(assignmentServiceModel);

        assertNotNull(result);
        verify(thesisAssignmentRepository).save(assignmentEntity);
    }

    @Test
    void testDeleteAssignmentByIdSuccess() {
        when(thesisAssignmentRepository.existsById(1L)).thenReturn(true);

        service.deleteAssignmentById(1L);

        verify(thesisAssignmentRepository).deleteById(1L);
    }

    @Test
    void testDeleteAssignmentByIdNotFound() {
        when(thesisAssignmentRepository.existsById(1L)).thenReturn(false);

        RuntimeException ex = assertThrows(RuntimeException.class, () -> service.deleteAssignmentById(1L));
        assertEquals("Assignment with ID 1 not found for deletion.", ex.getMessage());
    }

    @Test
    void testGetAssignmentByIdSuccess() {
        when(thesisAssignmentRepository.findById(1L)).thenReturn(Optional.of(assignmentEntity));
        when(modelMapper.map(assignmentEntity, ThesisAssignmentServiceModel.class)).thenReturn(assignmentServiceModel);

        ThesisAssignmentServiceModel result = service.getAssignmentById(1L);

        assertNotNull(result);
        assertEquals(1L, result.getId());
    }

    @Test
    void testGetAssignmentByIdNotFound() {
        when(thesisAssignmentRepository.findById(1L)).thenReturn(Optional.empty());

        RuntimeException ex = assertThrows(RuntimeException.class, () -> service.getAssignmentById(1L));
        assertEquals("Assignment with ID 1 not found.", ex.getMessage());
    }

    @Test
    void testGetAssignmentByStudentIdSuccess() {
        when(thesisAssignmentRepository.findByStudent_Id(10L)).thenReturn(assignmentEntity);
        when(modelMapper.map(assignmentEntity, ThesisAssignmentServiceModel.class)).thenReturn(assignmentServiceModel);

        ThesisAssignmentServiceModel result = service.getAssignmentByStudentId(10L);

        assertNotNull(result);
    }

    @Test
    void testGetAssignmentByStudentIdNotFound() {
        when(thesisAssignmentRepository.findByStudent_Id(10L)).thenReturn(null);

        RuntimeException ex = assertThrows(RuntimeException.class, () -> service.getAssignmentByStudentId(10L));
        assertTrue(ex.getMessage().contains("not found"));
    }

    @Test
    void testGetAllBySupervisor() {
        List<ThesisAssignment> assignments = List.of(assignmentEntity);
        when(thesisAssignmentRepository.findAllBySupervisorId(teacherServiceModel.getId())).thenReturn(assignments);
        when(modelMapper.map(any(ThesisAssignment.class), eq(ThesisAssignmentServiceModel.class))).thenReturn(assignmentServiceModel);

        List<ThesisAssignmentServiceModel> result = service.getAllBySupervisor(teacherServiceModel);

        assertEquals(1, result.size());
    }

    @Test
    void testGetAllByDepartmentApproved() {
        assignmentEntity.setApproved(true);
        List<ThesisAssignment> assignments = List.of(assignmentEntity);
        when(thesisAssignmentRepository.findAllByDepartment(Department.IT)).thenReturn(assignments);
        when(modelMapper.map(any(ThesisAssignment.class), eq(ThesisAssignmentServiceModel.class))).thenReturn(assignmentServiceModel);

        List<ThesisAssignmentServiceModel> result = service.getAllByDepartmentApproved(Department.IT);

        assertFalse(result.isEmpty());
    }

    @Test
    void testDepartmentApproveAssignmentSuccess() {
        assignmentEntity.setApproved(true);
        assignmentEntity.setDepartmentApproved(false);
        when(thesisAssignmentRepository.findById(1L)).thenReturn(Optional.of(assignmentEntity));
        when(thesisAssignmentRepository.save(any())).thenReturn(assignmentEntity);
        when(modelMapper.map(any(ThesisAssignment.class), eq(ThesisAssignmentServiceModel.class))).thenReturn(assignmentServiceModel);

        service.departmentApproveAssignment(1L);

        assertTrue(assignmentEntity.isDepartmentApproved());
    }

    @Test
    void testDepartmentApproveAssignmentNotApprovedThrows() {
        assignmentEntity.setApproved(false);
        when(thesisAssignmentRepository.findById(1L)).thenReturn(Optional.of(assignmentEntity));

        RuntimeException ex = assertThrows(RuntimeException.class, () -> service.departmentApproveAssignment(1L));
        assertTrue(ex.getMessage().contains("is not approved"));
    }

    @Test
    void testDepartmentRejectAssignmentSuccess() {
        assignmentEntity.setApproved(true);
        assignmentEntity.setDepartmentApproved(true);
        when(thesisAssignmentRepository.findById(1L)).thenReturn(Optional.of(assignmentEntity));
        when(thesisAssignmentRepository.save(any())).thenReturn(assignmentEntity);
        when(modelMapper.map(any(ThesisAssignment.class), eq(ThesisAssignmentServiceModel.class))).thenReturn(assignmentServiceModel);

        service.departmentRejectAssignment(1L);

        assertFalse(assignmentEntity.isDepartmentApproved());
    }

    @Test
    void testDepartmentRejectAssignmentNotApprovedThrows() {
        assignmentEntity.setApproved(false);
        when(thesisAssignmentRepository.findById(1L)).thenReturn(Optional.of(assignmentEntity));

        RuntimeException ex = assertThrows(RuntimeException.class, () -> service.departmentRejectAssignment(1L));
        assertTrue(ex.getMessage().contains("is not approved"));
    }

    @Test
    void testGetProposeViewModel() {
        when(userService.findByUsername("user")).thenReturn(userServiceModel);
        when(teacherService.findByDepartment(userServiceModel.getDepartment())).thenReturn(List.of(teacherServiceModel));

        Map<String, Object> model = service.getProposeViewModel("user");

        assertNotNull(model.get("teachers"));
    }

    @Test
    void testProposeAssignmentSuccess() {
        ThesisAssignmentDTO dto = new ThesisAssignmentDTO();
        dto.setSupervisorId(teacherServiceModel.getId());
        dto.setTopic("topic");
        dto.setGoal("goal");
        dto.setTasks("tasks");
        dto.setTechnologies("tech");

        when(studentService.findByUsername("user")).thenReturn(studentServiceModel);
        when(teacherService.findById(dto.getSupervisorId())).thenReturn(teacherServiceModel);
        when(modelMapper.map(any(), eq(ThesisAssignment.class))).thenReturn(assignmentEntity);
        when(thesisAssignmentRepository.save(assignmentEntity)).thenReturn(assignmentEntity);

        service.proposeAssignment(dto, "user");

        verify(thesisAssignmentRepository).save(any(ThesisAssignment.class));
    }

    @Test
    void testProposeAssignmentSupervisorNotFound() {
        ThesisAssignmentDTO dto = new ThesisAssignmentDTO();
        dto.setSupervisorId(99L);

        when(studentService.findByUsername("user")).thenReturn(studentServiceModel);
        when(teacherService.findById(dto.getSupervisorId())).thenReturn(null);

        RuntimeException ex = assertThrows(RuntimeException.class, () -> service.proposeAssignment(dto, "user"));
        assertTrue(ex.getMessage().contains("Supervisor with ID"));
    }

    @Test
    void testHandleSupervisorDecisionInvalid() {
        RuntimeException ex = assertThrows(RuntimeException.class, () -> service.handleSupervisorDecision(1L, "invalid"));

        assertTrue(ex.getMessage().contains("Invalid action"));
    }

}
