package com.nbu.graduation.spring.service.Impl;

import com.nbu.graduation.spring.model.Teacher;
import com.nbu.graduation.spring.model.enums.Department;
import com.nbu.graduation.spring.model.enums.Position;
import com.nbu.graduation.spring.model.service.DefenseServiceModel;
import com.nbu.graduation.spring.model.service.TeacherServiceModel;
import com.nbu.graduation.spring.repository.TeacherRepository;
import com.nbu.graduation.spring.service.TeacherService;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;
@Service
public class TeacherServiceImpl implements TeacherService {
    private final TeacherRepository teacherRepository;
    private final ModelMapper modelMapper;

    public TeacherServiceImpl(TeacherRepository teacherRepository, ModelMapper modelMapper) {
        this.teacherRepository = teacherRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    public TeacherServiceModel createTeacher(TeacherServiceModel teacherServiceModel) {
        if (teacherServiceModel == null) {
            throw new IllegalArgumentException("Teacher data cannot be null.");
        }

        Teacher teacher = this.modelMapper.map(teacherServiceModel, Teacher.class);
        return this.modelMapper.map(this.teacherRepository.save(teacher), TeacherServiceModel.class);
    }

    @Override
    public TeacherServiceModel updateTeacher(TeacherServiceModel teacherServiceModel) {
        if (teacherServiceModel == null || teacherServiceModel.getId() == null) {
            throw new IllegalArgumentException("Teacher or teacher ID cannot be null.");
        }

        if (!teacherRepository.existsById(teacherServiceModel.getId())) {
            throw new IllegalArgumentException("Teacher with ID " + teacherServiceModel.getId() + " does not exist.");
        }

        Teacher teacher = this.modelMapper.map(teacherServiceModel, Teacher.class);
        return this.modelMapper.map(this.teacherRepository.save(teacher), TeacherServiceModel.class);
    }

    @Override
    public TeacherServiceModel findById(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("Teacher ID cannot be null.");
        }

        return this.modelMapper.map(
                this.teacherRepository.findById(id)
                        .orElseThrow(() -> new IllegalArgumentException("Teacher with ID " + id + " not found.")),
                TeacherServiceModel.class);
    }

    @Override
    public TeacherServiceModel findByUserId(long id) {
        Teacher teacher = this.teacherRepository.findByUserId(id);
        if (teacher == null) {
            throw new IllegalArgumentException("Teacher with User ID " + id + " not found.");
        }
        return this.modelMapper.map(teacher, TeacherServiceModel.class);
    }

    @Override
    public List<TeacherServiceModel> findAll() {
        return this.teacherRepository.findAll().stream()
                .map(teacher -> modelMapper.map(teacher, TeacherServiceModel.class))
                .collect(Collectors.toList());
    }

    @Override
    public TeacherServiceModel findByUsername(String username) {
        if (username == null || username.isBlank()) {
            throw new IllegalArgumentException("Username cannot be null or empty.");
        }

        Teacher teacher = this.teacherRepository.findByUser_username(username);
        if (teacher == null) {
            throw new IllegalArgumentException("Teacher with username " + username + " not found.");
        }

        return this.modelMapper.map(teacher, TeacherServiceModel.class);
    }

    @Override
    public List<TeacherServiceModel> findByDepartment(Department department) {
        if (department == null) {
            throw new IllegalArgumentException("Department cannot be null.");
        }

        return this.teacherRepository.findAllByUser_department(department).stream()
                .map(teacher -> this.modelMapper.map(teacher, TeacherServiceModel.class))
                .collect(Collectors.toList());
    }

    @Override
    public void initializeTeachers() {
        Position[] positions = {
                Position.ASSISTANT, Position.SENIOR_ASSISTANT, Position.ASSOCIATE_PROFESSOR,
                Position.PROFESSOR, Position.PROFESSOR, Position.PROFESSOR, Position.PROFESSOR,
                Position.PROFESSOR, Position.PROFESSOR, Position.PROFESSOR, Position.PROFESSOR,
                Position.PROFESSOR, Position.PROFESSOR
        };

        List<Teacher> teachers = Arrays.stream(positions)
                .map(pos -> {
                    Teacher t = new Teacher();
                    t.setPosition(pos);
                    return t;
                })
                .collect(Collectors.toList());

        teacherRepository.saveAll(teachers);
    }

    @Override
    public void updateTeacherPosition(String username, Position position) {
        if (username == null || username.isBlank()) {
            throw new IllegalArgumentException("Username cannot be null or empty.");
        }
        if (position == null) {
            throw new IllegalArgumentException("Position cannot be null.");
        }

        TeacherServiceModel teacher = findByUsername(username);
        teacher.setPosition(position);
        updateTeacher(teacher);
    }

    @Override
    public List<TeacherServiceModel> generate3RandomTeachersForDepartment(Department department) {
        if (department == null) {
            throw new IllegalArgumentException("Department cannot be null.");
        }

        List<Teacher> teachers = teacherRepository.findAllByUser_Department(department);
        if (teachers.isEmpty()) {
            return Collections.emptyList();
        }

        Collections.shuffle(teachers);

        return teachers.stream()
                .limit(3)
                .map(teacher -> modelMapper.map(teacher, TeacherServiceModel.class))
                .collect(Collectors.toList());
    }

    @Override
    public List<DefenseServiceModel> findAllDefensesWhereTeacherParticipates(Long teacherId) {
        if (teacherId == null) {
            throw new IllegalArgumentException("Teacher ID cannot be null.");
        }

        Teacher teacher = this.teacherRepository.findById(teacherId)
                .orElseThrow(() -> new IllegalArgumentException("Teacher with ID " + teacherId + " not found."));

        TeacherServiceModel teacherModel = this.modelMapper.map(teacher, TeacherServiceModel.class);
        return teacherModel.getDefenses();
    }
}
