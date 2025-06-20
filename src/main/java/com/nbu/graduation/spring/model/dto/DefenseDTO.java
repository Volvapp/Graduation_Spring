package com.nbu.graduation.spring.model.dto;

import java.time.LocalDate;
import java.util.List;

public class DefenseDTO {
    private LocalDate defenseDate;
    private List<Long> committeeMemberIds;
    private List<Long> studentIds;
    private Double grade;

    public LocalDate getDefenseDate() {
        return defenseDate;
    }

    public void setDefenseDate(LocalDate defenseDate) {
        this.defenseDate = defenseDate;
    }

    public List<Long> getCommitteeMemberIds() {
        return committeeMemberIds;
    }

    public void setCommitteeMemberIds(List<Long> committeeMemberIds) {
        this.committeeMemberIds = committeeMemberIds;
    }

    public List<Long> getStudentIds() {
        return studentIds;
    }

    public void setStudentIds(List<Long> studentIds) {
        this.studentIds = studentIds;
    }

    public Double getGrade() {
        return grade;
    }

    public void setGrade(Double grade) {
        this.grade = grade;
    }
}
