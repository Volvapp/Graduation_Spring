package com.nbu.graduation.spring.model.dto;

import com.nbu.graduation.spring.model.enums.Department;

public class StudentDTO {
    private String firstName;
    private String lastName;
    private Department department;
    private String facultyNumber;
    private Long assignmentId;
    private Long defenseId;

    public StudentDTO() {
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

    public Department getDepartment() {
        return department;
    }

    public void setDepartment(Department department) {
        this.department = department;
    }

    public String getFacultyNumber() {
        return facultyNumber;
    }

    public void setFacultyNumber(String facultyNumber) {
        this.facultyNumber = facultyNumber;
    }

    public Long getAssignmentId() {
        return assignmentId;
    }

    public void setAssignmentId(Long assignmentId) {
        this.assignmentId = assignmentId;
    }

    public Long getDefenseId() {
        return defenseId;
    }

    public void setDefenseId(Long defenseId) {
        this.defenseId = defenseId;
    }
}
