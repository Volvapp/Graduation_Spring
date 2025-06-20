package com.nbu.graduation.spring.model.dto;

import com.nbu.graduation.spring.model.enums.Department;
import com.nbu.graduation.spring.model.enums.Position;

import java.util.List;

public class TeacherDTO {
    private String firstName;
    private String lastName;
    private Position position;
    private Department department;
    private List<Long> supervisedAssignmentIds;
    private List<Long> defenseIds;
    private List<Long> reviewIds;

    public TeacherDTO() {
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public Position getPosition() {
        return position;
    }

    public void setPosition(Position position) {
        this.position = position;
    }

    public Department getDepartment() {
        return department;
    }

    public void setDepartment(Department department) {
        this.department = department;
    }

    public List<Long> getSupervisedAssignmentIds() {
        return supervisedAssignmentIds;
    }

    public void setSupervisedAssignmentIds(List<Long> supervisedAssignmentIds) {
        this.supervisedAssignmentIds = supervisedAssignmentIds;
    }

    public List<Long> getDefenseIds() {
        return defenseIds;
    }

    public void setDefenseIds(List<Long> defenseIds) {
        this.defenseIds = defenseIds;
    }

    public List<Long> getReviewIds() {
        return reviewIds;
    }

    public void setReviewIds(List<Long> reviewIds) {
        this.reviewIds = reviewIds;
    }
}
