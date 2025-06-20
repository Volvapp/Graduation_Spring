package com.nbu.graduation.spring.model.service;


import java.util.HashMap;
import java.util.Map;

public class StudentServiceModel {
    private Long id;
    private String facultyNumber;
    private Double grade;
    private Map<Long, Double> committeeGrades;
    private boolean hasParticipated;
    private ThesisAssignmentServiceModel assignment;
    private DefenseServiceModel defense;
    private UserServiceModel user;

    public StudentServiceModel() {
        this.committeeGrades = new HashMap<>();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFacultyNumber() {
        return facultyNumber;
    }

    public void setFacultyNumber(String facultyNumber) {
        this.facultyNumber = facultyNumber;
    }

    public Double getGrade() {
        return grade;
    }

    public void setGrade(Double grade) {
        this.grade = grade;
    }

    public Map<Long, Double> getCommitteeGrades() {
        return committeeGrades;
    }

    public void setCommitteeGrades(Map<Long, Double> committeeGrades) {
        this.committeeGrades = committeeGrades;
    }

    public boolean isHasParticipated() {
        return hasParticipated;
    }

    public void setHasParticipated(boolean hasParticipated) {
        this.hasParticipated = hasParticipated;
    }

    public ThesisAssignmentServiceModel getAssignment() {
        return assignment;
    }

    public void setAssignment(ThesisAssignmentServiceModel assignment) {
        this.assignment = assignment;
    }

    public DefenseServiceModel getDefense() {
        return defense;
    }

    public void setDefense(DefenseServiceModel defense) {
        this.defense = defense;
    }

    public UserServiceModel getUser() {
        return user;
    }

    public void setUser(UserServiceModel user) {
        this.user = user;
    }

    @Override
    public String toString() {
        return "StudentServiceModel{" +
                "id=" + id +
                ", facultyNumber='" + facultyNumber + '\'' +
                ", grade=" + grade +
                ", committeeGrades=" + committeeGrades +
                ", hasParticipated=" + hasParticipated +
                ", assignment=" + assignment +
                ", defense=" + defense +
                ", user=" + user +
                '}';
    }
}
