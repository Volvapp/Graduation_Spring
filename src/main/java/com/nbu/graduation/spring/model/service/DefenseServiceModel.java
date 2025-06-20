package com.nbu.graduation.spring.model.service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class DefenseServiceModel {
    private Long id;
    private LocalDate defenseDate;
    private List<TeacherServiceModel> committeeMembers;
    private List<StudentServiceModel> students;

    public DefenseServiceModel() {
        this.committeeMembers = new ArrayList<>();
        this.students = new ArrayList<>();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getDefenseDate() {
        return defenseDate;
    }

    public void setDefenseDate(LocalDate defenseDate) {
        this.defenseDate = defenseDate;
    }

    public List<TeacherServiceModel> getCommitteeMembers() {
        return committeeMembers;
    }

    public void setCommitteeMembers(List<TeacherServiceModel> committeeMembers) {
        this.committeeMembers = committeeMembers;
    }

    public List<StudentServiceModel> getStudents() {
        return students;
    }

    public void setStudents(List<StudentServiceModel> students) {
        this.students = students;
    }

    @Override
    public String toString() {
        return "DefenseServiceModel{" +
                "id=" + id +
                ", defenseDate=" + defenseDate +
                ", committeeMembers=" + committeeMembers +
                ", students=" + students +
                '}';
    }
}
