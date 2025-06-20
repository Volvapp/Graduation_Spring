package com.nbu.graduation.spring.model;

import com.nbu.graduation.spring.model.enums.Department;
import jakarta.persistence.*;

import java.util.List;

@Entity
@Table(name = "thesis_assignments")
public class ThesisAssignment extends BaseEntity {
    @Column(name = "topic", nullable = false)
    private String topic;
    @Column(name = "goal", nullable = false)
    private String goal;
    @Column(name = "tasks", nullable = false)
    private String tasks;
    @Column(name = "technologies", nullable = false)
    private String technologies;
    @Column
    private boolean approved;
    @Column
    private boolean departmentApproved;
    @Enumerated(EnumType.STRING)
    private Department department;
    @OneToOne
    private Student student;
    @ManyToOne
    private Teacher supervisor;
    @OneToOne(mappedBy = "assignment")
    private Thesis thesis;

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public String getGoal() {
        return goal;
    }

    public void setGoal(String goal) {
        this.goal = goal;
    }

    public String getTasks() {
        return tasks;
    }

    public void setTasks(String tasks) {
        this.tasks = tasks;
    }

    public String getTechnologies() {
        return technologies;
    }

    public void setTechnologies(String technologies) {
        this.technologies = technologies;
    }

    public Student getStudent() {
        return student;
    }

    public void setStudent(Student student) {
        this.student = student;
    }

    public Teacher getSupervisor() {
        return supervisor;
    }

    public void setSupervisor(Teacher supervisor) {
        this.supervisor = supervisor;
    }

    public boolean isApproved() {
        return approved;
    }

    public void setApproved(boolean approved) {
        this.approved = approved;
    }

    public boolean isDepartmentApproved() {
        return departmentApproved;
    }

    public void setDepartmentApproved(boolean departmentApproved) {
        this.departmentApproved = departmentApproved;
    }

    public Department getDepartment() {
        return department;
    }

    public void setDepartment(Department department) {
        this.department = department;
    }

    public Thesis getThesis() {
        return thesis;
    }

    public void setThesis(Thesis thesis) {
        this.thesis = thesis;
    }

    @Override
    public String toString() {
        return "ThesisAssignment{" +
                "topic='" + topic + '\'' +
                ", goal='" + goal + '\'' +
                ", tasks='" + tasks + '\'' +
                ", technologies='" + technologies + '\'' +
                ", approved=" + approved +
                ", departmentApproved=" + departmentApproved +
                ", department=" + department +
                ", student=" + student +
                ", supervisor=" + supervisor +
                ", thesis=" + thesis +
                '}';
    }
}
