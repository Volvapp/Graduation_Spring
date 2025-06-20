package com.nbu.graduation.spring;

import com.nbu.graduation.spring.model.Teacher;
import com.nbu.graduation.spring.model.enums.Department;
import com.nbu.graduation.spring.model.enums.Position;
import com.nbu.graduation.spring.model.service.TeacherServiceModel;
import com.nbu.graduation.spring.repository.TeacherRepository;
import com.nbu.graduation.spring.service.Impl.TeacherServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class TeacherServiceImplTest {

    private TeacherRepository teacherRepository;
    private ModelMapper modelMapper;
    private TeacherServiceImpl teacherService;

    @BeforeEach
    void setUp() {
        teacherRepository = mock(TeacherRepository.class);
        modelMapper = new ModelMapper();
        teacherService = new TeacherServiceImpl(teacherRepository, modelMapper);
    }

    @Test
    void createTeacher_ShouldSaveAndReturnTeacher() {
        TeacherServiceModel input = new TeacherServiceModel();
        input.setPosition(Position.PROFESSOR);

        Teacher saved = new Teacher();
        saved.setPosition(Position.PROFESSOR);

        when(teacherRepository.save(any(Teacher.class))).thenReturn(saved);

        TeacherServiceModel result = teacherService.createTeacher(input);

        assertNotNull(result);
        assertEquals(Position.PROFESSOR, result.getPosition());
        verify(teacherRepository).save(any(Teacher.class));
    }

    @Test
    void findById_ShouldReturnTeacher_WhenExists() {
        Teacher teacher = new Teacher();
        teacher.setPosition(Position.ASSISTANT);

        when(teacherRepository.findById(1L)).thenReturn(Optional.of(teacher));

        TeacherServiceModel result = teacherService.findById(1L);

        assertNotNull(result);
        assertEquals(Position.ASSISTANT, result.getPosition());
    }

    @Test
    void findById_ShouldThrowException_WhenNotFound() {
        when(teacherRepository.findById(1L)).thenReturn(Optional.empty());

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> teacherService.findById(1L));

        assertEquals("Teacher with ID 1 not found.", ex.getMessage());
    }

    @Test
    void findByUsername_ShouldReturnTeacher_WhenExists() {
        Teacher teacher = new Teacher();
        teacher.setPosition(Position.PROFESSOR);

        when(teacherRepository.findByUser_username("user1")).thenReturn(teacher);

        TeacherServiceModel result = teacherService.findByUsername("user1");

        assertNotNull(result);
        assertEquals(Position.PROFESSOR, result.getPosition());
    }

    @Test
    void findByUsername_ShouldThrowException_WhenNotFound() {
        when(teacherRepository.findByUser_username("user1")).thenReturn(null);

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> teacherService.findByUsername("user1"));

        assertEquals("Teacher with username user1 not found.", ex.getMessage());
    }

    @Test
    void updateTeacher_ShouldSaveAndReturnUpdatedTeacher() {
        TeacherServiceModel input = new TeacherServiceModel();
        input.setId(1L);
        input.setPosition(Position.ASSOCIATE_PROFESSOR);

        when(teacherRepository.existsById(1L)).thenReturn(true);
        when(teacherRepository.save(any(Teacher.class))).thenAnswer(invocation -> invocation.getArgument(0));

        TeacherServiceModel result = teacherService.updateTeacher(input);

        assertNotNull(result);
        assertEquals(Position.ASSOCIATE_PROFESSOR, result.getPosition());
        verify(teacherRepository).save(any(Teacher.class));
    }

    @Test
    void updateTeacher_ShouldThrowException_WhenIdNull() {
        TeacherServiceModel input = new TeacherServiceModel();
        input.setId(null);

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> teacherService.updateTeacher(input));

        assertEquals("Teacher or teacher ID cannot be null.", ex.getMessage());
    }

    @Test
    void updateTeacher_ShouldThrowException_WhenTeacherNotExist() {
        TeacherServiceModel input = new TeacherServiceModel();
        input.setId(1L);

        when(teacherRepository.existsById(1L)).thenReturn(false);

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> teacherService.updateTeacher(input));

        assertEquals("Teacher with ID 1 does not exist.", ex.getMessage());
    }

    @Test
    void generate3RandomTeachersForDepartment_ShouldReturnUpTo3Teachers() {
        Teacher t1 = new Teacher(); t1.setPosition(Position.PROFESSOR);
        Teacher t2 = new Teacher(); t2.setPosition(Position.ASSISTANT);
        Teacher t3 = new Teacher(); t3.setPosition(Position.SENIOR_ASSISTANT);
        Teacher t4 = new Teacher(); t4.setPosition(Position.PROFESSOR);

        when(teacherRepository.findAllByUser_Department(Department.IT))
                .thenReturn(Arrays.asList(t1, t2, t3, t4));

        List<TeacherServiceModel> result = teacherService.generate3RandomTeachersForDepartment(Department.IT);

        assertNotNull(result);
        assertTrue(result.size() <= 3);
    }

    @Test
    void generate3RandomTeachersForDepartment_ShouldReturnEmptyList_WhenNoTeachers() {
        when(teacherRepository.findAllByUser_Department(Department.IT))
                .thenReturn(List.of());

        List<TeacherServiceModel> result = teacherService.generate3RandomTeachersForDepartment(Department.IT);

        assertNotNull(result);
        assertTrue(result.isEmpty());
    }
}
