package com.nbu.graduation.spring.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "department_approvals")
public class DepartmentApproval extends BaseEntity {

    @ManyToOne
    private Teacher teacher;

    @ManyToOne
    private ThesisAssignment assignment;

    @Column
    private boolean approved;

    public Teacher getTeacher() {
        return teacher;
    }

    public void setTeacher(Teacher teacher) {
        this.teacher = teacher;
    }

    public ThesisAssignment getAssignment() {
        return assignment;
    }

    public void setAssignment(ThesisAssignment assignment) {
        this.assignment = assignment;
    }

    public boolean isApproved() {
        return approved;
    }

    public void setApproved(boolean approved) {
        this.approved = approved;
    }
}
