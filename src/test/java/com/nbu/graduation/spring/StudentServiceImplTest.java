package com.nbu.graduation.spring;

import com.nbu.graduation.spring.model.Student;
import com.nbu.graduation.spring.model.service.DefenseServiceModel;
import com.nbu.graduation.spring.model.service.StudentServiceModel;
import com.nbu.graduation.spring.repository.StudentRepository;
import com.nbu.graduation.spring.service.Impl.StudentServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;

import java.time.LocalDate;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class StudentServiceImplTest {

    private StudentRepository studentRepository;
    private ModelMapper modelMapper;
    private StudentServiceImpl service;

    @BeforeEach
    void setUp() {
        studentRepository = mock(StudentRepository.class);
        modelMapper = new ModelMapper();
        service = new StudentServiceImpl(studentRepository, modelMapper);
    }

    @Test
    void createStudent_shouldThrow_whenNull() {
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> service.createStudent(null));
        assertEquals("Student data cannot be null.", ex.getMessage());
    }

    @Test
    void createStudent_shouldGenerateFacultyNumberAndSave() {
        StudentServiceModel input = new StudentServiceModel();
        input.setId(null);
        input.setHasParticipated(false);

        when(studentRepository.save(any(Student.class))).thenAnswer(invocation -> invocation.getArgument(0));

        StudentServiceModel created = service.createStudent(input);

        assertNotNull(created.getFacultyNumber());
        assertTrue(created.getFacultyNumber().startsWith("F"));
        verify(studentRepository).save(any(Student.class));
    }

    @Test
    void updateStudent_shouldThrow_whenNullOrIdNull() {
        assertThrows(IllegalArgumentException.class, () -> service.updateStudent(null));

        StudentServiceModel model = new StudentServiceModel();
        assertThrows(IllegalArgumentException.class, () -> service.updateStudent(model));
    }

    @Test
    void updateStudent_shouldThrow_whenStudentNotFound() {
        StudentServiceModel model = new StudentServiceModel();
        model.setId(1L);

        when(studentRepository.findById(1L)).thenReturn(Optional.empty());

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> service.updateStudent(model));
        assertTrue(ex.getMessage().contains("not found"));
    }



    @Test
    void deleteStudentById_shouldThrow_whenIdNull() {
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> service.deleteStudentById(null));
        assertEquals("Student ID cannot be null.", ex.getMessage());
    }

    @Test
    void deleteStudentById_shouldThrow_whenNotExist() {
        when(studentRepository.existsById(1L)).thenReturn(false);

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> service.deleteStudentById(1L));
        assertTrue(ex.getMessage().contains("does not exist"));
    }

    @Test
    void deleteStudentById_shouldCallRepositoryDelete() {
        when(studentRepository.existsById(1L)).thenReturn(true);

        service.deleteStudentById(1L);

        verify(studentRepository).deleteById(1L);
    }

    @Test
    void getStudentById_shouldThrow_whenIdNull() {
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> service.getStudentById(null));
        assertEquals("Student ID cannot be null.", ex.getMessage());
    }

    @Test
    void getStudentById_shouldThrow_whenNotFound() {
        when(studentRepository.findById(1L)).thenReturn(Optional.empty());

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> service.getStudentById(1L));
        assertTrue(ex.getMessage().contains("not found"));
    }

    @Test
    void generateFacultyNumber_shouldReturnCorrectFormat() {
        String facultyNumber = service.generateFacultyNumber();
        assertNotNull(facultyNumber);
        assertTrue(facultyNumber.matches("F\\d{6}"));
    }

    @Test
    void findAll_shouldReturnList() {
        Student s = new Student();
        s.setId(1L);
        when(studentRepository.findAll()).thenReturn(List.of(s));

        List<StudentServiceModel> result = service.findAll();

        assertEquals(1, result.size());
        assertEquals(1L, result.get(0).getId());
    }

    @Test
    void findByUsername_shouldThrow_whenNullOrBlank() {
        assertThrows(IllegalArgumentException.class, () -> service.findByUsername(null));
        assertThrows(IllegalArgumentException.class, () -> service.findByUsername(" "));
    }

    @Test
    void findByUsername_shouldThrow_whenNotFound() {
        when(studentRepository.findByUser_Username("user")).thenReturn(null);

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> service.findByUsername("user"));
        assertTrue(ex.getMessage().contains("not found"));
    }

    @Test
    void findByUsername_shouldReturnStudent() {
        Student student = new Student();
        student.setId(1L);
        when(studentRepository.findByUser_Username("user")).thenReturn(student);

        StudentServiceModel model = service.findByUsername("user");

        assertEquals(1L, model.getId());
    }

    @Test
    void markParticipation_shouldThrow_whenIdNull() {
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> service.markParticipation(null));
        assertEquals("Student ID cannot be null.", ex.getMessage());
    }

    @Test
    void markParticipation_shouldUpdateStudent() {
        StudentServiceModel student = new StudentServiceModel();
        student.setId(1L);
        student.setHasParticipated(false);

        when(studentRepository.findById(1L)).thenReturn(Optional.of(new Student()));
        when(studentRepository.save(any(Student.class))).thenAnswer(i -> i.getArgument(0));
        when(studentRepository.findById(1L)).thenReturn(Optional.of(new Student()));
        // Override getStudentById to use real method logic for test
        StudentServiceImpl spyService = spy(service);
        doReturn(student).when(spyService).getStudentById(1L);
        doReturn(student).when(spyService).updateStudent(any());

        spyService.markParticipation(1L);

        assertTrue(student.isHasParticipated());
        verify(spyService).updateStudent(student);
    }

    @Test
    void canParticipate_variousCases() {
        StudentServiceModel student = new StudentServiceModel();
        student.setId(1L);
        DefenseServiceModel defense = new DefenseServiceModel();
        defense.setDefenseDate(LocalDate.now());
        defense.setStudents(List.of(student));
        student.setDefense(defense);

        student.setHasParticipated(false);
        student.setGrade(null);

        StudentServiceImpl spyService = spy(service);
        doReturn(student).when(spyService).getStudentById(1L);

        // No other students waiting
        boolean canParticipate = spyService.canParticipate(1L);
        assertTrue(canParticipate);

        // Student already participated
        student.setHasParticipated(true);
        assertFalse(spyService.canParticipate(1L));
        student.setHasParticipated(false);

        // Student has grade
        student.setGrade(5.0);
        assertFalse(spyService.canParticipate(1L));
        student.setGrade(null);

        // Defense date not today
        defense.setDefenseDate(LocalDate.now().plusDays(1));
        assertFalse(spyService.canParticipate(1L));
        defense.setDefenseDate(LocalDate.now());

        // Other students waiting
        StudentServiceModel otherStudent = new StudentServiceModel();
        otherStudent.setId(2L);
        otherStudent.setHasParticipated(true);
        otherStudent.setGrade(null);
        defense.setStudents(List.of(student, otherStudent));
        assertFalse(spyService.canParticipate(1L));
    }

    @Test
    void getGraduatedInPeriod_shouldThrowOnInvalidDates() {
        LocalDate from = LocalDate.now();
        LocalDate to = from.minusDays(1);
        assertThrows(IllegalArgumentException.class,
                () -> service.getGraduatedInPeriod(from, to));
    }

    @Test
    void getGraduatedInPeriod_shouldReturnStudents() {
        LocalDate from = LocalDate.now().minusDays(5);
        LocalDate to = LocalDate.now();
        Student s = new Student();
        s.setId(1L);
        when(studentRepository.findAllByGradeGreaterThanAndDefense_DefenseDateBetween(3.0, from, to))
                .thenReturn(List.of(s));

        List<StudentServiceModel> result = service.getGraduatedInPeriod(from, to);

        assertEquals(1, result.size());
        assertEquals(1L, result.get(0).getId());
    }

    @Test
    void countSuccessfulBySupervisor_shouldThrowOnNull() {
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> service.countSuccessfulBySupervisor(null));
        assertEquals("Supervisor ID cannot be null.", ex.getMessage());
    }

    @Test
    void countSuccessfulBySupervisor_shouldReturnCount() {
        when(studentRepository.countByGradeGreaterThanAndAssignment_Supervisor_Id(3.0, 1L)).thenReturn(10);

        long count = service.countSuccessfulBySupervisor(1L);

        assertEquals(10L, count);
    }
}
