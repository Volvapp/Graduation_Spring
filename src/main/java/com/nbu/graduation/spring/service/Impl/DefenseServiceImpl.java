package com.nbu.graduation.spring.service.Impl;

import com.nbu.graduation.spring.model.Defense;
import com.nbu.graduation.spring.model.Student;
import com.nbu.graduation.spring.model.service.DefenseServiceModel;
import com.nbu.graduation.spring.model.service.StudentServiceModel;
import com.nbu.graduation.spring.model.service.TeacherServiceModel;
import com.nbu.graduation.spring.model.service.ThesisReviewServiceModel;
import com.nbu.graduation.spring.repository.DefenseRepository;
import com.nbu.graduation.spring.service.DefenseService;
import com.nbu.graduation.spring.service.StudentService;
import com.nbu.graduation.spring.service.TeacherService;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
public class DefenseServiceImpl implements DefenseService {

    private static final Logger logger = LoggerFactory.getLogger(DefenseServiceImpl.class);

    private final DefenseRepository defenseRepository;
    private final ModelMapper modelMapper;
    private final StudentService studentService;
    private final TeacherService teacherService;

    public DefenseServiceImpl(DefenseRepository defenseRepository, ModelMapper modelMapper, StudentService studentService, TeacherService teacherService) {
        this.defenseRepository = defenseRepository;
        this.modelMapper = modelMapper;
        this.studentService = studentService;
        this.teacherService = teacherService;
    }

    private void validateDefenseModel(DefenseServiceModel defense) {
        if (defense == null) {
            throw new IllegalArgumentException("Defense model не може да бъде null.");
        }

    }

    @Override
    @Transactional
    public DefenseServiceModel createDefense(DefenseServiceModel defenseServiceModel) {
        validateDefenseModel(defenseServiceModel);

        Defense defense = modelMapper.map(defenseServiceModel, Defense.class);
        defenseRepository.save(defense);
        logger.info("Създадена е нова защита с ID {}", defense.getId());
        return this.modelMapper.map(defense, DefenseServiceModel.class);
    }

    @Override
    @Transactional
    public DefenseServiceModel updateDefense(DefenseServiceModel defenseServiceModel) {
        if (defenseServiceModel == null || defenseServiceModel.getId() == null) {
            throw new IllegalArgumentException("Defense или ID на защитата не може да бъде null.");
        }

        Defense existingDefense = defenseRepository.findById(defenseServiceModel.getId())
                .orElseThrow(() -> new IllegalArgumentException("Защитата с ID " + defenseServiceModel.getId() + " не е намерена."));

        validateDefenseModel(defenseServiceModel);

        Defense defenseToUpdate = modelMapper.map(defenseServiceModel, Defense.class);
        defenseRepository.save(defenseToUpdate);
        logger.info("Обновена е защита с ID {}", defenseToUpdate.getId());

        return this.modelMapper.map(defenseToUpdate, DefenseServiceModel.class);
    }

