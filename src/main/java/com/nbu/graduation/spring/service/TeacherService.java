package com.nbu.graduation.spring.service;

import com.nbu.graduation.spring.model.enums.Department;
import com.nbu.graduation.spring.model.enums.Position;
import com.nbu.graduation.spring.model.service.DefenseServiceModel;
import com.nbu.graduation.spring.model.service.TeacherServiceModel;

import java.util.List;

public interface TeacherService {
    TeacherServiceModel createTeacher(TeacherServiceModel teacherServiceModel);

    TeacherServiceModel updateTeacher(TeacherServiceModel teacherServiceModel);

    TeacherServiceModel findById(Long id);

    TeacherServiceModel findByUserId(long id);

    List<TeacherServiceModel> findAll();

    TeacherServiceModel findByUsername(String username);

    List<TeacherServiceModel> findByDepartment(Department department);

    void initializeTeachers();

    void updateTeacherPosition(String username, Position position);

    List<TeacherServiceModel> generate3RandomTeachersForDepartment(Department department);

    List<DefenseServiceModel> findAllDefensesWhereTeacherParticipates(Long teacherId);
}
