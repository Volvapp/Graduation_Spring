package com.nbu.graduation.spring.model.service;

import com.nbu.graduation.spring.model.enums.Department;

public class ThesisAssignmentServiceModel {
    private Long id;
    private String topic;
    private String goal;
    private String tasks;
    private String technologies;
    private boolean approved;
    private boolean departmentApproved;
    private Department department;
    private StudentServiceModel student;
    private TeacherServiceModel supervisor;
    private ThesisServiceModel thesis;

    public ThesisAssignmentServiceModel() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

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

    public StudentServiceModel getStudent() {
        return student;
    }

    public void setStudent(StudentServiceModel student) {
        this.student = student;
    }

    public TeacherServiceModel getSupervisor() {
        return supervisor;
    }

    public void setSupervisor(TeacherServiceModel supervisor) {
        this.supervisor = supervisor;
    }

    public ThesisServiceModel getThesis() {
        return thesis;
    }

    public void setThesis(ThesisServiceModel thesis) {
        this.thesis = thesis;
    }

    @Override
    public String toString() {
        return "ThesisAssignmentServiceModel{" +
                "id=" + id +
                ", topic='" + topic + '\'' +
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
