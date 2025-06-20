package com.nbu.graduation.spring;

import com.nbu.graduation.spring.model.Defense;
import com.nbu.graduation.spring.model.Student;
import com.nbu.graduation.spring.model.Teacher;
import com.nbu.graduation.spring.model.service.DefenseServiceModel;
import com.nbu.graduation.spring.model.service.StudentServiceModel;
import com.nbu.graduation.spring.model.service.TeacherServiceModel;
import com.nbu.graduation.spring.model.service.ThesisReviewServiceModel;
import com.nbu.graduation.spring.repository.DefenseRepository;
import com.nbu.graduation.spring.service.Impl.DefenseServiceImpl;
import com.nbu.graduation.spring.service.StudentService;
import com.nbu.graduation.spring.service.TeacherService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.modelmapper.ModelMapper;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DefenseServiceImplTest {

    @Mock
    DefenseRepository defenseRepository;

    @Mock
    ModelMapper modelMapper;

    @Mock
    StudentService studentService;

    @InjectMocks
    DefenseServiceImpl defenseService;

    Defense defenseEntity;
    DefenseServiceModel defenseServiceModel;

    @BeforeEach
    void setUp() {
        defenseEntity = new Defense();
        defenseEntity.setId(1L);
        defenseEntity.setCommitteeMembers(new ArrayList<>());
        defenseEntity.setStudents(new ArrayList<>());

        defenseServiceModel = new DefenseServiceModel();
        defenseServiceModel.setId(1L);
        defenseServiceModel.setCommitteeMembers(new ArrayList<>());
        defenseServiceModel.setStudents(new ArrayList<>());
    }

    @Test
    void createDefense_ShouldSaveAndReturnDefense() {
        when(modelMapper.map(defenseServiceModel, Defense.class)).thenReturn(defenseEntity);
        when(defenseRepository.save(defenseEntity)).thenReturn(defenseEntity);
        when(modelMapper.map(defenseEntity, DefenseServiceModel.class)).thenReturn(defenseServiceModel);

        DefenseServiceModel result = defenseService.createDefense(defenseServiceModel);

        assertNotNull(result);
        verify(defenseRepository).save(defenseEntity);
        verify(modelMapper).map(defenseServiceModel, Defense.class);
        verify(modelMapper).map(defenseEntity, DefenseServiceModel.class);
    }

    @Test
    void createDefense_NullDefense_ShouldThrow() {
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> defenseService.createDefense(null));
        assertEquals("Defense model не може да бъде null.", ex.getMessage());
    }

    @Test
    void updateDefense_NullId_ShouldThrow() {
        defenseServiceModel.setId(null);
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> defenseService.updateDefense(defenseServiceModel));
        assertEquals("Defense или ID на защитата не може да бъде null.", ex.getMessage());
    }

    @Test
    void updateDefense_NotFound_ShouldThrow() {
        defenseServiceModel.setId(1L);
        when(defenseRepository.findById(1L)).thenReturn(Optional.empty());
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> defenseService.updateDefense(defenseServiceModel));
        assertTrue(ex.getMessage().contains("не е намерена"));
    }

    @Test
    void deleteDefenseById_ValidId_ShouldDelete() {
        when(defenseRepository.existsById(1L)).thenReturn(true);

        defenseService.deleteDefenseById(1L);

        verify(defenseRepository).deleteById(1L);
    }

    @Test
    void deleteDefenseById_NullId_ShouldThrow() {
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> defenseService.deleteDefenseById(null));
        assertEquals("ID не може да бъде null.", ex.getMessage());
    }

    @Test
    void deleteDefenseById_NotExists_ShouldThrow() {
        when(defenseRepository.existsById(1L)).thenReturn(false);
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> defenseService.deleteDefenseById(1L));
        assertTrue(ex.getMessage().contains("не съществува"));
    }

    @Test
    void getDefenseById_ValidId_ShouldReturn() {
        when(defenseRepository.findById(1L)).thenReturn(Optional.of(defenseEntity));
        when(modelMapper.map(defenseEntity, DefenseServiceModel.class)).thenReturn(defenseServiceModel);

        DefenseServiceModel result = defenseService.getDefenseById(1L);

        assertNotNull(result);
        verify(defenseRepository).findById(1L);
    }

    @Test
    void getDefenseById_NullId_ShouldThrow() {
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> defenseService.getDefenseById(null));
        assertEquals("ID не може да бъде null.", ex.getMessage());
    }

    @Test
    void getDefenseById_NotFound_ShouldThrow() {
        when(defenseRepository.findById(1L)).thenReturn(Optional.empty());
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> defenseService.getDefenseById(1L));
        assertTrue(ex.getMessage().contains("не е намерена"));
    }

    @Test
    void assignGradeToStudent_ValidInput_ShouldUpdateGrade() {
        Long studentId = 1L;
        Long teacherId = 10L;
        Double grade = 5.0;

        StudentServiceModel student = new StudentServiceModel();
        student.setId(studentId);
        student.setCommitteeGrades(new HashMap<>());

        DefenseServiceModel defense = new DefenseServiceModel();
        TeacherServiceModel teacher = new TeacherServiceModel();
        teacher.setId(teacherId);
        defense.setCommitteeMembers(List.of(teacher));

        student.setDefense(defense);

        when(studentService.getStudentById(studentId)).thenReturn(student);

        defenseService.assignGradeToStudent(studentId, grade, teacherId);

        assertEquals(grade, student.getCommitteeGrades().get(teacherId));
        verify(studentService).updateStudent(student);
    }

    @Test
    void assignGradeToStudent_InvalidGrade_ShouldThrow() {
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> defenseService.assignGradeToStudent(1L, 1.5, 10L));
        assertTrue(ex.getMessage().contains("между 2.0 и 6.0"));
    }

    @Test
    void setDefenseDateIfCommitteeMember_Valid_ShouldSetDate() {
        Long defenseId = 1L;
        Long teacherId = 2L;
        LocalDate futureDate = LocalDate.now().plusDays(1);

        Teacher teacher = new Teacher();
        teacher.setId(teacherId);
        Defense defense = new Defense();
        defense.setId(defenseId);
        defense.setCommitteeMembers(List.of(teacher));

        when(defenseRepository.findById(defenseId)).thenReturn(Optional.of(defense));

        boolean result = defenseService.setDefenseDateIfCommitteeMember(defenseId, teacherId, futureDate);

        assertTrue(result);
        assertEquals(futureDate, defense.getDefenseDate());
        verify(defenseRepository).save(defense);
    }

    @Test
    void setDefenseDateIfCommitteeMember_NotCommitteeMember_ShouldReturnFalse() {
        Long defenseId = 1L;
        Long teacherId = 2L;
        LocalDate futureDate = LocalDate.now().plusDays(1);

        Defense defense = new Defense();
        defense.setId(defenseId);
        defense.setCommitteeMembers(Collections.emptyList());

        when(defenseRepository.findById(defenseId)).thenReturn(Optional.of(defense));

        boolean result = defenseService.setDefenseDateIfCommitteeMember(defenseId, teacherId, futureDate);

        assertFalse(result);
        verify(defenseRepository, never()).save(any());
    }

    @Test
    void setDefenseDateIfCommitteeMember_PastDate_ShouldThrow() {
        Long defenseId = 1L;
        Long teacherId = 2L;
        LocalDate pastDate = LocalDate.now().minusDays(1);

        Teacher teacher = new Teacher();
        teacher.setId(teacherId);
        Defense defense = new Defense();
        defense.setId(defenseId);
        defense.setCommitteeMembers(List.of(teacher));

        when(defenseRepository.findById(defenseId)).thenReturn(Optional.of(defense));

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> defenseService.setDefenseDateIfCommitteeMember(defenseId, teacherId, pastDate));
        assertTrue(ex.getMessage().contains("не може да е в миналото"));
    }

    @Test
    void averagePresentedInPeriod_NoDefenses_ShouldReturnZero() {
        when(defenseRepository.findAllByDefenseDateBetween(any(), any())).thenReturn(Collections.emptyList());

        double avg = defenseService.averagePresentedInPeriod(LocalDate.now().minusDays(10), LocalDate.now());

        assertEquals(0.0, avg);
    }

    @Test
    void averagePresentedInPeriod_InvalidPeriod_ShouldThrow() {
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> defenseService.averagePresentedInPeriod(LocalDate.now(), LocalDate.now().minusDays(1)));
        assertTrue(ex.getMessage().contains("Крайна дата трябва да е след начална дата"));
    }
}
