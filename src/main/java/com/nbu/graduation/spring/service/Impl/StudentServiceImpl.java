package com.nbu.graduation.spring.service.Impl;

import com.nbu.graduation.spring.model.Student;
import com.nbu.graduation.spring.model.service.DefenseServiceModel;
import com.nbu.graduation.spring.model.service.StudentServiceModel;
import com.nbu.graduation.spring.repository.StudentRepository;
import com.nbu.graduation.spring.service.StudentService;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;
@Service
public class StudentServiceImpl implements StudentService {
    private final StudentRepository studentRepository;
    private final ModelMapper modelMapper;

    public StudentServiceImpl(StudentRepository studentRepository, ModelMapper modelMapper) {
        this.studentRepository = studentRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    public StudentServiceModel createStudent(StudentServiceModel studentServiceModel) {
        if (studentServiceModel == null) {
            throw new IllegalArgumentException("Student data cannot be null.");
        }


        Student student = this.modelMapper.map(studentServiceModel, Student.class);
        student.setFacultyNumber(generateFacultyNumber());
        this.studentRepository.save(student);

        return this.modelMapper.map(student, StudentServiceModel.class);
    }

    @Override
    public StudentServiceModel updateStudent(StudentServiceModel studentServiceModel) {
        if (studentServiceModel == null || studentServiceModel.getId() == null) {
            throw new IllegalArgumentException("Student or student ID cannot be null.");
        }

        Student existingStudent = this.studentRepository.findById(studentServiceModel.getId())
                .orElseThrow(() -> new IllegalArgumentException("Student with ID " + studentServiceModel.getId() + " not found."));

        Student student = this.modelMapper.map(studentServiceModel, Student.class);

        if (studentServiceModel.getCommitteeGrades() != null) {
            student.setCommitteeGrades(new HashMap<>(studentServiceModel.getCommitteeGrades()));
        }

        this.studentRepository.save(student);
        return this.modelMapper.map(student, StudentServiceModel.class);
    }

    @Override
    public void deleteStudentById(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("Student ID cannot be null.");
        }

        if (!studentRepository.existsById(id)) {
            throw new IllegalArgumentException("Student with ID " + id + " does not exist.");
        }

        this.studentRepository.deleteById(id);
    }

    @Override
    public StudentServiceModel getStudentById(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("Student ID cannot be null.");
        }

        Student student = this.studentRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Student with ID " + id + " not found."));

        StudentServiceModel model = this.modelMapper.map(student, StudentServiceModel.class);

        if (student.getCommitteeGrades() != null) {
            model.setCommitteeGrades(new HashMap<>(student.getCommitteeGrades()));
        }

        return model;
    }

    @Override
    public String generateFacultyNumber() {
        Random random = new Random();
        String sixDigitNum = String.format("%06d", random.nextInt(1_000_000));
        return "F" + sixDigitNum;
    }

    @Override
    public List<StudentServiceModel> findAll() {
        return this.studentRepository.findAll().stream()
                .map(student -> modelMapper.map(student, StudentServiceModel.class))
                .collect(Collectors.toList());
    }

    @Override
    public StudentServiceModel findByUsername(String username) {
        if (username == null || username.isBlank()) {
            throw new IllegalArgumentException("Username cannot be null or empty.");
        }

        Student student = this.studentRepository.findByUser_Username(username);
        if (student == null) {
            throw new IllegalArgumentException("Student with username " + username + " not found.");
        }

        return this.modelMapper.map(student, StudentServiceModel.class);
    }

    @Override
    public void initializeStudents() {
        List<Student> students = new ArrayList<>();
        for (int i = 0; i < 13; i++) {
            students.add(new Student(String.format("f%05d", i), null, null));
        }
        studentRepository.saveAll(students);
    }

    @Override
    public void markParticipation(Long studentId) {
        if (studentId == null) {
            throw new IllegalArgumentException("Student ID cannot be null.");
        }
        StudentServiceModel student = this.getStudentById(studentId);
        student.setHasParticipated(true);
        this.updateStudent(student);
    }

    @Override
    public boolean canParticipate(Long studentId) {
        if (studentId == null) {
            throw new IllegalArgumentException("Student ID cannot be null.");
        }

        StudentServiceModel student = this.getStudentById(studentId);
        DefenseServiceModel defense = student.getDefense();

        if (defense == null || defense.getDefenseDate() == null) {
            return false;
        }

        if (!defense.getDefenseDate().equals(LocalDate.now())) {
            return false;
        }

        if (student.isHasParticipated() || student.getGrade() != null) {
            return false;
        }

        boolean othersWaiting = defense.getStudents().stream()
                .filter(s -> !s.getId().equals(studentId))
                .anyMatch(s -> s.isHasParticipated() && s.getGrade() == null);

        return !othersWaiting;
    }

    @Override
    public List<StudentServiceModel> getGraduatedInPeriod(LocalDate from, LocalDate to) {
        if (from == null || to == null || from.isAfter(to)) {
            throw new IllegalArgumentException("Invalid date range.");
        }

        return this.studentRepository.findAllByGradeGreaterThanAndDefense_DefenseDateBetween(3.0, from, to)
                .stream()
                .map(student -> this.modelMapper.map(student, StudentServiceModel.class))
                .toList();
    }

    @Override
    public long countSuccessfulBySupervisor(Long supervisorId) {
        if (supervisorId == null) {
            throw new IllegalArgumentException("Supervisor ID cannot be null.");
        }
        return this.studentRepository.countByGradeGreaterThanAndAssignment_Supervisor_Id(3.0, supervisorId);
    }
}
