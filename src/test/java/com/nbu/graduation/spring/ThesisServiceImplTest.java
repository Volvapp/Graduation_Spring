package com.nbu.graduation.spring;

import com.nbu.graduation.spring.model.Thesis;
import com.nbu.graduation.spring.model.ThesisAssignment;
import com.nbu.graduation.spring.model.enums.Department;
import com.nbu.graduation.spring.model.service.*;
import com.nbu.graduation.spring.repository.ThesisRepository;
import com.nbu.graduation.spring.service.Impl.ThesisServiceImpl;
import com.nbu.graduation.spring.service.ThesisAssignmentService;
import com.nbu.graduation.spring.service.StudentService;
import com.nbu.graduation.spring.service.TeacherService;
import com.nbu.graduation.spring.service.UserService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.modelmapper.ModelMapper;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class ThesisServiceImplTest {

    @Mock
    private ThesisRepository thesisRepository;

    @Mock
    private ModelMapper modelMapper;

    @Mock
    private ThesisAssignmentService assignmentService;

    @Mock
    private StudentService studentService;

    @Mock
    private TeacherService teacherService;

    @Mock
    private UserService userService;

    @InjectMocks
    private ThesisServiceImpl thesisService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void createThesis_ShouldSaveAndReturnThesis() {
        ThesisServiceModel inputModel = new ThesisServiceModel();
        inputModel.setTitle("Title");
        inputModel.setUploadDate(LocalDate.now());
        ThesisAssignmentServiceModel assignmentModel = new ThesisAssignmentServiceModel();
        inputModel.setAssignment(assignmentModel);

        Thesis thesisEntity = new Thesis();
        Thesis savedEntity = new Thesis();
        savedEntity.setTitle("Title");

        when(modelMapper.map(inputModel.getAssignment(), ThesisAssignment.class)).thenReturn(new ThesisAssignment());
        when(modelMapper.map(inputModel, Thesis.class)).thenReturn(thesisEntity);
        when(thesisRepository.save(any())).thenReturn(savedEntity);
        when(modelMapper.map(savedEntity, ThesisServiceModel.class)).thenReturn(inputModel);

        ThesisServiceModel result = thesisService.createThesis(inputModel, "filePath");

        assertNotNull(result);
        verify(thesisRepository).save(any());
    }

    @Test
    void getThesesByDepartment_ShouldReturnMappedList() {
        Thesis thesis = new Thesis();
        thesis.setTitle("Title");
        List<Thesis> thesisList = List.of(thesis);
        when(thesisRepository.findByAssignment_Department(Department.IT)).thenReturn(thesisList);
        when(modelMapper.map(any(Thesis.class), eq(ThesisServiceModel.class))).thenReturn(new ThesisServiceModel());

        List<ThesisServiceModel> result = thesisService.getThesesByDepartment(Department.IT);

        assertEquals(1, result.size());
    }

    @Test
    void studentHasThesis_ShouldReturnTrueIfExists() {
        when(thesisRepository.existsByAssignment_Student_User_Id(1L)).thenReturn(true);
        assertTrue(thesisService.studentHasThesis(1L));
    }

    @Test
    void updateThesis_WhenIdIsNull_ShouldThrow() {
        ThesisServiceModel model = new ThesisServiceModel();
        model.setId(null);
        RuntimeException ex = assertThrows(RuntimeException.class, () -> thesisService.updateThesis(model));
        assertEquals("Thesis ID is required for update", ex.getMessage());
    }

    @Test
    void updateThesis_WhenNotFound_ShouldThrow() {
        ThesisServiceModel model = new ThesisServiceModel();
        model.setId(1L);
        when(thesisRepository.findById(1L)).thenReturn(Optional.empty());

        RuntimeException ex = assertThrows(RuntimeException.class, () -> thesisService.updateThesis(model));
        assertEquals("Thesis not found with ID: 1", ex.getMessage());
    }

    @Test
    void updateThesis_ShouldUpdateAndReturn() {
        ThesisServiceModel model = new ThesisServiceModel();
        model.setId(1L);
        model.setTitle("Updated");
        model.setUploadDate(LocalDate.now());
        model.setFilePath("filePath");

        ThesisAssignmentServiceModel assignmentModel = new ThesisAssignmentServiceModel();
        model.setAssignment(assignmentModel);

        Thesis thesisEntity = new Thesis();
        thesisEntity.setId(1L);

        when(thesisRepository.findById(1L)).thenReturn(Optional.of(thesisEntity));
        when(modelMapper.map(model.getAssignment(), ThesisAssignment.class)).thenReturn(new ThesisAssignment());
        when(thesisRepository.save(any(Thesis.class))).thenReturn(thesisEntity);
        when(modelMapper.map(thesisEntity, ThesisServiceModel.class)).thenReturn(model);

        ThesisServiceModel result = thesisService.updateThesis(model);

        assertNotNull(result);
        assertEquals("Updated", result.getTitle());
    }

    @Test
    void deleteThesisById_WhenNotFound_ShouldThrow() {
        when(thesisRepository.findById(1L)).thenReturn(Optional.empty());
        RuntimeException ex = assertThrows(RuntimeException.class, () -> thesisService.deleteThesisById(1L));
        assertEquals("Thesis not found with ID: 1", ex.getMessage());
    }

    @Test
    void deleteThesisById_ShouldDelete() {
        Thesis thesis = new Thesis();
        when(thesisRepository.findById(1L)).thenReturn(Optional.of(thesis));

        thesisService.deleteThesisById(1L);

        verify(thesisRepository).delete(thesis);
    }

    @Test
    void getThesisById_WhenNotFound_ShouldThrow() {
        when(thesisRepository.findById(1L)).thenReturn(Optional.empty());
        RuntimeException ex = assertThrows(RuntimeException.class, () -> thesisService.getThesisById(1L));
        assertEquals("Thesis not found with ID: 1", ex.getMessage());
    }

    @Test
    void getThesisById_ShouldReturnModel() {
        Thesis thesis = new Thesis();
        when(thesisRepository.findById(1L)).thenReturn(Optional.of(thesis));
        ThesisServiceModel model = new ThesisServiceModel();
        when(modelMapper.map(thesis, ThesisServiceModel.class)).thenReturn(model);

        ThesisServiceModel result = thesisService.getThesisById(1L);
        assertNotNull(result);
    }

    @Test
    void getStudentAssignment_ShouldReturnAssignment() {
        String username = "user";
        StudentServiceModel student = new StudentServiceModel();
        ThesisAssignmentServiceModel assignment = new ThesisAssignmentServiceModel();
        assignment.setId(1L);
        student.setAssignment(assignment);

        when(studentService.findByUsername(username)).thenReturn(student);
        when(assignmentService.getAssignmentById(1L)).thenReturn(assignment);

        ThesisAssignmentServiceModel result = thesisService.getStudentAssignment(username);
        assertNotNull(result);
        assertEquals(1L, result.getId());
    }

    @Test
    void getStudentAssignment_WhenStudentNull_ShouldReturnNull() {
        when(studentService.findByUsername("user")).thenReturn(null);
        assertNull(thesisService.getStudentAssignment("user"));
    }

    @Test
    void getThesesByTeacherDepartment_WhenTeacherNull_ShouldReturnEmptyList() {
        when(teacherService.findByUsername("user")).thenReturn(null);
        List<ThesisServiceModel> result = thesisService.getThesesByTeacherDepartment("user");
        assertTrue(result.isEmpty());
    }

    @Test
    void getThesesByTeacherDepartment_WhenDeptNull_ShouldReturnEmptyList() {
        TeacherServiceModel teacher = new TeacherServiceModel();
        teacher.setUser(new UserServiceModel());
        teacher.getUser().setDepartment(null);

        when(teacherService.findByUsername("user")).thenReturn(teacher);
        List<ThesisServiceModel> result = thesisService.getThesesByTeacherDepartment("user");
        assertTrue(result.isEmpty());
    }


    @Test
    void findUserByUsername_ShouldCallUserService() {
        String username = "user";
        UserServiceModel user = new UserServiceModel();
        when(userService.findByUsername(username)).thenReturn(user);

        UserServiceModel result = thesisService.findUserByUsername(username);
        assertEquals(user, result);
    }

    @Test
    void getThesisByStudentId_WhenNotFound_ShouldReturnNull() {
        when(thesisRepository.findByAssignment_Student_User_Id(1L)).thenReturn(null);
        assertNull(thesisService.getThesisByStudentId(1L));
    }

    @Test
    void getThesisByStudentId_ShouldReturnModel() {
        Thesis thesis = new Thesis();
        when(thesisRepository.findByAssignment_Student_User_Id(1L)).thenReturn(thesis);
        ThesisServiceModel model = new ThesisServiceModel();
        when(modelMapper.map(thesis, ThesisServiceModel.class)).thenReturn(model);

        ThesisServiceModel result = thesisService.getThesisByStudentId(1L);
        assertNotNull(result);
    }
}