    @Override
    @Transactional
    public void deleteDefenseById(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("ID не може да бъде null.");
        }
        if (!defenseRepository.existsById(id)) {
            throw new IllegalArgumentException("Защитата с ID " + id + " не съществува.");
        }
        defenseRepository.deleteById(id);
        logger.info("Изтрита е защита с ID {}", id);
    }

    @Override
    public DefenseServiceModel getDefenseById(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("ID не може да бъде null.");
        }
        Defense defense = defenseRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Защитата с ID " + id + " не е намерена."));
        return this.modelMapper.map(defense, DefenseServiceModel.class);
    }

    @Override
    @Transactional
    public void assignDefenseToStudent(ThesisReviewServiceModel review) {
        if (review == null || review.getThesis() == null || review.getThesis().getAssignment() == null) {
            throw new IllegalArgumentException("Некоректен обект за рецензия или теза.");
        }

        StudentServiceModel student = review.getThesis().getAssignment().getStudent();
        if (student == null) {
            throw new IllegalArgumentException("Студентът не е зададен.");
        }

        DefenseServiceModel defense = new DefenseServiceModel();
        List<TeacherServiceModel> committee = teacherService.generate3RandomTeachersForDepartment(student.getUser().getDepartment());
        if (committee.size() != 3) {
            throw new IllegalStateException("Неуспешно генериране на тричленна комисия.");
        }
        defense.setCommitteeMembers(new ArrayList<>(committee));
        defense.getStudents().add(student);

        DefenseServiceModel createdDefense = this.createDefense(defense);

        student.setDefense(createdDefense);
        studentService.updateStudent(student);
        logger.info("Назначена защита на студент с ID {}", student.getId());
    }

    @Override
    @Transactional
    public void assignGradeToStudent(Long studentId, Double grade, Long teacherId) {
        if (studentId == null || teacherId == null) {
            throw new IllegalArgumentException("ID на студент и учител трябва да са зададени.");
        }
        if (grade == null || grade < 2.0 || grade > 6.0) {
            throw new IllegalArgumentException("Оценката трябва да е между 2.0 и 6.0.");
        }

        StudentServiceModel student = this.studentService.getStudentById(studentId);
        if (student == null) {
            throw new IllegalArgumentException("Студент с ID " + studentId + " не е намерен.");
        }

        student.getCommitteeGrades().put(teacherId, grade);

        int expectedCount = student.getDefense() != null ? student.getDefense().getCommitteeMembers().size() : 0;
        if (expectedCount == 0) {
            throw new IllegalStateException("Студентът няма зададена комисия.");
        }

        if (student.getCommitteeGrades().size() == expectedCount) {
            double avgGrade = student.getCommitteeGrades()
                    .values()
                    .stream()
                    .mapToDouble(Double::doubleValue)
                    .average()
                    .orElse(0.0);

            BigDecimal bd = new BigDecimal(avgGrade).setScale(2, RoundingMode.HALF_UP);
            student.setGrade(bd.doubleValue());
            logger.info("Изчислена е средна оценка {} за студент с ID {}", bd.doubleValue(), studentId);
        }

        studentService.updateStudent(student);
    }

    @Override
    @Transactional
    public boolean setDefenseDateIfCommitteeMember(Long defenseId, Long teacherId, LocalDate defenseDate) {
        if (defenseId == null || teacherId == null || defenseDate == null) {
            throw new IllegalArgumentException("defenseId, teacherId и defenseDate не могат да бъдат null.");
        }

        Defense defense = defenseRepository.findById(defenseId)
                .orElseThrow(() -> new IllegalArgumentException("Защитата с ID " + defenseId + " не е намерена."));

        boolean isCommitteeMember = defense.getCommitteeMembers()
                .stream()
                .anyMatch(t -> t.getId().equals(teacherId));

        if (!isCommitteeMember) {
            logger.warn("Опит за задаване на дата от некомисиялен член (Teacher ID: {})", teacherId);
            return false;
        }

        if (defenseDate.isBefore(LocalDate.now())) {
            throw new IllegalArgumentException("Дата на защита не може да е в миналото.");
        }

        defense.setDefenseDate(defenseDate);
        defenseRepository.save(defense);
        logger.info("Дата на защита зададена за защита с ID {}: {}", defenseId, defenseDate);
        return true;
    }

    @Override
    public DefenseServiceModel getDefenseByDate(LocalDate defenseDate) {
        if (defenseDate == null) {
            throw new IllegalArgumentException("Дата не може да бъде null.");
        }
        Defense defense = this.defenseRepository.findByDefenseDate(defenseDate);
        if (defense == null) {
            throw new IllegalArgumentException("Не е намерена защита на дата " + defenseDate);
        }
        return this.modelMapper.map(defense, DefenseServiceModel.class);
    }

    @Override
    public Double averagePresentedInPeriod(LocalDate from, LocalDate to) {
        if (from == null || to == null) {
            throw new IllegalArgumentException("Датите за периода не могат да бъдат null.");
        }
        if (to.isBefore(from)) {
            throw new IllegalArgumentException("Крайна дата трябва да е след начална дата.");
        }

        List<Defense> defenses = defenseRepository.findAllByDefenseDateBetween(from, to);
        if (defenses.isEmpty()) {
            return 0.0;
        }

        long totalPresented = defenses.stream()
                .flatMap(defense -> defense.getStudents().stream())
                .filter(Student::isHasParticipated)
                .count();

        if (totalPresented == 0) {
            return 0.0;
        }

        long gradedCount = defenses.stream()
                .flatMap(defense -> defense.getStudents().stream())
                .filter(student -> student.getGrade() != null && student.isHasParticipated())
                .count();

        return (double) gradedCount / totalPresented;
    }

}
