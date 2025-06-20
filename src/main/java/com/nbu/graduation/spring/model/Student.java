package com.nbu.graduation.spring.model;

import jakarta.persistence.*;

import java.util.HashMap;
import java.util.Map;

@Entity
@Table(name = "students")
public class Student extends BaseEntity{

    @Column(name = "faculty_number", nullable = false, unique = true)
    private String facultyNumber;

    @Column(name = "grade")
    private Double grade;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "committee_grades", joinColumns = @JoinColumn(name = "student_id"))
    @MapKeyColumn(name = "member_id")
    @Column(name = "grade")
    private Map<Long, Double> committeeGrades;

    @Column(name = "has_participated")
    private boolean hasParticipated;

    @OneToOne(mappedBy = "student")
    private ThesisAssignment assignment;

   @ManyToOne
    private Defense defense;

    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;

    public Student(String facultyNumber, ThesisAssignment assignment, Defense defense) {
        this.committeeGrades = new HashMap<>();
        this.hasParticipated = false;
        this.facultyNumber = facultyNumber;
        this.assignment = assignment;
        this.defense = defense;
    }

    public Student() {
    }

    public String getFacultyNumber() {
        return facultyNumber;
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

    public void setFacultyNumber(String facultyNumber) {
        this.facultyNumber = facultyNumber;
    }

    public ThesisAssignment getAssignment() {
        return assignment;
    }

    public void setAssignment(ThesisAssignment assignment) {
        this.assignment = assignment;
    }

    public Defense getDefense() {
        return defense;
    }

    public void setDefense(Defense defense) {
        this.defense = defense;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
